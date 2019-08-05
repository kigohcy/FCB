/**
 * @(#)CustOptLogSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員操作記錄查詢Srv
 * 
 * Modify History:
 *  v1.00, 2016/06/22, Jimmy Yen
 *   1) First release
 *  
 */
package com.hitrust.bank.service;

import java.util.Date;

import com.hitrust.bank.model.CustOptLog;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface CustOptLogSrv {
	/**
	 * 查詢初始化
	 * 
	 * @return CustOptLog
	 */
	public CustOptLog queryInit();

	/**
	 * 查詢
	 * 
	 * @param strtDate
	 *            查詢起日
	 * @param endDate
	 *            查詢迄日
	 * @param userId
	 *            身分證字號
	 * @param fnctId
	 *            功能代碼
	 * @param page
	 *            分頁資訊
	 * @return PageQuery
	 */
	public PageQuery queryCustOptLog(Date strtDate, Date endDate, String userId, String fnctId, Page page);
	
	/**
     * description:查詢明細資料
     * @param staffOptLog
     */
	public void queryDetail(CustOptLog custOptLog);
	
}
