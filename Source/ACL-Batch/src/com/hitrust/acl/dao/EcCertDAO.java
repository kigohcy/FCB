/**
 * @(#)EcCertDAO.java
 *
 * Copyright (c) 2018 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2018/04/02
 *   1) First release
 *  
 */
package com.hitrust.acl.dao;

import java.util.List;
import java.util.Map;

import com.hitrust.framework.dao.BaseDAO;

public interface EcCertDAO extends BaseDAO {
	 /**
     * 取得到期憑證
     * 
     * @return List<Map<String,Object>> 憑證資料
     */
	public List<Map<String,Object>> getCert4Warning(String warnDay);
}
