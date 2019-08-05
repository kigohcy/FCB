/*
 * @(#) TrnsResultSearchSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/019, Jimmy
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.service;

import java.util.Date;

import com.hitrust.bank.model.TrnsData;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface TrnsResultSearchSrv {

	/**
	 * 取得查詢初始化資料(平台代號清單、查詢日期區間限制)
	 * 
	 * @return TrnsData 
	 */
	public TrnsData queryInit();

	/**
	 * 交易結果查詢
	 * 
	 * @param strtDate	查詢起始日期
	 * @param endDate	查詢結束日期
	 * @param custId	身分證字號
	 * @param realAcnt	銀行扣款帳號
	 * @param ecUser	電商平台會員代號
	 * @param ecId		平台代號
	 * @param trnsType	交易類別
	 * @param trnsStts	交易狀態
	 * @param page		分頁資訊
	 * @return
	 */
	public PageQuery queryTrnsData(Date strtDate, Date endDate, String custId, String realAcnt, String ecUser, String ecId, String trnsType, String trnsStts, Page page);

}
