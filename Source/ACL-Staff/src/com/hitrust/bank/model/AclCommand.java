/**
 * @(#) AclCommand.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/1/29, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model;

import com.hitrust.framework.model.BaseCommand;

public class AclCommand extends BaseCommand {

	private static final long serialVersionUID = 8767520185858447963L;
	
	private String returnMsg;  // 系統回覆訊息
	private String sessionKey; // session key
	
	// =============== Getter & Setter ===============
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

}
