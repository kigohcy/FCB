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

import com.hitrust.bank.dao.StaffUserDAO;
import com.hitrust.bank.model.StaffRole;
import com.hitrust.bank.model.StaffUser;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class StaffUserDAOImpl extends BaseDAOImpl implements StaffUserDAO {

	/**
	 * 取得所有角色清單
	 * @return List<StaffRole> or null
	 */
	@Override
	public List<StaffUser> queryStaffUsers() {
		DetachedCriteria dc=DetachedCriteria.forClass(StaffUser.class);
		dc.addOrder(Order.asc("deptId"));
		dc.addOrder(Order.asc("loginId"));
		return this.query(dc);
	}
}
