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

import com.hitrust.bank.model.StaffUser;
import com.hitrust.framework.dao.BaseDAO;

public interface StaffUserDAO extends BaseDAO {
	
	/**
	 * 取得所有使用者
	 * @return List<StaffUser> or null
	 */
	List<StaffUser> queryStaffUsers();
}
