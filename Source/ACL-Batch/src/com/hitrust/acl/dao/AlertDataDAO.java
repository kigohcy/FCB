/**
 * @(#)VwHostErrorLogDAO.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2017/10/02, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao;

import com.hitrust.acl.model.AlertData;
import com.hitrust.framework.dao.BaseDAO;


public interface AlertDataDAO extends BaseDAO {

	public void insertAlertData(AlertData alertData);

	/**
     * 以setlDate為條件刪除過期通知
     * 
     * @param setlDate
     */
    public void deleteBySetlDate(String setlDate);

	

}
