/**
 * @(#) MnthCrdtContDAO.java
 *
 * Directions:
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/03/23
 *    1) First release
 *
 */

package com.hitrust.bank.dao;

public interface MnthCrdtContDAO {
	
	/**
	 * 取得 月額度累計金額 by 交易月份+身分證字號+實體帳號+會員服務序號+電商平台代號+電商平台會員代號
	 * @param trnsMnth
	 * @param custId
	 * @param realAcnt
	 * @param custSerl
	 * @param ecId
	 * @param ecUser
	 * @return long
	 * @throws DBException
	 */
	public long getMnthSumByAcnt(String trnsMnth, String custId, String realAcnt, String custSerl, String ecId, String ecUser);
}
