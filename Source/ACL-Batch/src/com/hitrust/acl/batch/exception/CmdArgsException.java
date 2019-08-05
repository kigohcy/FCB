/*
 * @(#)CmdArgsException.java
 *
 * Copyright (c) 2008 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 * v1.00, 2008年8月20日, Kevin
 *   1) First release
 *
 */
package com.hitrust.acl.batch.exception;

/**
 *
 * @author Kevin Wang
 */
public class CmdArgsException extends Exception {
    
    /** Creates a new instance of CmdArgsException */
    public CmdArgsException() {
        super();
    }
    
    public CmdArgsException(String msg) {
        super(msg);
    }
    
}
