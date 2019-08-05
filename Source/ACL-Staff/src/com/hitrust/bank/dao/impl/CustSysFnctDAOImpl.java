/**
 * @(#)CustSysFnctDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員操作記錄查詢DAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/06/22, Jimmy Yen
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.CustSysFnctDAO;
import com.hitrust.bank.model.CustSysFnct;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class CustSysFnctDAOImpl extends BaseDAOImpl implements CustSysFnctDAO {
	/**
	 * 查詢功能清單
	 * 
	 * @param language
	 * @return List<CustSysFnct>
	 */
	@Override
	public List<CustSysFnct> getEnableCustSysFnctList(String language) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustSysFnct.class);
		detachedCriteria.add(Restrictions.eq("id.lngn", language));
		detachedCriteria.add(Restrictions.eq("isUse", "Y"));
		detachedCriteria.add(Restrictions.ne("menuId", "F03"));
		detachedCriteria.addOrder(Order.asc("id.fnctId"));
		return query(detachedCriteria);
	}
}
