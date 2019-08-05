/*
 * @(#)CommunicationException.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/02/21, Tim Cao
 *   1) First release
 */
package com.hitrust.bank.telegram.exception;

import com.hitrust.framework.exception.BaseException;
/**
 * 
 * The exception class of application(For Host processing).
 */
public class CommunicationException extends BaseException {
	
    /**
     * Constructor
     * @param errorCode
     * @param parameters
     */
    public CommunicationException(String errorCode, String[] parameters) {
    	super(null, errorCode, parameters, null);
    }	
    
    /**
     * Constructor
     * @param errorCode
     * @param parameters
     */
    public CommunicationException(String errorCode, String[] parameters, Exception e) {
    	super(null, errorCode, parameters, e);
    }
    
    /**
     * Constructor
     * @param errorMsg
     * @param errorCode
     */
    public CommunicationException(String errorMsg, String errorCode) {
    	super(errorMsg, errorCode, null, null);
    }
	
    /**
     * Constructor
     * @param errorMsg
     * @param errorCode
     * @param parameters
     */
    public CommunicationException(String errorMsg, String errorCode, String[] parameters) {
    	super(errorMsg, errorCode, parameters, null);
    }
	
    /**
     * Constructor
     * @param errorMsg
     * @param errorCode
     * @param parameters
     * @param e
     */
    public CommunicationException(String errorMsg, String errorCode, String[] parameters, Exception e) {
    	super(errorMsg, errorCode, parameters, e);
    }
    
    /**
     * Constructor
     * @param errorCode
     * @param e
     */
    public CommunicationException(String errorCode, Exception e) {
    	super(null, errorCode, null, e);
    }
    
    /**
     * Constructor
     * @param errorCode
     */
    public CommunicationException(String errorCode) {
    	super(null, errorCode, null, null);
    }
}
