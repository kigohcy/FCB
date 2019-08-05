/**
 * @(#)EcDataStatusMgmtController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 電商平台狀態管理 controller
 * 
 * Modify History:
 *  v1.00, 2018/04/09, Darren Tsai
 *   1) First release
 *  
 */
package com.hitrust.bank.controller;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.service.EcDataMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class EcDataStatusMgmtController extends BaseAutoCommandController {
	private static Logger	LOG	= Logger.getLogger(EcDataStatusMgmtController.class);
	private EcDataMgmtSrv	ecDataMgmtSrv;

	public void setEcDataMgmtSrv(EcDataMgmtSrv ecDataMgmtSrv) {
		this.ecDataMgmtSrv = ecDataMgmtSrv;
	}

	public EcDataStatusMgmtController() {
		setCommandClass(EcData.class);
	}

	/**
	 * 電商平台狀態查詢
	 * 
	 * @param command
	 */
	public void queryEcDataStatus(Command command) throws BusinessException, FrameException {
		EcData dataBean = (EcData) command;
		try {
			dataBean.setEcDataList(ecDataMgmtSrv.queryEcDataList());
		}
		catch (BusinessException e) {
			LOG.error("[queryEcDataStatus BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[queryEcDataStatus Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 調整電商平台狀態為啟動
	 * 
	 * @param command
	 */
	public void enableEcDataStatus(Command command) throws BusinessException, FrameException {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		EcData dataBean = (EcData) command;
		try {
			this.audiLog(dataBean);
			ecDataMgmtSrv.updateEcDataStatus(this.getPageCommandToHashMap(dataBean));
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.0002", user.getLocale()));
		}
		catch (BusinessException e) {
			LOG.error("[enableEcDataStatus BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[enableEcDataStatus Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 調整電商平台狀態為暫停
	 * 
	 * @param command
	 */
	public void pauseEcDataStatus(Command command) throws BusinessException, FrameException {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		EcData dataBean = (EcData) command;
		try {
			this.audiLog(dataBean);
			ecDataMgmtSrv.updateEcDataStatus(this.getPageCommandToHashMap(dataBean));
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.0003", user.getLocale()));
		}
		catch (BusinessException e) {
			LOG.error("[pauseEcDataStatus BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[pauseEcDataStatus Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 將頁面的的資料轉成HashMap(EC_DATA物件)
	 * 
	 * @param ecData
	 */
	private HashMap<String, Object> getPageCommandToHashMap(EcData ecData) throws BusinessException, FrameException {
		try {
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ecId", ecData.getEcId());
			parameters.put("stts", ecData.getStts());
			return parameters;
		}
		catch (BusinessException e) {
			LOG.error("[EcData getPageCommandToHashMap BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[EcData getPageCommandToHashMap Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 記錄應用系統日誌(TSB_APAUDITLOG)
	 * 
	 * @param dataBean command
	 */
	private void audiLog(EcData dataBean) throws BusinessException, FrameException {
		try {
			TsbAuditLog log = new TsbAuditLog();
			log.setEcId(dataBean.getEcId());
			if (!"D".equals(dataBean.getOperate())) {
				log.setEcNameCh(dataBean.getEcNameCh());
				log.setEcNameEn(dataBean.getEcNameEn());
				log.setStts(dataBean.getStts());
			}
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
		}
		catch (BusinessException e) {
			LOG.error("[EcData audiLog BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[EcData audiLog Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
}
