/**
 * @(#)DailyReportSrvImpl.java
 *
 * Copyright(c)2014 HiTRUST Incorporated.All rights reserved.
 * 
 * Description :每日交易明細表作業service實作
 *
 * Modify History:
 *  v1.00, 2017/10/03, Caleb Chen
 *   1) First release
 */
package com.hitrust.acl.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.hitrust.acl.dao.EcDataDAO;
import com.hitrust.acl.dao.TrnsDataDAO;
import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.acl.service.DailyReportSrv;

public class DailyReportSrvImpl implements DailyReportSrv {
    
    private static Category LOG = Logger.getLogger(DailyReportSrvImpl.class);

    private EcDataDAO ecDataDAO;
    private TrnsDataDAO trnsDataDAO;
    
    public void setEcDataDAO(EcDataDAO ecDataDAO) {
        this.ecDataDAO = ecDataDAO;
    }
    
    public void setTrnsDataDAO(TrnsDataDAO trnsDataDAO) {
        this.trnsDataDAO = trnsDataDAO;
    }

    /**
     * 取得全部扣款平台資料
     * 
     * @return List<EcData> 全部扣款平台資料
     */
    @Override
    public List<EcData> getEcDataList() {
        return ecDataDAO.getEcDataList();
    }

    /**
     * 取得每日交易明細表
     * 
     * @param strtDate
     * @param endDate
     * @param ecId
     * @return
     */
    @Override
    public List<TrnsData> getTrnsDataList(Date strtDate, Date endDate, String ecId) {
        return trnsDataDAO.getTrnsDataList(strtDate, endDate, ecId);
    }
}
