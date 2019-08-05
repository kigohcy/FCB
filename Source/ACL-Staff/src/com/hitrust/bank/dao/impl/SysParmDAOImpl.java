/**
 * @(#) SysParmDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/03/08, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.model.SysParm;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class SysParmDAOImpl extends BaseDAOImpl implements SysParmDAO {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(SysParmDAOImpl.class);

	/**
	 * 依據指定參數名稱, 取得系統參數值
	 * @param parm	參數名稱
	 * @return SysParm or null
	 */
	@Override
	public SysParm fetchSysParmByParm(String parm) {
		LOG.info("[取得系統參數]: " + parm);
		
		List<SysParm> lists = new ArrayList<SysParm>();
		SysParm sysParm = null;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SysParm.class);
		criteria.add(Restrictions.eq("parmCode", parm));
		lists = this.query(criteria);
		
		if (lists.size() > 0) {
			sysParm = lists.get(0);
		}
		
		return sysParm;
	}

}
