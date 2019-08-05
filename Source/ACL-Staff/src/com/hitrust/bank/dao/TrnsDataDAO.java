/**
 * @(#) TrnsDataDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/02/19, Jimmy
 * 	 1) JIRA-Number, First release
 *  v1.01, 2018/03/19
 *   1) 新增電商平台會員代號查詢條件
 * 
 */
package com.hitrust.bank.dao;

import java.util.Date;

import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface TrnsDataDAO extends BaseDAO {
	/**
	 * 取得交易結果清單
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
	 * @return PageQuery
	 */
	public PageQuery getTrnsDataList(Date strtDate, Date endDate, String custId, String realAcnt, String ecUser, String ecId, String trnsType, String trnsStts, Page page);
}
