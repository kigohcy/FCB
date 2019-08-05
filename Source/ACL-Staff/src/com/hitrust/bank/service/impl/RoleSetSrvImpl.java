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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.hitrust.bank.dao.StaffRoleDAO;
import com.hitrust.bank.dao.StaffRoleRghtDAO;
import com.hitrust.bank.dao.StaffSysFnctDAO;
import com.hitrust.bank.dao.StaffSysMenuDAO;
import com.hitrust.bank.dao.StaffUserDAO;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffRole;
import com.hitrust.bank.model.StaffRoleRght;
import com.hitrust.bank.model.StaffSysFnct;
import com.hitrust.bank.model.StaffSysMenu;
import com.hitrust.bank.model.StaffUser;
import com.hitrust.bank.model.base.AbstractStaffRoleRght.Id;
import com.hitrust.bank.service.RoleSetSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;

public class RoleSetSrvImpl implements RoleSetSrv {
	//============================== Log4j ==============================
	private Category LOG = Logger.getLogger(this.getClass());

	//============================== DAO injection ==============================
	private StaffRoleDAO staffRoleDAO;
	private StaffRoleRghtDAO staffRoleRghtDAO;
	private StaffSysMenuDAO staffSysMenuDAO;
	private StaffSysFnctDAO staffSysFnctDAO;
	// ============================== injection beans ==============================
	public void setStaffRoleDAO(StaffRoleDAO staffRoleDAO) {
		this.staffRoleDAO = staffRoleDAO;
	}

	public void setStaffRoleRghtDAO(StaffRoleRghtDAO staffRoleRghtDAO) {
		this.staffRoleRghtDAO = staffRoleRghtDAO;
	}

	public void setStaffSysMenuDAO(StaffSysMenuDAO staffSysMenuDAO) {
		this.staffSysMenuDAO = staffSysMenuDAO;
	}

	public void setStaffSysFnctDAO(StaffSysFnctDAO staffSysFnctDAO) {
		this.staffSysFnctDAO = staffSysFnctDAO;
	}

	//============================== implements ==============================
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
	 * 新增角色初始化
	 * 
	 * @return List<StaffSysMenu>
	 */
	@Override
	public List<StaffSysMenu> addRoleInit() {
		LOG.info("新增角色初始化");
		
		List<StaffSysMenu> menuList = new ArrayList<StaffSysMenu>();
		
		//根據使用者所對應的語系查詢模組清單
		List<StaffSysMenu> menus = this.getMenusByUserLang();

		if (menus != null) {
			StaffSysMenu staffSysMenu = new StaffSysMenu();
			
			//查詢各模組下的功能清單
			for (int i = 0; i < menus.size(); i++) {
				staffSysMenu = (StaffSysMenu) menus.get(i);
				String menuId = staffSysMenu.getId().getMenuId();
				
				//根據模組代號取得功能清單
				List<StaffSysFnct> fncts = staffSysFnctDAO.queryStaffSysFnct(menuId);
				
				staffSysMenu.setFncts(fncts);
				
				menuList.add(staffSysMenu);
			}
		}
		return menuList;
	}

	/**
	 * 新增角色
	 * 
	 * @param roleId 角色代號
	 * @param roleName 角色名稱
	 * @param fnctIds 開放功能清單
	 */
	public void addRole(String roleId, String roleName, String[] fnctIds) {
		LOG.info("新增角色開始");
		
		// 驗證該筆角色資料是否已存在於資料庫當中
		StaffRole staffRole = (StaffRole) staffRoleDAO.queryById(StaffRole.class, roleId);
		
		if (staffRole != null) {// 資料庫已存該筆資料
			LOG.error("資料庫有" + roleId + "的資料,無法新增");
			throw new BusinessException("message.db.have.data");
		} else {// 資料庫無該筆角色資料
			staffRole = new StaffRole();
			staffRole.setRoleId(roleId);
			staffRole.setRoleName(roleName);
			
			try {
				//寫入角色資料
				staffRoleDAO.save(staffRole);
			} catch (Exception e) {
				// 新增失敗
				LOG.error(roleId + "新增失敗",e);
				throw new BusinessException("message.sys.insert.failure");
			}
			
			//寫入該角色被授權功能設定
			if (fnctIds != null) {
				//寫入該角色被重新授權功能設定
				this.saveRoleRights(roleId, fnctIds);
			}
		}
	}
	
