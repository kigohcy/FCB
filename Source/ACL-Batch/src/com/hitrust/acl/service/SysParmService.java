/**
 * @(#) SysParmService.java
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/04/08
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.acl.service;

import java.util.List;

import com.hitrust.acl.model.SysParm;

public interface SysParmService {
	/**
	 * 依據指定參數名稱, 取得系統參數值
	 * @param parm	參數名稱
	 * @return SysParm or null
	 */
	public SysParm fetchSysParmByParm(String parm);

	/**
	 * 依據指定參數開頭名稱, 取得系統參數值List
	 * @param parm	參數名稱開始字串
	 * @return SysParm or null
	 */
	public List<SysParm> getSysParmListLike(String beginStr);

	/**
	 * 設定系統參數
	 * 
	 * @param parmCode
	 * @param parmValue
	 */
	public void updateSysParmByParmCode(String parmCode, String parmValue);
}
