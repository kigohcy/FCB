/**
 * @(#) CheckCertService.java
 *
 * Directions:憑證到期通知批次
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/04/02
 *    1) First release
 *
 */
package com.hitrust.acl.service;

import java.util.List;
import java.util.Map;

public interface CheckCertService {
	/**
     * 取得到期憑證
     * 
     * @return List<Map<String,Object>> 憑證資料
     */
	public List<Map<String,Object>> getCert4Warning(String warnDay);
}
