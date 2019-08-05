/**
 * @(#)RoleSetController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 角色設定controller
 * 
 * Modify History:
 *  v1.00, 2016/01/25, Jimmy
 *   1) First release
 *  
 */
package com.hitrust.bank.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.json.TsbAuditLogDetl;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffRole;
import com.hitrust.bank.model.StaffSysFnct;
import com.hitrust.bank.model.StaffSysMenu;
import com.hitrust.bank.service.RoleSetSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class RoleSetController extends BaseAutoCommandController {

	// ============================== Log4j ==============================
	private Category LOG = Logger.getLogger(this.getClass());

	// ============================== service injection ==============================
	private RoleSetSrv roleSetSrv;

	// ============================== injection beans ==============================
	public void setRoleSetSrv(RoleSetSrv roleSetSrv) {
		this.roleSetSrv = roleSetSrv;
	}

	// ============================== Constructor ==============================
	public RoleSetController() {
		setCommandClass(StaffRole.class);
	}

	/**
	 * 查詢角色清單
	 * 
	 * @param command
	 */
	public void roleQuery(Command command) throws BusinessException, FrameException {
		LOG.info("查詢角色清單");
		StaffRole staffRole = (StaffRole) command;
		
		try {
			
			List<StaffRole> roleList = roleSetSrv.queryRoles();
			
			staffRole.setRoleList(roleList);
		
		} catch (BusinessException e) {
			LOG.error("[roleQuery BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[roleQuery Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 新增角色初始化
	 * 
	 * @param command
	 */
	public void roleAddInit(Command command) throws BusinessException, FrameException {
		LOG.info("新增角色初始化");
		StaffRole staffRole = (StaffRole) command;
		
		try {
			
			List<StaffSysMenu> menuList = roleSetSrv.addRoleInit();

			staffRole.setStaffSysMenu(menuList);
			
		} catch (BusinessException e) {
			LOG.error("[roleAddInit BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[roleAddInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}

	/**
	 * 新增角色設定
	 * 
	 * @param command
	 */
	public void roleAdd(Command command) throws BusinessException, FrameException {
		LOG.info("新增角色設定");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		StaffRole staffRole = (StaffRole) command;
		
		try {
			
			// 準備操作記錄資料
			modifyAfterCommnd(staffRole);
			String roleId = staffRole.getRoleId().trim();
			String roleName = staffRole.getRoleName();
			String[] fnctIds = staffRole.getFnctIds();
			
			//記錄應用系統日誌(TSB_APAUDITLOG)
			this.audiLog(staffRole);
			
			// 新增角色設定
			roleSetSrv.addRole(roleId, roleName, fnctIds);

			// 回覆新增成功
			staffRole.setReturnMsg(I18nConverter.i18nMapping("message.sys.insert.success", user.getLocale()));
			
		} catch (BusinessException e) {
			LOG.error("[roleAdd BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[roleAdd Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		 
	}

	/**
	 * 角色修改初始化
	 * 
	 * @param command
	 */
	public void roleUpdateInit(Command command) throws BusinessException, FrameException {
		LOG.info("角色修改初始化");
		StaffRole staffRole = (StaffRole) command;
		
		try {
			
			//根據角色代號查詢角色以及權限列表
			StaffRole rtnRole = roleSetSrv.queryRoleFcntList(staffRole.getRoleId());
			
			staffRole.setStaffSysMenu(rtnRole.getStaffSysMenu());
			staffRole.setRoleId(rtnRole.getRoleId());
			staffRole.setRoleName(rtnRole.getRoleName());
			
		} catch (BusinessException e) {
			LOG.error("[roleUpdateInit BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[roleUpdateInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 修改角色設定
	 * 
	 * @param command
	 */
	public void roleUpdate(Command command) throws BusinessException, FrameException {
		LOG.info("修改角色設定");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		StaffRole staffRole = (StaffRole) command;

		try {
			
			// 準備操作記錄資料
			modifyAfterCommnd(staffRole);
			String roleId = staffRole.getRoleId().trim();
			String roleName = staffRole.getRoleName();
			String[] fnctIds = staffRole.getFnctIds();
			
			//記錄應用系統日誌(TSB_APAUDITLOG)
			this.audiLog(staffRole);
			
			// 修改角色設定
			roleSetSrv.updateRole(roleId, roleName, fnctIds);
			
			// 回覆修改成功
			staffRole.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", user.getLocale()));
			
		} catch (BusinessException e) {
			LOG.error("[roleUpdate BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[roleUpdate Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 刪除角色設定
	 * 
	 * @param command
	 */
	public void roleDelete(Command command) throws BusinessException, FrameException {
		LOG.info("刪除角色設定");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		StaffRole staffRole = (StaffRole) command;
		
		try {
			
			//記錄應用系統日誌(TSB_APAUDITLOG)
			this.audiLog(staffRole);
			
			// 刪除角色設定
			roleSetSrv.deleteRole(staffRole.getRoleId());

			// 回覆刪除成功
			staffRole.setReturnMsg(I18nConverter.i18nMapping("message.sys.delete.success", user.getLocale()));
			
		} catch (BusinessException e) {
			LOG.error("[roleDelete BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[roleDelete Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}

	/**
	 * 功能檢視
	 * 
	 * @param command
	 */
	public void functionView(Command command) throws BusinessException, FrameException {
		LOG.info("功能檢視");
		StaffRole staffRole = (StaffRole) command;
		
		try {
			
			//記錄應用系統日誌(TSB_APAUDITLOG)
			this.audiLog(staffRole);
		
			//顯示該使用這代號被授權的功能清單
			StaffRole rtnRole = roleSetSrv.queryFcntView(staffRole.getRoleId());

			staffRole.setStaffSysMenu(rtnRole.getStaffSysMenu());
			staffRole.setRoleId(rtnRole.getRoleId());
			staffRole.setRoleName(rtnRole.getRoleName());
			
		} catch (BusinessException e) {
			LOG.error("[functionView BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[functionView Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}

	/**
	 * 異動修改後 BaseCommand
	 * 
	 * @param role
	 */
	private void modifyAfterCommnd(StaffRole role)  throws BusinessException, FrameException {
		List<String> funcIds = Arrays.asList(role.getFnctIds());
		List<StaffSysMenu> menus = role.getStaffSysMenu();

		try {
			
			for (int i = 0; i < menus.size(); i++) {
				StaffSysMenu menu = menus.get(i);
				List<StaffSysFnct> fncts = menu.getFncts();

				for (int j = 0; j < fncts.size(); j++) {
					if (funcIds.contains(fncts.get(j).getId().getFnctId())) {
						fncts.get(j).setCheckFlag(true);
					} else {
						fncts.get(j).setCheckFlag(false);
					}
				}
			}
			
		} catch (BusinessException e) {
			LOG.error("[role modifyAfterCommnd BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[role modifyAfterCommnd Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 記錄應用系統日誌(TSB_APAUDITLOG)
	 * @param staffRole command
	 */
	private void audiLog(StaffRole staffRole) throws BusinessException, FrameException {
		
		try{
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();
			log.setRoleId(staffRole.getRoleId());
			
			String operateArray [] = {"Q", "D"};
		    List<String> operateList = Arrays.asList(operateArray);
		    
		    //Q:功能檢視、D:刪除
			if(!operateList.contains(staffRole.getOperate())){	
				
				TsbAuditLogDetl logDetl = null;
				//設定交易log
				log.setTsbAuditLogDetl(new ArrayList<>());
				log.setRoleName(staffRole.getRoleName());
				
				//只記五筆
				for(String fnctId : staffRole.getFnctIds()){
					
					if(log.getTsbAuditLogDetl().size() == 5){
						break;
					}
					logDetl = new TsbAuditLogDetl();
					logDetl.setFnctId(fnctId);
					log.getTsbAuditLogDetl().add(logDetl);
				}
			}
			
			staffRole.setFnProc(JsonUtil.object2Json(log, false));
		
		} catch (BusinessException e) {
			LOG.error("[staffRole audiLog BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[EstaffRolecData audiLog Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
}
