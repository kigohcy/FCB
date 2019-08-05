/**
 * @(#) Rfci301SvipDAOImpl.java
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
package com.hitrust.bank.dao.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.Rfci301SvipDAO;
import com.hitrust.bank.model.Rfci301Svip;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class Rfci301SvipDAOImpl extends BaseDAOImpl implements Rfci301SvipDAO {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(Rfci301SvipDAOImpl.class);

	/**
	 * 取得所有限閱戶資料
	 * @return List<String>
	 */
	@Override
	public List<Rfci301Svip> fetchRfci301SvipList() {
		//LOG.info("[]");
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Rfci301Svip.class);
		//ProjectionList project = Projections.projectionList();
		//criteria.add(Restrictions.in("id.idNo", custIds.toArray()));
		//project.add(Projections.groupProperty("id.idNo"));
		//criteria.setProjection(project);
		//criteria.addOrder(Order.asc("id.idNo"));
		
		List<Rfci301Svip> svips = query(criteria);
		
		return svips;
	}

}
