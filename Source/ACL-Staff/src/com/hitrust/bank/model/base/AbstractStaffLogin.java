/**
 * @(#) StaffLogin.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/28, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

public class AbstractStaffLogin extends AclCommand implements Serializable {

	private static final long serialVersionUID = 8403862136041354186L;
	
	// =============== Table Attribute ===============
	private String userId;	  // 使用者代號
	private String loginStts; // 登入狀態
	private String loginTime; // 最近登入時間 
	private String createTime;// 首次登入時間
	private String sessionId; // 登入SESSION
	
	// =============== Getter & Setter ===============
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginStts() {
		return loginStts;
	}
	public void setLoginStts(String loginStts) {
		this.loginStts = loginStts;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
