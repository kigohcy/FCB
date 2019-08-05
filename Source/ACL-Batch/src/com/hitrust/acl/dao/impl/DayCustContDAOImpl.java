/**
 * @(#) DayCustContDAO.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/03/28
 * 
 */
package com.hitrust.acl.dao.impl;

import java.util.List;
import java.util.Map;

import com.hitrust.acl.dao.DayCustContDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class DayCustContDAOImpl extends BaseDAOImpl implements DayCustContDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> countCustDataByStts() {
		StringBuffer sql = new StringBuffer();
		sql.append("select STTS, count(STTS) as STTS_COUNT ");
		sql.append("from CUST_DATA ");
		sql.append("group by STTS ");
		sql.append("order by STTS ");
		return this.queryNativeSql(sql.toString());
	}

	@Override
	public void deleteBySetlDate(String setlDate) {
		String sql = "delete from DAY_CUST_CONT where SETL_DATE = ? ";
        this.excuteNativeUpdateSql(sql, new String[]{setlDate});
	}

}
