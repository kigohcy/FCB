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

public class AbstractStaffDept extends AclCommand implements Serializable {

	private static final long serialVersionUID = 6563972264984151849L;
	
	// =============== Table Attribute ===============
	private String deptId; 		//部門代號
	private String deptName; 	//部門名稱
	
	// =============== Getter & Setter ===============
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
	
}
