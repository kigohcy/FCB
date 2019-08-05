/**
 * @(#)  DailyBindingRptSrv.java
 * 
 * Copyright (c) 2019 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2019/06/12
 * 
 */
package com.hitrust.acl.service;

import java.util.List;
import java.util.Map;

public interface  DailyBindingRptSrv {

	public List<Map<String, Object>> getCustAcntLog(String today);

	public String generateBindingRpt(String today);

}
