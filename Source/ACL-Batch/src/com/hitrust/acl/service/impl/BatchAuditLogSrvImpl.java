/**
 * @(#)BatchAuditLogSrvImpl.java
 *
 * Copyright(c)2014 HiTRUST Incorporated.All rights reserved.
 * 
 * Description :Log service實作
 *
 * Modify History:
 *  v1.00, 2017/11/10, Caleb
 *   1) First release
 */
package com.hitrust.acl.service.impl;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.hitrust.acl.dao.ApAclBatchAuditLogDAO;
import com.hitrust.acl.model.ApAclBatchAuditLog;
import com.hitrust.acl.model.base.AbstractApAclBatchAuditLog;
import com.hitrust.acl.service.BatchAuditLogSrv;
import com.hitrust.acl.util.DateUtil;

public class BatchAuditLogSrvImpl implements BatchAuditLogSrv {
    
    private static Category LOG = Logger.getLogger(BatchAuditLogSrvImpl.class);
    
    public static final String SYSTEM_CODE = "ACLINK";

    private ApAclBatchAuditLogDAO apAclBatchAuditLogDAO;
    
    public void setApAclBatchAuditLogDAO(ApAclBatchAuditLogDAO apAclBatchAuditLogDAO) {
        this.apAclBatchAuditLogDAO = apAclBatchAuditLogDAO;
    }
    
    
    /**
     * init audit log
     * 
     * @param jobExecutionContext
     * @return
     */
    @Override
    public ApAclBatchAuditLog initAuditLog(JobExecutionContext jobExecutionContext) throws Exception {
        String startDatetime = DateUtil.getCurrentTime("DT", "AD");
        AbstractApAclBatchAuditLog.Id id = new AbstractApAclBatchAuditLog.Id();
        id.setBatchId(jobExecutionContext.getJobDetail().getName());
        id.setStartDatetime(startDatetime);
        
        ApAclBatchAuditLog log = new ApAclBatchAuditLog();
        log.setBatchName(jobExecutionContext.getJobDetail().getDescription());
        log.setId(id);
        log.setSystemCode(SYSTEM_CODE);
        apAclBatchAuditLogDAO.save(log);
        
        return log;
    }

    /**
     * 修改 audit log 狀態
     * 
     * @param batchId
     * @param startDatetime
     * @param endDatetime
     * @param txnStatusCode
     */
    @Override
    public void updateAuditStatus(String batchId, String startDatetime, String endDatetime, String txnStatusCode) throws Exception {
        apAclBatchAuditLogDAO.updateAuditStatus(batchId, startDatetime, endDatetime, txnStatusCode);
    }

}
