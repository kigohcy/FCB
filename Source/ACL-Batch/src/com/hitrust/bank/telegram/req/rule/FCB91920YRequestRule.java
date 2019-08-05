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

import com.hitrust.bank.telegram.req.FCB91920YRequestInfo;
import com.hitrust.telegram.HostRequestInfo;


public class FCB91920YRequestRule extends GenericHostRequestRule {
	
	private static Logger LOG = Logger.getLogger(FCB91920YRequestRule.class);
	/**
	 * Constructor
	 * @param hostRequestInfo
	 */
	public FCB91920YRequestRule(HostRequestInfo hostRequestInfo) {
		super(hostRequestInfo);
	}

	/**
	 * Business to message
	 */
	protected HostRequestInfo[] businessToMessage() {
		FCB91920YRequestInfo info = (FCB91920YRequestInfo)this.hostRequestInfo;
		return new HostRequestInfo[]{info};
	}	
}
