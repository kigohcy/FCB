/**
 * @(#)CustDataDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustDataDAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/02/17, Evan
 *   1) First release
 *  v1.01, 2016/06/08, Yann
 *   1) 會員服務統計
 *  v1.02, 2016/07/25, Yann
 *   1) 增加限閱戶權限控管
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustDataDAO;
import com.hitrust.bank.model.CustData;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.framework.dao.impl.BaseDAOImpl;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class CustDataDAOImpl extends BaseDAOImpl implements CustDataDAO {
	
	/**
	 * 查詢最新的會員累算資料 (會員服務統計-總表)
	 * @return List
	 */
	@Override
	public List countCustData() {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.STTS, count(a.STTS) as CNT");
		sb.append("  from CUST_DATA a");
		sb.append(" group by a.STTS");
		sb.append(" order by a.STTS");
		
		return this.queryNativeSql(sb.toString());
	}
	
	/**
	 * 會員服務統計-總表-明細查詢
	 * @param stts 會員狀態
	 * @return
	 */
	@Override
	public PageQuery getCustDataDetl(String stts, Page page) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CustData.class);
		if(!StringUtil.isBlank(stts)){
			criteria.add(Restrictions.eq("stts", stts));
		}
		
		//v1.02, 增加限閱戶權限控管
//		LoginUser user = (LoginUser) APLogin.getCurrentUser();
//		criteria.add(Restrictions.not(Restrictions.in("custId", user.getViewLimitIds())));
		
		criteria.addOrder(Order.asc("stts"));
		criteria.addOrder(Order.desc("sttsDttm")); //TSBACL-91
		
		return query(criteria, page);
	}
	
	/**
	 * 會員服務統計-總表-明細查詢
	 * @param stts 會員狀態
	 * @return
	 */
	@Override
	public List<CustData> getCustDataDetl(String stts) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CustData.class);
		if(!StringUtil.isBlank(stts)){
			criteria.add(Restrictions.eq("stts", stts));
		}
		
		//v1.02, 增加限閱戶權限控管
//		LoginUser user = (LoginUser) APLogin.getCurrentUser();
//		criteria.add(Restrictions.not(Restrictions.in("custId", user.getViewLimitIds())));
		
		criteria.addOrder(Order.asc("stts"));
		criteria.addOrder(Order.desc("sttsDttm")); //ACL-91
		
		return query(criteria);
	}
	
	/**
	 * get custName by custId
	 * @param custId
	 * @return
	 */
	@Override
	public String getCustNameByCustId(String custId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select CUST_NAME from CUST_DATA ");
		sb.append(" where CUST_ID = '" + custId +"'");
		
		List list = this.queryNativeSql(sb.toString());
		
		if(list.size() > 0){
			Map<Object, Object> map = (Map<Object, Object>) list.get(0);
			String custName  = (String) map.get("CUST_NAME");
			return custName;
			
		}else return "";
	}
	
	/**
	 * get custName by realAcnt
	 * @param realAcnt
	 * @return
	 */
	@Override
	public String getCustNameByRealAcnt(String realAcnt) {
		StringBuffer sb = new StringBuffer();
		sb.append("select CUST_NAME from CUST_DATA ");
		sb.append(" where CUST_ID = ");
		sb.append("(select top 1 CUST_ID from CUST_ACNT where REAL_ACNT='"+realAcnt+"')");
		
		List list = this.queryNativeSql(sb.toString());
		
		if(list.size() > 0){
			Map<Object, Object> map = (Map<Object, Object>) list.get(0);
			String custName  = (String) map.get("CUST_NAME");
			return custName;
			
		}else return "";
	}
}
