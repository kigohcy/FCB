/**
 * @(#) NoPermissionException.java
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

public class NoPermissionException extends BaseException {

	private static final long serialVersionUID = 7981055000463070168L;
	
	public NoPermissionException(String errorCode) {
		super(null, errorCode, null, null);
	}

	public NoPermissionException(String errorMsg, String errorCode, Object[] parameters, Exception e) {
		super(errorMsg, errorCode, parameters, e);
	}

}
