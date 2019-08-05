/**
 * @(#) SysParmDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/03/08, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao;

import com.hitrust.bank.model.SysParm;
import com.hitrust.framework.dao.BaseDAO;

public interface SysParmDAO extends BaseDAO {
	
	/**
	 * 依據指定參數名稱, 取得系統參數值
	 * @param parm	參數名稱
	 * @return SysParm or null
	 */
	public SysParm fetchSysParmByParm(String parm);

}
