/**
 * @(#)RoleSetSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 角色設定srv
 * 
 * Modify History:
 *  v1.00, 2016/01/25, Jimmy
 *   1) First release
 *  
 */
package com.hitrust.bank.service;

import java.util.List;

import com.hitrust.bank.model.StaffDept;
import com.hitrust.bank.model.StaffRole;
import com.hitrust.bank.model.StaffUser;

public interface UserSetSrv {
	/**
	 * 查詢所有使用者清單
	 * 
	 * @return List<StaffUser>
	 */
	public List<StaffUser> queryUsers();

	/**
	 * 查詢使用者
	 * 
	 * @param loginId
	 * @return StaffUser
	 */
	public StaffUser queryUsers(String loginId);

	/**
	 * 查詢Login使用者
	 * 
	 * @param loginId
	 * @return StaffUser
	 */
	public StaffUser queryLoginUsers(String loginId);

	/**
	 * 查詢新增使用者
	 * 
	 * @param loginId
	 * @return StaffUser
	 */
	public StaffUser queryAddUsers(String loginId);

	/**
	 * 查詢所有部門清單
	 * 
	 * @return List<StaffDept>
	 */
	public List<StaffDept> queryDepts();

	/**
	 * 查詢所有角色清單
	 * 
	 * @return List<StaffRole>
	 */
	public List<StaffRole> queryRoles();

	/**
	 * 新增使用者
	 * 
	 * @param staffuser
	 */
	public void saveNewUser(StaffUser staffuser);

	/**
	 * 修改使用者
	 * 
	 * @param staffuser
	 */
	public void updateUser(StaffUser staffuser);
}
