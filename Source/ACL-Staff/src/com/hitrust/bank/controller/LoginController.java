/**
 * @(#) LoginController.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/05, Eason Hsu
 * 	 1) JIRA-Number, First release
 *  v1.01, 2017/06/19, Eason Hsu
 *   1) TSBACL-149, 客服系統無法取得使用者真實IP
 *  v1.02, 2017/08/14, Eason Hsu
 *   1) TSBACL-149, 增加 HTTP header value 追蹤 Log
 */
package com.hitrust.bank.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.hitrust.acl.common.AppEnv;
import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.common.TBCodeHelper;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.exception.MemaErrorExection;
import com.hitrust.acl.exception.NotLoginException;
import com.hitrust.acl.util.MAC;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.LdapHelper;
import com.hitrust.bank.json.AclSysFnct;
import com.hitrust.bank.json.AclSysModel;
import com.hitrust.bank.model.Login;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffSysFnct;
import com.hitrust.bank.model.StaffSysMenu;
import com.hitrust.bank.model.StaffUser;
import com.hitrust.bank.model.SysParm;
import com.hitrust.bank.service.LoginSettingsSrv;
import com.hitrust.bank.service.UserSetSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.util.Constants;
import com.hitrust.framework.util.DateUtil;
import com.hitrust.framework.web.Anonymous;
import com.hitrust.framework.web.BaseAutoCommandController;

public class LoginController extends BaseAutoCommandController implements Anonymous{
	
	// Log4j
	private static Logger LOG = Logger.getLogger(LoginController.class);
	
	// ============================== service injection ==============================
	private UserSetSrv userSetSrv;
	private LoginSettingsSrv loginSettingsSrv;
	// ============================== injection beans ==============================
	public LoginController () {
		super.setCommandClass(Login.class);
	}
	public void setUserSetSrv(UserSetSrv userSetSrv) {
 		this.userSetSrv = userSetSrv;
 	}
	
	/**
	 * 初始使用者登入資料
	 * @param command
	 * @param req
	 */
	public void initLogin(Command command, HttpServletRequest req, HttpServletResponse response) {
		LOG.info("========== initial Login information ==========");
		LOG.debug(req.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY));
		Login uiBean = (Login) command;
		
		boolean isLogin = false;			 // 是否登入成功
		String loginId = uiBean.getUserId();  // 使用者代號
		String loginMema = uiBean.getUserPswd();// 使用者密碼
		
