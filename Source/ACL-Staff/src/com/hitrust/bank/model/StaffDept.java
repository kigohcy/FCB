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

import com.hitrust.bank.model.base.AbstractStaffDept;


public class StaffDept extends AbstractStaffDept {

	private static final long serialVersionUID = 6927700742682225234L;
	
	List<StaffDept> userList;		//所有使用者清單
	List<StaffDept> deptList;
	
	public List<StaffDept> getUserList() {
		return userList;
	}

	public void setUserList(List<StaffDept> userList) {
		this.userList = userList;
	}
	
	
}
