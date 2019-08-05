/**
 * @(#)CustAcntLogDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員連結設定記錄CustAcntLogDAO
 * 
 * Modify History:
 *  v1.00, 2016/02/15, Evan
 *   1) First release
 *  v1.01, 2018/03/20
 *   1) 新增電商平台會員代號查詢條件
 *  
 */

package com.hitrust.bank.dao;

import java.util.Date;

import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface CustAcntLogDAO extends BaseDAO {

	/**
	 * 查詢會員結設定記錄
	 * 
	 * @param strtDate
	 * @param endDate
	 * @param custId
	 * @param realAcnt
	 * @param ecUser 電商平台會員代號
	 * @param ecId
	 * @param queryStts
	 * @return
	 */
	public PageQuery getCustAcntLog(Date strtDate, Date endDate, String custId, String realAcnt, String ecUser, String ecId, String queryStts, String execSrc,
			Page page);
}
