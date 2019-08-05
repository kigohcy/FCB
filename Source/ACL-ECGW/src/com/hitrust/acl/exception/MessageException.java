/*
 * @(#)MessageException.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/02/21, Tim Cao
 *   1) First release
 */
package com.hitrust.acl.exception;

/**
 * 
 * The exception class of transaction.
 */
public class MessageException extends BaseException {

	private static final long serialVersionUID = -3534082270461392182L;

	public MessageException(String errorCode) {
		super((String)null, new String[]{errorCode, null});
	}
	
	public MessageException(String msg, String errorCode) {
		super(msg, new String[]{errorCode, null});
	}
		
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 */
	public MessageException(Exception e, String errorCode) {
		super(e, new String[]{errorCode, null});
	}	
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 * @param errorMsg
	 */
	public MessageException(Exception e, String errorCode, String errorMsg) {
		super(e, new String[]{errorCode, errorMsg});
	}	
}
