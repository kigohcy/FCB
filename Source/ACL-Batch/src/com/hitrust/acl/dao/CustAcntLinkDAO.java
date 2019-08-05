/**
 * @(#)CustAcntLinkDAO.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2017/10/02, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao;

import java.util.List;
import java.util.Map;

import com.hitrust.framework.dao.BaseDAO;

public interface CustAcntLinkDAO extends BaseDAO {
	
	/**
	 * 查詢最新的約定帳號累算資料 (約定帳號統計-總表)
	 * @param ecId 平台代碼
	 * @return List
	 */
	public List<Map<String, Object>> countCustAcntLink(String ecId);
}
