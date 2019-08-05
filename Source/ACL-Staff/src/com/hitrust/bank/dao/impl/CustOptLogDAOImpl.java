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
 *  v1.01, 2016/07/25, Yann
 *   1) 增加限閱戶權限控管
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.Date;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustOptLogDAO;
import com.hitrust.bank.model.CustOptLog;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.framework.dao.impl.BaseDAOImpl;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class CustOptLogDAOImpl extends BaseDAOImpl implements CustOptLogDAO {
	
	/**
	 * 查詢操作紀錄
	 * 
	 * @param strtDate
	 *            查詢起日
	 * @param endDate
	 *            查詢迄日
	 * @param userId
	 *            身分證字號
	 * @param fnctId
	 *            功能ID
	 * @param page
	 *            分頁資訊
	 * @return PageQuery
	 */
	@Override
	public PageQuery getCustOptLog(Date strtDate, Date endDate, String userId, String fnctId, Page page) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustOptLog.class);

		if (!StringUtil.isBlank(strtDate)) {
			detachedCriteria.add(Restrictions.ge("oprtDttm", strtDate));
		}
		if (!StringUtil.isBlank(endDate)) {
			detachedCriteria.add(Restrictions.le("oprtDttm", endDate));
		}
		if (!StringUtil.isBlank(userId)) {
			detachedCriteria.add(Restrictions.eq("userId", userId));
		}
		if (!StringUtil.isBlank(fnctId)) {
			detachedCriteria.add(Restrictions.eq("fnctId", fnctId));
		}
		
		//v1.01, 增加限閱戶權限控管
//		LoginUser user = (LoginUser) APLogin.getCurrentUser();
//		detachedCriteria.add(Restrictions.not(Restrictions.in("userId", user.getViewLimitIds())));
		
		detachedCriteria.addOrder(Order.desc("oprtDttm"));
		
		return query(detachedCriteria, page);
	}
}
