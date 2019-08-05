/*
 * @(#)FCB91148WRequestRule.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req.rule;

import org.apache.log4j.Logger;

import com.hitrust.bank.telegram.req.FCB91148WRequestInfo;
import com.hitrust.telegram.HostRequestInfo;


public class FCB91148WRequestRule extends GenericHostRequestRule {
	
	private static Logger LOG = Logger.getLogger(FCB91148WRequestRule.class);
	/**
	 * Constructor
	 * @param hostRequestInfo
	 */
	public FCB91148WRequestRule(HostRequestInfo hostRequestInfo) {
		super(hostRequestInfo);
	}

	/**
	 * Business to message
	 */
	protected HostRequestInfo[] businessToMessage() {
		FCB91148WRequestInfo info = (FCB91148WRequestInfo)this.hostRequestInfo;
		return new HostRequestInfo[]{info};
	}	
}
