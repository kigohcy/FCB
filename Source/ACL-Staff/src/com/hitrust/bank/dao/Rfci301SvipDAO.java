/**
 * @(#) Rfci301SvipDAO.java
 *
 * Directions: 限閱戶名單檔 DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/07/25, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.Rfci301Svip;
import com.hitrust.framework.dao.BaseDAO;

public interface Rfci301SvipDAO extends BaseDAO {

	/**
	 * 取得所有限閱戶資料
	 * @return List<Rfci301Svip>
	 */
	public List<Rfci301Svip> fetchRfci301SvipList();
}
