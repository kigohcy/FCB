/**
 * @(#) LoginSettingsSrvImpl.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/16, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.service.impl;

import java.util.List;
import org.apache.log4j.Logger;

import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.dao.StaffLoginDAO;
import com.hitrust.bank.dao.StaffRoleDAO;
import com.hitrust.bank.dao.StaffSysFnctDAO;
import com.hitrust.bank.dao.StaffSysMenuDAO;
import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffLogin;
import com.hitrust.bank.model.StaffSysFnct;
import com.hitrust.bank.model.StaffSysMenu;
import com.hitrust.bank.model.SysParm;
import com.hitrust.bank.service.LoginSettingsSrv;

public class LoginSettingsSrvImpl implements LoginSettingsSrv {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(LoginSettingsSrvImpl.class);
	
	// DAO injection
	private StaffSysFnctDAO staffSysFnctDAO;
	private StaffSysMenuDAO staffSysMenuDAO;
	private StaffLoginDAO staffLoginDAO;
	private StaffRoleDAO staffRoleDAO;
	private SysParmDAO sysParmDAO;
	
	/**
	 * 依據使用者代號取得登入資訊
	 * @param userId	使用者代號
	 * @return LoginUser or null
	 */
	@Override
	public LoginUser fetchStaffLogin(String userId) {
		LOG.info("[取得使用者代碼]: " + userId + " 登入控制檔資訊");
		
		StaffLogin login = staffLoginDAO.fetchStaffLogin(userId);
		
		LoginUser user = null;
		
		if (StringUtil.isBlank(login)) {
			return user;
		} else {
			user = new LoginUser();
			
			String loginTime = DateUtil.formateDateTimeForUser(login.getLoginTime());
			user.setUserId(login.getUserId());
			user.setLoginStts(login.getLoginStts());
			user.setLastLoginTime(loginTime);
			user.setSessionId(login.getSessionId());
		}
		
		return user;
	}
	
	/**
	 * 依據 角色代碼 & 語系 取得 Menu 受權清單
	 * @param roleIds	角色代碼
	 * @param lngn		使用者語系
	 * @return List<StaffSysMenu>
	 */
	@Override
	public List<StaffSysMenu> fetchStaffSysMenuList(String roleId, String lngn) {
		List<StaffSysMenu> menus = staffSysMenuDAO.fetchStaffSysMenuList(roleId, lngn);
		return menus;
	}
	
	/**
	 * 依據 角色代碼 & 語系 取得功能清單
	 * @param roleIds	角色代碼
	 * @param lngn		使用者語系
	 * @return List<StaffSysFnct>
	 */
	@Override
	public List<StaffSysFnct> fetchStaffSysFnctList(String roleId, String lngn) {
		List<StaffSysFnct> fncts = staffSysFnctDAO.fetchStaffSysFnctList(roleId, lngn);
		return fncts;
	}
	
	/**
	 * 依據指定參數名稱, 取得系統參數值
	 * @param parm	參數名稱
	 * @return SysParm or null
	 */
	@Override
	public SysParm fetchSysparmByPram(String parm) {
		SysParm sysParm = null;
		
		sysParm = sysParmDAO.fetchSysParmByParm(parm);
		
		return sysParm;
	}
	
	/**
	 * 新增使用者登入資料
	 * @param userId	使用者代號
	 * @param session	session Id
	 * @return LoginUser or null
	 */
	@Override
	public LoginUser insertStaffLogin(String userId, String sessionId) {
		LOG.info("[新者使用者代碼]: " + userId + ", 登入控制資料");
		
		StaffLogin login = new StaffLogin();
		
		LoginUser user = null;

		// 產生 6 碼亂數
		String rmVal = CommonUtil.randomGenerator(6);
		
		login.setUserId(userId);
		login.setLoginStts("Y");
		login.setLoginTime(DateUtil.getCurrentTime("DT", "AD"));
		login.setCreateTime(DateUtil.getCurrentTime("DT", "AD"));
		login.setSessionId(sessionId + userId + rmVal);
		
		staffLoginDAO.save(login);
		
		user = new LoginUser();
		user.setUserId(login.getUserId());
		user.setLoginStts(login.getLoginStts());
		user.setLastLoginTime(DateUtil.formateDateTimeForUser(login.getLoginTime()));
		user.setSessionId(login.getSessionId());
		
		return user;
	}
	
	/**
	 * 更新使用者登入資料
	 * @param userId	 使用者代號
	 * @param sessionId	 session Id
	 * @return LoginUser or null
	 */
	public LoginUser updateStaffLogin(String userId, String sessionId) {
		LOG.info("[更新使用者代號]: " + userId + ", 登入控制資料");
		
		StaffLogin login = staffLoginDAO.fetchStaffLogin(userId);
		
		LoginUser user = null;
		
		// 產生 6 碼亂數
		String rmVal = CommonUtil.randomGenerator(6);
		
		if (!StringUtil.isBlank(login)) {
			login.setLoginStts("Y");
			login.setLoginTime(DateUtil.getCurrentTime("DT", "AD"));
			login.setSessionId(sessionId + userId + rmVal);
			staffLoginDAO.update(login);
			
			user = new LoginUser();
			user.setUserId(login.getUserId());
			user.setLoginStts(login.getLoginStts());
			user.setLastLoginTime(DateUtil.formateDateTimeForUser(login.getLoginTime()));
			user.setSessionId(login.getSessionId());
		}
		
		return user;
	}
	
	/**
	 * 依據 roleIds 查詢資料是否存在
	 * @param roleIds	角色代碼
	 * @return true: 存在, false: 不存在
	 */
	@Override
	public boolean fetchStaffRoleByIds(String roleId) {
		
		boolean result = false;
		
		result = staffRoleDAO.fetchStaffRoleByIds(roleId);
		
		LOG.info("[使用者授權角色是否存在]: " + result);
		
		return result;
	}

	/**
	 * 依據使用者 ID 更新登入狀態
	 * @param userId
	 * @param stts	 Y:已登入，N:未登入
	 */
	@Override
	public void updateLoginSttsByUserId(String userId, String stts) {
		LOG.info("[User_Id]: " + userId);
		
		StaffLogin login = (StaffLogin) staffLoginDAO.queryById(StaffLogin.class, userId);
		
		if (!StringUtil.isBlank(login)) {
			login.setLoginStts(stts);
			staffLoginDAO.update(login);
		}
	}
	

	// =============== injection bean ===============
	public void setStaffSysFnctDAO(StaffSysFnctDAO staffSysFnctDAO) {
		this.staffSysFnctDAO = staffSysFnctDAO;
	}
	public void setStaffSysMenuDAO(StaffSysMenuDAO staffSysMenuDAO) {
		this.staffSysMenuDAO = staffSysMenuDAO;
	}
	public void setStaffLoginDAO(StaffLoginDAO staffLoginDAO) {
		this.staffLoginDAO = staffLoginDAO;
	}
	public void setStaffRoleDAO(StaffRoleDAO staffRoleDAO) {
		this.staffRoleDAO = staffRoleDAO;
	}
	public void setSysParmDAO(SysParmDAO sysParmDAO) {
		this.sysParmDAO = sysParmDAO;
	}

}
