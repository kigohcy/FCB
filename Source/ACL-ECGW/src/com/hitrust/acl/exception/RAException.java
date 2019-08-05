/*
 * @(#)RAException.java
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
 * The exception class of RA.
 */
public class RAException extends BaseException {

	private static final long serialVersionUID = -9007728994786657970L;

	public RAException(String errorCode) {
		super((String)null, new String[]{errorCode, null});
	}
	
	public RAException(String msg, String errorCode) {
		super(msg, new String[]{errorCode, null});
	}	
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 */
	public RAException(Exception e, String errorCode) {
		super(e, new String[]{errorCode, null});
	}	
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 * @param errorMsg
	 */
	public RAException(Exception e, String errorCode, String errorMsg) {
		super(e, new String[]{errorCode, errorMsg});
	}	
}
