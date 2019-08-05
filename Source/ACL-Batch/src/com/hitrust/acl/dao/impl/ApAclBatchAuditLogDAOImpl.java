/**
 * @(#)ApAclBatchAuditLogDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : ApAclBatchAuditLogDAOImpl
 * 
 * Modify History:
 *  v1.00, 2017/11/10, Caleb
 *   1) First release
 *  
 */
package com.hitrust.acl.dao.impl;

import com.hitrust.acl.dao.ApAclBatchAuditLogDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class ApAclBatchAuditLogDAOImpl extends BaseDAOImpl implements ApAclBatchAuditLogDAO {

    /**
     * 修改 audit log 狀態
     * 
     * @param batchId
     * @param startDatetime
     * @param endDatetime
     * @param txnStatusCode
     */
    @Override
    public void updateAuditStatus(String batchId, String startDatetime, String endDatetime, String txnStatusCode) {
        String sql = " Update AP_ACL_BATCH_AUDIT_LOG set Txn_Status_Code = ? , End_Datetime = ? where BATCH_ID = ? and Start_Datetime = ? ";
      
        this.excuteNativeUpdateSql(sql, new String[]{txnStatusCode, endDatetime, batchId, startDatetime});
    }
}
