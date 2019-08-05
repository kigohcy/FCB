/*
 * @(#)FCB91970266ResponsetRule.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res.rule;

import org.apache.log4j.Logger;
import com.hitrust.telegram.HostRequestInfo;
import com.hitrust.telegram.HostResponseInfo;

public class FCB91970266ResponsetRule  extends GenericHostResponseRule {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(FCB91970266ResponsetRule.class);

	/**
	 * Constructor
	 * @param hostRequestInfo
	 * @param hostResponseInfo
	 */
	public FCB91970266ResponsetRule(HostRequestInfo hostRequestInfo, HostResponseInfo hostResponseInfo) {
		super(hostRequestInfo, hostResponseInfo);
	}
	
	/**
	 * Message to business
	 */
	protected void messageToBusiness() {
		
	}
	

}
