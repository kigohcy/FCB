/**
 * @(#) DayCustContService.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/03/28
 * 
 */
package com.hitrust.acl.service;

import java.util.List;
import java.util.Map;

public interface DayCustContService {
	/**
	 * 統計客戶資料
	 * 
	 * @param condition
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> countCustDataByStts();
	
	/**
	 * 日終累算作業
	 * 
	 * @param setlDate
	 * @return
	 */
	public void genRpt03(String setlDate) throws Exception;
}
