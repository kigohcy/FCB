/**
 * @(#) TbCodeDAOImpl.java
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/04/17
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.TbCodeDAO;
import com.hitrust.bank.model.TbCode;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class TbCodeDAOImpl extends BaseDAOImpl implements TbCodeDAO {

	@Override
	public List<TbCode> fetchByCodeIdsLike(String codeId) {
		DetachedCriteria  detachedCriteria = DetachedCriteria.forClass(TbCode.class);
		DetachedCriteria add = detachedCriteria.add(Restrictions.or(Restrictions.like("id.codeId","01-"+codeId+"%"),Restrictions.like("id.codeId","02-"+codeId+"%")));
		detachedCriteria.addOrder(Order.asc("id.codeId"));
		
		return query(detachedCriteria);
	}

}
