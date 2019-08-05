/**
 * @(#) TxncsSvipDAOImpl.java
 *
 * Directions: 敏感客戶名單檔 DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, Jun 23, 2016, Eason Hsu
 *    1) First release
 *
 */

package com.hitrust.bank.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.TxncsSvipDAO;
import com.hitrust.bank.model.TxncsSvip;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class TxncsSvipDAOImpl extends BaseDAOImpl implements TxncsSvipDAO {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(TxncsSvipDAOImpl.class);

	/**
	 * 取得所有敏感客戶資料
	 * @return List<String>
	 */
	@Override
	public List<String> fetchTxncsSvipList(List<String> custIds) {
		LOG.info("[]");
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TxncsSvip.class);
		ProjectionList project = Projections.projectionList();
		criteria.add(Restrictions.in("id.idNo", custIds.toArray()));
		project.add(Projections.groupProperty("id.idNo"));
		criteria.setProjection(project);
		criteria.addOrder(Order.asc("id.idNo"));
		
		List<String> svips = query(criteria);
		
		return svips;
	}

}
