/**
 * @(#)CustAcntLogSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員連結設定記錄CustAcntLogSrvImpl
 * 
 * Modify History:
 *  v1.00, 2016/02/15, Evan
 *   1) First release
 *  v1.01, 2018/03/20
 *   1) 新增電商平台會員代號查詢條件
 *  
 */
package com.hitrust.bank.service.impl;

import java.util.Date;
import org.apache.log4j.Logger;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustAcntLogDAO;
import com.hitrust.bank.dao.CustDataDAO;
import com.hitrust.bank.dao.EcDataDAO;
import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.model.CustAcntLog;
import com.hitrust.bank.model.SysParm;
import com.hitrust.bank.service.CustAcntLogSrv;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class CustAcntLogSrvImpl implements CustAcntLogSrv {

	// Log4j
	private static Logger LOG = Logger.getLogger(CustAcntLogSrvImpl.class);

	// DAO injection
	private EcDataDAO ecDataDAO;
	private CustAcntLogDAO custAcntLogDAO;
	private SysParmDAO sysParmDAO;
	private CustDataDAO custDataDAO;

	public void setSysParmDAO(SysParmDAO sysParmDAO) {
		this.sysParmDAO = sysParmDAO;
	}

	public void setEcDataDAO(EcDataDAO ecDataDAO) {
		this.ecDataDAO = ecDataDAO;
	}

	public void setCustAcntLogDAO(CustAcntLogDAO custAcntLogDAO) {
		this.custAcntLogDAO = custAcntLogDAO;
	}
	
	public void setCustDataDAO(CustDataDAO custDataDAO) {
		this.custDataDAO = custDataDAO;
	}

	
	/**
	 * 查詢系統參數
	 * 
	 * @return CustAcntLog 客服系統帳號連結-查詢範圍限制
	 */
	@Override
	public CustAcntLog queryInit() {
		CustAcntLog custAcntLog = new CustAcntLog();
		custAcntLog.setEcData(ecDataDAO.getEcDataList());
		// 客服系統帳號連結-查詢範圍限制
		SysParm sysParm = (SysParm) sysParmDAO.queryById(SysParm.class, "STAFF_ACNT_QURY_LIMT");

		custAcntLog.setSysParam(sysParm.getParmValue());
		return custAcntLog;
	}

	/**
	 * 查詢會員結設定記錄
	 * 
	 * @param startDate 查詢起日
	 * @param endDate 查詢迄日
	 * @param queryType 查詢類別
	 * @param id 身分證字號
	 * @param acnt 實體帳號
	 * @param ecUser 電商平台會員代號
	 * @param ecId 平台代號
	 * @param stts 執行結果
	 * @param execSrc 執行來源
	 * @param page Page
	 * @return PageQuery
	 */
	@Override
	public PageQuery queryCustAcntLog(Date strtDate, Date endDate, String queryType, String custId, String realAcnt, String ecUser, 
			String ecId, String stts, String execSrc, Page page, CustAcntLog dataBean) {

		String queryStts = "";

		if ("01".equals(queryType)) {// 連結綁定
			if ("01".equals(stts)) {
				queryStts = "00"; // 連結成功
			} else if ("02".equals(stts)) {
				queryStts = "01"; // 連結失敗
			} else {
				queryStts = "00,01";
			}
		} else if ("02".equals(queryType)) { // 取消綁定
			if ("01".equals(stts)) {
				queryStts = "02"; // 取消成功
			} else if ("02".equals(stts)) {
				queryStts = "03"; // 取消失敗
			} else {
				queryStts = "02,03";
			}
		} else if ("04".equals(queryType) | "05".equals(queryType)| "06".equals(queryType)) {//啟用 or 暫停 or 終止
			queryStts = queryType;
		}  else { // 不分連結或取消
			if ("01".equals(stts)) {
				queryStts = "00,02"; // 所有成功
			} else if ("02".equals(stts)) {
				queryStts = "01,03"; // 所有失敗
			} else {
				// 全部
			}
		}
		
		//TSBACL-71, 實體帳號 右補0, 補滿11位
		if(!StringUtil.isBlank(realAcnt)){
			if(realAcnt.length() < 11){
				realAcnt = "00000000000" + realAcnt;
				realAcnt = realAcnt.substring(realAcnt.length()-11);
			}
		}
		
		String custName = "";
		//因不join CustData, 由於目前custId,realAcnt為查詢條件必須擇一輸入, 所以CustName由custId或realAcnt取得
		if(!StringUtil.isBlank(custId)){
			custName = custDataDAO.getCustNameByCustId(custId);
			dataBean.setCustName(custName);
		}else if(!StringUtil.isBlank(realAcnt)){
			//由實體帳號找到客戶ID
			custName = custDataDAO.getCustNameByRealAcnt(realAcnt);
			dataBean.setCustName(custName);
		}
		
		//V1.01 新增電商平台會員代號查詢條件
		PageQuery pageQuery = custAcntLogDAO.getCustAcntLog(strtDate, endDate, custId, realAcnt, ecUser, ecId, queryStts, execSrc, page);
		//V1.01 新增電商平台會員代號查詢條件 End
		return pageQuery;
	}
}
