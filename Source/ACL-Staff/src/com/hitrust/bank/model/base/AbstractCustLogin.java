/**
 * @(#)AbstractCustLogin.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustLogin base model
 * 
 * Modify History:
 *  v1.00, 2016/06/06, Yann
 *   1) First release, 二階
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

/**
 * CustLogin generated by hbm2java
 */
public class AbstractCustLogin extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = -6878100998717584238L;
	
	// =============== Table Attribute ===============
	private String userId;
	private String loginStts;
	private String loginTime;
	private String createTime;
	private String sessionId;

	// =============== Getter & Setter ===============
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginStts() {
		return this.loginStts;
	}

	public void setLoginStts(String loginStts) {
		this.loginStts = loginStts;
	}

	public String getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
