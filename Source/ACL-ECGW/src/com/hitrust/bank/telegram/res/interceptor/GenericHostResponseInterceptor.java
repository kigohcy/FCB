/*
 * @(#)GenericHostResponseInterceptor.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2007/04/04, Tim Cao
 *   1)first release
 */
package com.hitrust.bank.telegram.res.interceptor;

import com.hitrust.bank.telegram.exception.UtilException;
import com.hitrust.telegram.interceptor.HostResponseInterceptor;

public abstract class GenericHostResponseInterceptor implements HostResponseInterceptor {
	
	protected String messageCode;  // message code
	protected byte[] recvTelegram; // Recv telegram
	
	/**
	 * Constructor
	 * @param messageCode
	 * @param recvTelegram
	 */
	public GenericHostResponseInterceptor(String messageCode, byte[] recvTelegram) {
		this.messageCode  = messageCode;
		this.recvTelegram = recvTelegram;
	}

	public byte[] process(){
		this.intercept();
		
		return this.recvTelegram;
	}
	
	/**
	 * Do intercept and alter telegram
	 */
	protected abstract void intercept() throws UtilException;
}
