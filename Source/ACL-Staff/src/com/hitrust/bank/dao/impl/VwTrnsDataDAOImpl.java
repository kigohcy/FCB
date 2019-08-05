/**
 * @(#) VwTrnsDataDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/06/02, Jimmy
 * 	 1) First release
 *  v1.01, 2016/07/25, Yann
 *   1) 增加限閱戶權限控管
 *   
 *  v1.02, 2019/06/19, Organ  
 *   1) 交易量統計增加繳費稅   
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.VwTrnsDataDAO;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.VwTrnsData;
import com.hitrust.framework.dao.impl.BaseDAOImpl;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class VwTrnsDataDAOImpl extends BaseDAOImpl implements VwTrnsDataDAO {
	/**
	 * 查詢平台總表
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param ecId
	 *            平台代號
	 * @param trnsStts
	 *            交易狀態
	 * @return List<?>
	 */
	@Override
	public List<?> getTrnsPlatformQuantity(String strtDate, String endDate, String ecId, String trnsStts) {
		StringBuffer sb = new StringBuffer();
		//20190625 交易量統計增加繳費稅 Begin
		sb.append("select EC_ID, TRNS_TYPE, EC_NAME_CH, count(TRNS_AMNT) as COUNT, sum(TRNS_AMNT) as TRNS_AMNT, sum(FEE_AMNT) as FEE_AMNT, FEE_TYPE, FEE_RATE, TAX_TYPE, TAX_RATE ");
		//20190625 交易量統計增加繳費稅 End
		sb.append(" from VW_TRNS_DATA");
//		sb.append(" where TRNS_STTS=02");

		sb.append(" WHERE TRNS_DTTM>= convert(datetime2, '" + strtDate + " 00:00:00') and TRNS_DTTM<= convert(datetime2, '" + endDate + " 23:59:59')");
		if (!StringUtil.isBlank(ecId)) {
			sb.append(" and EC_ID = " + "'" + ecId + "'");
		}
		if(StringUtils.isNoneBlank(trnsStts)){
			sb.append(" AND TRNS_STTS = "+trnsStts);
		}

		//20190625 交易量統計增加繳費稅 Begin 
		sb.append(" group by EC_ID, TRNS_TYPE, EC_NAME_CH, FEE_TYPE, FEE_RATE, TAX_TYPE, TAX_RATE");
		
		sb.append(" order by EC_ID, TRNS_TYPE, EC_NAME_CH, FEE_TYPE, FEE_RATE");

		return this.queryNativeSql(sb.toString());
	}

	/**
	 * 查詢月報表
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param ecId
	 *            平台代號
	 * @param trnsStts
	 *            交易狀態
	 * @return List<?>
	 */
	@Override
	public List<?> getTrnsMonthlyQuantity(String strtDate, String endDate, String ecId, String trnsStts) {
		StringBuffer sb = new StringBuffer();
		//20190625 交易量統計增加繳費稅 Begin
		sb.append("select TRNS_MNTH as TRNS_TIME, EC_ID, TRNS_TYPE, EC_NAME_CH, FEE_TYPE, FEE_RATE, TAX_TYPE, TAX_RATE, count(TRNS_MNTH) as COUNT, sum(TRNS_AMNT) as TRNS_AMNT, sum(FEE_AMNT) as FEE_AMNT");
		//20190625 交易量統計增加繳費稅 End
		sb.append(" from VW_TRNS_DATA");
//		sb.append(" where TRNS_STTS=02");

		sb.append(" WHERE TRNS_DTTM >= convert(datetime2, '" + strtDate + " 00:00:00') and TRNS_DTTM<= convert(datetime2, '" + endDate + " 23:59:59')");
		if (!StringUtil.isBlank(ecId)) {
			sb.append(" and EC_ID = " + "'" + ecId + "'");
		}
		if(StringUtils.isNoneBlank(trnsStts)){
			sb.append(" AND TRNS_STTS = "+trnsStts);
		}

		//20190625 交易量統計增加繳費稅 Begin
		sb.append(" group by TRNS_MNTH, EC_ID, TRNS_TYPE, EC_NAME_CH, FEE_TYPE, FEE_RATE, TAX_TYPE, TAX_RATE");
		//20190625 交易量統計增加繳費稅 End
		sb.append(" order by TRNS_MNTH, EC_ID, TRNS_TYPE, EC_NAME_CH, FEE_TYPE, FEE_RATE");

		return this.queryNativeSql(sb.toString());
	}

	/**
	 * 查詢日報表
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param ecId
	 *            平台代號
	 * @param trnsStts
	 *            交易狀態
	 * @return List<?>
	 */
	@Override
	public List<?> getTrnsDailyQuantity(String strtDate, String endDate, String ecId, String trnsStts) {
		StringBuffer sb = new StringBuffer();
		
		//20190625 交易量統計增加繳費稅 Begin
		sb.append("select TRNS_DATE as TRNS_TIME, EC_ID, TRNS_TYPE, EC_NAME_CH, FEE_TYPE, FEE_RATE, TAX_TYPE, TAX_RATE, count(TRNS_DATE) as COUNT, sum(TRNS_AMNT) as TRNS_AMNT, sum(FEE_AMNT) as FEE_AMNT");
		//20190625 交易量統計增加繳費稅 End
		sb.append(" from VW_TRNS_DATA");
//		sb.append(" where TRNS_STTS=02");

		sb.append(" WHERE TRNS_DTTM>= convert(datetime2, '" + strtDate + " 00:00:00') and TRNS_DTTM<= convert(datetime2, '" + endDate + " 23:59:59')");
		if (!StringUtil.isBlank(ecId)) {
			sb.append(" and EC_ID = " + "'" + ecId + "'");
		}
		if(StringUtils.isNoneBlank(trnsStts)){
			sb.append(" AND TRNS_STTS = "+trnsStts);
		}

		//20190625 交易量統計增加繳費稅 Begin
		sb.append(" group by TRNS_DATE, EC_ID, TRNS_TYPE, EC_NAME_CH, FEE_TYPE, FEE_RATE, TAX_TYPE, TAX_RATE");
	    //20190625 交易量統計增加繳費稅 End		
		sb.append(" order by TRNS_DATE, EC_ID, TRNS_TYPE, EC_NAME_CH, FEE_TYPE, FEE_RATE");

		return this.queryNativeSql(sb.toString());
	}

	/**
	 * 查詢明細資料
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param trnsType
	 *            交易類型(A:扣款,B:退款)
	 * @param trnsTime
	 *            交易時間
	 * @param ecId
	 *            平台代號
	 * @param rptType
	 *            報表類型(platform:平台別，monthly:月報表，daily:日報表)
	 * @param page
	 *            分業資料
	 * @param trnsStts
	 *            交易狀態
	 * @return PageQuery
	 */
	@Override
	public PageQuery getTrnsDetialQuantity(Date strtDate, Date endDate, String trnsType, String trnsMnth, String trnsDate, String ecId, Page page, String trnsStts) {
		DetachedCriteria dc = DetachedCriteria.forClass(VwTrnsData.class);
		dc.add(Restrictions.between("trnsDttm", strtDate, endDate));
		if(StringUtils.isNoneBlank(trnsStts)){
			dc.add(Restrictions.eq("trnsStts", trnsStts));
		}
		

		if (!StringUtil.isBlank(ecId)) {
			dc.add(Restrictions.eq("id.ecId", ecId));
		}

		if (!StringUtil.isBlank(trnsType)) {
			dc.add(Restrictions.eq("trnsType", trnsType));
		}

		if (!StringUtil.isBlank(trnsMnth)) {
			dc.add(Restrictions.eq("trnsMnth", trnsMnth));
		}

		if (!StringUtil.isBlank(trnsDate)) {
			dc.add(Restrictions.eq("trnsDate", trnsDate));
		}
		
		//v1.01, 增加限閱戶權限控管
//		LoginUser user = (LoginUser) APLogin.getCurrentUser();
//		dc.add(Restrictions.not(Restrictions.in("custId", user.getViewLimitIds())));
		
		dc.addOrder(Order.asc("id.ecId"));
		dc.addOrder(Order.asc("trnsDttm"));

		return this.query(dc, page);
	}
	
	/**
	 * 查詢明細資料
	 * 
	 * @param strtDate
	 *            查詢起始日期
	 * @param endDate
	 *            查詢結束日期
	 * @param trnsType
	 *            交易類型(A:扣款,B:退款)
	 * @param trnsTime
	 *            交易時間
	 * @param ecId
	 *            平台代號
	 * @param trnsStts
	 *            交易狀態
	 * @param rptType
	 *            報表類型(platform:平台別，monthly:月報表，daily:日報表)
	 * @param page
	 *            分業資料
	 * param trnsStts
	 *            交易狀態
	 * @return PageQuery
	 */
	@Override
	public List<VwTrnsData> getTrnsDetialQuantity(Date strtDate, Date endDate, String trnsType, String trnsMnth, String trnsDate, String ecId, String trnsStts) {
		DetachedCriteria dc = DetachedCriteria.forClass(VwTrnsData.class);
		dc.add(Restrictions.between("trnsDttm", strtDate, endDate));
		if(StringUtils.isNoneBlank(trnsStts)){
			dc.add(Restrictions.eq("trnsStts", trnsStts));
		}

		if (!StringUtil.isBlank(ecId)) {
			dc.add(Restrictions.eq("id.ecId", ecId));
		}

		if (!StringUtil.isBlank(trnsType)) {
			dc.add(Restrictions.eq("trnsType", trnsType));
		}

		if (!StringUtil.isBlank(trnsMnth)) {
			dc.add(Restrictions.eq("trnsMnth", trnsMnth));
		}

		if (!StringUtil.isBlank(trnsDate)) {
			dc.add(Restrictions.eq("trnsDate", trnsDate));
		}
		
		dc.addOrder(Order.asc("id.ecId"));
		dc.addOrder(Order.asc("trnsDttm"));

		return this.query(dc);
	}
}
