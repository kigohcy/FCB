/**
 * @(#) MemaErrorExection.java
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/04/16
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.acl.exception;

import com.hitrust.framework.exception.BaseException;

public class MemaErrorExection extends BaseException {
	private static final long serialVersionUID = 8174094947925638804L;

	public MemaErrorExection(String errorCode) {
    	super(null, errorCode, null, null);
    }
	
	public MemaErrorExection(String errorMsg, String errorCode, Object[] parameters, Exception e) {
		super(errorMsg, errorCode, parameters, e);
	}
}
