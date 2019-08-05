/**
 * @(#) LogoutController.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016年2月16日, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */
package com.hitrust.bank.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.model.Login;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.service.LoginSettingsSrv;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.util.Constants;
import com.hitrust.framework.web.BaseAutoCommandController;

public class LogoutController extends BaseAutoCommandController {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(LogoutController.class);

	// service injection
	private LoginSettingsSrv loginSettingsSrv;
		
	public LogoutController() {
		super.setCommandClass(Login.class);
	}
	
	/**
	 * 系統登出
	 * @param command
	 * @param req
	 */
	public void logout(Command command, HttpServletRequest req) {
		
		// 取得目前 session 物件
		HttpSession session = req.getSession(false);
		
		LoginUser user = (LoginUser) session.getAttribute(Constants.LOGIN_USER);
		
		if (!StringUtil.isBlank(user)) {
			LOG.info("========== [使用者]: " + user.getUserId() + " 登出系統 ========== ");

			// 更新使用者登入狀態
			loginSettingsSrv.updateLoginSttsByUserId(user.getUserId(), "N");
			
			session.invalidate();
			
		} else {
			LOG.info("========== [Session 不存在...] ========== ");
		}
	}
	
	// =============== bean injection ===============
	public void setLoginSettingsSrv(LoginSettingsSrv loginSettingsSrv) {
		this.loginSettingsSrv = loginSettingsSrv;
	}
}
