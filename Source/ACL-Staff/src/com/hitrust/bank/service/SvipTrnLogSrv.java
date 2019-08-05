/**
 * @(#) SvipTrnLogSrv.java
 *
 * Directions: 敏感客戶查詢LOG檔
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/06/24, Eason Hsu
 *    1) First release
 *
 */

package com.hitrust.bank.service;

public interface SvipTrnLogSrv {
	
	/**
	 * 新增 敏感戶查詢記錄
	 * @param custId	 身分證字號
	 * @param condition1 查詢條件-身分證字號
	 * @param condition2 查詢條件-實體帳號
	 * @param funcId	 功能代碼
	 * @param reportId	 報表代碼
	 */
	public void insertSvipTrnLog(String custId, String condition1, String condition2, String funcId, String reportId);

}
