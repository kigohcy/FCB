/**
 * @(#)CustAcntDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntDAO
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.CustAcnt;
import com.hitrust.framework.dao.BaseDAO;

public interface CustAcntDAO extends BaseDAO {
	
	/**
	 * 用會員代碼 取得會員帳號檔 
	 * @param custId 身分證號 
	 * @return  List<CustAcnt>	會員帳號檔 
	 */ 
	public List<CustAcnt> getCustAcntByCustId(String custId);

}
