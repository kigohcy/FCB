/**
 * @(#)AbstractApAclBatchAuditLog.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 應用系統批次稽核日誌 (AP_ACL_BATCH_AUDIT_LOG)
 * 
 * Modify History:
 *  v1.00, 2017/11/10, Caleb
 *   1) First release
 *  
 */
package com.hitrust.acl.model.base;

import java.io.Serializable;

import com.hitrust.framework.model.BaseCommand;

public class AbstractApAclBatchAuditLog extends BaseCommand implements Serializable {

    private static final long serialVersionUID = -651943804440713009L;

    // =============== KEY ===============
    private AbstractApAclBatchAuditLog.Id id; // 複合 KEY
    
    // =============== Table Attribute ===============
    private String systemCode;      // 系統代號 
    private String batchName;       // 批次作業名稱
    private String endDatetime;     // 結束時間
    private String txnStatusCode;     // 執行結果
    
    // =============== Getter & Setter ===============
    public String getSystemCode() {
        return systemCode;
    }
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
    public String getBatchName() {
        return batchName;
    }
    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
    public String getEndDatetime() {
        return endDatetime;
    }
    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }
    public String getTxnStatusCode() {
        return txnStatusCode;
    }
    public void setTxnStatusCode(String txnStatusCode) {
        this.txnStatusCode = txnStatusCode;
    }
    public AbstractApAclBatchAuditLog.Id getId() {
        return id;
    }
    public void setId(AbstractApAclBatchAuditLog.Id id) {
        this.id = id;
    }
    
    // =============== 複合 KEY ===============
    public static class Id implements Serializable {

        private static final long serialVersionUID = -5069822547660689514L;

        // =============== Table Attribute ===============
        private String batchId;         // 批次作業代號
        private String startDatetime;   // 開始時間

        // =============== Getter & Setter ===============
        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getStartDatetime() {
            return startDatetime;
        }

        public void setStartDatetime(String startDatetime) {
            this.startDatetime = startDatetime;
        }
    }
}
