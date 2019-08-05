/*
 * @(#)ApplicationException.java
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
 * The exception class of application.
 */
public class ApplicationException extends BaseException {

	private static final long serialVersionUID = 6969452565494563847L;

	/**
	 * Constructor
	 * @param errorCode
	 */
	public ApplicationException(String errorCode) {
		super((String)null, new String[]{errorCode, null});
	}
	/**
	 * Constructor
	 * @param msg
	 * @param errorCode
	 */
	public ApplicationException(String msg, String errorCode) {
		super(msg, new String[]{errorCode, null});
	}
	/**
	 * Constructor
	 * @param msg
	 * @param errorCode
	 * @param errorMsg
	 */
	public ApplicationException(String msg, String errorCode , String errorMsg) {
		super(msg, new String[]{errorCode, errorMsg});
	}	
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 */
	public ApplicationException(Exception e, String errorCode) {
		super(e, new String[]{errorCode, null});
	}	
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 * @param errorMsg
	 */
	public ApplicationException(Exception e, String errorCode, String errorMsg) {
		super(e, new String[]{errorCode, errorMsg});
	}
}
