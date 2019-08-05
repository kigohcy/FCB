/**
 * @(#) StaffSysMenu.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/13, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.hitrust.bank.model.base.AbstractStaffSysMenu;

public class StaffSysMenu extends AbstractStaffSysMenu {

	private static final long serialVersionUID = -8184015737505744538L;

	// =============== Not Table Attribute ===============
	List<StaffSysFnct> fncts; //該模組底下的功能清單
	
	// =============== Getters & Setters ===============
	public List<StaffSysFnct> getFncts() {
		return fncts;
	}

	public void setFncts(List<StaffSysFnct> fncts) {
		this.fncts = fncts;
	}

	// =============== Constructor ===============
	public StaffSysMenu() {}
	
	public StaffSysMenu(StaffSysMenu menu) {
		BeanUtils.copyProperties(menu, this);
	}
}
