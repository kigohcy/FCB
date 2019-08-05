/*
 * @(#) TrnsResultSearchSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/019, Jimmy
 * 	 1) JIRA-Number, First release
 *  v1.01, 2018/03/19
 *   1) 新增電商平台會員代號查詢條件
 * 
 */
package com.hitrust.bank.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustDataDAO;
import com.hitrust.bank.dao.EcDataDAO;
import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.dao.TrnsDataDAO;
import com.hitrust.bank.model.CustData;
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.SysParm;
import com.hitrust.bank.model.TrnsData;
import com.hitrust.bank.service.TrnsResultSearchSrv;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class TrnsResultSearchSrvImpl implements TrnsResultSearchSrv {

	//============================== Log4j ==============================
	private static Logger LOG = Logger.getLogger(LoginSettingsSrvImpl.class);
		
	//============================== DAO injection ==============================
	private EcDataDAO ecDataDAO;
	private TrnsDataDAO trnsDataDAO;
	private CustDataDAO custDataDAO;
	private SysParmDAO sysParmDAO;

	//============================== injection beans ==============================
	public void setSysParmDAO(SysParmDAO sysParmDAO) {
		this.sysParmDAO = sysParmDAO;
	}

	public void setTrnsDataDAO(TrnsDataDAO trnsDataDAO) {
		this.trnsDataDAO = trnsDataDAO;
	}

	public void setEcDataDAO(EcDataDAO ecDataDAO) {
		this.ecDataDAO = ecDataDAO;
	}

	public void setCustDataDAO(CustDataDAO custDataDAO) {
		this.custDataDAO = custDataDAO;
	}
	
	//============================== implements ==============================
	/**
	 * 取得查詢初始化資料(平台代號清單、查詢日期區間限制)
	 * 
	 * @return TrnsData 
	 */
	@Override
	public TrnsData queryInit() {
		TrnsData trnsData = new TrnsData();
		
		//取得平台代號清單
		trnsData.setEcData(ecDataDAO.getEcDataList());

		//取得查詢日期區間限制
		SysParm sysParm = (SysParm) sysParmDAO.queryById(SysParm.class, "STAFF_TRNS_QURY_LIMT");
		trnsData.setQueryLimt(sysParm.getParmValue());
		
		return trnsData;
	}
	
	/**
	 * 交易結果查詢
	 * 
	 * @param strtDate	查詢起始日期
	 * @param endDate	查詢結束日期
	 * @param custId	身分證字號
	 * @param realAcnt	銀行扣款帳號
	 * @param ecUser	電商平台會員代號
	 * @param ecId		平台代號
	 * @param trnsType	交易類別
	 * @param trnsStts	交易狀態
	 * @param page		分頁資訊
	 * @return
	 */
	@Override
	public PageQuery queryTrnsData(Date strtDate, Date endDate, String custId, String realAcnt, String ecUser, String ecId, 
			String trnsType, String trnsStts, Page page) {
		
		//TSBACL-71, 實體帳號 右補0, 補滿11位
		if(!StringUtil.isBlank(realAcnt)){
			if(realAcnt.length() < 11){
				realAcnt = "00000000000" + realAcnt;
				realAcnt = realAcnt.substring(realAcnt.length()-11);
			}
		}
		//v1.01 新增電商平台會員代號查詢條件
		PageQuery pageQuery = trnsDataDAO.getTrnsDataList(strtDate, endDate, custId, realAcnt, ecUser, ecId, trnsType, trnsStts, page);
		//v1.01 新增電商平台會員代號查詢條件 End
		List<TrnsData> rtnTrnsDataList = (List<TrnsData>) pageQuery.getResult();
		for (int i = 0; i < rtnTrnsDataList.size(); i++) {
			TrnsData rtnTrnsData = rtnTrnsDataList.get(i);
			
			//根據平台代號取得相對應的平台英文名稱
			String rtnEcId = rtnTrnsData.getId().getEcId();
			EcData rtnEcData = (EcData) ecDataDAO.queryById(EcData.class, rtnEcId);
			if(!StringUtil.isBlank(rtnEcData))
				rtnTrnsData.setEcNameEn(rtnEcData.getEcNameCh());

			//根據身分證字號取得相對應的會員名稱
			String rtnCustId = rtnTrnsData.getCustId();
			CustData custData = (CustData) custDataDAO.queryById(CustData.class, rtnCustId);
			if(!StringUtil.isBlank(custData))
				rtnTrnsData.setCustName(custData.getCustName());
		}
		return pageQuery;
	}

}
