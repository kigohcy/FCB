/**
 * @(#) EcDataDAOImpl.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.All rights reserved.
 * 
 * Description : EcDataDAOImpl
 * 
 * Modify History:
 *	v1.00, 2017/10/02, Caleb Chen
 * 	 1)First release
 * 
 */
package com.hitrust.acl.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.dao.EcDataDAO;
import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class EcDataDAOImpl extends BaseDAOImpl implements EcDataDAO {

	/**
	 * 取得全部扣款平台資料
	 * @return List<EcData> 全部扣款平台資料
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<EcData> getEcDataList() {
		
		DetachedCriteria  detachedCriteria = DetachedCriteria.forClass(EcData.class);
		detachedCriteria.addOrder(Order.asc("ecId"));
		
		return query(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public EcData fetchEcDataByKey(String ecId) {
		DetachedCriteria dc = DetachedCriteria.forClass(EcData.class);
        dc.add(Restrictions.eq("ecId", ecId));

        return (EcData) this.query(dc).get(0);
	}
}
