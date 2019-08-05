/**
 * @(#) EcDataAprvDAOImpl.java
 *
 * Copyright (c) 2018 HiTRUST Incorporated.All rights reserved.
 * 
 * Description : 電商核可管理 EcDataAprvDAOImpl
 * 
 * Modify History:
 *	v1.00, 2018/04/12
 * 	 1) First release
 * 
 */
package com.hitrust.bank.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.dao.EcDataAprvDAO;
import com.hitrust.bank.model.EcDataAprv;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class EcDataAprvDAOImpl extends BaseDAOImpl implements EcDataAprvDAO {
	/**
	 * 取得待覆核扣款平台資料
	 * 
	 * @param strtDate
	 * @param endDate
	 * @param oprtType
	 * @param dataStts
	 * @return List<EcDataAprv> 待覆核扣款平台資料
	 */
	@Override
	public List<EcDataAprv> getEcDataAprvList(String strtDate, String endDate, String oprtType, String dataStts) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EcDataAprv.class);
		if (StringUtils.isNoneBlank(strtDate)) {
			detachedCriteria.add(Restrictions.ge("id.cretDttm", DateUtil.formatStrToDate(strtDate, "000000")));
		}
		if (StringUtils.isNoneBlank(endDate)) {
			detachedCriteria.add(Restrictions.le("id.cretDttm", DateUtil.formatStrToDate(endDate, "235959")));
		}
		if (StringUtils.isNoneBlank(oprtType)) {
			detachedCriteria.add(Restrictions.eq("oprtType", oprtType));
		}
		if (StringUtils.isNoneBlank(dataStts)) {
			detachedCriteria.add(Restrictions.eq("dataStts", dataStts));
		}
		detachedCriteria.addOrder(Order.desc("id.cretDttm"));
		detachedCriteria.addOrder(Order.asc("id.ecId"));
		return query(detachedCriteria);
	}
}
