/**
 * @(#)DayCustContDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayCustContDAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/06/08, Yann
 *   1) First release, 二階
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.List;

import com.hitrust.bank.dao.DayCustContDAO;
import com.hitrust.bank.model.DayCustCont;
import com.hitrust.framework.criterion.HqlDetachedCriteria;
import com.hitrust.framework.criterion.HqlRestrictions;
import com.hitrust.framework.criterion.OrderBy;
import com.hitrust.framework.criterion.ReturnEntry;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class DayCustContDAOImpl extends BaseDAOImpl implements DayCustContDAO {

	/**
	 * 查詢會員日終累計 (會員服務統計-日報)
	 * @param starDate 查詢起日
	 * @param endDate 查詢迄日
	 * @return List
	 */
	@Override
	public List<DayCustCont> getDayCustContList(String starDate, String endDate) {
		
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[]{"DayCustCont a"});
		hqlDc.setReturnEntry(ReturnEntry.forName("new DayCustCont(a)"));
		hqlDc.add(HqlRestrictions.ge("a.id.setlDate", starDate));
		hqlDc.add(HqlRestrictions.le("a.id.setlDate", endDate));
		
		hqlDc.addOrder(OrderBy.desc("a.id.setlDate")); //TSBACL-91
		hqlDc.addOrder(OrderBy.asc("a.id.stts"));
		
		return query(hqlDc);
	}
	
	/**
	 * 查詢會員日終累計 (會員服務統計-月報), 每個月取最後的一筆
	 * @param date 查詢日期
	 * @return DayCustCont
	 */
	@Override
	public List<DayCustCont> getLastDayCustCont(String date) {
		
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[]{"DayCustCont a"});
		hqlDc.setReturnEntry(ReturnEntry.forName("new DayCustCont(a)"));
		hqlDc.add(HqlRestrictions.eq("a.id.setlDate", date));
		
		hqlDc.addOrder(OrderBy.asc("a.id.setlDate"));
		hqlDc.addOrder(OrderBy.asc("a.id.stts"));
		
		return query(hqlDc);
	}
}
