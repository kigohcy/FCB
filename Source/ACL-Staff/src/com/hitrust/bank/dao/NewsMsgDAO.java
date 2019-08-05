/**
 * @(#) NewsMsgDAO.java
 *
 * Directions: 
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 * v1.00, 2016年06月06日, Jimmy Yen
 * 1) JIRA-Number, First release
 *
 */
package com.hitrust.bank.dao;

import java.util.Date;
import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface NewsMsgDAO extends BaseDAO {

	/**
	 * 查詢公告資訊
	 * 
	 * @param beginDate
	 *            公告起日
	 * @param endDate
	 *            公告迄日
	 * @param type
	 *            公告類型
	 * @param title
	 *            公告標題
	 * @param page
	 *            分頁資訊
	 * @return PageQuery
	 */
	public PageQuery queryMsgs(Date beginDate, Date endDate, String type, String title, Page page);
	
	/**
	 * 根據公告序號刪除公告圖檔
	 * 
	 * @param seq 公告序號
	 */
	public void deleteImagBySeq(String seq);
	
	/**
	 * 根據公告序號異動公告圖檔
	 * 
	 * @param seq 公告序號
	 * @param userId 操作人員代號
	 * @param dttm 系統時間
	 */
	public void updateImagBySeq(String seq, String userId, String dttm);
	
}
