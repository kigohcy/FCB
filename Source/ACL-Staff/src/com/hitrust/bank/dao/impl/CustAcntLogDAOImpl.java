/**
 * @(#)CustAcntLogDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員連結設定記錄CustAcntLogDAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/02/15, Evan
 *   1) First release
 *  v1.01, 2016/07/25, Yann
 *   1) 增加限閱戶權限控管
 *  v1.02, 2018/03/20
 *   1) 新增電商平台會員代號查詢條件
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustAcntLogDAO;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.framework.criterion.HqlDetachedCriteria;
import com.hitrust.framework.criterion.HqlRestrictions;
import com.hitrust.framework.criterion.OrderBy;
import com.hitrust.framework.criterion.ReturnEntry;
import com.hitrust.framework.dao.impl.BaseDAOImpl;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class CustAcntLogDAOImpl extends BaseDAOImpl implements CustAcntLogDAO {
	
	private static Logger LOG = Logger.getLogger(CustAcntLogDAOImpl.class);
	
	public CustAcntLogDAOImpl() {
	}

	/**
	 * 查詢會員結設定記錄
	 * 
	 * @param startDate
	 * @param endDate
	 * @param custId
	 * @param realAcnt
	 * @param ecUser 電商平台會員代號
	 * @param ecId
	 * @param queryStts
	 * @return
	 */
	@Override
	public PageQuery getCustAcntLog(Date strtDate, Date endDate, String custId, String realAcnt, String ecUser,  String ecId, 
			String queryStts, String execSrc, Page page) {
		
		//不join CustData, 因為CustData可能無資料
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[] {"CustAcntLog a", "EcData b"});
		
		hqlDc.add(HqlRestrictions.eq("a.ecId", "{b.ecId}"));
		//hqlDc.add(HqlRestrictions.eq("a.custId", "{c.custId}"));
		hqlDc.setReturnEntry(ReturnEntry.forName("new CustAcntLog(a, b.ecNameCh, b.ecNameEn, '')"));
		
		if (!StringUtil.isBlank(strtDate)) {
			hqlDc.add(HqlRestrictions.ge("a.cretDttm", strtDate));
		}
		if (!StringUtil.isBlank(endDate)) {
			hqlDc.add(HqlRestrictions.le("a.cretDttm", endDate));
		}
		if (!StringUtil.isBlank(custId)) {
			hqlDc.add(HqlRestrictions.eq("a.custId", custId));
		}
		if (!StringUtil.isBlank(realAcnt)) {
			hqlDc.add(HqlRestrictions.eq("a.realAcnt", realAcnt));
		}
		//V1.02 新增電商平台會員代號查詢條件
		if(!StringUtil.isBlank(ecUser)){
			hqlDc.add(HqlRestrictions.eq("a.ecUser", ecUser));
		}
		//V1.02 新增電商平台會員代號查詢條件 End		
		if (!StringUtil.isBlank(ecId)) {
			hqlDc.add(HqlRestrictions.eq("a.ecId", ecId));
		}
		if (!StringUtil.isBlank(queryStts)) {
			hqlDc.add(HqlRestrictions.in("a.stts", queryStts.split(",")));
		}
		if (!StringUtil.isBlank(execSrc)) {
			hqlDc.add(HqlRestrictions.eq("a.execSrc", execSrc));
		}
		
		//v1.01, 增加限閱戶權限控管
//		LoginUser user = (LoginUser) APLogin.getCurrentUser();
//
//		List<String> viewLimitIds = user.getViewLimitIds();
//		for(int i=0; viewLimitIds!=null && i<viewLimitIds.size(); i++){
//			hqlDc.add(HqlRestrictions.ne("a.custId", viewLimitIds.get(i)));
//		}
		
		
		hqlDc.addOrder(OrderBy.desc("a.cretDttm")); //TSBACL-66
		hqlDc.addOrder(OrderBy.asc("a.custId"));
		hqlDc.addOrder(OrderBy.asc("a.ecId"));
		hqlDc.addOrder(OrderBy.asc("a.ecUser"));

		return query(hqlDc, page);
	}
}
