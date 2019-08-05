/**
 * @(#)LdapHelper.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 用於登入身分驗證
 * 
 * Modify History:
 *  v1.00, 2016/02/15, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.AppEnv;
import com.hitrust.acl.exception.NotLoginException;
import com.hitrust.acl.util.MAC;
import com.hitrust.bank.model.StaffUser;
import com.hitrust.bank.service.UserSetSrv;
import com.tsib.APSC;

public class LdapHelper {
	// Log4j
    private Logger LOG = Logger.getLogger(LdapHelper.class);
    
    private String userName = "管理員";	// 使用者名稱
    private String errorMsg = "";		// LDAP 詳細錯誤信息
    private String errorCode = "";		// LDAP 錯誤代碼 
    private String roleId = ""; // 使用者受權角色
    
    
 	
    /**
     * 功能： LDAP 身分驗證
     * @param loginId 登入代號
     * @param loginMema 登入密碼
     * @return true: 登入成功, false: 登入失敗
     */
    public void checkAuthenticate(StaffUser staffuser) throws NotLoginException {
    		roleId = staffuser.getRoleId();
    }

    // =============== Getter & Setter ===============
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
