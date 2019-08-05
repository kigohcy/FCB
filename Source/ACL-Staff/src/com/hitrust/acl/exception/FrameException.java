/**
 * @(#) FrameException.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/18 Eason Hsu
 *    1) JIRA-Number, First release
 *
 */
package com.hitrust.acl.exception;

import com.hitrust.framework.exception.BaseException;

public class FrameException extends BaseException {

	private static final long serialVersionUID = -65626581103240119L;
	
	public FrameException(String errorCode) {
		super(null, errorCode, null, null);
	}
	

	public FrameException(String errorMsg, String errorCode, Object[] parameters, Exception e) {
		super(errorMsg, errorCode, parameters, e);
	}

}
