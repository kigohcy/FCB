/**
 * @(#) StaffSysFnctDAOImpl.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/13, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.StaffSysFnctDAO;
import com.hitrust.bank.model.StaffSysFnct;
import com.hitrust.framework.criterion.HqlDetachedCriteria;
import com.hitrust.framework.criterion.HqlRestrictions;
import com.hitrust.framework.criterion.OrderBy;
import com.hitrust.framework.criterion.ReturnEntry;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class StaffSysFnctDAOImpl extends BaseDAOImpl implements StaffSysFnctDAO {

	/**
	 * 依據 角色代碼 & 語系 取得功能清單
	 * 
	 * @param roleIds	角色代碼
	 * @param lngn		使用者語系
	 * @return List<StaffSysFnct>
	 */
	@Override
	public List<StaffSysFnct> fetchStaffSysFnctList(String roleId, String lngn) {

		HqlDetachedCriteria criteria = HqlDetachedCriteria.forEntityNames(new String[] { "StaffSysFnct A", "StaffRoleRght B" });
		criteria.add(HqlRestrictions.eq("A.isUse", "Y"));
		criteria.add(HqlRestrictions.eq("A.id.fnctId", "{B.id.fnctId}"));
		criteria.add(HqlRestrictions.eq("A.id.lngn", lngn));
		criteria.add(HqlRestrictions.eq("B.id.roleId", roleId));
		criteria.addOrder(OrderBy.asc("A.serl"));
		criteria.setReturnEntry(ReturnEntry.forName("distinct A"));
		
		return query(criteria);
	}

	/**
	 * 取得功能代碼清單
	 * 
	 * @return
	 */
	@Override
	public List<StaffSysFnct> getEnableStaffSysFnctList(String language) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StaffSysFnct.class);
		detachedCriteria.add(Restrictions.eq("id.lngn", language));
		detachedCriteria.add(Restrictions.eq("isUse", "Y"));
		detachedCriteria.addOrder(Order.asc("id.fnctId"));
		return query(detachedCriteria);
	}

	/**
	 * 依據模組代號查詢功能清單
	 * 
	 * @param menuId 模組代號
	 * @return List<StaffSysFnct>
	 */
	@Override
	public List<StaffSysFnct> queryStaffSysFnct(String menuId) {
		DetachedCriteria dc = DetachedCriteria.forClass(StaffSysFnct.class);
		dc.add(Restrictions.eq("menuId", menuId));
		dc.add(Restrictions.eq("isUse", "Y"));
		dc.add(Restrictions.eq("isSet", "Y"));
		dc.addOrder(Order.asc("serl"));
		return this.query(dc);
	}
}
