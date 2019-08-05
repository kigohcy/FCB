/**
 * @(#) NotLoginException.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/05, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.exception;

import com.hitrust.framework.exception.BaseException;

public class NotLoginException extends BaseException {

	private static final long serialVersionUID = -5022406461620610111L;

	public NotLoginException(String errorCode) {
    	super(null, errorCode, null, null);
    }
	
	public NotLoginException(String errorMsg, String errorCode, Object[] parameters, Exception e) {
		super(errorMsg, errorCode, parameters, e);
	}

}
