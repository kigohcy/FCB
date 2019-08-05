/*
 * @(#)BaseException.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/02/21, Tim Cao
 *   1) First release
 */
package com.hitrust.acl.exception;

import org.apache.log4j.Logger;

/**
 * 
 * Super class of all system exception.
 */
public class BaseException extends Exception {

	private static final long serialVersionUID = 5195999072048508799L;

	// log4J category
    static Logger LOG = Logger.getLogger(BaseException.class);

    protected String errorCode;
    protected String errorMsg;
    protected Exception errorCause;
    
    public BaseException() {
        super();
    }
    
    public BaseException(String msg) {
    	super(msg);
    	this.errorMsg = msg;
    }
    
    /**
     * Constructor
     * @param e
     */
    public BaseException(Exception e) {
    	super(e.getMessage());
    	this.errorCause = e;
    }
    
    public BaseException(String msg, String[] error) {
    	super(msg);
    	this.errorCode = error[0];
    	this.errorMsg  = error[1];    	
    }
    
    /**
     * Constructor
     * @param e
     * @param error
     */
    public BaseException(Exception e, String[] error) {
    	super(e.getMessage());
    	this.errorCode = error[0];
    	this.errorMsg  = error[1];
    	this.errorCause = e;
    }
    
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Exception getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(Exception errorCause) {
		this.errorCause = errorCause;
	}
}
