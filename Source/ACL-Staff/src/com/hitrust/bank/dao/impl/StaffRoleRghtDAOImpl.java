/**
 * @(#) StaffRoleRghtDAOImpl.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/16, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.StaffRoleRghtDAO;
import com.hitrust.bank.model.StaffRoleRght;
import com.hitrust.framework.criterion.HqlDetachedCriteria;
import com.hitrust.framework.criterion.HqlRestrictions;
import com.hitrust.framework.criterion.ReturnEntry;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class StaffRoleRghtDAOImpl extends BaseDAOImpl implements StaffRoleRghtDAO {

	/**
	 * 依據 角色代碼 取得受權 Menu List
	 * 
	 * @param roleId	角色代碼
	 * @return	回傳 List<StaffRoleRght>
	 */
	@Override
	public List<StaffRoleRght> fetchRoleRghtList(String roleId) {
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[]{"StaffRoleRght a", "StaffSysFnct b"});
		hqlDc.add(HqlRestrictions.eq("a.id.fnctId", "{b.fnctId}"));
		hqlDc.add(HqlRestrictions.eq("a.id.roleId", roleId));
		hqlDc.setReturnEntry(ReturnEntry.forName("new StaffRoleRght(a, b.menuId)"));

		return query(hqlDc);
	}
	
	/**
	 * 根據角色代號刪除授權功能
	 * 
	 * @param roleId 角色代碼
	 */
	@Override
	public void deleteByRoleId(String roleId) {
		String hql = "delete from StaffRoleRght where ROLE_ID = '" + roleId + "'";
		bulkUpdate(hql);
	}

	/**
	 * 根據角色代號取得授權功能
	 * 
	 * @param roleId 角色代碼
	 * @return List<StaffRoleRght>
	 */
	@Override
	public List<StaffRoleRght> queryByRoleId(String roleId) {
		DetachedCriteria dc = DetachedCriteria.forClass(StaffRoleRght.class);
		dc.add(Restrictions.eq("id.roleId", roleId));
		return query(dc);
	}
}
