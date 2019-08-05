/**
 * @(#)TrnsDataDAO.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2017/10/02, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.framework.dao.BaseDAO;

public interface TrnsDataDAO extends BaseDAO {

    /**
     * 統計TRNS_DATA交易金額by各ECID
     * 
     * @param strtDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> countTrnsData(String strtDate, String endDate);

    /**
     * 取得每日交易明細表
     * 
     * @param strtDate
     * @param endDate
     * @param ecId
     * @return
     */
    public List<TrnsData> getTrnsDataList(Date strtDate, Date endDate, String ecId);

    /**
     * 以交易狀態為條件取得明細
     * 
     * @param strtDate
     * @param endDate
     * @param trnsStts
     * @return
     */
    public List<TrnsData> getTrnsDataByStts(Date strtDate, Date endDate, String trnsStts);
    
    /**
     * 取得交易明細表
     * 
     * @param ecId
     * @param custId
     * @param trnsType
     * @param ordrNo
     * @param trnsStts
     * @return
     */
    public TrnsData getTrnsDataByConditions(String ecId, String custId, String trnsType, String ordrNo, String trnsStts);
    
    
    /**
     * 以交易狀態為條件取得明細
     * 
     * @param ecId
     * @param strtDate
     * @param endDate
     * @param trnsStts
     * @param orderAttrName
     * @return
     */
    public List<TrnsData> getTrnsData4JKOS(String ecId, Date strtDate, Date endDate, String trnsStts, String orderAttrName);
    
    
    /**
	 * 依據 交易日期取得狀態未明交易
	 * @param strtDate
     * @param endDate
	 */
    public List<TrnsData> fetchNukonwSttsTrns(Date strtDate, Date endDate);
}
