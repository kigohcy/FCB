/**
 * @(#)RoleSetSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 角色設定srv實作
 * 
 * Modify History:
 *  v1.00, 2016/01/25, Jimmy
 *   1) First release
 *  
 */
package com.hitrust.bank.service.impl;

import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.hitrust.bank.dao.StaffDeptDAO;
import com.hitrust.bank.dao.StaffRoleDAO;
import com.hitrust.bank.dao.StaffUserDAO;
import com.hitrust.bank.model.StaffDept;
import com.hitrust.bank.model.StaffRole;
import com.hitrust.bank.model.StaffUser;
import com.hitrust.bank.service.UserSetSrv;
import com.hitrust.framework.exception.BusinessException;

public class UserSetSrvImpl implements UserSetSrv {
	// ============================== Log4j ==============================
	private Category		LOG	= Logger.getLogger(this.getClass());
	private StaffUserDAO	staffUserDAO;
	private StaffDeptDAO	staffDeptDAO;
	private StaffRoleDAO	staffRoleDAO;

	// ============================== injection beans ==============================
	public void setStaffUserDAO(StaffUserDAO staffUserDAO) {
		this.staffUserDAO = staffUserDAO;
	}

	public void setStaffDeptDAO(StaffDeptDAO staffDeptDAO) {
		this.staffDeptDAO = staffDeptDAO;
	}

	public void setStaffRoleDAO(StaffRoleDAO staffRoleDAO) {
		this.staffRoleDAO = staffRoleDAO;
	}
	// ============================== implements ==============================

	/**
	 * 查詢所有使用者清單
	 * 
	 * @return List<StaffUser>
	 */
	@Override
	public List<StaffUser> queryUsers() {
		List<StaffUser> userList = staffUserDAO.queryStaffUsers();
		return userList;
	}

	/**
	 * 查詢使用者
	 * 
	 * @param loginId
	 * @return StaffUser
	 */
	@Override
	public StaffUser queryUsers(String loginId) {
		Object staffUser = staffUserDAO.queryById(StaffUser.class, loginId);
		if (null == staffUser) {
			LOG.info("資料庫無 " + loginId + " 的資料，無法查詢。");
			throw new BusinessException("message.db.have.no.data");
		}
		return (StaffUser) staffUser;
	}

	/**
	 * 查詢Login使用者
	 * 
	 * @param loginId
	 * @return StaffUser
	 */
	@Override
	public StaffUser queryLoginUsers(String loginId) {
		return (StaffUser) staffUserDAO.queryById(StaffUser.class, loginId);
	}

	/**
	 * 查詢新增使用者
	 * 
	 * @param loginId
	 * @return StaffUser
	 */
	@Override
	public StaffUser queryAddUsers(String loginId) {
		return (StaffUser) staffUserDAO.queryById(StaffUser.class, loginId);
	}

	/**
	 * 查詢所有角色清單
	 * 
	 * @return List<StaffDept>
	 */
	@Override
	public List<StaffDept> queryDepts() {
		List<StaffDept> deptList = staffDeptDAO.queryStaffDepts();
		return deptList;
	}

	/**
	 * 查詢所有角色清單
	 * 
	 * @return List<StaffRole>
	 */
	@Override
	public List<StaffRole> queryRoles() {
		List<StaffRole> roleList = staffRoleDAO.queryStaffRoles();
		return roleList;
	}

	/**
	 * 新增User
	 * 
	 * @param staffuser
	 */
	public void saveNewUser(StaffUser staffuser) {
		staffDeptDAO.save(staffuser);
	}

	/**
	 * 修改User
	 * 
	 * @param staffuser
	 */
	public void updateUser(StaffUser staffuser) {
		staffDeptDAO.update(staffuser);
	}
}
