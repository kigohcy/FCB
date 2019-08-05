/*
 * @(#)FCB9110021RequestRule.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req.rule;

import org.apache.log4j.Logger;

import com.hitrust.bank.telegram.req.FCB9110021RequestInfo;
import com.hitrust.telegram.HostRequestInfo;


public class FCB9110021RequestRule extends GenericHostRequestRule {
	
	private static Logger LOG = Logger.getLogger(FCB9110021RequestRule.class);
	/**
	 * Constructor
	 * @param hostRequestInfo
	 */
	public FCB9110021RequestRule(HostRequestInfo hostRequestInfo) {
		super(hostRequestInfo);
	}

	/**
	 * Business to message
	 */
	protected HostRequestInfo[] businessToMessage() {
		FCB9110021RequestInfo info = (FCB9110021RequestInfo)this.hostRequestInfo;
		return new HostRequestInfo[]{info};
	}	
}
