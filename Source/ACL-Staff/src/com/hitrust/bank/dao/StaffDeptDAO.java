/**
 * @(#) StaffRoleDAO.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, Nov 16, 2015, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.StaffDept;
import com.hitrust.framework.dao.BaseDAO;

public interface StaffDeptDAO extends BaseDAO {
	
	/**
	 * 取得所有部門代號
	 * @return List<StaffRole> or null
	 */
	List<StaffDept> queryStaffDepts();

}
