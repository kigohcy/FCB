/**
 * @(#)CustOptLogDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員操作記錄查詢 DAO
 * 
 * Modify History:
 *  v1.00, 2016/06/27, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.Date;
import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface CustOptLogDAO extends BaseDAO {
	
	/**
	 * 操作紀錄查詢
	 * @param	strtDate
     * @param	endDate	
     * @param	userId
     * @param   fnctId
     * @param	page
     * @return	
     */
	public PageQuery getCustOptLog(Date strtDate, Date endDate, String userId, String fnctId, Page page);
	
}
