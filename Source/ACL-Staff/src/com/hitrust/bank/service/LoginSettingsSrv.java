/**
 * @(#) LoginSettingsSrv.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/16, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.service;

import java.util.List;

import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffSysFnct;
import com.hitrust.bank.model.StaffSysMenu;
import com.hitrust.bank.model.SysParm;

public interface LoginSettingsSrv {
	
	/**
	 * 依據使用者代號取得登入資訊
	 * @param userId	使用者代號
	 * @return LoginUser or null
	 */
	public LoginUser fetchStaffLogin(String userId); 
	
	/**
	 * 依據 roleIds 查詢資料是否存在
	 * @param roleIds	角色代碼
	 * @return true: 存在, false: 不存在
	 */
	public boolean fetchStaffRoleByIds(String roleId); 
	
	/**
	 * 依據 角色代碼 & 語系 取得 Menu 受權清單
	 * @param roleIds	角色代碼
	 * @param lngn		使用者語系
	 * @return List<StaffSysMenu>
	 */
	public List<StaffSysMenu> fetchStaffSysMenuList(String roleId, String lngn);
	
	
	/**
	 * 依據 角色代碼 & 語系 取得功能清單
	 * @param roleIds	角色代碼
	 * @param lngn		使用者語系
	 * @return List<StaffSysFnct>
	 */
	public List<StaffSysFnct> fetchStaffSysFnctList(String roleId, String lngn);
	
	
	/**
	 * 依據指定參數名稱, 取得系統參數值
	 * @param parm	參數名稱
	 * @return SysParm or null
	 */
	public SysParm fetchSysparmByPram(String parm);
	
	/**
	 * 新增使用者登入資料
	 * @param userId	使用者代號
	 * @param session	session Id
	 * @return LoginUser or null
	 */
	public LoginUser insertStaffLogin(String userId, String sessionId);
	
	/**
	 * 更新使用者登入資料
	 * @param userId	 使用者代號
	 * @param sessionId	 session Id
	 * @return LoginUser or null
	 */
	public LoginUser updateStaffLogin(String userId, String sessionId);
	
	/**
	 * 依據使用者 ID 更新登入狀態
	 * @param userId
	 * @param stts	 Y:已登入，N:未登入
	 */
	public void updateLoginSttsByUserId(String userId, String stts);
}
