/**
 * @(#) StaffRole.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, Nov 16, 2015, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.model;

import com.hitrust.bank.model.base.AbstractTxncsSvip;

public class UserStates extends AbstractTxncsSvip{
	
	
	private static final long serialVersionUID = 1L;

	// =============== Not Table Attribute ===============
	private String userStates;
	private String statesName;

	public String getUserStates() {
		return userStates;
	}
	public void setUserStates(String userStates) {
		this.userStates = userStates;
	}
	public String getStatesName() {
		return statesName;
	}
	public void setStatesName(String statesName) {
		this.statesName = statesName;
	}
}
