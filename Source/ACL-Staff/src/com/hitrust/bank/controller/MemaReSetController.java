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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.exception.MemaErrorExection;
import com.hitrust.acl.util.MAC;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffUser;
import com.hitrust.bank.service.UserSetSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.util.DateUtil;
import com.hitrust.framework.web.BaseAutoCommandController;


public class MemaReSetController extends BaseAutoCommandController {

	// ============================== Log4j ==============================
	private Category LOG = Logger.getLogger(this.getClass());

	// ============================== service injection ==============================
	private UserSetSrv userSetSrv;

	// ============================== injection beans ==============================
	public void setUserSetSrv(UserSetSrv userSetSrv) {
		this.userSetSrv = userSetSrv;
	}

	// ============================== Constructor ==============================
	public MemaReSetController() {
		setCommandClass(StaffUser.class);
	}

	/**
	 * 密碼重設
	 * 
	 * @param command
	 */
	public void memaReSet(Command command) throws BusinessException, FrameException {
		LOG.info("密碼重設");
		LoginUser user = (LoginUser)APLogin.getCurrentUser();
		StaffUser staffuser = (StaffUser) command;
		try {
			staffuser = userSetSrv.queryUsers(user.getUserId());
			
		} catch (BusinessException e) {
			LOG.error("[memaReSet BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[memaReSet Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 密碼重設變更
	 * 
	 * @param command
	 */
	public void memaReSetUpdate(Command command, HttpServletRequest req) throws BusinessException, FrameException {
		LOG.info("密碼重設變更");
		LoginUser user = (LoginUser)APLogin.getCurrentUser();
		StaffUser staffuserMsg = (StaffUser) command;
		String oldMema = req.getParameter("oldMema");
		String newMema = req.getParameter("newMema");
		try {
			StaffUser staffuser = userSetSrv.queryUsers(user.getUserId());
			if(MAC.encryptePswd(oldMema).equals(staffuser.getLoginMema())) {
				if(!MAC.encryptePswd(newMema).equals(staffuser.getLoginMema1()) && 
						!MAC.encryptePswd(newMema).equals(staffuser.getLoginMema2()) &&
						!MAC.encryptePswd(newMema).equals(staffuser.getLoginMema3())) {
					String nowDate = DateUtil.getCurrentTime("DT","AD");
					staffuser.setLoginMema3(staffuser.getLoginMema2());
					staffuser.setLoginMema2(staffuser.getLoginMema1());
					staffuser.setLoginMema1(staffuser.getLoginMema());
					staffuser.setLoginMema(MAC.encryptePswd(newMema));
					staffuser.setUpdateDate(nowDate);
					staffuser.setChangeMemaDate(nowDate);
					staffuser.setStates("E");
					userSetSrv.updateUser(staffuser);
					staffuserMsg.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", user.getLocale()));

				}else {
					throw new BusinessException("message.sys.change.3th");
				}
			}else {
				throw new BusinessException("message.sys.change.oldMema");
			}
		} catch (BusinessException e) {
			LOG.error("[memaReSetUpdate BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[memaReSetUpdate Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	
	/**
	 * 密碼變更
	 * 
	 * @param command
	 */
	public void updateMema(Command command, HttpServletRequest req) throws BusinessException, FrameException {
		LOG.info("密碼變更");
		LoginUser user = (LoginUser)APLogin.getCurrentUser();
		String oldMema = req.getParameter("oldMema");
		String newMema = req.getParameter("newMema");
		String userId = req.getParameter("userId");
		String newStts = req.getParameter("newStts");
		StaffUser staffuser = null;
		try {
//			StaffUser staffuser = userSetSrv.queryUsers(user.getUserId());
			staffuser = userSetSrv.queryUsers(userId);
			if(staffuser==null){
				throw new MemaErrorExection ("無法取得使用者資料");
			}
			if(MAC.encryptePswd(oldMema).equals(staffuser.getLoginMema())) {
				if(!MAC.encryptePswd(newMema).equals(staffuser.getLoginMema1()) && 
						!MAC.encryptePswd(newMema).equals(staffuser.getLoginMema2()) &&
						!MAC.encryptePswd(newMema).equals(staffuser.getLoginMema3())) {
					String nowDate = DateUtil.getCurrentTime("DT","AD");
					staffuser.setLoginMema3(staffuser.getLoginMema2());
					staffuser.setLoginMema2(staffuser.getLoginMema1());
					staffuser.setLoginMema1(staffuser.getLoginMema());
					staffuser.setLoginMema(MAC.encryptePswd(newMema));
					if(StringUtils.equals(user.getUserStts(), "A")){
						staffuser.setStates("E");
					}
					staffuser.setUpdateDate(nowDate);
					staffuser.setChangeMemaDate(nowDate);
					userSetSrv.updateUser(staffuser);
//					staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", user.getLocale()));
					staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", initSysLocale(req)));

				}else {
					throw new MemaErrorExection("message.sys.change.3th");
				}
			}else {
				throw new MemaErrorExection("message.sys.change.oldMema");
			}
		} catch (MemaErrorExection e) {
			LOG.error("[memaReSetUpdate NotLoginException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[memaReSetUpdate Exception]: ", e);
			throw new FrameException("message.sys.error");
		}finally{
			if(user!=null && staffuser != null){
				user.setUserStts(staffuser.getStates());
			}
		}
	}
	
	/**
	 * 密碼保留
	 * 
	 * @param command
	 */
	public void keepMema(Command command, HttpServletRequest req) throws BusinessException, FrameException {
		String userId = req.getParameter("userId");
		try {
			StaffUser staffuser = userSetSrv.queryUsers(userId);
			String nowDate = DateUtil.getCurrentTime("DT","AD");
			staffuser.setUpdateDate(nowDate);
			staffuser.setChangeMemaDate(nowDate);
			userSetSrv.updateUser(staffuser);
			staffuser.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", initSysLocale(req)));
		} catch (Exception e) {
			LOG.error("[memaReSetUpdate Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 初始系統語系
	 * @param req
	 * @return
	 */
	private Locale initSysLocale(HttpServletRequest req) {
		String _locale = req.getParameter("_locale");
		
		// 預設繁體中文
		Locale locale = new Locale("zh", "TW");
		
		if (!StringUtil.isBlank(_locale)) {
			String[] lngn = _locale.split("_");
 			locale = new Locale(lngn[0], lngn[1]);
		}
		
		return locale;
	}
}
