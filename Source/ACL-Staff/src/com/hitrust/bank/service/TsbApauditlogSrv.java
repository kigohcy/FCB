/**
 * @(#) TsbApauditlogSrv.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/18, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.service;

import javax.servlet.http.HttpServletRequest;

import com.hitrust.framework.model.BaseCommand;

public interface TsbApauditlogSrv {
	
	/**
	 * 新增應用系統日誌
	 * @param rslt	  交易執行結果, 成功: Y, 失敗: N
	 * @param rsltMsg 交易執行失敗錯誤代碼或訊息
	 * @param commnd
	 * @param req
	 */
	public void insertTsbApauditLog(String rslt, String rsltMsg, BaseCommand commnd, HttpServletRequest req);

}
