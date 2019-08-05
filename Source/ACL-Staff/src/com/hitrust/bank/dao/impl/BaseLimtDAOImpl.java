/**
 * @(#)BaseLimtDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : BaseLimtDAO
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.hitrust.bank.dao.BaseLimtDAO;
import com.hitrust.bank.model.BaseLimt;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class BaseLimtDAOImpl extends BaseDAOImpl implements BaseLimtDAO {
	
	/**
	 * 取所有的核定限額資料檔 
	 * @return List<BaseLimt> 法定限額資料檔 
	 */
	@Override
	public List<BaseLimt> getBaseLimtList() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BaseLimt.class);
		
		return query(detachedCriteria);
	}

}
