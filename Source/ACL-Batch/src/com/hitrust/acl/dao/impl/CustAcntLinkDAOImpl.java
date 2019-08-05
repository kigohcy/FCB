/**
 * @(#)CustAcntLinkDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntLinkDAOImpl
 * 
 * Modify History:
 *  v1.00, 2017/10/02, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao.impl;

import java.util.List;
import java.util.Map;

import com.hitrust.acl.dao.CustAcntLinkDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class CustAcntLinkDAOImpl extends BaseDAOImpl implements CustAcntLinkDAO {
	
	/**
	 * 查詢所有綁定帳號的最新服務狀態
	 * @param ecId 平台代碼
	 * @return List
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<Map<String, Object>> countCustAcntLink(String ecId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select STTS, count(STTS) as cnt ");
		sb.append(" from CUST_ACNT_LINK ");
		sb.append(" where EC_ID = ? ");
		
		sb.append(" group by STTS ");
		sb.append(" order by STTS ");
		
		return this.queryNativeSql(sb.toString(), new String[]{ecId});
	}
}
