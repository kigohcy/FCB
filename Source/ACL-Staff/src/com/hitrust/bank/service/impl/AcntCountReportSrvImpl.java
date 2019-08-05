/**
 * @(#)AcntCountReportSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 約定帳號統計service實作
 * 
 * Modify History:
 *  v1.00, 2016/06/06, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.dao.CustAcntLinkDAO;
import com.hitrust.bank.dao.DayAcntContDAO;
import com.hitrust.bank.model.CustAcntLink;
import com.hitrust.bank.model.DayAcntCont;
import com.hitrust.bank.service.AcntCountReportSrv;
import com.hitrust.framework.model.page.PageQuery;

public class AcntCountReportSrvImpl implements AcntCountReportSrv {

	// ====== Log4j ======
	private static Logger LOG = Logger.getLogger(LoginSettingsSrvImpl.class);

	// ====== DAO injection ======
	private CustAcntLinkDAO custAcntLinkDAO;
	private DayAcntContDAO dayAcntContDAO;

	// ====== injection beans ======
	public void setCustAcntLinkDAO(CustAcntLinkDAO custAcntLinkDAO) {
		this.custAcntLinkDAO = custAcntLinkDAO;
	}
	public void setDayAcntContDAO(DayAcntContDAO dayAcntContDAO) {
		this.dayAcntContDAO = dayAcntContDAO;
	}

	/**
	 * 約定帳號統計-總表查詢
	 * 
	 * @param custAcntLink
	 */
	@Override
	public void queryCustAcntLinkCount(CustAcntLink custAcntLink) {
		
		LOG.debug("ecId="+custAcntLink.getqEcId());
		
		//查詢總表資料
		List list = custAcntLinkDAO.countCustAcntLink(custAcntLink.getqEcId());
		
		List dataList = new ArrayList();
		CustAcntLink data = null;
		
		//分類交易資料
		for (int i = 0; i < list.size(); i++) {
			Map<Object, Object> map = (Map<Object, Object>) list.get(i);
			String ecId   = (String) map.get("EC_ID");
			String ecName = (String) map.get("EC_NAME_CH");
			String stts   = (String) map.get("STTS");
			int cnt       = (int) map.get("CNT");
			
			if(data==null || !ecId.equals(data.getEC_ID())){
				if(data!=null){
					dataList.add(data);
				}
				data = new CustAcntLink();
				data.setEC_ID(ecId);
				data.setEC_NAME(ecName);
			}
			
			if("00".equals(stts)){ //啟用
				data.setCNT_00(cnt);
			}else if("01".equals(stts)){ //暫停
				data.setCNT_01(cnt);
			}else if("02".equals(stts)){ //終止
				data.setCNT_02(cnt);
			}
			//帳號總數
			data.setCNT_TOTL(data.getCNT_TOTL()+cnt);
		}//end loop
		
		if(list!=null && list.size()>0){
			dataList.add(data);
			custAcntLink.setReportData(dataList);
		}else{
			custAcntLink.setReportData(null);
		}
	}

	/**
	 * 月報表查詢
	 * 
	 * @param custAcntLink
	 */
	@Override
	public void queryMonthly(CustAcntLink custAcntLink) {
		
		String qEcId = custAcntLink.getqEcId();
		String starDate = DateUtil.revertDateTime(custAcntLink.getStrtDate());
		String endDate = DateUtil.revertDateTime(custAcntLink.getEndDate());
		String today = DateUtil.getToday();
		String queryDate = starDate;
		
		List<DayAcntCont> tempList = null;
		List<DayAcntCont> list = new ArrayList<DayAcntCont>();
		
		while(Integer.parseInt(queryDate) <= Integer.parseInt(endDate) 
		   && Integer.parseInt(queryDate) < Integer.parseInt(today)) {
			//
			if(Integer.parseInt(queryDate.substring(0,6)) < Integer.parseInt(today.substring(0,6))){
				//小於系統當月, 以該月最後一日為查詢日
				int maxDate = getMaxMonthDay(queryDate);
				queryDate = queryDate.substring(0,6) + maxDate;
			}else if(Integer.parseInt(queryDate.substring(0,6)) == Integer.parseInt(today.substring(0,6))){
				queryDate = endDate;
				if(Integer.parseInt(queryDate) >= Integer.parseInt(today)){
					//當月, 最大為系統日前一日
					queryDate = DateUtil.countDate(today, -1);
				}
			}
			
			tempList = dayAcntContDAO.getLastDayAcntCont(qEcId, queryDate);
			LOG.debug("queryDate="+queryDate+"/count="+tempList.size());
			list.addAll(tempList);
			//
			queryDate = DateUtil.countDate(queryDate, +1);
		}//end loop
		
		
		//
		List dataList = new ArrayList();
		CustAcntLink data = null;
		
		//分類交易資料
		for (int i = 0; i < list.size(); i++) {
			DayAcntCont item = (DayAcntCont) list.get(i);
			String ecId   = item.getId().getEcId();
			String ecName = item.getEcNameCh();
			String month  = DateUtil.formateDateTimeForUser(item.getId().getSetlDate()).substring(0,7);
			String stts   = item.getId().getStts();
			int cnt       = item.getTotlCont();
			
			if(data==null || !ecId.equals(data.getEC_ID()) || !month.equals(data.getDATE())){
				if(data!=null){
					dataList.add(data);
				}
				data = new CustAcntLink();
				data.setEC_ID(ecId);
				data.setEC_NAME(ecName);
				data.setDATE(month); //YYYY/MM
			}
			
			if("00".equals(stts)){ //啟用
				data.setCNT_00(cnt);
			}else if("01".equals(stts)){ //暫停
				data.setCNT_01(cnt);
			}else if("02".equals(stts)){ //終止
				data.setCNT_02(cnt);
			}
			//帳號總數
			data.setCNT_TOTL(data.getCNT_TOTL()+cnt);
		}//end loop
		
		if(list!=null && list.size()>0){
			dataList.add(data);
			custAcntLink.setReportData(dataList);
		}else{
			custAcntLink.setReportData(null);
		}
	}

	/**
	 * 日報表查詢
	 * 
	 * @param custAcntLink
	 */
	@Override
	public void queryDaily(CustAcntLink custAcntLink) {
		
		String qEcId = custAcntLink.getqEcId();
		String starDate = DateUtil.revertDateTime(custAcntLink.getStrtDate());
		String endDate = DateUtil.revertDateTime(custAcntLink.getEndDate());
		
		//查詢日報資料
		List<DayAcntCont> list = dayAcntContDAO.getDayAcntContList(qEcId, starDate, endDate);
		
		List dataList = new ArrayList();
		CustAcntLink data = null;
		
		//分類交易資料
		for (int i = 0; i < list.size(); i++) {
			DayAcntCont item = (DayAcntCont) list.get(i);
			String ecId   = item.getId().getEcId();
			String ecName = item.getEcNameCh();
			String date   = DateUtil.formateDateTimeForUser(item.getId().getSetlDate());
			String stts   = item.getId().getStts();
			int cnt       = item.getTotlCont();
			
			if(data==null || !ecId.equals(data.getEC_ID()) || !date.equals(data.getDATE())){
				if(data!=null){
					dataList.add(data);
				}
				data = new CustAcntLink();
				data.setEC_ID(ecId);
				data.setEC_NAME(ecName);
				data.setDATE(date);
			}
			
			if("00".equals(stts)){ //啟用
				data.setCNT_00(cnt);
			}else if("01".equals(stts)){ //暫停
				data.setCNT_01(cnt);
			}else if("02".equals(stts)){ //終止
				data.setCNT_02(cnt);
			}
			//帳號總數
			data.setCNT_TOTL(data.getCNT_TOTL()+cnt);
		}//end loop
		
		if(list!=null && list.size()>0){
			dataList.add(data);
			custAcntLink.setReportData(dataList);
		}else{
			custAcntLink.setReportData(null);
		}
	}

	/**
	 * 明細資料查詢
	 * @param custAcntLink
	 */
	@Override
	public PageQuery queryDetail(CustAcntLink custAcntLink) {
		
		String ecId = custAcntLink.getdEcId();
		String stts = custAcntLink.getdStts();

		LOG.debug("queryDetail: ecId="+ecId+"/stts="+stts);
		
		// 查詢總表
		PageQuery rtn = custAcntLinkDAO.getCustAcntLinkDetl(ecId, stts, custAcntLink.getPage());
		
		return rtn;
	}
	
	/**
	 * 明細資料查詢
	 * @param custAcntLink
	 */
	@Override
	public List<CustAcntLink> queryDetailForReportDetail(CustAcntLink custAcntLink) {
		
		String ecId = custAcntLink.getdEcId();
		String stts = custAcntLink.getdStts();

		List<CustAcntLink> reportDetailDateList = custAcntLinkDAO.getCustAcntLinkDetl(ecId, stts);
		for(CustAcntLink custAcntLinkDetail : reportDetailDateList){
			custAcntLinkDetail.setRunDate(new SimpleDateFormat("yyyy/MM/dd").format(custAcntLinkDetail.getSttsDttm()));
		}
		return reportDetailDateList;
	}
	
	/**
	 * 功能：取得當月的最大天數
	 * 
	 * @param today
	 *            日期
	 * @return int 該日期所在月份的最大天數
	 * @exception none
	 */
	static public int getMaxMonthDay(String today) {
		if (today == null || today.length() < 6) {
			LOG.debug("getMaxMonthDay error for input today is :" + today);
			return -1;
		}
		int Now_yyyy = Integer.parseInt(today.substring(0, 4));
		int Now_mm = Integer.parseInt(today.substring(4, 6)) - 1;

		Calendar dd = Calendar.getInstance();
		dd.clear();
		dd.set(Calendar.YEAR, Now_yyyy);
		dd.set(Calendar.MONTH, Now_mm);

		return dd.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
}
