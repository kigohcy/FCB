/*
 * @(#)AclBatch.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 * v1.00, 2008年3月14日, Kevin Wang
 *   1) First release
 *
 */
package com.hitrust.acl.batch;

/**
 * 批次介面，供手動以Main方式執行使用
 *
 * @author Kevin Wang
 */
public interface AclBatch {
    
    public void execute(String args[]);
}
