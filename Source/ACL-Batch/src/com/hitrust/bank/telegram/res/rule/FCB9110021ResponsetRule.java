/*
 * @(#)FCB9110021ResponsetRule.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/08/15, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res.rule;

import org.apache.log4j.Logger;
import com.hitrust.telegram.HostRequestInfo;
import com.hitrust.telegram.HostResponseInfo;

public class FCB9110021ResponsetRule  extends GenericHostResponseRule {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(FCB9110021ResponsetRule.class);

	/**
	 * Constructor
	 * @param hostRequestInfo
	 * @param hostResponseInfo
	 */
	public FCB9110021ResponsetRule(HostRequestInfo hostRequestInfo, HostResponseInfo hostResponseInfo) {
		super(hostRequestInfo, hostResponseInfo);
	}
	
	/**
	 * Message to business
	 */
	protected void messageToBusiness() {
		
	}
	

}
