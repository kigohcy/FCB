/**
 * @(#)UnlockMgmtController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 未綁定解鎖controller
 * 
 * Modify History:
 *  v1.00, 2016/12/21, Yann
 *   1) TSBACL-143, First release
 *  
 */
package com.hitrust.bank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.AuthenticationHelper;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.WSResBean;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.CustData;
import com.hitrust.bank.model.CustPltf;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.service.SrvSttsMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class UnlockMgmtController extends BaseAutoCommandController {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(UnlockMgmtController.class);
	
	// service injection
	private SrvSttsMgmtSrv srvSttsMgmtSrv;
	
	public void setSrvSttsMgmtSrv(SrvSttsMgmtSrv srvSttsMgmtSrv) {
		this.srvSttsMgmtSrv = srvSttsMgmtSrv;
	}
	
	// constructor
	public UnlockMgmtController() {
		setCommandClass(CustPltf.class);
	}

	/**
	 * 查詢初始
	 * @param command
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
		
		CustPltf databBean = (CustPltf) command;
		// 設定查詢初始化狀態
		databBean.setInitQuery(true);
	}

	/**
	 * 會員登入狀態查詢
	 * @param command
	 */
	public void query(Command command) throws BusinessException, FrameException {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		CustPltf dataBean = (CustPltf) command;
		dataBean.setInitQuery(false);
		String custId = dataBean.getCustId();
		
		try {
			//沒有operate表示是 按確認或上一頁的查詢
			if(StringUtil.isBlank(dataBean.getOperate())){
				custId = dataBean.getqCustId();
			}
			
			LOG.debug("custId="+custId);
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();
			//設定交易log
			dataBean.setFnKeyValue(custId);
			log.setCustId(custId);
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
//			List<String> viewLimitIds = user.getViewLimitIds();
//			if(viewLimitIds.contains(custId)){
//				LOG.debug("custId is viewLimitId..");
//				throw new BusinessException("message.sys.NoData");
//			}

			//
			CustData custData = srvSttsMgmtSrv.queryCustData(custId);
			if(custData != null && !"02".equals(custData.getStts())){
				//客戶已是會員,請由服務狀態管理進行解鎖
				throw new BusinessException("message.F0210.query.fail");
			}
			
			try{
				AuthenticationHelper helper = new AuthenticationHelper();
				WSResBean resBean = helper.queryErrorNo(custId);
				
				//0：失敗、1：成功
				if ("1".equals(resBean.getResult())) {
					String codeStts = this.compseCustStts("code", resBean.getCODEFAILTIMES());
					String pwdStts  = this.compseCustStts("pwd", resBean.getPWDFAILTIMES());
					dataBean.setCustStts(codeStts + "," + pwdStts);
				}else{
					LOG.debug("RESULT:"+resBean.getResult()+"/ERRORMSG="+resBean.getErrorMsg());
					dataBean.setCustStts(resBean.getErrorMsg());
				}
			}catch(Exception e){
				//連線網銀異常
				LOG.error("Exception:"+e.toString(), e);
				dataBean.setCustStts(I18nConverter.i18nMapping("message.F0202.wsFail", user.getLocale()));
			}
			
			// ========== 敏感戶查詢處理 ==========
			Map<String, String> custIds = new HashMap<String, String>();
			custIds.put(custId, custId);
			CommonUtil.checkTxncsSvip(custIds, custId, "", dataBean.getFuncId(), "");
			
		} catch (BusinessException e) {
			LOG.error("[UnlockMgmt query BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[UnlockMgmt query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
		dataBean.setqCustId(custId);
	}
	
	/**
	 * 解鎖
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void unlock(Command command) throws BusinessException, FrameException {
		CustPltf dataBean = (CustPltf) command;
		
		AuthenticationHelper helper = null;
		WSResBean resBean = null;
		
		try {
			LOG.debug("custId=" + dataBean.getqCustId());
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();	
			//設定交易關鍵值、狀態
			dataBean.setFnKeyValue(dataBean.getqCustId());
			log.setCustId(dataBean.getqCustId());
			log.setStts(dataBean.getCustStts());
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
			try {
				String rtnMsg = "";
				helper = new AuthenticationHelper();
				resBean = helper.unlock(dataBean.getqCustId());
				
				//0：失敗、1：成功、2：首次登入
				if ("1".equals(resBean.getResult())) { //成功
					rtnMsg = I18nConverter.i18nMapping("message.sys.0004", new Locale("zh", "TW"));
					
				} else {
					LOG.debug("RESULT:"+resBean.getResult()+"/ERRORMSG="+resBean.getErrorMsg());
					throw new BusinessException(resBean.getErrorMsg(), "", null, null);
				}
				
				dataBean.setReturnMsg(rtnMsg);
				
			} catch (Exception e) {
				LOG.error("[發送 Web service 失敗]: " + e.getMessage(), e);
				throw new BusinessException("message.F0202.wsFail");
			}
			
		} catch (BusinessException e) {
			LOG.error("[unlock BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[unlock Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 兜組會員登入狀態(0-4, 9:Lock)
	 * @param type  pwd: 密碼, code: 代碼
	 * @param times 錯誤次數
	 * @return
	 */
	private String compseCustStts(String type, int times) {
		String rtnVal = "";
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		if ("code".equals(type)) {
			
			switch (times) {
			case 0: rtnVal = I18nConverter.i18nMapping("F0202.field.custCode.Normal", user.getLocale()); 
				break;
			case 9: rtnVal = I18nConverter.i18nMapping("F0202.field.custCode.Lock", user.getLocale());
				break;
			default: 
				rtnVal = I18nConverter.i18nMapping("F0202.field.custCode.ErrorTimes", user.getLocale());
				rtnVal = rtnVal.replace("#", String.valueOf(times));
				break;
			}
			
		} else {
			switch (times) {
			case 0: rtnVal = I18nConverter.i18nMapping("F0202.field.pwd.Normal", user.getLocale()); 
				break;
			case 9: rtnVal = I18nConverter.i18nMapping("F0202.field.pwd.Lock", user.getLocale());
				break;
			default: 
				rtnVal = I18nConverter.i18nMapping("F0202.field.pwd.ErrorTimes", user.getLocale());
				rtnVal = rtnVal.replace("#", String.valueOf(times));
				break;
			}
		}
		
		return rtnVal;
	}
}
