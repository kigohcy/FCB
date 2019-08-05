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

import com.hitrust.bank.dao.StaffDeptDAO;
import com.hitrust.bank.dao.StaffUserDAO;
import com.hitrust.bank.model.StaffDept;
import com.hitrust.bank.model.StaffRole;
import com.hitrust.bank.model.StaffUser;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class StaffDeptDAOImpl extends BaseDAOImpl implements StaffDeptDAO {

	/**
	 * 取得所有角色清單
	 * @return List<StaffRole> or null
	 */
	@Override
	public List<StaffDept> queryStaffDepts() {
		DetachedCriteria dc=DetachedCriteria.forClass(StaffDept.class);
		dc.addOrder(Order.asc("deptId"));
		return this.query(dc);
	}
}
