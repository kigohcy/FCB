/**
 * @(#)TrnsLimitsMgmtController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 交易限額controller
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.CustAcnt;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.service.TrnsLimitsMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class TrnsLimitsMgmtController extends BaseAutoCommandController {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(TrnsLimitsMgmtController.class);
	
	// service injection
	private TrnsLimitsMgmtSrv trnsLimitsMgmtSrv;
	
	public void setTrnsLimitsMgmtSrv(TrnsLimitsMgmtSrv trnsLimitsMgmtSrv) {
		this.trnsLimitsMgmtSrv = trnsLimitsMgmtSrv;
	}

	// constructor
	public TrnsLimitsMgmtController() {
		setCommandClass(CustAcnt.class);
	}
	
	/**
	 * 交易限額查詢初始
	 * @param command
	 */
	public void queryInit(Command command){
		
		CustAcnt dataBean = (CustAcnt) command;
		//設定系統初始頁
		dataBean.setInitQuery(true);
	}
	
	/**
	 * 交易限額查詢
	 * @param command
	 */
	public void query(Command command) throws BusinessException, FrameException {
		
		CustAcnt dataBean = (CustAcnt) command;
		dataBean.setInitQuery(false);
		String custId = dataBean.getCustId();
		
		try {
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();
			//設定交易log
			dataBean.setFnKeyValue(custId);
			log.setCustId(custId);
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
			trnsLimitsMgmtSrv.queryTrnsLimitsSrvData(dataBean);
			
			// ========== 敏感戶查詢處理 ==========
			if (dataBean.getCustData() != null) {
				Map<String, String> custIds = new HashMap<String, String>();
				custIds.put(custId, custId);
				CommonUtil.checkTxncsSvip(custIds, custId, "", dataBean.getFuncId(), "");
			}
			
		} catch (BusinessException e) {
			LOG.error("[TrnsLimitsMgmt query BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[TrnsLimitsMgmt query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 限額設定
	 * @param command
	 */
	public void updateTrnsLimtAcnt(Command command) throws BusinessException, FrameException {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		CustAcnt dataBean = (CustAcnt) command;
		
		try {
			
			trnsLimitsMgmtSrv.updateTrnsLimtAcnt(dataBean);
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.limit.success", user.getLocale()));//限額設定 成功
			//更新 command 內容 for 操作記錄, 記錄異動後資料
			trnsLimitsMgmtSrv.recordAfterOpt(dataBean);
			
		} catch (BusinessException e) {
			LOG.error("[updateTrnsLimtAcnt query BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[updateTrnsLimtAcnt query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
}
