/**
 * @(#)DayCustEcContDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayCustEcContDAOImpl
 * 
 * Modify History:
 *  v1.00, 2017/10/02, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao.impl;

import java.util.List;
import java.util.Map;

import com.hitrust.acl.dao.DayCustEcContDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class DayCustEcContDAOImpl extends BaseDAOImpl implements DayCustEcContDAO {

    /**
     * 查詢最新的會員服務統計資料 (會員服務統計-總表)
     * @return List<Map<String, String>>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> countDayCustEcCont() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select A.EC_CONT, count(A.EC_CONT) as TOTL_CONT ");
        sb.append(" from (select CUST_ID, count(CUST_ID) as EC_CONT ");
        sb.append("     from CUST_PLTF ");
        sb.append("     where (STTS='00' or STTS='01') ");
        sb.append("     group by CUST_ID) as A ");
        sb.append(" group by A.EC_CONT ");
        sb.append(" order by A.EC_CONT desc ");
        
        return this.queryNativeSql(sb.toString());
    }

    /**
     * 以setlDate為條件刪除會員日終累計
     * 
     * @param setlDate
     */
    @Override
    public void deleteBySetlDate(String setlDate) {
        String sql = " delete from DAY_CUST_EC_CONT where SETL_DATE = ? ";
        this.excuteNativeUpdateSql(sql, new String[]{setlDate});
    }

}
