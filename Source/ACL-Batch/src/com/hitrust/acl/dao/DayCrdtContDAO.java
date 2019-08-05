/**
 * @(#)DayCrdtContDAO.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2017/10/18, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao;

import com.hitrust.framework.dao.BaseDAO;

public interface DayCrdtContDAO extends BaseDAO {
    
    /**
     * 回沖日額度金額
     * 
     * @param trnsDttm
     * @param acntIndt
     * @param trnsAmnt
     */
    public void updateDayContByAcntIndt(String trnsDttm, String acntIndt, Long trnsAmnt);

}
