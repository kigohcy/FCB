package com.hitrust.bank.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.hitrust.bank.dao.DayCrdtContDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class DayCrdtContDAOImpl extends BaseDAOImpl implements DayCrdtContDAO {
	// Log4j
	private static Logger LOG = Logger.getLogger(DayCrdtContDAOImpl.class);
	
	/**
	 * 取得 日額度累計金額 by 交易月份+身分證字號+實體帳號+會員服務序號+電商平台代號+電商平台會員代號
	 * @param trnsDay
	 * @param custId
	 * @param realAcnt
	 * @param custSerl
	 * @param ecId
	 * @param ecUser
	 * @return long
	 */
	@Override
	public long getDaySumByCustIdAndAcnt(String trnsDay, String custId, String realAcnt, String custSerl, String ecId, String ecUser) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(DAY_CONT) as SUM_DAY_CONT from DAY_CRDT_CONT ");
		sql.append(" where TRNS_DATE='"+trnsDay+"' AND CUST_ID = '"+custId+"' AND REAL_ACNT = '"+realAcnt+"' AND CUST_SERL='"+custSerl+"' ");
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
			if(map.get("SUM_DAY_CONT") != null){
				BigDecimal sumDayCont = (BigDecimal)map.get("SUM_DAY_CONT");
				return sumDayCont.longValue();
			}else{
				return 0L;
			}
		}
	}
	
}
