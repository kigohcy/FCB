/**
 * @(#) StaffRoleDAOImpl.java
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

import com.hitrust.bank.dao.StaffRoleDAO;
import com.hitrust.bank.model.StaffRole;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class StaffRoleDAOImpl extends BaseDAOImpl implements StaffRoleDAO {

	/**
	 * 取得所有角色清單
	 * @return List<StaffRole> or null
	 */
	@Override
	public List<StaffRole> queryStaffRoles() {
		DetachedCriteria dc=DetachedCriteria.forClass(StaffRole.class);
		dc.add(Restrictions.ne("roleId", "ADMIN"));
		dc.addOrder(Order.asc("roleId"));
		
		return this.query(dc);
	}

	/**
	 * 依據 roleIds 查詢資料是否存在
	 * @param roleIds	角色代碼
	 * @return true: 存在, false: 不存在
	 */
	@Override
	public boolean fetchStaffRoleByIds(String roleId) {
		boolean result = false;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffRole.class);
		criteria.add(Restrictions.eq("roleId", roleId));
		
		List<StaffRole> roles = this.query(criteria);
		
		if (roles.size() > 0) {
			result = true;
		}
		
		return result;
	}
}
