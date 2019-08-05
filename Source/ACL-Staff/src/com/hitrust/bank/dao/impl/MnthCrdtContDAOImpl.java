/**
 * @(#) MnthCrdtContDAOImpl.java
 *
 * Directions:
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/03/23
 *    1) First release
 *
 */

package com.hitrust.bank.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hitrust.bank.dao.MnthCrdtContDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class MnthCrdtContDAOImpl extends BaseDAOImpl implements MnthCrdtContDAO {
	
	/**
	 * 取得 月額度累計金額 by 交易月份+身分證字號+實體帳號+會員服務序號+電商平台代號+電商平台會員代號
	 * @param trnsMnth
	 * @param custId
	 * @param realAcnt
	 * @param custSerl
	 * @param ecId
	 * @param ecUser
	 * @return long
	 * @throws DBException
	 */
	@Override
	public long getMnthSumByAcnt(String trnsMnth, String custId, String realAcnt, String custSerl, String ecId, String ecUser) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(MNTH_CONT) as SUM_MNTH_CONT from MNTH_CRDT_CONT ");
		sql.append(" where TRNS_MNTH='"+trnsMnth+"' and CUST_ID='"+custId+"' and REAL_ACNT='"+realAcnt+"' and CUST_SERL='"+custSerl+"' ");
		if(StringUtils.isNoneBlank(ecId)){
			sql.append(" AND EC_ID='"+ecId+"' ");
		}
		if(StringUtils.isNoneBlank(ecUser)){
			sql.append(" AND EC_USER='"+ecUser+"' ");
		}
		
		List<?> resultList = this.queryNativeSql(sql.toString());
		if(resultList.size() ==0){
			return 0L;
		}else{
			Map<Object, Object> map = (Map<Object, Object>)resultList.get(0);
			if(map.get("SUM_MNTH_CONT") != null){
				BigDecimal sumMnthCont = (BigDecimal)map.get("SUM_MNTH_CONT");
				return sumMnthCont.longValue();
			}else{
				return 0L;
			}
		}
	}

}
