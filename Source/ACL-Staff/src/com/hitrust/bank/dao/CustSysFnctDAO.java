/**
 * @(#)CustSysFnctDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員操作記錄查詢DAO
 * 
 * Modify History:
 *  v1.00, 2016/06/22, Jimmy Yen
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.CustSysFnct;
import com.hitrust.framework.dao.BaseDAO;

public interface CustSysFnctDAO extends BaseDAO {
	/**
	 * 查詢功能清單
	 * 
	 * @param language
	 * @return List<CustSysFnct>
	 */
	public List<CustSysFnct> getEnableCustSysFnctList(String language);
	
}
