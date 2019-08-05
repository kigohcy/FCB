/**
 * @(#)CustPltfDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustPltfDAO
 * 
 * Modify History:
 *  v1.00, 2016/02/17, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.CustPltf;
import com.hitrust.framework.dao.BaseDAO;

public interface CustPltfDAO extends BaseDAO {
	
	/**
	 * 取會員已綁定的各平台資料
	 * @param custId 身分證號
	 * @return	List<CustPltf> 會員平台資料檔 
	 */
	public List<CustPltf> getEcDataByCust(String custId);
	
	
	/**
	 * 取得綁定會員平台資料 
	 * @param ecId	平台代碼
	 * @return	List<CustPltf> 會員平台資料檔 
	 */
	public List<CustPltf> getCustPltfByEcId(String ecId);
}
