/**
 * @(#) LoginUser.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/28, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.service.LoginSettingsSrv;
import com.hitrust.framework.APSystem;
import com.hitrust.framework.model.BaseCommand;

public class LoginUser extends BaseCommand implements HttpSessionBindingListener {

	private static final long serialVersionUID = -3743148259738479952L;
	
	// Log4j
	private static Logger LOG = Logger.getLogger(LoginUser.class);
	
	// =============== User information ===============
	private String userId;	  	  // 使用者代碼
	private String userName;	  // 使用者名稱
	private String ip;			  // 使用者登入 IP
	private String loginStts; 	  // 登入狀態
	private String lastLoginTime; // 最後登入時間 
	private String jsonMenu;	  // 使用者授權 Menu
	private String sessionId;	  // Session Id
	private List<String> rights;  // 使用者受權功能清單
	private String roleId; // 使用者受權角色
	private Locale locale;		  // 使用者語系
	private int pageRow;		  // 分頁顯示筆數
    private List<String> viewLimitIds;  // 限閱戶清單
    private String changeMemaDate; //變更密碼時間
    private String userStts;	   //使用者狀態
    private String brchBank = null;		//所屬分行
	
	
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		
	}
	
	/**
	 * Session 銷毀/同名稱的屬性被異動時才觸發 valueUnbound
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		
		// 更新使用者登入狀態
		if (!StringUtil.isBlank(userId)) {
			LOG.info("========== [使用者代碼]: " + userId + " session id: " +  sessionId + " destroyed, 並將使用者登入狀態更新為 \"已登出\" ========== ");
			
			try {
				
				LoginSettingsSrv loginSettingsSrv = (LoginSettingsSrv) APSystem.getApplicationContext().getBean("loginSettingsSrv");
				loginSettingsSrv.updateLoginSttsByUserId(userId, "N");
				
			} catch (Exception e) {
				LOG.error("[valueUnbound Exception]: ", e);
			}
		}
	
	}
	
	// =============== Getter & Setter ===============

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getLoginStts() {
		return loginStts;
	}
	public void setLoginStts(String loginStts) {
		this.loginStts = loginStts;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getJsonMenu() {
		return jsonMenu;
	}
	public void setJsonMenu(String jsonMenu) {
		this.jsonMenu = jsonMenu;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public List<String> getRights() {
		return rights;
	}
	public void setRights(List<String> rights) {
		this.rights = rights;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public int getPageRow() {
		return pageRow;
	}
	public void setPageRow(int pageRow) {
		this.pageRow = pageRow;
	}
	public List<String> getViewLimitIds() {
		return viewLimitIds;
	}
	public void setViewLimitIds(List<String> viewLimitIds) {
		this.viewLimitIds = viewLimitIds;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getChangeMemaDate() {
		return changeMemaDate;
	}
	public void setChangeMemaDate(String changeMemaDate) {
		this.changeMemaDate = changeMemaDate;
	}
	public String getUserStts() {
		return userStts;
	}
	public void setUserStts(String userStts) {
		this.userStts = userStts;
	}
	
	public String getBrchBank() {
		return brchBank;
	}

	public void setBrchBank(String brchBank) {
		this.brchBank = brchBank;
	}

}