		LdapHelper ldapHelper = null;
		StaffUser staffuser = null;
		String kaptcha;
//		boolean mustChangePswd = false;
//		req.setAttribute("mustChangeMema", "N");
		try {
			kaptcha = (String)req.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			if(!StringUtils.equalsIgnoreCase(kaptcha, uiBean.getKaptcha())){
				throw new NotLoginException("message.sys.login.errorKaptcha");
			}
			staffuser = userSetSrv.queryLoginUsers(loginId);
			ldapHelper = new LdapHelper();
			if(staffuser!=null) {
	        		LOG.info(" [USER_ID]: " + staffuser.getLoginId());
	        		if("E".equals(staffuser.getStates()) || "A".equals(staffuser.getStates())){
	        			if(MAC.encryptePswd(loginMema).equals(staffuser.getLoginMema())){
	        				ldapHelper.checkAuthenticate(staffuser);
	            			isLogin = true;
	            			staffuser.setErrorMemaCount(0);
	            			staffuser.setFinalLoginDate(DateUtil.getCurrentTime("DT","AD"));
//	            			if("A".equals(staffuser.getStates())) {
//	            				mustChangePswd = true;
//	            			}
	        			}else{
	        				staffuser.setErrorMemaCount(staffuser.getErrorMemaCount()+1);
	    	        		if(staffuser.getErrorMemaCount()>=5) {
	    	        			staffuser.setStates("L");
	    	        			staffuser.setUpdateDate(DateUtil.getCurrentTime("DT","AD"));
	    	        			throw new NotLoginException("message.sys.login.lock");
	    	        		}else {
	    	        			throw new NotLoginException("message.sys.login.errorMema");
	    	        		}
	        			}
	        		}else if("L".equals(staffuser.getStates())){
	        			throw new NotLoginException("message.sys.login.lock2");
	        		}else if("S".equals(staffuser.getStates())){
	        			throw new NotLoginException("message.sys.login.stop");
	        		}else if("D".equals(staffuser.getStates())){
	        			throw new NotLoginException("message.sys.login.disable");
	        		}else {
	        			throw new NotLoginException("message.sys.login.states");
		            }
        		
        		/*
        		if(MAC.encryptePswd(loginMema).equals(staffuser.getLoginMema()) && "E".equals(staffuser.getStates())) {
        			ldapHelper.checkAuthenticate(staffuser);
        			isLogin = true;
        			staffuser.setErrorMemaCount(0);
        			staffuser.setFinalLoginDate(DateUtil.getCurrentTime("DT","AD"));
        		}else if(!MAC.encryptePswd(loginMema).equals(staffuser.getLoginMema())) {
	        		staffuser.setErrorMemaCount(staffuser.getErrorMemaCount()+1);
	        		if(staffuser.getErrorMemaCount()>=5) {
	        			staffuser.setStates("L");
	        			staffuser.setUpdateDate(DateUtil.getCurrentTime("DT","AD"));
	        			throw new NotLoginException("message.sys.login.lock");
	        		}else {
	        			throw new NotLoginException("message.sys.login.errorMema");
	        		}
	            } else if (!"E".equals(staffuser.getStates())) {
	            	try {
//						if("A".equals(staffuser.getStates()) || CommonUtil.getDay(staffuser.getChangeMemaDate(), DateUtil.getCurrentTime("DT","AD"))>=90) {
						if("A".equals(staffuser.getStates())) {
							//導去修改頁
							mustChangePswd = true;
							isLogin = true;
//							RequestDispatcher requestDispatcher =req.getRequestDispatcher("/0905/loginMemaReSet.html");
//							requestDispatcher.forward(req,response);
						}else {
							throw new NotLoginException("message.sys.login.states");
						}
					} catch (Exception e) {
						throw new NotLoginException("message.sys.error");
					}
	            } else {
	            	throw new NotLoginException("message.sys.error");
	            }*/
	        } else {
	        	throw new NotLoginException("message.sys.login.nonUser");
	        }
			
			if (isLogin) {
				// 檢核使用者登入狀態
				checkLogin(loginId, ldapHelper, req);
				LoginUser user = (LoginUser) APLogin.getCurrentUser();
				user.setChangeMemaDate(staffuser.getChangeMemaDate());
				user.setUserStts(staffuser.getStates());
//				APLogin.set(user);
				
				if(StringUtils.equals("A", user.getUserStts())){
					//新戶
//					uiBean.setMustChangeMema(true);
					this.returnView.set("/changeMema");
				}else{
					//取得密碼保留天數
					SysParm sysParm = loginSettingsSrv.fetchSysparmByPram("PSWD_KEEP_DAY");
					if(CommonUtil.getDay(staffuser.getChangeMemaDate().substring(0,8), DateUtil.getCurrentTime("DT","AD").substring(0,8)) >= Integer.parseInt(sysParm.getParmValue())){
						this.returnView.set("/changeMema");
					}
				}
			} else {
				TBCodeHelper helper = new TBCodeHelper("04", ldapHelper.getErrorCode());
				throw new NotLoginException(helper.getTbCodeMsg(), null, null, null);
			}
		} catch (NotLoginException e) {
			LOG.error("[initLogin Exception]", e);
			throw e;
		}catch (Exception e) {
			LOG.error("[initLogin Exception]", e);
			throw new NotLoginException(e.getMessage());
		} finally{
			if(staffuser!=null)
				userSetSrv.updateUser(staffuser);
		}
	}
	
	// =============== private Method ===============
	/**
	 * 檢核使用者登入狀態, 並取得授權模組與功能清單
	 * @param userId	   使用者代號
	 * @param ldapHelper   LdapHelper
	 * @param req		   HttpServletRequest
	 */
	private void checkLogin(String userId, LdapHelper ldapHelper, HttpServletRequest req) {
		LoginUser user = null;
		
		LOG.info("========== [登入使用者代號]: " + userId + " [使用者授權角色]: " + ldapHelper.getRoleId() + " ==========");
		
		String jsonMenu = "";  // 使用者授權 Menu
		int pageRow = 25;	   // 分頁顯示筆數(default)
		
		// 初始系統語系
		Locale locale = initSysLocale(req);
		
		// 檢核 Session 是否為新生成
		HttpSession session = validateSession(req);
		
		List<String> rights = null;		 // 使用者受權功能代碼
		List<StaffSysMenu> menus = null; // 使用者受權模組
		List<StaffSysFnct> fncts = null; // 使用者受權功能

		// 檢核使用者授權角色是否存在		
		boolean isRole = loginSettingsSrv.fetchStaffRoleByIds(ldapHelper.getRoleId());

		if (!isRole) {
			//TSBACL-28
			throw new NotLoginException("message.sys.noRole");
		}
		
		// 取得使用者登入控制檔資訊
		user = loginSettingsSrv.fetchStaffLogin(userId);
		
		// 檢核使用者是否為第一次登入
		if (StringUtil.isBlank(user)) {
			// 新者使用者登入控制資料
			user = loginSettingsSrv.insertStaffLogin(userId, session.getId());
		} else {
			// 更新使用者登入控制資料
			user = loginSettingsSrv.updateStaffLogin(userId, session.getId());
		}
		
		// 取得使用者授權模組
		LOG.info("[取得使用者代碼:] " + user.getUserId() + ", 授權模組清單");
		menus = loginSettingsSrv.fetchStaffSysMenuList(ldapHelper.getRoleId(), String.valueOf(locale));

		// 取得使用者授權功能清單
		LOG.info("[取得使用者代碼:] " + user.getUserId() + ", 授權功能清單");
		fncts = loginSettingsSrv.fetchStaffSysFnctList(ldapHelper.getRoleId(), String.valueOf(locale));

		// 檢核授權模組與功能清單是否為空
		if (menus.size() == 0 || fncts.size() == 0) {
			//TSBACL-28
			throw new NotLoginException("message.sys.noRole");
		}
		
		// 組合 JSON 格式的 Menu 提供前端使用
		LOG.info("[組合使用者代碼]: " + user.getUserId() +  ", JSON 格式功能清單(前端使用)");
		jsonMenu = this.composeJsonMenu(menus, fncts);
		
		// 取得使用者受權功能代碼
		rights = this.getUserRights(fncts);
		
		// 設定分頁顯示筆數
		SysParm sysParm = loginSettingsSrv.fetchSysparmByPram("STAFF_QURY_PAGE_ROW");
		
		if (!StringUtil.isBlank(sysParm)) {
			
			pageRow = Integer.parseInt(sysParm.getParmValue());
			Page.setDefaultItemsPerPage(pageRow);
		}
		
		// 設定使用者基本資訊
		user.setUserName(ldapHelper.getUserName());
		user.setIp(this.getRealClientAddr(req)); // v1.01, 取得使用者真實IP
		user.setJsonMenu(jsonMenu);
		user.setRights(rights);
		user.setRoleId(ldapHelper.getRoleId());
		user.setLocale(locale);
		user.setPageRow(pageRow);
		
		LOG.info("==================== User login information start ====================");
		LOG.info("= [User_Id]: " 	  + user.getUserId()  + " [User_Name]: " 	  + user.getUserName());
		LOG.info("= [User_Ip]: " 	  + user.getIp() 	  + " [User_SessionKey]: " + user.getSessionId());
		LOG.info("= [User_RoleIds]: " + user.getRoleId() + " [User_Lngn]: " 	  + user.getLocale());
		LOG.info("= [User_PageRow]: " + user.getPageRow());
		LOG.info("= [User_FNCTS]: "   + user.getRights());
		LOG.info("==================== User login information end ====================");
		
		// 將 LoginUser 物件存放在 Session
		session.setAttribute(Constants.LOGIN_USER, user);
		session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		
		// 將 LoginUser 物件存放在 TheardLocal 中, 讓操作記錄流程可取到 User 資料
		APLogin.set(user);
	}
	
	/**
	 * 組合 JSON Menu 提供前端 Javascript 產生 Menu 使用
	 * @param menus
	 * @param fncts
	 * @return
	 */
	private String composeJsonMenu(List<StaffSysMenu> menus, List<StaffSysFnct> fncts) {
		
		String json = "";
		
		List<AclSysModel> sysModels = new ArrayList<AclSysModel>();
		List<AclSysFnct> sysFncts = null;
		AclSysModel sysModel = null;
		AclSysFnct sysFnct = null;
		
		for (StaffSysMenu menu : menus) {
			sysModel = new AclSysModel();
			sysFncts = new ArrayList<AclSysFnct>();
			
			for (StaffSysFnct fnct : fncts) {
				if (menu.getId().getMenuId().equals(fnct.getMenuId())) {
					sysFnct = new AclSysFnct(fnct.getUrl(), fnct.getFnctName());
					sysFncts.add(sysFnct);
				}
			}
			
			sysModel.setTitle(menu.getMenuName());
			sysModel.setDetail(sysFncts);
			
			sysModels.add(sysModel);
			
		}
		
		// 轉換成 JSON 物件
		json = JsonUtil.object2Json(sysModels, false);
		
		return json;
	}
	
	/**
	 * 取得使用者受權功能清單
	 * @param fncts
	 * @return
	 */
	private List<String> getUserRights(List<StaffSysFnct> fncts) {
		
		List<String> rights = new ArrayList<String>();
	
		for (StaffSysFnct fnct : fncts) {
			rights.add(fnct.getId().getFnctId());
		}
		
		// 系統登入登出
		rights.add("F0000");
		
		return rights;
	}
	
	/**
	 * 檢核 HttpSession 是否為新生成
	 * @param session
	 */
	private HttpSession validateSession (HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		/*
		 * 每次登入時, 先將當前 Session 銷毀在重新產生, 確保 Session 是最新的
		 * 避免同一個 browser 開啟二個頁籤, Session 視為同一個的狀況
		 */
		if (!StringUtil.isBlank(session)) {
			LOG.info("[銷毀當前 Session]: " + session.getId());
			session.invalidate();
		}
		
		session = req.getSession(false);
		
		if (StringUtil.isBlank(session)) {
			session = req.getSession();
			LOG.info("[產生全新 Session]: " + session.getId());
		}
		
		return session;
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
	
	/**
	 * 取得客戶端真實 IP
	 * @param req
	 * @return 客戶端 IP 位置
	 * @since v1.01
	 */
	private String getRealClientAddr(HttpServletRequest req) {
		
		String realAddr = req.getHeader(AppEnv.getString("REAL_IP"));
		
		// ========== for 追蹤 Http header value ==========
		String name = "";	// Header 名稱
		String value = "";	// Header 值
		
		Enumeration<String> names = req.getHeaderNames();
		Enumeration<String> values = null;
		
		while (names.hasMoreElements()) {
			name = names.nextElement();
			values = req.getHeaders(name);
			
			while (values.hasMoreElements()) {
				value = values.nextElement();
				LOG.info("[Header]:" + name + ", [Value]:" + value);
			}
		}
		
		if (StringUtil.isBlank(realAddr)) {
			LOG.error("取得客戶真實 IP 失敗, 預設從 request.getRemoteAddr() 取得 IP");
			realAddr = req.getRemoteAddr();
		} 
		
		return realAddr;
	}
	
	// =============== bean injection ===============
	public void setLoginSettingsSrv(LoginSettingsSrv loginSettingsSrv) {
		this.loginSettingsSrv = loginSettingsSrv;
	}
	
}
