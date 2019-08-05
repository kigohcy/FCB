/**
 * @(#)AcntCountReportSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 約定帳號統計service
 * 
 * Modify History:
 *  v1.00, 2016/06/06, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.service;

import java.util.List;

import com.hitrust.bank.model.CustAcntLink;
import com.hitrust.framework.model.page.PageQuery;

public interface AcntCountReportSrv {


	/**
	 * 總表查詢
	 * @param vwTrnsData
	 */
	public void queryCustAcntLinkCount(CustAcntLink custAcntLink);

	/**
	 * 月報表查詢
	 * @param vwTrnsData
	 */
	public void queryMonthly(CustAcntLink custAcntLink);

	/**
	 * 日報表查詢
	 * @param vwTrnsData
	 */
	public void queryDaily(CustAcntLink custAcntLink);

	/**
	 * 明細資料查詢
	 * @param custAcntLink
	 * @return
	 */
	public PageQuery queryDetail(CustAcntLink custAcntLink);

	/**
	 * 明細資料查詢
	 * @param custAcntLink
	 * @return
	 */
	public List<CustAcntLink> queryDetailForReportDetail(CustAcntLink custAcntLink);
}
