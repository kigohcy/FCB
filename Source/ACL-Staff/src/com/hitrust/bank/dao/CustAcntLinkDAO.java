/**
 * @(#)CustAcntLinkDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntLinkDAO
 * 
 * Modify History:
 *  v1.00, 2016/02/17, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.CustAcntLink;
import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface CustAcntLinkDAO extends BaseDAO {
	
	/**
	 * 取得會員綁定的各平台之帳號資料
	 * @param custId	身分證號	
	 * @param ecId		平台代碼
	 * @return	List<CustAcntLink> 會員帳號連結檔 
	 */
	public List<CustAcntLink> getCustAcntLink(String custId, String ecId);
	
	/**
	 * 查詢最新的約定帳號累算資料 (約定帳號統計-總表)
	 * @param ecId 平台代碼
	 * @return List
	 */
	public List countCustAcntLink(String ecId);
	
	/**
	 * 總表約定帳號統計明細查詢
	 * @param ecId 平台代碼
	 * @param stts 連結狀態
	 * @return List<CustAcntLink>
	 */
	public PageQuery getCustAcntLinkDetl(String ecId, String stts, Page page);
	
	/**
	 * 總表約定帳號統計明細查詢
	 * @param ecId 平台代碼
	 * @param stts 連結狀態
	 * @return List<CustAcntLink>
	 */
	public List<CustAcntLink> getCustAcntLinkDetl(String ecId, String stts);
}
