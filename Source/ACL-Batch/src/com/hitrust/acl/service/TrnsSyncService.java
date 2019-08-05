/**
 * @(#)DayAcntContService.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2018/04/22,
 *   1) First release
 *  
 */
package com.hitrust.acl.service;

import java.util.List;

import com.hitrust.acl.model.TrnsData;

public interface TrnsSyncService {
	/**
	 * 交易不明同步作業
	 * 
	 * @param setlDate
	 * @return
	 */
	public void TrnsSync(String date) throws Exception;
}
