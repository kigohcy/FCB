/**
 * @(#) StaffSysFnct.java
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

import org.springframework.beans.BeanUtils;

import com.hitrust.bank.model.base.AbstractStaffSysFnct;

public class StaffSysFnct extends AbstractStaffSysFnct {

	private static final long serialVersionUID = 5727560534513812885L;

	// =============== Not Table Attribute ===============
	boolean checkFlag;	//判斷該功能選項是否被勾選
	
	// =============== Constructor ===============
	
	public StaffSysFnct() {}
	
	public StaffSysFnct(StaffSysFnct fnct) {
		BeanUtils.copyProperties(fnct, this);
	}
	
	//=============== Getters & Setters ===============
	public boolean isCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(boolean checkFlag) {
		this.checkFlag = checkFlag;
	}
}
