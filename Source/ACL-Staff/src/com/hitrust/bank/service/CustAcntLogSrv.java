/**
 * @(#)CustAcntLogSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員連結設定記錄CustAcntLogSrv
 * 
 * Modify History:
 *  v1.00, 2016/02/15, Evan
 *   1) First release
 *  v1.01, 2018/03/20
 *   1) 新增電商平台會員代號查詢條件
 *  
 */
package com.hitrust.bank.service;

import java.util.Date;

import com.hitrust.bank.model.CustAcntLog;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface CustAcntLogSrv {

	/**
	 * 查詢系統參數
	 * @return CustAcntLog  客服系統帳號連結-查詢範圍限制
	 */
	public CustAcntLog queryInit();

	/**
	 * 查詢會員結設定記錄
	 * @param startDate 	查詢起日
	 * @param endDate		查詢迄日
	 * @param queryType		查詢類別
	 * @param id			身分證字號
	 * @param acnt			實體帳號
	 * @param ecUser 電商平台會員代號
	 * @param ecId			平台代號
	 * @param stts			執行結果
	 * @param execSrc		執行來源
	 * @param page			Page
	 * @return				PageQuery
	 */
	public PageQuery queryCustAcntLog(Date strtDate, Date endDate, String queryType, String custId, String realAcnt, String ecUser, String ecId,
			String stts, String execSrc, Page page, CustAcntLog dataBean);
	
}
