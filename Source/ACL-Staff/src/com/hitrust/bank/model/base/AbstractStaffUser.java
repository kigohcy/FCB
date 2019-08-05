/**
 * @(#) AbstractStaffRole.java
 * 行員角色 
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/16, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

public class AbstractStaffUser extends AclCommand implements Serializable {

	private static final long serialVersionUID = -9223244656181976533L;
	
	// =============== Table Attribute ===============
	private String loginId; 	//登入代碼
	private String loginMema; 	//登入密碼
	private String loginMema1; 	//登入密碼1
	private String loginMema2; 	//登入密碼2
	private String loginMema3; 	//登入密碼3
	private String deptId;		//部門代號
	private String userId;		//員工代號
	private String userName;		//員工名稱
	private String roleId;		//權限代碼
	private String email;		//email
	private String states;		//狀態 "A"dd:新建, "D"isable:終止, "S"top:暫停, "L"ock:鎖定, "E"nable:啟用
	private String createDate;	//建檔時間
	private String updateDate;	//變更時間
	private String finalLoginDate;	//最後登入時間
	private String changeMemaDate; //變更密碼時間
	private int errorMemaCount;		//密碼錯誤次數
	
	// =============== Getter & Setter ===============
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLoginMema() {
		return loginMema;
	}
	public void setLoginMema(String loginMema) {
		this.loginMema = loginMema;
	}
	public String getLoginMema1() {
		return loginMema1;
	}
	public void setLoginMema1(String loginMema1) {
		this.loginMema1 = loginMema1;
	}
	public String getLoginMema2() {
		return loginMema2;
	}
	public void setLoginMema2(String loginMema2) {
		this.loginMema2 = loginMema2;
	}
	public String getLoginMema3() {
		return loginMema3;
	}
	public void setLoginMema3(String loginMema3) {
		this.loginMema3 = loginMema3;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
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
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStates() {
		return states;
	}
	public void setStates(String states) {
		this.states = states;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getFinalLoginDate() {
		return finalLoginDate;
	}
	public void setFinalLoginDate(String finalLoginDate) {
		this.finalLoginDate = finalLoginDate;
	}
	public int getErrorMemaCount() {
		return errorMemaCount;
	}
	public void setErrorMemaCount(int errorMemaCount) {
		this.errorMemaCount = errorMemaCount;
	}
	public String getChangeMemaDate() {
		return changeMemaDate;
	}
	public void setChangeMemaDate(String changeMemaDate) {
		this.changeMemaDate = changeMemaDate;
	}
}
