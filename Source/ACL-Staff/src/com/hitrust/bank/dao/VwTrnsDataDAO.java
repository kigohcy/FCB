/**
 * @(#) VwTrnsDataDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/06/02, Jimmy
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.dao;

import java.util.Date;
import java.util.List;

import com.hitrust.bank.model.VwTrnsData;
import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface VwTrnsDataDAO extends BaseDAO {
	/**
	 * 查詢平台總表
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param ecId
	 *            平台代號
	 * @param trnsStts
	 *            交易狀態
	 * @return List<?>
	 */
	public List<?> getTrnsPlatformQuantity(String strtDate, String endDate, String ecId, String trnsStts);

	/**
	 * 查詢月報表
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param ecId
	 *            平台代號
	 * @param trnsStts
	 *            交易狀態
	 * @return List<?>
	 */
	public List<?> getTrnsMonthlyQuantity(String strtDate, String endDate, String ecId, String trnsStts);

	/**
	 * 查詢日報表
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param ecId
	 *            平台代號
	 * @param trnsStts
	 *            交易狀態
	 * @return List<?>
	 */
	public List<?> getTrnsDailyQuantity(String strtDate, String endDate, String ecId, String trnsStts);

	/**
	 * 查詢明細資料
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param trnsType
	 *            交易類型(A:扣款,B:退款)
	 * @param trnsTime
	 *            交易時間
	 * @param ecId
	 *            平台代號
	 * @param rptType
	 *            報表類型(platform:平台別，monthly:月報表，daily:日報表)
	 * @param page
	 *            分業資料
	 * @param trnsStts
	 *            交易狀態
	 * @return PageQuery
	 */
	public PageQuery getTrnsDetialQuantity(Date strtDate, Date endDate, String trnsType, String trnsTime, String ecId, String rptType, Page page, String trnsStts);
	
	/**
	 * 查詢明細資料
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param trnsType
	 *            交易類型(A:扣款,B:退款)
	 * @param trnsTime
	 *            交易時間
	 * @param ecId
	 *            平台代號
	 * @param rptType
	 *            報表類型(platform:平台別，monthly:月報表，daily:日報表)
	 * @param trnsStts
	 *            交易狀態
	 */
	public List<VwTrnsData> getTrnsDetialQuantity(Date strtDate, Date endDate, String trnsType, String trnsTime, String ecId, String rptType, String trnsStts);
}
