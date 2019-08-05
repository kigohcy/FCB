/**
 * @(#)DayAcntContDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayAcntContDAOImpl
 * 
 * Modify History:
 *  v1.00, 2017/10/02, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao.impl;

import com.hitrust.acl.dao.DayAcntContDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class DayAcntContDAOImpl extends BaseDAOImpl implements DayAcntContDAO {

    /**
     * 以setlDate為條件刪除會員日終累計
     * 
     * @param setlDate
     */
    @Override
    public void deleteBySetlDate(String setlDate) {
        String sql = " delete from DAY_ACNT_CONT where SETL_DATE = ? ";
        this.excuteNativeUpdateSql(sql, new String[]{setlDate});
    }
}
