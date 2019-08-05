/**
 * @(#)DayAcntContDAO.java
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

import com.hitrust.framework.dao.BaseDAO;

public interface DayAcntContDAO extends BaseDAO {
	
    /**
     * 以setlDate為條件刪除帳號日終累計
     * 
     * @param setlDate
     */
    public void deleteBySetlDate(String setlDate);
}
