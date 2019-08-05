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

import com.hitrust.bank.model.StaffRole;
import com.hitrust.framework.dao.BaseDAO;

public interface StaffRoleDAO extends BaseDAO {
	
	/**
	 * 取得所有角色清單
	 * @return List<StaffRole> or null
	 */
	List<StaffRole> queryStaffRoles();

	/**
	 * 依據 roleIds 查詢資料是否存在
	 * @param roleIds	角色代碼
	 * @return true: 存在, false: 不存在
	 */
	public boolean fetchStaffRoleByIds(String roleId ); 
}
