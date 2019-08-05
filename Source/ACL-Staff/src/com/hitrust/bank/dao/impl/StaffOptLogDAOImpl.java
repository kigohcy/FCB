/**
 * @(#)StaffOptLogDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 操作記錄查詢StaffOptLogDAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/01/25, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.impl;


import java.util.Date;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.StaffOptLogDAO;
import com.hitrust.bank.model.StaffOptLog;
import com.hitrust.framework.dao.impl.BaseDAOImpl;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class StaffOptLogDAOImpl extends BaseDAOImpl implements StaffOptLogDAO {

	public PageQuery queryByCondition(String startDate, String endDate,
			String userId, String fnctId, Page page) {
		return null;
		
	}
	
	/**
	 * 操作紀錄查詢
	 * @param	strtDate
     * @param	endDate	
     * @param	userId
     * @param   fnctId
     * @param	page
     * @return	
     */
	@Override
	public PageQuery getStaffOptLog(Date strtDate, Date endDate,
			String userId, String fnctId, Page page) {
		
           DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StaffOptLog.class);
           
           if(!StringUtil.isBlank(strtDate)){
        	   detachedCriteria.add(Restrictions.ge("oprtDttm", strtDate));
	       }
	       if(!StringUtil.isBlank(endDate)){
	    	   detachedCriteria.add(Restrictions.le("oprtDttm", endDate));
	       }
	       if(!StringUtil.isBlank(userId)){
	    	   detachedCriteria.add(Restrictions.eq("userId", userId));
	       }
	       if(!StringUtil.isBlank(fnctId)){
	    	   detachedCriteria.add(Restrictions.eq("fnctId", fnctId));
	       }
	       
	       detachedCriteria.addOrder(Order.desc("oprtDttm"));
	       
		return query(detachedCriteria, page);
	}
}
