/**
 * @(#)CustAcntLogDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntLogDAOImpl
 * 
 * Modify History:
 *  v1.00, 2019/06/11, Organ Hsieh
 *   1) First release
 *  
 */
package com.hitrust.acl.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.CustAcntLogDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class CustAcntLogDAOImpl extends BaseDAOImpl implements CustAcntLogDAO {
	
	static Logger LOG = Logger.getLogger(CustAcntLogDAOImpl.class);
	/**
	 * 查詢所有綁定帳號的最新服務狀態
	 * @param ecId 平台代碼
	 * @return List
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<Map<String, Object>> getCustAcntLog(String today) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT  EC_ID,CUST_ID,CASE WHEN STTS='00' THEN 'A' ELSE 'D' END AS STATUS,REAL_ACNT,CRET_DTTM ");
		sb.append(" FROM CUST_ACNT_LOG WITH (NOLOCK) ");
		sb.append(" WHERE STTS IN ('00' ,'02') AND ERR_CODE='0000' AND CRET_DTTM < CONVERT(CHAR(10), GETDATE(),126)+' 00:00:00' ");
		//sb.append(" WHERE WHERE STTS IN ('00' ,'02') AND ERR_CODE='0000' AND CRET_DTTM < ? ");
		sb.append(" ORDER BY EC_ID,CUST_ID,CRET_DTTM ");
		String sbSql=sb.toString();
		LOG.info("SQL:"+sbSql);
		today += " 00:00:00";
		LOG.info("Today:"+today);
		//return this.queryNativeSql(sbSql, new String[]{today});
		return this.queryNativeSql(sbSql, new String[]{});
	}
}
