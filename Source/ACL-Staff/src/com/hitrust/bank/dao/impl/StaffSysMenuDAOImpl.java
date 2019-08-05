/**
 * @(#) StaffSysMenuDAOImpl.java
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.StaffSysMenuDAO;
import com.hitrust.bank.model.StaffSysMenu;
import com.hitrust.framework.criterion.HqlDetachedCriteria;
import com.hitrust.framework.criterion.HqlRestrictions;
import com.hitrust.framework.criterion.ReturnEntry;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class StaffSysMenuDAOImpl extends BaseDAOImpl implements StaffSysMenuDAO {

	/**
	 * 依據 角色代碼 & 語系 取得 Menu 受權清單
	 * 
	 * @param roleIds	角色代碼
	 * @param lngn		使用者語系
	 * @return List<StaffSysMenu>
	 */
	@Override
	public List<StaffSysMenu> fetchStaffSysMenuList(String roleId, String lngn) {

		HqlDetachedCriteria criteria = HqlDetachedCriteria.forEntityNames(new String[] { "StaffSysMenu A", "StaffSysFnct B", "StaffRoleRght C" });
		criteria.add(HqlRestrictions.eq("A.id.menuId", "{B.menuId}"));
		criteria.add(HqlRestrictions.eq("A.id.lngn", lngn));
		criteria.add(HqlRestrictions.eq("B.id.lngn", lngn));
		criteria.add(HqlRestrictions.eq("B.isUse", "Y"));
		criteria.add(HqlRestrictions.eq("C.id.fnctId", "{B.id.fnctId}"));
		criteria.add(HqlRestrictions.eq("C.id.roleId", roleId));
		criteria.setReturnEntry(ReturnEntry.forName("distinct A"));

		return query(criteria);
	}

	/**
	 * 依據語系查詢模組清單
	 * 
	 * @param language 使用者語系
	 * @return List<StaffSysMenu>
	 */
	@Override
	public List<StaffSysMenu> queryStaffSysMenu(String language) {
		DetachedCriteria dc = DetachedCriteria.forClass(StaffSysMenu.class);
		dc.add(Restrictions.eq("id.lngn", language));
		dc.addOrder(Order.asc("serl"));
		return this.query(dc);
	}
}
