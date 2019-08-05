/**
 * @(#) SysParmServiceImpl.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/03/28
 * 
 */
package com.hitrust.acl.service.impl;

import java.util.List;

import com.hitrust.acl.dao.SysParmDAO;
import com.hitrust.acl.model.SysParm;
import com.hitrust.acl.service.SysParmService;

public class SysParmServiceImpl implements SysParmService {
	
	private SysParmDAO sysParmDAO;

	public void setSysParmDAO(SysParmDAO sysParmDAO) {
		this.sysParmDAO = sysParmDAO;
	}

	@Override
	public SysParm fetchSysParmByParm(String parm) {
		return sysParmDAO.fetchSysParmByParm(parm);
	}
	
	/**
	 * 依據指定參數開頭名稱, 取得系統參數值List
	 * @param parm	參數名稱開始字串
	 * @return SysParm or null
	 */
   @Override
	public List<SysParm> getSysParmListLike(String beginStr) {
	   return sysParmDAO.getSysParmListLike(beginStr);
   }
  
   /**
    * 設定系統參數
    * 
    * @param parmCode
    * @param parmValue
    */
   @Override
   public void updateSysParmByParmCode(String parmCode, String parmValue) {
	   sysParmDAO.updateSysParmByParmCode(parmCode, parmValue);
   }
}