	/**
	 * 根據角色代號查詢角色以及權限列表
	 * 
	 * @param roleId 角色代號
	 * @return StaffRole
	 */
	public StaffRole queryRoleFcntList(String roleId) {
		LOG.info("修改角色初始化");
		
		// 驗證該筆角色資料是否已存在於資料庫當中
		StaffRole staffRole = (StaffRole) staffRoleDAO.queryById(StaffRole.class, roleId);
		
		if (staffRole == null) {// 資料庫無該筆角色資料
			LOG.error("資料庫無" + roleId + "的資料,無法更新");
			throw new BusinessException("message.db.have.no.data");
		} else {// 資料庫已存該筆資料
			//根據角色代號取得授權功能清單
			List<StaffRoleRght> roleRghts = staffRoleRghtDAO.queryByRoleId(staffRole.getRoleId());
			
			List<String> funcIds = new ArrayList<String>();
			for (int i = 0; i < roleRghts.size(); i++) {
				funcIds.add(roleRghts.get(i).getId().getFnctId());
			}
			
			//根據使用者所對應的語系查詢模組清單
			List<StaffSysMenu> menus = this.getMenusByUserLang();
			
			if (menus != null) {
				List<StaffSysMenu> rtnMenus = new ArrayList<StaffSysMenu>();
				StaffSysMenu staffSysMenu = null;
				
				for (int i = 0; menus != null & i < menus.size(); i++) {
					staffSysMenu = (StaffSysMenu) menus.get(i);
					List<StaffSysFnct> rtnSysFuncts = new ArrayList<StaffSysFnct>();
					
					//根據模組代號取得功能清單
					List<StaffSysFnct> staffSysFncts = staffSysFnctDAO.queryStaffSysFnct(staffSysMenu.getId().getMenuId());
					for (int j = 0; staffSysFncts != null & j < staffSysFncts.size(); j++) {
						StaffSysFnct sysFnct = staffSysFncts.get(j);
						
						//若該角色被授權該功能則checkFlag設為true作為畫面顯示的依據
						if (funcIds.contains(sysFnct.getId().getFnctId())) {
							sysFnct.setCheckFlag(true);
						}
						rtnSysFuncts.add(sysFnct);
					}
					staffSysMenu.setFncts(rtnSysFuncts);
					rtnMenus.add(staffSysMenu);
				}
				staffRole.setStaffSysMenu(rtnMenus);
			}
		}
		return staffRole;
	}

	/**
	 * 修改角色設定
	 * 
	 * @param roleId 角色代號
	 * @param roleName 角色名稱
	 * @param fnctIds 開放功能清單
	 */
	public void updateRole(String roleId, String roleName, String[] fnctIds) {
		LOG.info("修改角色開始");
		
		// 驗證該筆角色資料是否已存在於資料庫當中
		StaffRole staffRole = (StaffRole) staffRoleDAO.queryById(StaffRole.class, roleId);
		
		if (staffRole == null) {// 資料庫無該筆角色資料
			LOG.error("資料庫無" + roleId + "的資料,無法更新");
			throw new BusinessException("message.db.have.no.data");
		} else {// 資料庫已存該筆資料
			staffRole.setRoleName(roleName);
			
			//更新該角色代號所對應的角色名稱
			try {
				staffRoleDAO.update(staffRole);
			} catch (Exception e) {
				// 修改失敗
				LOG.error(roleId + "修改失敗",e);
				throw new BusinessException("message.sys.update.failure");
			}
			
			//刪除該角色代號舊有的授權功能設定
			try {
				staffRoleRghtDAO.deleteByRoleId(roleId);
			} catch (Exception e) {
				// 修改失敗
				LOG.error(roleId + "權限刪除失敗",e);
				throw new BusinessException("message.sys.update.failure");
			}
			
			//寫入該角色被重新授權功能設定
			if (fnctIds != null) {
				//寫入該角色被重新授權功能設定
				this.saveRoleRights(roleId, fnctIds);
			}
		}
	}
	
