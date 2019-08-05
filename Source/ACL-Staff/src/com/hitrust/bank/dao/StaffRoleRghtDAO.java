/**
 * @(#) StaffRoleRghtDAO.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/16, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.StaffRoleRght;
import com.hitrust.framework.dao.BaseDAO;

public interface StaffRoleRghtDAO extends BaseDAO {

	/**
	 * 依據 角色代碼 取得受權 Menu List
	 * 
	 * @param roleId	角色代碼
	 * @return	回傳 List<StaffRoleRght>
	 */
	public List<StaffRoleRght> fetchRoleRghtList(String roleId);

	/**
	 * 根據角色代號刪除授權功能
	 * 
	 * @param roleId 角色代碼
	 */
	public void deleteByRoleId(String roleId);
	
	/**
	 * 根據角色代號取得授權功能
	 * 
	 * @param roleId 角色代碼
	 * @return List<StaffRoleRght>
	 */
	public List<StaffRoleRght> queryByRoleId(String roleId);
}
