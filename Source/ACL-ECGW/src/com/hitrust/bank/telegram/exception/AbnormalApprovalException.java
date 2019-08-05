/*
 * @(#)AbnormalApprovalException.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/02/21, Tim Cao
 *   1) First release
 */
package com.hitrust.bank.telegram.exception;

/**
 * 
 * The exception class of util operation.
 */
public class AbnormalApprovalException extends RuntimeException {
	

    /**
     * Constructor
     * @param errorMsg
     */
    public AbnormalApprovalException(String errorMsg) {
    	super(errorMsg);
    }
}
