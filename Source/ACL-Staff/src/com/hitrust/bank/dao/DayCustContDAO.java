/**
 * @(#)DayCustContDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayCustContDAO
 * 
 * Modify History:
 *  v1.00, 2016/06/08, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.DayCustCont;
import com.hitrust.framework.dao.BaseDAO;

public interface DayCustContDAO extends BaseDAO {
	
	/**
	 * 查詢會員日終累計 (會員服務統計-日報)
	 * @param starDate 查詢起日
	 * @param endDate 查詢迄日
	 * @return List
	 */
	public List<DayCustCont> getDayCustContList(String starDate, String endDate);
	
	/**
	 * 查詢會員日終累計 (會員服務統計-月報), 每個月取最後的一筆
	 * @param date 查詢日期
	 * @return DayCustCont
	 */
	public List<DayCustCont> getLastDayCustCont(String date);
	
}
