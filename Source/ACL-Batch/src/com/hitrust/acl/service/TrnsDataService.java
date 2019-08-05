/**
 * @(#) TrnsDataService.java
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

import java.util.Date;
import java.util.List;

import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.TrnsData;

public interface TrnsDataService {
	/**
     * 查詢街口交易資料
     * 
     * @param ecId
     * @param strtDate
     * @param endDate
     * @param trnsStts
     * @param orderAttrName
     */
    public List<TrnsData> getTrnsData4JKOS(String ecId, Date strtDate, Date endDate, String trnsStts,
			String orderAttrName);
    
    /**
     * 查詢電商資料
     */
    public List<EcData> getEcDataList();
}
