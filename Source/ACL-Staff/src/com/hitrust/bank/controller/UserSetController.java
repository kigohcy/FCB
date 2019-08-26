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

import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.AppEnv;
import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.mail.MailUtil;
import com.hitrust.acl.util.MAC;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffDept;
import com.hitrust.bank.model.StaffRole;
import com.hitrust.bank.model.StaffUser;

import com.hitrust.bank.service.UserSetSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.util.DateUtil;
import com.hitrust.framework.web.BaseAutoCommandController;


public class UserSetController extends BaseAutoCommandController {

	// ============================== Log4j ==============================
	private Category LOG = Logger.getLogger(this.getClass());

	// ============================== service injection ==============================
	private UserSetSrv userSetSrv;

	// ============================== injection beans ==============================
	public void setUserSetSrv(UserSetSrv userSetSrv) {
		this.userSetSrv = userSetSrv;
	}

	// ============================== Constructor ==============================
	public UserSetController() {
		setCommandClass(StaffUser.class);
	}

	/**
	 * 查詢角色清單
	 * 
	 * @param command
	 */
	public void userQuery(Command command) throws BusinessException, FrameException {
		LOG.info("查詢角色清單");
		StaffUser staffuser = (StaffUser) command;
		
		try {
			List<StaffUser> userList = userSetSrv.queryUsers();
			List<StaffDept> deptList = userSetSrv.queryDepts();
			List<StaffRole> roleList = userSetSrv.queryRoles();
			for(StaffUser staffUser : userList) {
				for(StaffDept staffDept : deptList) {
					if(staffDept.getDeptId().equals(staffUser.getDeptId())) {
						staffUser.setDeptId(staffDept.getDeptName());
					}
				}
				for(StaffRole staffRole : roleList) {
					if(staffRole.getRoleId().equals(staffUser.getRoleId())) {
						staffUser.setRoleId(staffRole.getRoleName());
					}
				}
			}
			
			staffuser.setUserList(userList);
			staffuser.setDeptList(deptList);
			staffuser.setRoleList(roleList);
		} catch (BusinessException e) {
			LOG.error("[userQuery BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[userQuery Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
//	/**
//	 * 新增使用者初始化
//	 * 
//	 * @param command
//	 */
//	public void userAddInit(Command command) throws BusinessException, FrameException {
//		LOG.info("新增使用者");
//		StaffUser staffuser = (StaffUser) command;
//		
//		try {
//			staffuser.setDeptList(userSetSrv.queryDepts());
//			staffuser.setRoleList(userSetSrv.queryRoles());
//		} catch (BusinessException e) {
//			LOG.error("[roleQuery BusinessException]: ", e);
//			throw e;
//			
//		} catch (Exception e) {
//			LOG.error("[roleQuery Exception]: ", e);
//			throw new FrameException("message.sys.error");
//		}
//	}
	
	/**
	 * 新增使用者
	 * 
	 * @param command
	 */
	public void userAdd(Command command) throws BusinessException, FrameException {
		LOG.info("新增使用者");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		StaffUser staffuser = (StaffUser) command;
		
		try {
			staffuser.setLoginMema("");
			staffuser.setStates("A");
			staffuser.setErrorMemaCount(0);
			staffuser.setCreateDate(DateUtil.getCurrentTime("DT","AD"));
			staffuser.setChangeMemaDate(DateUtil.getCurrentTime("DT","AD"));
			if(userSetSrv.queryAddUsers(staffuser.getLoginId())!=null) {
				staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.insert.failure", user.getLocale() ) + ", 使用者已存在");
				return;
			}
			
			//20190826  新增直接寄信給客戶Begin
			//userSetSrv.saveNewUser(staffuser);
			String mema = CommonUtil.randomGenerator(8);
			staffuser.setLoginMema(MAC.encryptePswd(mema));
			userSetSrv.saveNewUser(staffuser);
			sendMail(mema, staffuser);
			//20190826  新增直接寄信給客戶End
			staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.insert.success", user.getLocale()));
			
		} catch (BusinessException e) {
			LOG.error("[userAdd BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[userAdd Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
		//記錄應用系統日誌(TSB_APAUDITLOG)
//		this.audiLog(staffuser);
	}
	
	
	/**
	 * 修改使用者初始化
	 * 
	 * @param command
	 */
	public void userUpdateInit(Command command) throws BusinessException, FrameException {
		LOG.info("修改使用者");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		StaffUser staffuser = (StaffUser) command;
		try {
			StaffUser newStaffuser = userSetSrv.queryUsers(staffuser.getLoginId());
			BeanUtils.copyProperties(staffuser, newStaffuser);
			staffuser.setDeptList(userSetSrv.queryDepts());
			staffuser.setRoleList(userSetSrv.queryRoles());
			if(staffuser.getStates().equals("A")) {
				staffuser.setStatesList("A,新建;S,暫停");
			}else if(staffuser.getStates().equals("L")){
				staffuser.setStatesList("L,鎖定");
			}else if(staffuser.getStates().equals("D")){
				staffuser.setStatesList("E,終止");
			}else{
				staffuser.setStatesList("E,啟用;S,暫停");
			}
		} catch (BusinessException e) {
			LOG.error("[userUpdateInit BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[userUpdateInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 修改使用者
	 * 
	 * @param command
	 */
	public void userUpdate(Command command) throws BusinessException, FrameException {
		LOG.info("修改使用者");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		StaffUser staffuser = (StaffUser) command;
		
		try {
			staffuser.setUpdateDate(DateUtil.getCurrentTime("DT","AD"));
			userSetSrv.updateUser(staffuser);
			staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", user.getLocale()));
		} catch (BusinessException e) {
			LOG.error("[userUpdate BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[userUpdate Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
		//記錄應用系統日誌(TSB_APAUDITLOG)
//		this.audiLog(staffuser);
	}
	
	/**
	 * 重設使用者密碼
	 * 
	 * @param command
	 */
	public void resetMema(Command command) throws BusinessException, FrameException {
		LOG.info("重設使用者密碼");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		StaffUser staffuser = (StaffUser) command;
		try {
			StaffUser newStaffuser = userSetSrv.queryUsers(staffuser.getLoginId());
			newStaffuser.setFuncId(staffuser.getFuncId());
			String operate = staffuser.getOperate();
			BeanUtils.copyProperties(staffuser, newStaffuser);
			String nowDate = DateUtil.getCurrentTime("DT","AD");
			String mema = CommonUtil.randomGenerator(8);
			staffuser.setLoginMema3(staffuser.getLoginMema2());
			staffuser.setLoginMema2(staffuser.getLoginMema1());
			staffuser.setLoginMema1(staffuser.getLoginMema());
			staffuser.setLoginMema(MAC.encryptePswd(mema));
			staffuser.setUpdateDate(nowDate);
			staffuser.setChangeMemaDate(nowDate);
			staffuser.setStates("A");
			staffuser.setErrorMemaCount(0);
			staffuser.setOperate(operate);
			userSetSrv.updateUser(staffuser);
			sendMail(mema, staffuser);
			staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.reset.success", user.getLocale()));
		} catch (BusinessException e) {
			LOG.error("[resendMema BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[resendMema Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		//記錄應用系統日誌(TSB_APAUDITLOG)
//		this.audiLog(staffuser);
	}
	
	/**
	 * 終止使用者
	 * 
	 * @param command
	 */
	public void userDisable(Command command) throws BusinessException, FrameException {
		LOG.info("終止使用者");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		StaffUser staffuser = (StaffUser) command;
		
		try {
			if("admin".equals(staffuser.getLoginId())) {
				staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.disable.failure.ADMIN", user.getLocale()));
			}else {
				StaffUser newStaffuser = userSetSrv.queryUsers(staffuser.getLoginId());
				newStaffuser.setFuncId(staffuser.getFuncId());
				String operate = staffuser.getOperate();
				BeanUtils.copyProperties(staffuser, newStaffuser);
				staffuser.setUpdateDate(DateUtil.getCurrentTime("DT","AD"));
				staffuser.setStates("D");
				staffuser.setOperate(operate);
				userSetSrv.updateUser(staffuser);
				staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.disable.success", user.getLocale()));
			}
			
		} catch (BusinessException e) {
			LOG.error("[userDisable BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[userDisable Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		//記錄應用系統日誌(TSB_APAUDITLOG)
//		this.audiLog(staffuser);
	}
	
	public void sendMail(String mema, StaffUser staffuser){
		//發送email
		String subject = "密碼設定/重設";
		String templateFile = "STF-S-01.vm";
		
     	HashMap mailMap = new HashMap();
 		mailMap.put("LOGIN_ID", staffuser.getLoginId());
 		mailMap.put("LOGIN_MEMA", mema);
 		mailMap.put("USER_ID", staffuser.getUserId());
 		mailMap.put("USER_NAME", staffuser.getUserName());
 		mailMap.put("URL", AppEnv.getString("STAFF_URL"));
 		
 		try {
 			String mailContent = MailUtil.renderMailHtmlContent(mailMap, templateFile);
 	 		new MailUtil().addMail(mailContent, subject, staffuser.getEmail(), null, null, null);
 		}catch (BusinessException e) {
			LOG.error("[sendMail BusinessException]: ", e);
		} catch (Exception e) {
			LOG.error("[sendMail Exception]: ", e);
		}
	}
}
