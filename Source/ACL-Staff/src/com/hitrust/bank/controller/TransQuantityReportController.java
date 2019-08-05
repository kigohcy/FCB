/**
 * @(#)TransQuantityReportController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 交易量統計報表controller
 * 
 * Modify History:
 *  v1.00, 2016/06/02, Jimmy Yen
 *   1) First release
 *  v1.01, 2016/07/07, Jimmy Yen
 *   1) 記錄應用系統日誌
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
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.VwTrnsData;
import com.hitrust.bank.service.EcDataMgmtSrv;
import com.hitrust.bank.service.TransQuantityReportSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class TransQuantityReportController extends DownloadController {

	// ===== Log4j =====
	private static Logger LOG = Logger.getLogger(TransQuantityReportController.class);

	// ===== Constructor =====
	public TransQuantityReportController() {
		setCommandClass(VwTrnsData.class);
	}

	// ===== service injection =====
	private TransQuantityReportSrv transQuantityReportSrv;
	private EcDataMgmtSrv ecDataMgmtSrv;
	
	// ===== injection beans =====
	public void setTransQuantityReportSrv(TransQuantityReportSrv transQuantityReportSrv) {
		this.transQuantityReportSrv = transQuantityReportSrv;
	}
	
	public void setEcDataMgmtSrv(EcDataMgmtSrv ecDataMgmtSrv) {
		this.ecDataMgmtSrv = ecDataMgmtSrv;
	}


	/**
	 * 交易量統計-初始化
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
		LOG.info("交易量統計-初始化");
		VwTrnsData vwTrnsData = (VwTrnsData) command;

		try {
			// 取得系統日期
			String today = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("D", "AD"));
			vwTrnsData.setEndDate(today);
			vwTrnsData.setStrtDate(today);

			// 取得查詢初始化資料
			VwTrnsData rtnTrnsData = transQuantityReportSrv.queryInit();
			vwTrnsData.setEcData(rtnTrnsData.getEcData());

		} catch (BusinessException e) {
			LOG.error("[QuantityReport queryInit BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[QuantityReport queryInit Exception]: ", e);
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
		VwTrnsData vwTrnsData = (VwTrnsData) command;
		
		//v1.01
		audiLog(vwTrnsData);
		
		if(StringUtil.isBlank(vwTrnsData.getqEcId().trim())){
			vwTrnsData.setReportEcName("全部");
		}else{
			EcData ecData = ecDataMgmtSrv.queryEcData(vwTrnsData.getqEcId());
			vwTrnsData.setReportEcName(ecData.getEcNameCh());
		}
		vwTrnsData.setReportDate(DateUtil.formateDateTimeForUser(DateUtil.getToday()));
		
		this.toExcel(vwTrnsData, response, request);
	}

	/**
	 * 交易量統計-查詢
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void query(Command command) throws BusinessException, FrameException {
		LOG.info("交易量統計-查詢");
		VwTrnsData vwTrnsData = (VwTrnsData) command;
		vwTrnsData.setInitFlag(false);

		try {
			//v1.01
			audiLog(vwTrnsData);
			
			String rptType = vwTrnsData.getRptType();

			if ("platform".equals(rptType)) {
				// 查詢平台總表
				transQuantityReportSrv.queryPlatform(vwTrnsData);
			} else if ("monthly".equals(rptType)) {
				// 查詢月報表
				transQuantityReportSrv.queryMonthly(vwTrnsData);
			} else if ("daily".equals(rptType)) {
				// 查詢日報表
				transQuantityReportSrv.queryDaily(vwTrnsData);
			}

		} catch (BusinessException e) {
			LOG.error("[QuantityReport query BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[QuantityReport query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 交易量統計-明細資料
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void queryDetail(Command command) throws BusinessException, FrameException {
		LOG.info("交易量統計-明細資料");
		VwTrnsData vwTrnsData = (VwTrnsData) command;
		vwTrnsData.setInitFlag(false);
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		try {
			Page page = vwTrnsData.getPage();
			String qEcId = vwTrnsData.getqEcId();

			vwTrnsData.setReportDetail(transQuantityReportSrv.queryDetail(vwTrnsData, "", "", qEcId, user.getLocale()));
			
			PageQuery pageQuery = transQuantityReportSrv.queryDetail(vwTrnsData, "", "", qEcId, page);
			setQueryPage(pageQuery);
			
//			// ========== 敏感戶查詢處理 ==========
//			Map<String, String> custIds = new HashMap<String, String>();
//			List<VwTrnsData> vwTrnsDatas = (List<VwTrnsData>) pageQuery.getResult();
//			
//			for (VwTrnsData TrnsData : vwTrnsDatas) {
//				String custId = TrnsData.getCustId();
//				custIds.put(custId, custId);
//			}
//			
//			CommonUtil.checkTxncsSvip(custIds, "", "", vwTrnsData.getFuncId(), "");
			
		} catch (BusinessException e) {
			LOG.error("[QuantityReport queryDetail BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[QuantityReport queryDetail Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 交易量統計-細部明細資料
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void queryDetail2(Command command) throws BusinessException, FrameException {
		LOG.info("交易量統計-個別明細資料");
		VwTrnsData vwTrnsData = (VwTrnsData) command;
		vwTrnsData.setInitFlag(false);
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		try {
			Page page = vwTrnsData.getPage();
			String dEcId = vwTrnsData.getdEcId();
			String rptType = vwTrnsData.getRptType();
			String trnsType = vwTrnsData.getTrnsType();

			vwTrnsData.setReportDetail(transQuantityReportSrv.queryDetail(vwTrnsData, trnsType, rptType, dEcId, user.getLocale()));
			
			PageQuery pageQuery = transQuantityReportSrv.queryDetail(vwTrnsData, trnsType, rptType, dEcId, page);
			setQueryPage(pageQuery);
			
//			// ========== 敏感戶查詢處理 ==========
//			Map<String, String> custIds = new HashMap<String, String>();
//			List<VwTrnsData> vwTrnsDatas = (List<VwTrnsData>) pageQuery.getResult();
//			
//			for (VwTrnsData TrnsData : vwTrnsDatas) {
//				String custId = TrnsData.getCustId();
//				custIds.put(custId, custId);
//			}
//			
//			CommonUtil.checkTxncsSvip(custIds, "", "", vwTrnsData.getFuncId(), "");
			
		} catch (BusinessException e) {
			LOG.error("[QuantityReport queryDetail BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[QuantityReport queryDetail Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 記錄應用系統日誌(TSB_APAUDITLOG)
	 * @param dataBean command
	 */
	private void audiLog(VwTrnsData dataBean) throws BusinessException, FrameException {
		
		try {
			//記錄應用系統日誌(TSB_APAUDITLOG) 準備資料
			TsbAuditLog log = new TsbAuditLog();
			
			if("Q".equals(dataBean.getOperate())){
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
