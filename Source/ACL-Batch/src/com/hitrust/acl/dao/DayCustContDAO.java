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
package com.hitrust.acl.dao;

import java.util.List;
import java.util.Map;

import com.hitrust.framework.dao.BaseDAO;

public interface DayCustContDAO extends BaseDAO {
	/**
	 * 統計客戶資料
	 * 
	 * @param condition
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> countCustDataByStts();
	
	
	/**
     * 以setlDate為條件刪除會員日終累計
     * 
     * @param setlDate
     */
    public void deleteBySetlDate(String setlDate);
}
