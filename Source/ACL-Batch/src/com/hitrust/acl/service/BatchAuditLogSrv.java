/**
 * @(#)BatchAuditLogSrv.java
 *
 * Copyright(c)2014 HiTRUST Incorporated.All rights reserved.
 * 
 * Description :Log service介面
 *
 * Modify History:
 *  v1.00, 2017/11/10, Caleb
 *   1) First release
 */
package com.hitrust.acl.service;

import org.quartz.JobExecutionContext;

import com.hitrust.acl.model.ApAclBatchAuditLog;

public interface BatchAuditLogSrv {

    /**
     * init audit log
     * 
     * @param jobExecutionContext
     * @return
     */
    public ApAclBatchAuditLog initAuditLog(JobExecutionContext jobExecutionContext) throws Exception;
    
    /**
     * 修改audit log
     * 
     * @param
     * @throws Exception
     */
    public void updateAuditStatus(String batchId, String startDatetime, String endDatetime, String txnStatusCode) throws Exception;
}
