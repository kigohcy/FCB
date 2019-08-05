/**
 * @(#)DayAcntContDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayAcntContDAO
 * 
 * Modify History:
 *  v1.00, 2016/06/07, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.DayAcntCont;
import com.hitrust.framework.dao.BaseDAO;

public interface DayAcntContDAO extends BaseDAO {
	
	/**
	 * 查詢帳號日終累計 (約定帳號統計-日報)
	 * @param ecId 平台代碼
	 * @param starDate 查詢起日
	 * @param endDate 查詢迄日
	 * @return List
	 */
	public List<DayAcntCont> getDayAcntContList(String ecId, String starDate, String endDate);
	
	/**
	 * 查詢帳號日終累計 (約定帳號統計-月報), 每個月取最後的一筆
	 * @param ecId 平台代碼
	 * @param date 查詢日期
	 * @return DayAcntCont
	 */
	public List<DayAcntCont> getLastDayAcntCont(String ecId, String date);
	
}
