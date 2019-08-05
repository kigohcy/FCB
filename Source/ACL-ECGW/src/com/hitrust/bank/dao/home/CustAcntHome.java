/**
 * @(#) CustAcntHome.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntHome
 * 
 * Modify History:
 *  v1.00, 2016/03/31, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.home;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.BeanHome;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.bank.dao.beans.CustAcnt;

public class CustAcntHome extends BeanHome {
	// Log4j
	private static Logger LOG = Logger.getLogger(CustAcntHome.class);

	public CustAcntHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 Primary Key 取得會員帳號
	 * @param custId 身分證字號
	 * @param realAcnt 實體帳號
	 * @return CustAcnt or null
	 * @throws DBException
	 */
	public CustAcnt fetchCustAcntByKey(String custId, String realAcnt) throws DBException {
		DBExec exec = null;
		CustAcnt data = null;
		
		LOG.info("查詢CUST_ACNT");
		StringBuffer sb = new StringBuffer();
		sb.append("select * from CUST_ACNT where CUST_ID = ? and REAL_ACNT = ?");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, custId);
			exec.setString(2, realAcnt);
			exec.executeQuery();
			
			if (exec.next()) {
				data = new CustAcnt();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchCustAcntByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			exec.close();
		}
		
		return data;
	}
	
	private void fillBean(DBExec exec, CustAcnt data) throws SQLException {
		data.CUST_ID   = exec.getString("CUST_ID");
		data.REAL_ACNT = exec.getString("REAL_ACNT");
		data.TRNS_LIMT = exec.getLong("TRNS_LIMT");
		data.DAY_LIMT  = exec.getLong("DAY_LIMT");
		data.MNTH_LIMT = exec.getLong("MNTH_LIMT");
		data.CRET_DTTM = exec.getString("CRET_DTTM");
		data.MDFY_USER = exec.getString("MDFY_USER");
		data.MDFY_DTTM = exec.getString("MDFY_DTTM");
	}
	
}
