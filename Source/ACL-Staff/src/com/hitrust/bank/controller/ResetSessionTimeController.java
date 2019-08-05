/**
 * @(#) ResetSessionTimeController.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/02/24, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */
package com.hitrust.bank.controller;

import org.apache.log4j.Logger;

import com.hitrust.bank.model.AclCommand;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class ResetSessionTimeController extends BaseAutoCommandController {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(ResetSessionTimeController.class);
		
	public ResetSessionTimeController() {
		super.setCommandClass(AclCommand.class);
	}

	/**
	 * 同步 User session time
	 * @param commnd
	 */
	public void resetSessionTime(Command commnd) {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		LOG.info("========== Synchronize User_Id: " + user.getUserId() + " session time ==========");
	}
	
}
