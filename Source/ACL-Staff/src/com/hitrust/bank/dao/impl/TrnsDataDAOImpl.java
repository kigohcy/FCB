/**
 * @(#) TrnsDataDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/02/19, Jimmy
 * 	 1) First release
 *  v1.01, 2016/07/25, Yann
 *   1) 增加限閱戶權限控管
 *  v1.01, 2018/03/19
 *   1) 新增電商平台會員代號查詢條件
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.TrnsDataDAO;
import com.hitrust.bank.model.TrnsData;
import com.hitrust.framework.dao.impl.BaseDAOImpl;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class TrnsDataDAOImpl extends BaseDAOImpl implements TrnsDataDAO {
	
	/**
	 * 取得交易結果清單
	 * 
	 * @param strtDate	查詢起始日期
	 * @param endDate	查詢結束日期
	 * @param custId	身分證字號
	 * @param realAcnt	銀行扣款帳號
	 * @param ecUser	電商平台會員代號
	 * @param ecId		平台代號
	 * @param trnsType	交易類別
	 * @param trnsStts	交易狀態
	 * @param page		分頁資訊
	 * @return PageQuery
	 */
	@Override
	public PageQuery getTrnsDataList(Date strtDate, Date endDate, String custId, String realAcnt, String ecUser, String ecId, String trnsType, String trnsStts, Page page) {
		DetachedCriteria dc = DetachedCriteria.forClass(TrnsData.class);

		dc.add(Restrictions.between("trnsDttm", strtDate, endDate));

		if (!StringUtil.isBlank(custId)) {
			dc.add(Restrictions.eq("custId", custId));
		}
		
		if (!StringUtil.isBlank(ecId)) {
			dc.add(Restrictions.eq("id.ecId", ecId));
		}
		
		if (!StringUtil.isBlank(realAcnt)) {
			dc.add(Restrictions.eq("realAcnt", realAcnt));
		}
		
		//v1.01 新增電商平台會員代號查詢條件
		if(!StringUtil.isBlank(ecUser)){
			dc.add(Restrictions.eq("ecUser", ecUser));
		}
		//v1.01 新增電商平台會員代號查詢條件 End
		
		if (!StringUtil.isBlank(trnsType)) {
			dc.add(Restrictions.eq("trnsType", trnsType));
		}
		if(StringUtils.isNoneBlank(trnsStts)){
			dc.add(Restrictions.eq("trnsStts", trnsStts));
		}
		
		//v1.01, 增加限閱戶權限控管
//		LoginUser user = (LoginUser) APLogin.getCurrentUser();
//		dc.add(Restrictions.not(Restrictions.in("custId", user.getViewLimitIds())));
		
		dc.addOrder(Order.desc("trnsDttm"));

		return this.query(dc, page);
	}

}
