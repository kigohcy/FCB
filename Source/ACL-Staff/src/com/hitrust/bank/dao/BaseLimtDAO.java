/**
 * @(#)BaseLimtDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : BaseLimtDAO
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.BaseLimt;
import com.hitrust.framework.dao.BaseDAO;

public interface BaseLimtDAO extends BaseDAO {
	
	/**
	 * 取所有的核定限額資料檔 
	 * @return List<BaseLimt> 法定限額資料檔 
	 */
	public List<BaseLimt> getBaseLimtList();

}
