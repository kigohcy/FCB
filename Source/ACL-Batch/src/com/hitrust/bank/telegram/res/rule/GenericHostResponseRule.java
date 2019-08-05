/*
 * @(#)GenericHostResponseRule.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2007/04/04, Tim Cao
 *   1)first release
 */
package com.hitrust.bank.telegram.res.rule;

import com.hitrust.bank.telegram.res.GenericHostResponseInfo;
import com.hitrust.telegram.HostRequestInfo;
import com.hitrust.telegram.HostResponseInfo;
import com.hitrust.telegram.rule.HostResponseRule;

public abstract class GenericHostResponseRule implements HostResponseRule {
	
	protected HostRequestInfo  hostRequestInfo;  // Host request info
	protected HostResponseInfo hostResponseInfo; // Host response info
	
	/**
	 * Constructor
	 * @param hostRequestInfo
	 * @param hostResponseInfo
	 */
	public GenericHostResponseRule(HostRequestInfo hostRequestInfo, HostResponseInfo hostResponseInfo) {
		this.hostRequestInfo  = hostRequestInfo;
		this.hostResponseInfo = hostResponseInfo;
	}

	/**
	 * Process business rule
	 */
	public void process(String transactionCode) {
		// 1.Check response status
//		if (!MessageUtil.checkTelegramStatus(transactionCode, (GenericHostResponseInfo) this.hostResponseInfo)) {
//			return;
//		}
		// 2.Message to business
		this.messageToBusiness();
	}
	
	/**
	 * Business to message
	 */
	protected abstract void messageToBusiness();
}
