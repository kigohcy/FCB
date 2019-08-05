/**
 * @(#) TrnsDataDAOImpl.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2017/10/02, Caleb Chen
 * 	 1) First release
 *  
 */
package com.hitrust.acl.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.dao.TrnsDataDAO;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class TrnsDataDAOImpl extends BaseDAOImpl implements TrnsDataDAO {
	
    /**
     * 統計TRNS_DATA交易金額by各ECID
     * 
     * @param strtDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> countTrnsData(String strtDate, String endDate) {
        StringBuilder sb = new StringBuilder();
        List<String> params = new ArrayList<>();
        
        sb.append(" select EC_ID, TRNS_TYPE, sum(TRNS_AMNT) as SUM_AMNT, count(SETL_ID) as CONT ");
        sb.append(" from  TRNS_DATA ");
        sb.append(" where TRNS_DTTM>= convert(datetime2, ? ) and TRNS_DTTM<= convert(datetime2, ? ) ");
        
        strtDate += " 00:00:00";
        endDate  += " 23:59:59";
        
        params.add(strtDate);
        params.add(endDate);
        
        sb.append(" and TRNS_STTS='02' ");
        sb.append(" group by EC_ID, TRNS_TYPE ");
        sb.append(" order by EC_ID, TRNS_TYPE ");
        
        return this.queryNativeSql(sb.toString(), params.toArray(new String[params.size()]));
    }
    
    /**
     * 取得每日交易明細表
     * 
     * @param strtDate
     * @param endDate
     * @param ecId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TrnsData> getTrnsDataList(Date strtDate, Date endDate, String ecId) {
        DetachedCriteria dc = DetachedCriteria.forClass(TrnsData.class);

        dc.add(Restrictions.eq("ecId", ecId));
        dc.add(Restrictions.between("trnsDttm", strtDate, endDate));
        dc.add(Restrictions.eq("trnsStts", "02")); //交易成功
        dc.addOrder(Order.asc("ordrNo"));

        return this.query(dc);
    }

    /**
     * 以交易狀態為條件取得明細
     * 
     * @param strtDate
     * @param endDate
     * @param trnsStts
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TrnsData> getTrnsDataByStts(Date strtDate, Date endDate, String trnsStts) {
        DetachedCriteria dc = DetachedCriteria.forClass(TrnsData.class);

        dc.add(Restrictions.eq("trnsStts", trnsStts));
        dc.add(Restrictions.between("trnsDttm", strtDate, endDate));
        dc.addOrder(Order.asc("trnsDttm"));

        return this.query(dc);
    }

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
    @SuppressWarnings("unchecked")
    @Override
    public TrnsData getTrnsDataByConditions(String ecId, String custId, String trnsType, String ordrNo,
            String trnsStts) {
        DetachedCriteria dc = DetachedCriteria.forClass(TrnsData.class);

        dc.add(Restrictions.eq("ecId", ecId));
        dc.add(Restrictions.eq("custId", custId));
        dc.add(Restrictions.eq("trnsType", trnsType));
        dc.add(Restrictions.eq("ordrNo", ordrNo));
        dc.add(Restrictions.eq("trnsStts", trnsStts));

        List<TrnsData> list = this.query(dc);
        
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<TrnsData> getTrnsData4JKOS(String ecId, Date strtDate, Date endDate, String trnsStts,
			String orderAttrName) {
		 DetachedCriteria dc = DetachedCriteria.forClass(TrnsData.class);
	 	dc.add(Restrictions.eq("id.ecId", ecId));
        dc.add(Restrictions.eq("trnsStts", trnsStts));
        dc.add(Restrictions.between("trnsDttm", strtDate, endDate));
        dc.addOrder(Order.asc(orderAttrName));

        return this.query(dc);
	}
    
    /**
	 * 依據 交易日期取得狀態未明交易
	 * @param strtDate	  交易日期(yyyyMMdd)
	 * @param endDate	  交易日期(yyyyMMdd)
	 * @return List<TrnsData>
	 * @throws DBException 
	 */
    @SuppressWarnings("unchecked")
	@Override
	public List<TrnsData> fetchNukonwSttsTrns(Date strtDate, Date endDate) {
    		
    	String stts = "01";
    	DetachedCriteria dc = DetachedCriteria.forClass(TrnsData.class);
        dc.add(Restrictions.eq("trnsStts", stts));
        dc.add(Restrictions.between("trnsDttm", strtDate, endDate));
        dc.addOrder(Order.asc("id.ecId"));
        dc.addOrder(Order.asc("trnsType"));
        return this.query(dc);
	}
}
