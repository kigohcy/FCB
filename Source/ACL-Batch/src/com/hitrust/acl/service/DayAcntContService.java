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

public interface DayAcntContService {
	/**
	 * 日終累算作業
	 * 
	 * @param setlDate
	 * @return
	 */
	public void genRpt03(String setlDate) throws Exception;
}
