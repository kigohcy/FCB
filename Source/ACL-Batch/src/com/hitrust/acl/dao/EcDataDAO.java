/**
 * @(#)EcDataDAO.java
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

import java.util.List;

import com.hitrust.acl.model.EcData;
import com.hitrust.framework.dao.BaseDAO;

public interface EcDataDAO extends BaseDAO {

	/**
	 * 取得全部扣款平台資料
	 * @return List<EcData> 全部扣款平台資料
	 */
	public List<EcData> getEcDataList();
	
	
	public EcData fetchEcDataByKey(String ecId);

}
