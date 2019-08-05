/**
 * @(#)MnthCrdtContDAO.java
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

public interface MnthCrdtContDAO extends BaseDAO {

    /**
     * 回沖月額度金額
     * 
     * @param trnsMnth
     * @param acntIndt
     * @param trnsAmnt
     */
    public void updateMnthContByAcntIndt(String trnsMnth, String acntIndt, Long trnsAmnt);
}
