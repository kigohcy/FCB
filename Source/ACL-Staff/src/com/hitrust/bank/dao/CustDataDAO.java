/**
 * @(#)CustDataDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustDataDAO
 * 
 * Modify History:
 *  v1.00, 2016/02/17, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.CustData;
import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface CustDataDAO extends BaseDAO {
	
	/**
	 * 查詢最新的會員累算資料 (會員服務統計-總表)
	 * @return List
	 */
	public List countCustData();
	
	/**
	 * 會員服務統計-總表-明細查詢
	 * @param stts 會員狀態
	 * @param page
	 * @return
	 */
	public PageQuery getCustDataDetl(String stts, Page page);
	
	/**
	 * 會員服務統計-總表-明細查詢
	 * @param stts 會員狀態
	 * @return
	 */
	public List<CustData> getCustDataDetl(String stts);
	
	/**
	 * get custName by custId
	 * @param custId
	 * @return
	 */
	public String getCustNameByCustId(String custId);
	
	/**
	 * get custName by realAcnt
	 * @param realAcnt
	 * @return
	 */
	public String getCustNameByRealAcnt(String realAcnt);
}
