/**
 * @(#)CustCountReportController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員服務統計controller
 * 
 * Modify History:
 *  v1.00, 2016/06/08, Yann
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
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.CustData;
import com.hitrust.bank.service.CustCountReportSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.PageQuery;
import com.hitrust.framework.web.BaseAutoCommandController;

public class CustCountReportController extends DownloadController {

	// ===== Log4j =====
	private static Logger LOG = Logger.getLogger(CustCountReportController.class);

	// ===== Constructor =====
	public CustCountReportController() {
		setCommandClass(CustData.class);
	}

	// ===== service injection =====
	private CustCountReportSrv custCountReportSrv;

	// ===== injection beans =====
	public void setCustCountReportSrv(CustCountReportSrv custCountReportSrv) {
		this.custCountReportSrv = custCountReportSrv;
	}

	/**
	 * 約定帳號統計-初始化
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
		CustData uibean = (CustData) command;
		
		try {
			// 取得系統日期
			String today = DateUtil.getToday();
			today = DateUtil.formateDateTimeForUser(today);
			uibean.setStrtDate(today);
			uibean.setEndDate(today);
			
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
		CustData custData = (CustData) command;
		custData.setInitFlag(false);

		try {
			//v1.01
			audiLog(custData);
			
			String rptType = custData.getRptType();
			
			if ("total".equals(rptType)) {
				// 查詢總表
				custCountReportSrv.queryCustDataCount(custData);
			} else if ("monthly".equals(rptType)) {
				// 查詢月報表
				custCountReportSrv.queryMonthly(custData);
			} else if ("daily".equals(rptType)) {
				// 查詢日報表
				custCountReportSrv.queryDaily(custData);
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
		
		CustData custData = (CustData) command;
		custData.setInitFlag(false);
		try {
			custData.setReportDetailData(custCountReportSrv.queryDetailForReport(custData));
			PageQuery pageQuery = custCountReportSrv.queryDetail(custData);
			setQueryPage(pageQuery);
			
//			// ========== 敏感戶查詢處理 ==========
//			Map<String, String> custIds = new HashMap<String, String>();
//			List<CustData> custDates = (List<CustData>) pageQuery.getResult();
//			
//			for (CustData custDate : custDates) {
//				String custId = custDate.getCustId();
//				custIds.put(custId, custId);
//			}
//			CommonUtil.checkTxncsSvip(custIds, "", "", custData.getFuncId(), "");
			
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
		CustData custData = (CustData) command;
		
		//v1.01
		audiLog(custData);
		
		custData.setReportDate(DateUtil.formateDateTimeForUser(DateUtil.getToday()));
		this.toExcel(custData, response, request);
	}
	
	/**
	 * 記錄應用系統日誌(TSB_APAUDITLOG)
	 * @param dataBean command
	 */
	//v1.01
	private void audiLog(CustData dataBean) throws BusinessException, FrameException {
		
		try {
			//記錄應用系統日誌(TSB_APAUDITLOG) 準備資料
			TsbAuditLog log = new TsbAuditLog();
			
			//查詢或下載
			if("Q".equals(dataBean.getOperate()) || "O".equals(dataBean.getOperate())){
				log.setStartDate(dataBean.getStrtDate());
				log.setEndDate(dataBean.getEndDate());
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
