/**
 * @(#)CustAcntLinkDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntLinkDAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/02/17, Evan
 *   1) First release
 *  v1.01, 2016/06/07, Yann
 *   1) 約定帳號統計
 *  v1.02, 2016/07/25, Yann
 *   1) 增加限閱戶權限控管
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustAcntLinkDAO;
import com.hitrust.bank.model.CustAcntLink;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.framework.criterion.HqlDetachedCriteria;
import com.hitrust.framework.criterion.HqlRestrictions;
import com.hitrust.framework.criterion.OrderBy;
import com.hitrust.framework.criterion.ReturnEntry;
import com.hitrust.framework.dao.impl.BaseDAOImpl;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class CustAcntLinkDAOImpl extends BaseDAOImpl implements CustAcntLinkDAO {

	/**
	 * 取得會員綁定的各平台之帳號資料
	 * @param custId	身分證號
	 * @param ecId		平台代碼
	 * @return	List<CustAcntLink> 會員帳號連結檔 
	 */
	@Override
	public List<CustAcntLink> getCustAcntLink(String custId, String ecId) {
		
		DetachedCriteria dCriteria = DetachedCriteria.forClass(CustAcntLink.class);
		dCriteria.add(Restrictions.eq("id.custId", custId));
		dCriteria.add(Restrictions.eq("id.ecId", ecId));
		
		return query(dCriteria);
	}
	
	/**
	 * 查詢最新的約定帳號累算資料 (約定帳號統計-總表)
	 * @param ecId 平台代碼
	 * @return List
	 */
	@Override
	public List countCustAcntLink(String ecId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.EC_ID, b.EC_NAME_CH, a.STTS, count(a.STTS) as CNT");
		sb.append("  from CUST_ACNT_LINK a, EC_DATA b");
		sb.append(" where a.EC_ID = b.EC_ID");
		
		if (!StringUtil.isBlank(ecId)) {
			sb.append(" and a.EC_ID = " + "'" + ecId + "'");
		}
		
		sb.append(" group by a.EC_ID, b.EC_NAME_CH, a.STTS");
		sb.append(" order by a.EC_ID, a.STTS");
		
		return this.queryNativeSql(sb.toString());
	}
	
	/**
	 * 總表約定帳號統計明細查詢
	 * @param ecId 平台代碼
	 * @param stts 連結狀態
	 * @return List<CustAcntLink>
	 */
	@Override
	public PageQuery getCustAcntLinkDetl(String ecId, String stts, Page page) {
		
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[]{"CustAcntLink a","EcData b"});
		hqlDc.add(HqlRestrictions.eq("a.id.ecId","{b.ecId}"));
		hqlDc.setReturnEntry(ReturnEntry.forName("new CustAcntLink(a, b.ecNameCh)"));
		
		if(!StringUtil.isBlank(ecId)){
			hqlDc.add(HqlRestrictions.eq("a.id.ecId", ecId));
		}
		if(!StringUtil.isBlank(stts)){
			hqlDc.add(HqlRestrictions.eq("a.stts", stts));
		}
		
		hqlDc.addOrder(OrderBy.asc("a.id.ecId"));
		hqlDc.addOrder(OrderBy.asc("a.id.custId"));
		hqlDc.addOrder(OrderBy.asc("a.id.ecUser"));
		hqlDc.addOrder(OrderBy.asc("a.sttsDttm"));
		
		return query(hqlDc, page);
	}
	
	/**
	 * 總表約定帳號統計明細查詢
	 * @param ecId 平台代碼
	 * @param stts 連結狀態
	 * @return List<CustAcntLink>
	 */
	@Override
	public List<CustAcntLink> getCustAcntLinkDetl(String ecId, String stts) {
		
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[]{"CustAcntLink a","EcData b"});
		hqlDc.add(HqlRestrictions.eq("a.id.ecId","{b.ecId}"));
		hqlDc.setReturnEntry(ReturnEntry.forName("new CustAcntLink(a, b.ecNameCh)"));
		
		if(!StringUtil.isBlank(ecId)){
			hqlDc.add(HqlRestrictions.eq("a.id.ecId", ecId));
		}
		if(!StringUtil.isBlank(stts)){
			hqlDc.add(HqlRestrictions.eq("a.stts", stts));
		}

		hqlDc.addOrder(OrderBy.asc("a.id.ecId"));
		hqlDc.addOrder(OrderBy.asc("a.id.custId"));
		hqlDc.addOrder(OrderBy.asc("a.id.ecUser"));
		hqlDc.addOrder(OrderBy.asc("a.sttsDttm"));
		
		return query(hqlDc);
	}
}
