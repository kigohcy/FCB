/**
 * @(#)CustCountReportSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員服務統計service實作
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
import com.hitrust.bank.dao.CustDataDAO;
import com.hitrust.bank.dao.DayCustContDAO;
import com.hitrust.bank.model.CustData;
import com.hitrust.bank.model.DayCustCont;
import com.hitrust.bank.service.CustCountReportSrv;
import com.hitrust.framework.model.page.PageQuery;

public class CustCountReportSrvImpl implements CustCountReportSrv {

	// ====== Log4j ======
	private static Logger LOG = Logger.getLogger(LoginSettingsSrvImpl.class);

	// ====== DAO injection ======
	private CustDataDAO custDataDAO;
	private DayCustContDAO dayCustContDAO;

	// ====== injection beans ======
	public void setCustDataDAO(CustDataDAO custDataDAO) {
		this.custDataDAO = custDataDAO;
	}
	public void setDayCustContDAO(DayCustContDAO dayCustContDAO) {
		this.dayCustContDAO = dayCustContDAO;
	}

	/**
	 * 會員服務統計-總表查詢
	 * 
	 * @param custData
	 */
	@Override
	public void queryCustDataCount(CustData custData) {
		
		//查詢總表資料
		List list = custDataDAO.countCustData();
		
		List dataList = new ArrayList();
		CustData data = null;
		
		//分類交易資料
		for (int i = 0; i < list.size(); i++) {
			Map<Object, Object> map = (Map<Object, Object>) list.get(i);
			String stts   = (String) map.get("STTS");
			int cnt       = (int) map.get("CNT");
			
			if(data==null){
				data = new CustData();
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
			custData.setReportData(dataList);
		}else{
			custData.setReportData(null);
		}
	}

	/**
	 * 月報表查詢
	 * 
	 * @param custData
	 */
	@Override
	public void queryMonthly(CustData custData) {
		
		String starDate = DateUtil.revertDateTime(custData.getStrtDate());
		String endDate = DateUtil.revertDateTime(custData.getEndDate());
		String today = DateUtil.getToday();
		String queryDate = starDate;
		
		List<DayCustCont> tempList = null;
		List<DayCustCont> list = new ArrayList<DayCustCont>();
		
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
			
			tempList = dayCustContDAO.getLastDayCustCont(queryDate);
			LOG.debug("queryDate="+queryDate+"/count="+tempList.size());
			list.addAll(tempList);
			//
			queryDate = DateUtil.countDate(queryDate, +1);
		}//end loop
		
		//
		List dataList = new ArrayList();
		CustData data = null;
		
		//分類交易資料
		//for (int i = 0; i < list.size(); i++) {
		for (int i = list.size()-1; i >= 0; i--) { //TSBACL-91,反向排序
			DayCustCont item = (DayCustCont) list.get(i);
			String month  = DateUtil.formateDateTimeForUser(item.getId().getSetlDate()).substring(0,7);
			String stts   = item.getId().getStts();
			int cnt       = item.getTotlCont();
			
			if(data==null || !month.equals(data.getDATE())){
				if(data!=null){
					dataList.add(data);
				}
				data = new CustData();
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
			custData.setReportData(dataList);
		}else{
			custData.setReportData(null);
		}
	}

	/**
	 * 日報表查詢
	 * 
	 * @param custData
	 */
	@Override
	public void queryDaily(CustData custData) {
		
		String starDate = DateUtil.revertDateTime(custData.getStrtDate());
		String endDate = DateUtil.revertDateTime(custData.getEndDate());
		
		//查詢日報資料
		List<DayCustCont> list = dayCustContDAO.getDayCustContList(starDate, endDate);
		
		List dataList = new ArrayList();
		CustData data = null;
		
		//分類交易資料
		for (int i = 0; i < list.size(); i++) {
			DayCustCont item = (DayCustCont) list.get(i);
			String date   = DateUtil.formateDateTimeForUser(item.getId().getSetlDate());
			String stts   = item.getId().getStts();
			int cnt       = item.getTotlCont();
			
			if(data==null || !date.equals(data.getDATE())){
				if(data!=null){
					dataList.add(data);
				}
				data = new CustData();
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
			custData.setReportData(dataList);
		}else{
			custData.setReportData(null);
		}
	}

	/**
	 * 明細資料查詢
	 * @param custData
	 */
	@Override
	public PageQuery queryDetail(CustData custData) {
		
		String stts = custData.getdStts();

		LOG.debug("queryDetail: stts="+stts);
		
		// 查詢總表
		PageQuery rtn = custDataDAO.getCustDataDetl(stts, custData.getPage());
		
		return rtn;
	}
	
	/**
	 * 明細資料查詢
	 * @param custData
	 */
	@Override
	public List<CustData> queryDetailForReport(CustData custData) {
		
		String stts = custData.getdStts();

		// 查詢總表
		List<CustData> list = custDataDAO.getCustDataDetl(stts);
		
		for(CustData custDataDetail : list) {
			custDataDetail.setRunDate(new SimpleDateFormat("yyyy/MM/dd").format(custDataDetail.getSttsDttm()));
			if("00".equals(custDataDetail.getStts()))
				custDataDetail.setStts("啟用");
			else if("01".equals(custDataDetail.getStts()))
				custDataDetail.setStts("暫停");
			else if("02".equals(custDataDetail.getStts()))
				custDataDetail.setStts("終止");
		}
		return list;
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
