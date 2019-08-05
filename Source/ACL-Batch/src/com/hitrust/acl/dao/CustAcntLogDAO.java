/**
 * @(#)CustAcntLogDAO.java
 *
 * Copyright (c) 2019 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2019/06/11, Organ Hsieh
 *   1) First release
 *  
 */
package com.hitrust.acl.dao;

import java.util.List;
import java.util.Map;

import com.hitrust.framework.dao.BaseDAO;

public interface CustAcntLogDAO extends BaseDAO {
	
	/**
	 * 查詢客戶綁定/解除日報表
	 * @param today 當天日期
	 * @return List
	 */
	public List<Map<String, Object>> getCustAcntLog(String today);
}
