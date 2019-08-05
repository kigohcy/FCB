/**
 * @(#)StaffOptLogDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 操作記錄查詢StaffOptLogDAO
 * 
 * Modify History:
 *  v1.00, 2016/01/25, XXXX
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;


import java.util.Date;

import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface StaffOptLogDAO extends BaseDAO{
	
		PageQuery queryByCondition(String startDate, String endDate, String userId,
			String funcId, Page page);
		/**
		 * 操作紀錄查詢
		 * @param	strtDate
	     * @param	endDate	
	     * @param	userId
	     * @param   fnctId
	     * @param	page
	     * @return	
	     */
		public  PageQuery getStaffOptLog(Date strtDate, Date endDate, String userId, String fnctId, Page page);
	
}
