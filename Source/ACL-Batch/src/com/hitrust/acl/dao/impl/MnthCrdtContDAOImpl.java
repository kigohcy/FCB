/**
 * @(#)MnthCrdtContDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : MnthCrdtContDAOImpl
 * 
 * Modify History:
 *  v1.00, 2017/10/18, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao.impl;

import com.hitrust.acl.dao.MnthCrdtContDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class MnthCrdtContDAOImpl extends BaseDAOImpl implements MnthCrdtContDAO {

    /**
     * 回沖月額度金額
     * 
     * @param trnsMnth
     * @param acntIndt
     * @param trnsAmnt
     */
    @Override
    public void updateMnthContByAcntIndt(String trnsMnth, String acntIndt, Long trnsAmnt) {
        
        String sql = " Update MNTH_CRDT_CONT set MNTH_CONT= MNTH_CONT - ? where ACNT_INDT = ? and TRNS_MNTH = ? ";
        
        this.excuteNativeUpdateSql(sql, new String[]{ Long.toString(trnsAmnt), acntIndt, trnsMnth });
        
    }

}
