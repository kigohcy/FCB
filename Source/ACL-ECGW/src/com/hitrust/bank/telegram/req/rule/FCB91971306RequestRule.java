/*
 * @(#)FCB91971306RequestRule.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req.rule;

import org.apache.log4j.Logger;

import com.hitrust.bank.telegram.req.FCB91971306RequestInfo;
import com.hitrust.telegram.HostRequestInfo;


public class FCB91971306RequestRule extends GenericHostRequestRule {
	
	private static Logger LOG = Logger.getLogger(FCB91971306RequestRule.class);
	/**
	 * Constructor
	 * @param hostRequestInfo
	 */
	public FCB91971306RequestRule(HostRequestInfo hostRequestInfo) {
		super(hostRequestInfo);
	}

	/**
	 * Business to message
	 */
	protected HostRequestInfo[] businessToMessage() {
		FCB91971306RequestInfo info = (FCB91971306RequestInfo)this.hostRequestInfo;
		return new HostRequestInfo[]{info};
	}	
}
