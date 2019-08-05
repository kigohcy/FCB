/**
 * @(#) TxncsSvipDAO.java
 *
 * Directions: 敏感客戶名單檔 DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/06/23, Eason Hsu
 *    1) First release
 *
 */

package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.framework.dao.BaseDAO;

public interface TxncsSvipDAO extends BaseDAO {

	/**
	 * 取得所有敏感客戶資料
	 * @param custIds 
	 * @return List<String>
	 */
	public List<String> fetchTxncsSvipList(List<String> custIds);
}