	/**
	 * 根據角色代號刪除一個角色
	 * 
	 * @param roleId 角色代號
	 */
	public void deleteRole(String roleId) {
		LOG.info("刪除角色");
		
		// 驗證該筆角色資料是否已存在於資料庫當中
		StaffRole staffRole = (StaffRole) staffRoleDAO.queryById(StaffRole.class, roleId);
		
		if (staffRole == null) {// 資料庫無該筆角色資料
			LOG.error("資料庫無" + roleId + "的資料,無法刪除");
			throw new BusinessException("message.db.have.no.data");
		} else {// 資料庫已存該筆資料
			
			//刪除角色資料
			try {
				staffRoleDAO.delete(staffRole);
			} catch (Exception e) {
				LOG.error(roleId + "刪除失敗",e);
				throw new BusinessException("message.sys.delete.failure");
			}
			
			//刪除功能授權設定
			try {
				staffRoleRghtDAO.deleteByRoleId(staffRole.getRoleId());
			} catch (Exception e) {
				LOG.error(roleId + "權限刪除失敗",e);
				throw new BusinessException("message.sys.delete.failure");
			}
		}
	}

	/**
	 * 查詢功能檢視權限列表
	 * 
	 * @param roleId 角色代號
	 * @return StaffRole
	 */
	public StaffRole queryFcntView(String roleId) {
		LOG.info("功能檢視");
		
		// 驗證該筆角色資料是否已存在於資料庫當中
		StaffRole staffRole = (StaffRole) staffRoleDAO.queryById(StaffRole.class, roleId);
		
		if (staffRole == null) {// 資料庫無該筆角色資料
			LOG.error("資料庫無" + roleId + "的資料,無法查詢功能檢視");
			throw new BusinessException("message.db.have.no.data");// 資料庫無資料
		} else {// 資料庫已存該筆資料
			//根據角色代號取得授權功能清單
			List<StaffRoleRght> roleRghts = staffRoleRghtDAO.queryByRoleId(roleId);
			
			if (roleRghts.size() > 0) {
				//根據角色代號查詢角色以及權限列表
				staffRole = this.queryRoleFcntList(roleId);
				List<StaffSysMenu> menuList = staffRole.getStaffSysMenu();
				
				for (int i = 0; i < menuList.size(); i++) {
					List<StaffSysFnct> rtnSysFuncts = new ArrayList<StaffSysFnct>();
					
					//當checkFlag為true才顯示在畫面上
					List<StaffSysFnct> sysFuncts = menuList.get(i).getFncts();
					for (int j = 0; sysFuncts != null & j < sysFuncts.size(); j++) {
						if (sysFuncts.get(j).isCheckFlag() == true) {
							rtnSysFuncts.add(sysFuncts.get(j));
						}
					}
					menuList.get(i).setFncts(rtnSysFuncts);
				}
			}
		}
		return staffRole;
	}
	
	/**
	 * 寫入角色被重新授權功能設定
	 * 
	 * @param roleId 角色代號
	 * @param fnctIds 角色被授權的功能清單
	 */
	private void saveRoleRights(String roleId, String[] fnctIds){
		for (int i = 0; i < fnctIds.length; i++) {
			StaffRoleRght roleRght = new StaffRoleRght();
			StaffRoleRght.Id id = new Id();
			id.setFnctId(fnctIds[i]);
			id.setRoleId(roleId);
			roleRght.setId(id);
			
			try {
				staffRoleRghtDAO.save(roleRght);
			} catch (Exception e) {
				LOG.error(roleId + "功能授權失敗",e);
				throw new BusinessException("message.sys.insert.failure");
			}
		}
	}
	
	/**
	 * 根據使用者所對應的語系查詢模組清單
	 * 
	 * @return List<StaffSysMenu>
	 */
	private List<StaffSysMenu> getMenusByUserLang(){
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		String lang = user.getLocale().getLanguage() + "_" + user.getLocale().getCountry();
		return staffSysMenuDAO.queryStaffSysMenu(lang);
	}
}
