/**
 * @(#)CustPltfDAO.java
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

public interface CustPltfDAO extends BaseDAO {
	
    /**
     * 查詢最新的會員服務統計資料
     * @return List<Map<String, String>>
     */
	public List<Map<String, Object>> countDayCustEcCont();
	
}
