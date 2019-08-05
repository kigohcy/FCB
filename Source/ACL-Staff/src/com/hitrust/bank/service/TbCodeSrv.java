/**
 * @(#)TbCodeSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : TbCodeSrv
 * 
 * Modify History:
 *  v1.00, 2016/03/03, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.service;

import java.util.List;

import com.hitrust.bank.model.TbCode;
import com.hitrust.bank.model.base.AbstractTbCode;

public interface TbCodeSrv {
	
	/**
	 * 查詢訊息代碼資料
	 * @return
	 */
	public TbCode getTbCode(String codeId);
	
	/**
	 * 依據 訊息代碼查詢(Like)
	 * 
	 * @param codeId	訊息代碼
	 * @return	回傳 List<TbCode>
	 */
	public List<TbCode> fetchByCodeIdLike(String codeId);
	
	/**
	 * 新增 訊息代碼
	 * 
	 * @param tbCode	訊息代碼
	 * @return	
	 */
	public void insert(TbCode tbCode);
	
	/**
	 * 查詢 訊息代碼By Id
	 * 
	 * @param id
	 * @return	TbCode
	 */
	public TbCode fetchById(AbstractTbCode.Id id);
	
	/**
	 * 修改 訊息代碼
	 * 
	 * @param tbCode	訊息代碼
	 * @return	
	 */
	public void updateTbCode(TbCode tbCode);
	
	/**
	 * 刪除  訊息代碼
	 * 
	 * @param tbCode	訊息代碼
	 * @return	TbCode
	 */
	public void deleteTbCode(TbCode tbCode);

}
