/**
 * @(#)ApAclBatchAuditLogDAO.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2017/11/10, Caleb
 *   1) First release
 *  
 */
package com.hitrust.acl.dao;

import com.hitrust.framework.dao.BaseDAO;

public interface ApAclBatchAuditLogDAO extends BaseDAO {

    /**
     * 修改 audit log 狀態
     * 
     * @param batchId
     * @param startDatetime
     * @param endDatetime
     * @param txnStatusCode
     */
    public void updateAuditStatus(String batchId, String startDatetime, String endDatetime, String txnStatusCode);
}
