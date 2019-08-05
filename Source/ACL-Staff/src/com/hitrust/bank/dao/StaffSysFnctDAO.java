/**
 * @(#) StaffSysFnctDAO.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/13, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.StaffSysFnct;
import com.hitrust.framework.dao.BaseDAO;

public interface StaffSysFnctDAO extends BaseDAO {

	/**
	 * 依據 角色代碼 & 語系 取得功能清單
	 * 
	 * @param roleIds 角色代碼
	 * @param lngn 使用者語系
	 * @return List<StaffSysFnct>
	 */
	public List<StaffSysFnct> fetchStaffSysFnctList(String roleId, String lngn);

	/**
	 * 取得功代碼清單
	 * 
	 * @return
	 */
	public List<StaffSysFnct> getEnableStaffSysFnctList(String language);

	/**
	 * 依據模組代號查詢功能清單
	 * 
	 * @param menuId 模組代號
	 * @return List<StaffSysFnct>
	 */
	public List<StaffSysFnct> queryStaffSysFnct(String menuId);
}
