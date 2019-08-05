/**
 * @(#) StaffRoleRght.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/16, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model;

import org.springframework.beans.BeanUtils;

import com.hitrust.bank.model.base.AbstractStaffRoleRght;

public class StaffRoleRght extends AbstractStaffRoleRght {

	private static final long serialVersionUID = 1595796804118620894L;
	
	// =============== Not Table Attribute ===============
	private String menuId;	// 功能模組代號

	// =============== Constructor ===============
	public StaffRoleRght() {}
	
	public StaffRoleRght(StaffRoleRght rght, String menuId) {
		BeanUtils.copyProperties(rght, this);
		this.menuId = menuId;
	}
	
	// =============== Getters & Setters ===============
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
}
