/**
 * @(#) Login.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00,2015/11/05, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model;

public class Login extends AclCommand {

	private static final long serialVersionUID = -3169983293309424723L;
	
	// =============== Input Attribute ===============
	private String userId; // 使用者代碼
	private String userPswd; // 使用者密碼
	private String roleId;	 // 角色代碼
	private String kaptcha;  // 圖形驗證
	
	// =============== output Attribute ===============
	private String jsonMenu; // 使用者受權 Menu
	
	// =============== Getter & Setter ===============
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPswd() {
		return userPswd;
	}
	public void setUserPswd(String userPswd) {
		this.userPswd = userPswd;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getJsonMenu() {
		return jsonMenu;
	}
	public void setJsonMenu(String jsonMenu) {
		this.jsonMenu = jsonMenu;
	}
	public String getKaptcha() {
		return kaptcha;
	}
	public void setKaptcha(String kaptcha) {
		this.kaptcha = kaptcha;
	}

}
