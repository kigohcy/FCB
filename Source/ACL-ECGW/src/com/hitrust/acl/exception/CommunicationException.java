/*
 * @(#)CommunicationException.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/02/21, Tim Cao
 *   1) First release
 */
package com.hitrust.acl.exception;

//import com.hitrust.framework.exception.BaseException;
/**
 * 
 * The exception class of communication.
 */
public class CommunicationException extends BaseException {

	private static final long serialVersionUID = -9051736015133521524L;

	public CommunicationException(String errorCode) {
		super((String)null, new String[]{errorCode, null});
	}
	
	public CommunicationException(String msg, String errorCode) {
		super(msg, new String[]{errorCode, null});
	}
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 */
	public CommunicationException(Exception e, String errorCode) {
		super(e, new String[]{errorCode, null});
	}	
	
	/**
	 * Constructor
	 * @param e
	 * @param errorCode
	 * @param errorMsg
	 */
	public CommunicationException(Exception e, String errorCode, String errorMsg) {
		super(e, new String[]{errorCode, errorMsg});
	}
}