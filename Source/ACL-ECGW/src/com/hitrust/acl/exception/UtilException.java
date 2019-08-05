/*
 * @(#)UtilException.java
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
 * The exception class of util operation.
 */
public class UtilException extends RuntimeException {

	private static final long serialVersionUID = -5134980471159367464L;

	protected String errorCode;     // 錯誤代碼(設定檔中定義其message)
    protected String errorMsg;      // 錯誤信息(For debug)
    protected String[] parameters;  // 參數列表(錯誤代碼對應之message需要的參數列表)
    protected Exception errorCause; // 錯誤發生原因(原始Exception)
    
    
	public UtilException(String msg) {
		super(msg);
	}	
	
	/**
     * Constructor
     * @param errorMsg
     * @param errorCode
     * @param parameters
     * @param e
     */
    public UtilException(String errorMsg, Exception e) {
    	super(e.getMessage());
    	this.errorMsg   = errorMsg;
    	this.errorCause = e;
    }
    
    /**
     * Constructor
     * @param errorMsg
     * @param errorCode
     * @param parameters
     * @param e
     */
    public UtilException(String errorMsg, String errorCode, String[] parameters, Exception e) {
    	super(e==null? errorMsg : e.getMessage());
    	this.errorCode  = errorCode;
    	this.errorMsg   = errorMsg;
    	this.errorCause = e;
    	this.parameters = parameters;
    }
    
	public UtilException(Exception e) {
		super(e);
	}
	
	// Getter & Setter
	public Exception getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(Exception errorCause) {
		this.errorCause = errorCause;
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

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
}
