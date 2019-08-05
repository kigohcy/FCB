/**
 * @(#) TsbApauditlogSrvImpl.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/18, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */
package com.hitrust.bank.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.TsbApauditlogDAO;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.TsbApauditlog;
import com.hitrust.bank.model.base.AbstractTsbApauditlog.Id;
import com.hitrust.bank.service.TsbApauditlogSrv;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.BaseCommand;

public class TsbApauditlogSrvImpl implements TsbApauditlogSrv {

	// Log4j
	private static Logger LOG = Logger.getLogger(TsbApauditlogSrvImpl.class);
	
	// DAO injection
	private TsbApauditlogDAO tsbApauditlogDAO;

	/**
	 * 新增應用系統日誌
	 * @param rslt	  交易執行結果, 成功: Y, 失敗: N
	 * @param rsltMsg 交易執行失敗錯誤代碼或訊息
	 * @param commnd
	 * @param req
	 */
	@Override
	public void insertTsbApauditLog(String rslt, String rsltMsg, BaseCommand commnd, HttpServletRequest req) {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		String funcId = commnd.getFuncId();
		String operate = commnd.getOperate();
		String fnKeyValue = commnd.getFnKeyValue();
		String fnProc = commnd.getFnProc();
		String userId = (StringUtil.isBlank(user)) ? req.getParameter("userId") : user.getUserId();
		String ip = (StringUtil.isBlank(user)) ? req.getRemoteAddr() : user.getIp();
		String errMsg = "";
		Locale locale = (StringUtil.isBlank(user)) ? new Locale("zh", "TW") : user.getLocale();
		
		String funcName = I18nConverter.i18nMapping("function.Id." + funcId, locale);
		
		if ("N".equals(rslt)) {
			errMsg = I18nConverter.i18nMapping(rsltMsg, locale);
		}
		
		Id id = new Id();
		id.setProjCode("ACLINK");
		id.setUserId(userId);
		id.setProcDatetime(DateUtil.getCurrentTime("DT", "AD"));
		id.setClientIp(ip);
		id.setFnType(operate.startsWith("E") ? "E" : operate);
		id.setFnName(funcName);
		id.setFnStts(("Y".equals(rslt)) ? rslt : errMsg);
		id.setFnKeyvalue(fnKeyValue);
		
		try {
			if (StringUtil.isBlank(fnProc) || fnProc.getBytes("UTF-8").length <= 500) {
				id.setFnProc(fnProc);
			} else {
				LOG.info("[輸入欄位長度超過 500]:" + fnProc);
			}
			
		} catch (UnsupportedEncodingException e) {
			LOG.error("[insertTsbApauditLog UnsupportedEncodingException]: ", e); 
		}
		
		
		TsbApauditlog apauditlog = new TsbApauditlog();
		apauditlog.setId(id);
		
		tsbApauditlogDAO.save(apauditlog);
	}

	// =============== injection bean ===============
	public void setTsbApauditlogDAO(TsbApauditlogDAO tsbApauditlogDAO) {
		this.tsbApauditlogDAO = tsbApauditlogDAO;
	}

}
