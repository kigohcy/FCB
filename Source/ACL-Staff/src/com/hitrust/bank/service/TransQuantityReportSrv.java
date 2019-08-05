/**
 * @(#)TransQuantityReportSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/06/02, Jimmy
 *   1) First release
 *  
 */

package com.hitrust.bank.service;

import java.util.List;
import java.util.Locale;

import com.hitrust.bank.model.VwTrnsData;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface TransQuantityReportSrv {

	/**
	 * 取得查詢初始化資料(平台代號清單)
	 * @return
	 */
	public VwTrnsData queryInit();

	/**
	 * 平台總表查詢
	 * @param vwTrnsData
	 */
	public void queryPlatform(VwTrnsData vwTrnsData);

	/**
	 * 月報表查詢
	 * @param vwTrnsData
	 */
	public void queryMonthly(VwTrnsData vwTrnsData);

	/**
	 * 日報表查詢
	 * @param vwTrnsData
	 */
	public void queryDaily(VwTrnsData vwTrnsData);

	/**
	 * 明細資料查詢
	 * @param vwTrnsData
	 * @param trnsType		交易類型(A:扣款,B:退款)
	 * @param rptType		報表類型(platform:平台別，monthly:月報表，daily:日報表)
	 * @param ecId			平台代號
	 * @param page			分頁資訊
	 * @return
	 */
	public PageQuery queryDetail(VwTrnsData vwTrnsData, String trnsType, String rptType, String ecId, Page page);

	/**
	 * 明細資料查詢
	 * @param vwTrnsData
	 * @param trnsType		交易類型(A:扣款,B:退款)
	 * @param rptType		報表類型(platform:平台別，monthly:月報表，daily:日報表)
	 * @param ecId			平台代號
	 * @param locale
	 * @return
	 */
	public List<VwTrnsData> queryDetail(VwTrnsData vwTrnsData, String trnsType, String rptType, String ecId, Locale locale);
}
