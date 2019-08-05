/**
 * @(#) TbCodeDAO.java
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/04/17
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.TbCode;
import com.hitrust.framework.dao.BaseDAO;

public interface TbCodeDAO extends BaseDAO {
	/**
	 * 依據 訊息代碼查詢(Like)
	 * 
	 * @param codeId	訊息代碼
	 * @return	回傳 List<TbCode>
	 */
	public List<TbCode> fetchByCodeIdsLike(String codeId);
}
