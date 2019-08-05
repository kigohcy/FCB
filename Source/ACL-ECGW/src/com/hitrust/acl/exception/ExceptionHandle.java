/*
 * @(#)ExceptionHandle.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/02/21, Tim Cao
 *   1) First release
 *  v1.01, 2006/04/27, Brent Zhang
 *   1) modify method : handleCheckedException()  
 */
package com.hitrust.acl.exception;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;

/**
 * 
 * Handle all the exception.
 */
public class ExceptionHandle {
	
	// Log4J
	static Logger LOG = Logger.getLogger(ExceptionHandle.class);
	
	/**
	 * Handle all exceptions which are subclass of BaseException. 
	 * @param exception
	 */
	public static String handleCheckedException(BaseException exception, String locale) {
		// 1.Get error code, error message, exception message 
		String errorCode = exception.getErrorCode();
		String errorMsg  = exception.getErrorMsg();
		String message   = exception.getMessage();
		
		// if the message is not null ,then use the message ,add by brent 2006/04/27
		if (errorMsg != null && errorMsg.length() > 0) {
			message  = (message==null)? errorMsg : message;
		} else {
			if (errorCode!=null && !"".equals(errorCode)) {
				errorMsg = APSystem.codeMessage(locale, errorCode);
				message  = (message==null)? errorMsg : message;
			} else {
				errorMsg = message;
			}
		}
		// 2.Get configuration of the exception
//		String exceptionClass = exception.getClass().getName();
//		String needLog  = APSystem.getExceptionParameterValue(exceptionClass, "needLog");
//		String needMail = APSystem.getExceptionParameterValue(exceptionClass, "needMail");		
//		// 3. LOG the exception
//		if ("1".equals(needLog)) {
//			LOG.debug(errorMsg);
//			LOG.error(exceptionClass + " : " + message);
//		}		
//		// 4. Email notify for the exception
//		if ("1".equals(needMail)) {
//			sendMail();
//		}
		
		return errorMsg;
	}
	
	public static String handeUnCheckedException(Exception exception, String locale) {
		return null;
	}
	
	private static void sendMail() {
		
	}
}
