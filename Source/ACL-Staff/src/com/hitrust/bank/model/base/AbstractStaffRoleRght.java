/**
 * @(#) AbstractStaffRoleRght.java
 * 行員角色權限
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

public class AbstractStaffRoleRght extends AclCommand implements Serializable {

	private static final long serialVersionUID = -2406198256064918788L;

	// =============== KEY ===============
	private AbstractStaffRoleRght.Id id; // 複合 KEY
	
	// =============== Getter & Setter ===============
	public AbstractStaffRoleRght.Id getId() {
		return id;
	}
	public void setId(AbstractStaffRoleRght.Id id) {
		this.id = id;
	}

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = 5643927707165637750L;
		
		// =============== Table Attribute ===============
		private String roleId; // 角色代碼 
		private String fnctId; // 功能代碼 
		
		// =============== Getter & Setter ===============
		public String getRoleId() {
			return roleId;
		}
		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}
		public String getFnctId() {
			return fnctId;
		}
		public void setFnctId(String fnctId) {
			this.fnctId = fnctId;
		}
	}
	
}
