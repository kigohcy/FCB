/*
 * @(#)GenericHostRequestInterceptor.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2007/04/04, Tim Cao
 *   1)first release
 */
package com.hitrust.bank.telegram.req.interceptor;

import java.util.HashMap;

//import com.hitrust.lib.APSystem;
import com.hitrust.telegram.interceptor.HostRequestInterceptor;
//import com.hitrust.util.CustomizingXmlFormat;

public abstract class GenericHostRequestInterceptor implements HostRequestInterceptor {
	
	protected String messageCode;  // message code
	protected byte[] sendTelegram; // Send telegram
	
	/**
	 * Constructor
	 * @param messageCode
	 * @param sendTelegram
	 */
	public GenericHostRequestInterceptor(String messageCode, byte[] sendTelegram) {
		this.messageCode  = messageCode;
		this.sendTelegram = sendTelegram;
	}

	/**
	 * Special process
	 */
	public byte[] process() {
		this.intercept();
		// get the send encoding 
//		HashMap messageParameters =  APSystem.getMsgTransactionParameters(this.messageCode);
//		String sendEncoding = (String)messageParameters.get("sEncoding");
//		//set empty elements  from <tagName/> to <tagName></tagName>
//		this.sendTelegram = CustomizingXmlFormat.formatByteExpandEmptyElements(this.sendTelegram,sendEncoding,true);
		return this.sendTelegram;
	}
	
	/**
	 * Do intercept and alter telegram
	 */
	protected abstract void intercept();
}
