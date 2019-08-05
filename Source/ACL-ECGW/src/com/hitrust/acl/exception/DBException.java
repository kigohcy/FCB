/*
 * @(#)DBException.java
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
 * The exception class of database operation.
 */
public class DBException extends BaseException {

	private static final long serialVersionUID = 1841245969537597030L;

	public DBException(String errorCode) {
		super((String)null, new String[]{errorCode, null});
	}
	
	public DBException(String msg, String errorCode) {
		super(msg, new String[]{errorCode, null});
	}
	
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 */
	public DBException(Exception e, String errorCode) {
		super(e, new String[]{errorCode, null});
	}	
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 * @param errorMsg
	 */
	public DBException(Exception e, String errorCode, String errorMsg) {
		super(e, new String[]{errorCode, errorMsg});
	}	
}
