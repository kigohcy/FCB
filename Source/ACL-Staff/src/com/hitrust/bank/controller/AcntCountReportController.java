/**
 * @(#)AcntCountReportController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 約定帳號統計controller
 * 
 * Modify History:
 *  v1.00, 2016/06/06, Yann
 *   1) First release
 *  v1.01, 2016/07/07, Jimmy Yen
 *   1) 記錄應用系統日誌
 *  
 */
package com.hitrust.bank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.CustAcntLink;
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.service.AcntCountReportSrv;
import com.hitrust.bank.service.EcDataMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.PageQuery;
import com.hitrust.framework.web.BaseAutoCommandController;

public class AcntCountReportController extends DownloadController {

	// ===== Log4j =====
	private static Logger LOG = Logger.getLogger(AcntCountReportController.class);

	// ===== Constructor =====
	public AcntCountReportController() {
		setCommandClass(CustAcntLink.class);
	}

	// ===== service injection =====
	private AcntCountReportSrv acntCountReportSrv;
	private EcDataMgmtSrv ecDataMgmtSrv;

	// ===== injection beans =====
	public void setAcntCountReportSrv(AcntCountReportSrv acntCountReportSrv) {
		this.acntCountReportSrv = acntCountReportSrv;
	}
	public void setEcDataMgmtSrv(EcDataMgmtSrv ecDataMgmtSrv) {
		this.ecDataMgmtSrv = ecDataMgmtSrv;
	}

	/**
	 * 約定帳號統計-初始化
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
		CustAcntLink uibean = (CustAcntLink) command;
		
		try {
			// 取得系統日期
			String today = DateUtil.getToday();
			today = DateUtil.formateDateTimeForUser(today);
			uibean.setStrtDate(today);
			uibean.setEndDate(today);
			
			// 取得查詢初始化資料
			List<EcData> list = ecDataMgmtSrv.queryEcDataList();
			uibean.setEcDataList(list);
			
		} catch (BusinessException e) {
			LOG.error("[queryInit BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[queryInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 約定帳號統計-查詢
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void query(Command command) throws BusinessException, FrameException {
		CustAcntLink custAcntLink = (CustAcntLink) command;
		custAcntLink.setInitFlag(false);

		try {
			//v1.01
			audiLog(custAcntLink);
			
			String rptType = custAcntLink.getRptType();
			
			if ("total".equals(rptType)) {
				// 查詢總表
				acntCountReportSrv.queryCustAcntLinkCount(custAcntLink);
			} else if ("monthly".equals(rptType)) {
				// 查詢月報表
				acntCountReportSrv.queryMonthly(custAcntLink);
			} else if ("daily".equals(rptType)) {
				// 查詢日報表
				acntCountReportSrv.queryDaily(custAcntLink);
			}

		} catch (BusinessException e) {
			LOG.error("[query BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 約定帳號統計-明細資料
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void queryDetail(Command command) throws BusinessException, FrameException {
		
		CustAcntLink custAcntLink = (CustAcntLink) command;
		custAcntLink.setInitFlag(false);
		try {
			custAcntLink.setReportDetailData(acntCountReportSrv.queryDetailForReportDetail(custAcntLink));
			PageQuery pageQuery = acntCountReportSrv.queryDetail(custAcntLink);
			setQueryPage(pageQuery);

		} catch (BusinessException e) {
			LOG.error("[queryDetail BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[queryDetail Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * Excel下載
	 * @param command
	 * @param response
	 * @param request
	 */
	public void download(Command command, HttpServletResponse response, HttpServletRequest request) {
		CustAcntLink custAcntLink = (CustAcntLink) command;
		
		//v1.01
		audiLog(custAcntLink);
		
		if(StringUtil.isBlank(custAcntLink.getqEcId().trim())){
			custAcntLink.setReportEcId("全部");
		}else{
			EcData ecData = ecDataMgmtSrv.queryEcData(custAcntLink.getqEcId());
			custAcntLink.setReportEcId(ecData.getEcNameCh());
		}
		custAcntLink.setReportDate(DateUtil.formateDateTimeForUser(DateUtil.getToday()));
		this.toExcel(custAcntLink, response, request);
	}
	
	/**
	 * 記錄應用系統日誌(TSB_APAUDITLOG)
	 * @param dataBean command
	 */
	//v1.01
	private void audiLog(CustAcntLink dataBean) throws BusinessException, FrameException {
		
		try {
			//記錄應用系統日誌(TSB_APAUDITLOG) 準備資料
			TsbAuditLog log = new TsbAuditLog();
			
			//查詢或下載
			if("Q".equals(dataBean.getOperate()) || "O".equals(dataBean.getOperate())){
				log.setStartDate(dataBean.getStrtDate());
				log.setEndDate(dataBean.getEndDate());
				log.setEcId(dataBean.getqEcId());
				log.setQueryType(dataBean.getRptType());
			}
			
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
		} catch (BusinessException e) {
			LOG.error("[EcData audiLog BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[EcData audiLog Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
}
