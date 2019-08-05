/**
 * @(#) StaffSysMenuDAO.java
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

import com.hitrust.bank.model.StaffSysMenu;
import com.hitrust.framework.dao.BaseDAO;

public interface StaffSysMenuDAO extends BaseDAO {

	/**
	 * 依據 角色代碼 & 語系 取得 Menu 受權清單
	 * 
	 * @param roleIds	角色代碼
	 * @param lngn		使用者語系
	 * @return List<StaffSysMenu>
	 */
	public List<StaffSysMenu> fetchStaffSysMenuList(String roleId, String lngn);

	/**
	 * 依據語系查詢模組清單
	 * 
	 * @param language 使用者語系
	 * @return List<StaffSysMenu> or null
	 */
	public List<StaffSysMenu> queryStaffSysMenu(String language);
}
