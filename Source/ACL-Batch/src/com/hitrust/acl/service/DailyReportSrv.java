/**
 * @(#)DailyReportSrv.java
 *
 * Copyright(c)2014 HiTRUST Incorporated.All rights reserved.
 * 
 * Description :每日交易明細表作業service介面
 *
 * Modify History:
 *  v1.00, 2017/10/03, Caleb Chen
 *   1) First release
 */
package com.hitrust.acl.service;

import java.util.Date;
import java.util.List;

import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.TrnsData;

public interface DailyReportSrv {

    /**
     * 取得全部扣款平台資料
     * 
     * @return List<EcData> 全部扣款平台資料
     */
    public List<EcData> getEcDataList();
    
    /**
     * 取得每日交易明細表
     * 
     * @param strtDate
     * @param endDate
     * @param ecId
     * @return
     */
    public List<TrnsData> getTrnsDataList(Date strtDate, Date endDate, String ecId);
    
}
