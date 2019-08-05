/**
 * @(#)TrnsLimitsMgmtSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : TrnsLimitsMgmtSrv
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.service;

import com.hitrust.bank.model.CustAcnt;

public interface TrnsLimitsMgmtSrv {
	
	/**
	 * 交易限額查詢服務
	 * @param custAcnt command
	 */
	public void queryTrnsLimitsSrvData(CustAcnt custAcnt);
	
	/**
	 * 修改交易限額
	 * @param custAcnt	command
	 */
	public void updateTrnsLimtAcnt(CustAcnt custAcnt);
	
	
	/**
	 * 記錄更新後結果
	 * @param custAcnt	command
	 */
	public void recordAfterOpt(CustAcnt custAcnt);

}
