/**
 * @(#)DefaultHostRequestInterceptor.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated.
 * All rights reserved.
 * Description :
 * Modify History:
 *  v1.00, 2007-10-18, Royal Shen
 *   1) First release
 */


package com.hitrust.bank.telegram.req.interceptor;

public class DefaultHostRequestInterceptor extends GenericHostRequestInterceptor {

	/**
	 * @param messageCode
	 * @param sendTelegram
	 */
	public DefaultHostRequestInterceptor(String messageCode, byte[] sendTelegram) {
		super(messageCode, sendTelegram);
	}

	/* (non-Javadoc)
	 * @see com.hitrust.factoring.model.bo.telegram.req.interceptor.GenericHostRequestInterceptor#intercept()
	 */
	protected void intercept() {
		//do nothing
	}

}
