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

import com.hitrust.bank.model.StaffRole;
import com.hitrust.bank.model.StaffSysMenu;
import com.hitrust.bank.model.StaffUser;

public interface RoleSetSrv {

	/**
	 * 查詢所有角色清單
	 * 
	 * @return List<StaffRole>
	 */
	public List<StaffRole> queryRoles();

	/**
	 * 新增角色初始化
	 * 
	 * @return List<StaffSysMenu>
	 */
	public List<StaffSysMenu> addRoleInit();

	/**
	 * 新增角色
	 * 
	 * @param roleId 角色代號
	 * @param roleName 角色名稱
	 * @param fnctIds 開放功能清單
	 */
	public void addRole(String roleId, String roleName, String[] fnctIds);
	
	/**
	 * 根據角色代號查詢角色以及權限列表
	 * 
	 * @param roleId 角色代號
	 * @return
	 */
	public StaffRole queryRoleFcntList(String roleId);

	/**
	 * 修改角色設定
	 * 
	 * @param roleId 角色代號
	 * @param roleName 角色名稱
	 * @param fnctIds 開放功能清單
	 */
	public void updateRole(String roleId, String roleName, String[] fnctIds);

	/**
	 * 根據角色代號刪除一個角色
	 * 
	 * @param roleId 角色代號
	 */
	public void deleteRole(String roleId);
	
	/**
	 * 查詢功能檢視權限列表
	 * 
	 * @param roleId 角色代號
	 * @return StaffRole
	 */
	public StaffRole queryFcntView(String roleId);
}
