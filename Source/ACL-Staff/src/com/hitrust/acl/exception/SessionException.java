/**
 * @(#) SessionException.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/01, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.exception;

import com.hitrust.framework.exception.BaseException;

public class SessionException extends BaseException {

	private static final long serialVersionUID = -4514053270653547413L;
	
	public SessionException(String errorCode) {
		super(null, errorCode, null, null);
	}

	public SessionException(String errorMsg, String errorCode, Object[] parameters, Exception e) {
		super(errorMsg, errorCode, parameters, e);
	}

}
