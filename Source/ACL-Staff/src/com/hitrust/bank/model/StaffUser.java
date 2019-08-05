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

import java.util.ArrayList;
import java.util.List;

import com.hitrust.bank.model.base.AbstractStaffUser;

public class StaffUser extends AbstractStaffUser {

	private static final long serialVersionUID = -5512392891720081795L;
	
	List<StaffUser> userList;		//所有使用者清單
	List<StaffDept> deptList;		//所有部門清單
	List<StaffRole> roleList;		//所有權限清單
	List<UserStates> statesList;
	
	public List<StaffUser> getUserList() {
		return userList;
	}

	public void setUserList(List<StaffUser> userList) {
		this.userList = userList;
	}

	public List<StaffDept> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<StaffDept> deptList) {
		this.deptList = deptList;
	}

	public List<StaffRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<StaffRole> roleList) {
		this.roleList = roleList;
	}

	public List<UserStates> getStatesList() {
		return statesList;
	}

	public void setStatesList(String s) {
		String[] items = s.split(";");
		List<UserStates> list = new ArrayList<>();
		for(int i=0;i<items.length;i++) {
			UserStates us = new UserStates();
			String[] nextItems = items[i].split(",");
			us.setUserStates(nextItems[0].trim());
			us.setStatesName(nextItems[1].trim());
			list.add(i, us);
		}
		this.statesList = list;
	}
}
