/**
 * @(#) EcDataDAOImpl.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.All rights reserved.
 * 
 * Description : 扣款平台管理 ECDataDAOImpl
 * 
 * Modify History:
 *	v1.00, 2015/01/29, Evan
 * 	 1)First release
 * 
 */
package com.hitrust.bank.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import com.hitrust.bank.dao.EcDataDAO;
import com.hitrust.bank.model.EcData;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class EcDataDAOImpl extends BaseDAOImpl implements EcDataDAO {

	/**
	 * 取得全部扣款平台資料
	 * @return List<EcData> 全部扣款平台資料
	 */
	@Override
	public List<EcData> getEcDataList() {
		
		DetachedCriteria  detachedCriteria = DetachedCriteria.forClass(EcData.class);
		detachedCriteria.addOrder(Order.asc("ecId"));
		
		return query(detachedCriteria);
	}
}
