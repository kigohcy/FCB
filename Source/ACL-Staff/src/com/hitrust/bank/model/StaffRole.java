/**
 * @(#) StaffRole.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, Nov 16, 2015, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model;

import java.util.List;

import com.hitrust.bank.model.base.AbstractStaffRole;

public class StaffRole extends AbstractStaffRole {

	private static final long serialVersionUID = 4112760509400868639L;
	
	// =============== Not Table Attribute ===============
	List<StaffRole> roleList;			//所有角色清單
	List<StaffSysMenu> staffSysMenu;	//角色被授權的模組清單
	String[] fnctIds;					//角色被授權的功能清單
	
	// =============== Getters & Setters ===============
	public List<StaffRole> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<StaffRole> roleList) {
		this.roleList = roleList;
	}
	public List<StaffSysMenu> getStaffSysMenu() {
		return staffSysMenu;
	}
	public void setStaffSysMenu(List<StaffSysMenu> staffSysMenu) {
		this.staffSysMenu = staffSysMenu;
	}
	public String[] getFnctIds() {
		return fnctIds;
	}
	public void setFnctIds(String[] fnctIds) {
		this.fnctIds = fnctIds;
	}
}
