/**
 * @(#) EcDataDAO.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.All rights reserved.
 * 
 * Description : 扣款平台管理 ECDataDAO
 * 
 * Modify History:
 *	v1.00, 2015/01/29, Evan
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.EcData;
import com.hitrust.framework.dao.BaseDAO;

public interface EcDataDAO extends BaseDAO {

	/**
	 * 取得全部扣款平台資料
	 * @return List<EcData> 全部扣款平台資料
	 */
	public List<EcData> getEcDataList();

}
