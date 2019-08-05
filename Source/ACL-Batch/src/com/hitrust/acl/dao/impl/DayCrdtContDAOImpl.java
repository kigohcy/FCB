/**
 * @(#)DayCrdtContDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayCrdtContDAOImpl
 * 
 * Modify History:
 *  v1.00, 2017/10/18, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao.impl;

import com.hitrust.acl.dao.DayCrdtContDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class DayCrdtContDAOImpl extends BaseDAOImpl implements DayCrdtContDAO {

    /**
     * 回沖日額度金額
     * 
     * @param trnsDttm
     * @param acntIndt
     * @param trnsAmnt
     */
    @Override
    public void updateDayContByAcntIndt(String trnsDttm, String acntIndt, Long trnsAmnt) {
        
        String sql = " Update DAY_CRDT_CONT set DAY_CONT= DAY_CONT - ? where ACNT_INDT = ? and TRNS_DATE = ? ";
        
        this.excuteNativeUpdateSql(sql, new String[]{Long.toString(trnsAmnt), acntIndt, trnsDttm});
    }

}
