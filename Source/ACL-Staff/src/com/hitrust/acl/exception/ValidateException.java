/**
 * @(#) ValidateException.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/17, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.acl.exception;

import com.hitrust.framework.exception.BaseException;

public class ValidateException extends BaseException {

	private static final long serialVersionUID = 4028758205517484260L;

	public ValidateException(String errorCode) {
		super(null, errorCode, null, null);
	}
	
	public ValidateException(String errorMsg, String errorCode, Object[] parameters, Exception e) {
		super(errorMsg, errorCode, parameters, e);
	}

}
