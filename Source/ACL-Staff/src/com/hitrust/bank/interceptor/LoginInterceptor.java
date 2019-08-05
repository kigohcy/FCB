/**
 * @(#) LoginInterceptor.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/28, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.exception.NoPermissionException;
import com.hitrust.acl.exception.SessionException;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.StaffLoginDAO;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffLogin;
import com.hitrust.framework.APSystem;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.util.Constants;
import com.hitrust.framework.web.Anonymous;

public class LoginInterceptor implements HandlerInterceptor {
	
	private static Logger LOG = Logger.getLogger(LoginInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object obj, Exception exception) throws Exception {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object obj, ModelAndView view) throws Exception {
		
		// do Nothing
	}

	/**
	 * 請求提交之前處理
	 * @param req
	 * @param res
	 * @param obj
	 */
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception {
		
		String url = req.getServletPath().replaceAll(".html", "");
		String fnctId = (String) APSystem.getUrlAttributes(url).get("function");
		String method = (String) APSystem.getUrlAttributes(url).get("method");
		String result = (String) APSystem.getUrlAttributes(url).get("result");
		String error  = (String) APSystem.getUrlAttributes(url).get("error");
		boolean isDuplicate = false; // 是否重復登入, 或使用相同 Browser 登入相同使用者
		
		LOG.info("[url]: " + url + " [fnctId]: " + fnctId + " [method]: " + method + " [result]: " + result + " [error]: " + error);
		
		HttpSession session = req.getSession(true);

		if (!(obj instanceof Anonymous)) {
			
			// 取得 session 中 LoginUser 物件
			LoginUser user = (LoginUser) session.getAttribute(Constants.LOGIN_USER);

			try {
				// 檢核是否未經過登入, 就直接拋 Exception
				if (StringUtil.isBlank(user)) {
					throw new SessionException("message.sys.noLogin");
					
				} else {
					// 檢核是否為重復登入
					LOG.info("========== 檢核是否為重復登入 ==========");
					
					isDuplicate = this.checkSession(user.getUserId(), req.getParameter("sessionKey"));
					
					if (isDuplicate) {
						throw new SessionException("message.sys.noLogin");
					}
					
					// 設定分頁顯示筆數
					if (Page.getDefaultItemsPerPage() != user.getPageRow()) {
						Page.setDefaultItemsPerPage(user.getPageRow());
					}
					
					// 將 session Keep 在 AP 端
					APLogin.set(user);
				}
				
				// 檢核使用者目前操作功能是否已受權
				LOG.info("[USER_ID]: " + user.getUserId() + " [FNCTID]: " + fnctId);
				if (this.permissionCheck(user, fnctId)) {
					session.invalidate();
					throw new NoPermissionException("message.sys.noPermission");
				}
				
			} catch (SessionException e) {
				throw e;
				
			} catch (NoPermissionException e) {
				throw e;
				
			} catch (Exception e) {
				LOG.error("[LoginInterceptor preHandle Exception]: ", e);
				throw new FrameException("message.sys.err");
				
			} finally {
				req.setAttribute("_fnctId", fnctId);
			}
			
		} else {
			LOG.info("[URL]: " + req.getServletPath());
		}
		
		return true;
	}
	
	/**
	 * 檢核目前 SessionId, 是否為目前使用者所有
	 * @param userId
	 * @param sessionId
	 * @return
	 */
	private boolean checkSession(String userId, String sessionId) {
		
		// 從 Spring pool 取得指定 bean
		StaffLoginDAO staffLoginDAO = (StaffLoginDAO) APSystem.getApplicationContext().getBean("staffLoginDAO");
		
		try {
			StaffLogin login = staffLoginDAO.fetchStaffLogin(userId);
			
			if (!StringUtil.isBlank(login)) {
				if (StringUtil.isBlank(sessionId) || !sessionId.equals(login.getSessionId())) {
					LOG.info("Session ID 不一致 [STAFF_LOGIN.SESSON_ID: " + login.getSessionId() + "][Session: " + sessionId);
					return true;
				}
			}
			
			return false;
			
		} catch (Exception e) {
			LOG.error("[checkSession Exception]: ", e);
			return true;
		}
		
	}
	
	/**
	 * 檢核該功能是否有受權給使用者
	 * @param user
	 * @param fnctId
	 * @return
	 */
	private boolean permissionCheck(LoginUser user, String fnctId) {
		List<String> rights = user.getRights();
		
		if (!rights.contains(fnctId)) {
			
			return true;
		}
		
		return false;
	}

}
