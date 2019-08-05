/**
 * @(#) EcDataAprvDAO.java
 *
 * Copyright (c) 2018 HiTRUST Incorporated.All rights reserved.
 * 
 * Description : 電商核可管理 EcDataAprvDAO
 * 
 * Modify History:
 *	v1.00, 2018/04/12
 * 	 1) First release
 * 
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.EcDataAprv;
import com.hitrust.framework.dao.BaseDAO;

public interface EcDataAprvDAO extends BaseDAO {
	/**
	 * 取得待覆核扣款平台資料
	 * 
	 * @return List<EcDataAprv> 待覆核扣款平台資料
	 */
	public List<EcDataAprv> getEcDataAprvList(String strtDate, String endDate,  String oprtType, String dataStts);
}
