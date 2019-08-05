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

public class AbstractStaffRole extends AclCommand implements Serializable {

	private static final long serialVersionUID = 8089769460572840844L;
	
	// =============== Table Attribute ===============
	private String roleId;	// 角色代碼
	private String roleName;// 角色名稱 

	// =============== Getter & Setter ===============
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
