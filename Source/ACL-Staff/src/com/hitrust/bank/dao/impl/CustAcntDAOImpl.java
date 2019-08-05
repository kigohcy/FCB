/**
 * @(#)CustAcntDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntDAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.CustAcntDAO;
import com.hitrust.bank.model.CustAcnt;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class CustAcntDAOImpl extends BaseDAOImpl implements CustAcntDAO {

	/**
	 * 用會員代碼 取得會員帳號檔 
	 * @param custId 身分證號 
	 * @return  List<CustAcnt>	會員帳號檔 
	 */ 
	@Override
	public List<CustAcnt> getCustAcntByCustId(String custId) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CustAcnt.class);
		detachedCriteria.add(Restrictions.eq("id.custId", custId));
		
		return query(detachedCriteria);
	}

}
