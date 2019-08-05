/**
 * @(#)DayAcntContDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayAcntContDAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/06/07, Yann
 *   1) First release, 二階
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.List;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.DayAcntContDAO;
import com.hitrust.bank.model.DayAcntCont;
import com.hitrust.framework.criterion.HqlDetachedCriteria;
import com.hitrust.framework.criterion.HqlRestrictions;
import com.hitrust.framework.criterion.OrderBy;
import com.hitrust.framework.criterion.ReturnEntry;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class DayAcntContDAOImpl extends BaseDAOImpl implements DayAcntContDAO {

	/**
	 * 查詢帳號日終累計 (約定帳號統計-日報)
	 * @param ecId 平台代碼
	 * @param starDate 查詢起日
	 * @param endDate 查詢迄日
	 * @return List
	 */
	@Override
	public List<DayAcntCont> getDayAcntContList(String ecId, String starDate, String endDate) {
		
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[]{"DayAcntCont a","EcData b"});
		hqlDc.add(HqlRestrictions.eq("a.id.ecId", "{b.ecId}"));
		hqlDc.setReturnEntry(ReturnEntry.forName("new DayAcntCont(a, b.ecNameCh)"));
		hqlDc.add(HqlRestrictions.ge("a.id.setlDate", starDate));
		hqlDc.add(HqlRestrictions.le("a.id.setlDate", endDate));
		
		if(!StringUtil.isBlank(ecId)){
			hqlDc.add(HqlRestrictions.eq("a.id.ecId", ecId));
		}
		
		hqlDc.addOrder(OrderBy.asc("a.id.setlDate"));
		hqlDc.addOrder(OrderBy.asc("a.id.ecId"));
		hqlDc.addOrder(OrderBy.asc("a.id.stts"));
		
		return query(hqlDc);
	}
	
	/**
	 * 查詢帳號日終累計 (約定帳號統計-月報), 每個月取最後的一筆
	 * @param ecId 平台代碼
	 * @param date 查詢日期
	 * @return DayAcntCont
	 */
	@Override
	public List<DayAcntCont> getLastDayAcntCont(String ecId, String date) {
		
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[]{"DayAcntCont a","EcData b"});
		hqlDc.add(HqlRestrictions.eq("a.id.ecId", "{b.ecId}"));
		hqlDc.setReturnEntry(ReturnEntry.forName("new DayAcntCont(a, b.ecNameCh)"));
		hqlDc.add(HqlRestrictions.eq("a.id.setlDate", date));
		
		if(!StringUtil.isBlank(ecId)){
			hqlDc.add(HqlRestrictions.eq("a.id.ecId", ecId));
		}
		
		hqlDc.addOrder(OrderBy.asc("a.id.setlDate"));
		hqlDc.addOrder(OrderBy.asc("a.id.ecId"));
		hqlDc.addOrder(OrderBy.asc("a.id.stts"));
		
		return query(hqlDc);
	}
}
