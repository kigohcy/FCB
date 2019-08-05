/**
 * @(#)CustCountReportSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員服務統計service
 * 
 * Modify History:
 *  v1.00, 2016/06/06, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.service;

import java.util.List;

import com.hitrust.bank.model.CustData;
import com.hitrust.framework.model.page.PageQuery;

public interface CustCountReportSrv {


	/**
	 * 總表查詢
	 * @param vwTrnsData
	 */
	public void queryCustDataCount(CustData custData);

	/**
	 * 月報表查詢
	 * @param vwTrnsData
	 */
	public void queryMonthly(CustData custData);

	/**
	 * 日報表查詢
	 * @param vwTrnsData
	 */
	public void queryDaily(CustData custData);

	/**
	 * 明細資料查詢
	 * @param custData
	 * @return
	 */
	public PageQuery queryDetail(CustData custData);

	/**
	 * 明細資料查詢
	 * @param custData
	 * @return
	 */
	public List<CustData> queryDetailForReport(CustData custData);
}
