/**
 * @(#)SrvSttsMgmtController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 服務狀態管理controller
 * 
 * Modify History:
 *  v1.00, 2016/02/16, Evan
 *   1) First release
 *  v1.01, 2016/12/22, Yann
 *   2) TSBACL-143, 未綁定解鎖
 *  
 */
package com.hitrust.bank.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.AuthenticationHelper;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.WSResBean;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.CustPltf;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.service.SrvSttsMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class SrvSttsMgmtController extends BaseAutoCommandController {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(SrvSttsMgmtController.class);
	
	// service injection
	private SrvSttsMgmtSrv srvSttsMgmtSrv;
	
	public void setSrvSttsMgmtSrv(SrvSttsMgmtSrv srvSttsMgmtSrv) {
		this.srvSttsMgmtSrv = srvSttsMgmtSrv;
	}
	
	// constructor
	public SrvSttsMgmtController() {
		setCommandClass(CustPltf.class);
	}

	/**
	 * 服務狀態查詢初始
	 * @param command
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
		
		CustPltf databBean = (CustPltf) command;
		// 設定查詢初始化狀態
		databBean.setInitQuery(true);
	}

	/**
	 * 服務狀態查詢
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
				custId = dataBean.getCustData().getCustId();
			}
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();
			//設定交易log
			dataBean.setFnKeyValue(custId);
			log.setCustId(custId);
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
			try{
				LOG.debug("CustId="+custId);
				AuthenticationHelper helper = new AuthenticationHelper();
				WSResBean resBean = helper.queryErrorNo(custId);
				
				//0：失敗、1：成功
				if ("1".equals(resBean.getResult())) {
					String codeStts = this.compseCustStts("code", resBean.getCODEFAILTIMES());
					String pwdStts  = this.compseCustStts("pwd", resBean.getPWDFAILTIMES());
					dataBean.setCustStts(codeStts + "," + pwdStts);
				}else{
					//v1.01
					LOG.debug("RESULT:"+resBean.getResult()+"/ERRORMSG="+resBean.getErrorMsg());
					dataBean.setCustStts(resBean.getErrorMsg());
				}
			}catch(Exception e){
				//連線網銀異常
				LOG.error("Exception:"+e.toString(), e);
				dataBean.setCustStts(I18nConverter.i18nMapping("message.F0202.wsFail", user.getLocale()));
			}
			
			srvSttsMgmtSrv.querySrvSttsData(dataBean);
			
			// ========== 敏感戶查詢處理 ==========
			if (dataBean.getCustData() != null) {
				Map<String, String> custIds = new HashMap<String, String>();
				custIds.put(custId, custId);
				CommonUtil.checkTxncsSvip(custIds, custId, "", dataBean.getFuncId(), "");
			}
			
		} catch (BusinessException e) {
			LOG.error("[SrvSttsMgmt query BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[SrvSttsMgmt query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 修改綁定帳號服務狀態
	 * @param command
	 */
	public void updateAcntStts(Command command, HttpServletRequest req) throws BusinessException, FrameException {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		CustPltf dataBean = (CustPltf) command;
		
		try {
			
			srvSttsMgmtSrv.updateAcntStts(dataBean);
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.0001", user.getLocale()));//平台/帳號服務狀態異動成功
			//更新 command 內容 for 操作記錄, 記錄異動後資料
			srvSttsMgmtSrv.recordAfterOpt(dataBean);
			
		} catch (BusinessException e) {
			LOG.error("[updateAcntStts BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[updateAcntStts Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 啟用 custData 服務狀態
	 * @param command
	 */
	public void updateOpenCustStts(Command command) throws BusinessException, FrameException {
		
		CustPltf dataBean = (CustPltf) command;
		
		try {
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();
			//設定交易關鍵值、狀態
			dataBean.setFnKeyValue(dataBean.getCustData().getCustId());
			log.setCustId(dataBean.getCustData().getCustId());
			log.setStts("00");
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
			//00:啟用, 01:暫停
			String msg = srvSttsMgmtSrv.updateCustStts("00", dataBean.getCustData().getCustId());
			dataBean.setReturnMsg(msg);
			//更新 command 內容 for 操作記錄, 記錄異動後資料
			
			srvSttsMgmtSrv.recordAfterOpt(dataBean);
		} catch (BusinessException e) {
			LOG.error("[updateOpenCustStts BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[updateOpenCustStts Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 暫停 custData 服務狀態
	 * @param command
	 */
	public void updateStopCustStts(Command command) throws BusinessException, FrameException {
		
		CustPltf dataBean = (CustPltf) command;
		
		try {
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();	
			//設定交易關鍵值、狀態
			dataBean.setFnKeyValue(dataBean.getCustData().getCustId());
			log.setCustId(dataBean.getCustData().getCustId());
			log.setStts("01");
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
			//00:啟用, 01:暫停
			String msg = srvSttsMgmtSrv.updateCustStts("01", dataBean.getCustData().getCustId());
			dataBean.setReturnMsg(msg);
			//更新 command 內容 for 操作記錄, 記錄異動後資料
			srvSttsMgmtSrv.recordAfterOpt(dataBean);
			
		} catch (BusinessException e) {
			LOG.error("[updateStopCustStts BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[updateStopCustStts Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 會員狀態解鎖
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void updateLockCustStts(Command command) throws BusinessException, FrameException {
		CustPltf dataBean = (CustPltf) command;
		
		AuthenticationHelper helper = null;
		WSResBean resBean = null;
		
		try {
			LOG.debug("custId=" + dataBean.getCustData().getCustId());
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();	
			//設定交易關鍵值、狀態
			dataBean.setFnKeyValue(dataBean.getCustData().getCustId());
			log.setCustId(dataBean.getCustData().getCustId());
			log.setStts(dataBean.getCustStts()); //v1.01
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
			try {
				String rtnMsg = "";
				helper = new AuthenticationHelper();
				resBean = helper.unlock(dataBean.getCustData().getCustId());
				
				//0：失敗、1：成功、2：首次登入
				if ("1".equals(resBean.getResult())) {
					rtnMsg = I18nConverter.i18nMapping("message.sys.0004", new Locale("zh", "TW"));
					
				} else {
					//v1.01
					LOG.debug("RESULT:"+resBean.getResult()+"/ERRORMSG="+resBean.getErrorMsg());
					throw new BusinessException(resBean.getErrorMsg(), "", null, null);
				}
				
				dataBean.setReturnMsg(rtnMsg);
				
			} catch (Exception e) {
				LOG.error("[發送 Web service 失敗]: " + e.getMessage(), e);
				//v1.01
				throw new BusinessException("message.F0202.wsFail");
			}
			
			//更新 command 內容 for 操作記錄, 記錄異動後資料
			srvSttsMgmtSrv.recordAfterOpt(dataBean);
			
		} catch (BusinessException e) {
			LOG.error("[updateStopCustStts BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[updateStopCustStts Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 兜組會員狀態
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
