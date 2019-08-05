/**
 * @(#) CustDataHome.java
 *
 * Directions: 會員資料檔 DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/28, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */
package com.hitrust.bank.dao.home;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.BeanHome;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.bank.dao.beans.CustData;

public class CustDataHome extends BeanHome {

	// Log4j
	private static Logger LOG = Logger.getLogger(CustDataHome.class);

	public CustDataHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 Primary Key 取得會員資料
	 * @param custId 身分證字號
	 * @return CustData or null
	 * @throws DBException
	 */
	public CustData fetchCustDataByKey(String custId) throws DBException {
		DBExec exec = null;
		CustData data = null;
		
		LOG.info("查詢CUST_DATA");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM CUST_DATA WHERE CUST_ID = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, custId);
			exec.executeQuery();
			
			while (exec.next()) {
				data = new CustData();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchCustDataByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return data;
	}
	
	private void fillBean(DBExec exec, CustData data) throws SQLException {
		data.CUST_ID   = exec.getString("CUST_ID");
		data.CUST_NAME = exec.getString("CUST_NAME");
		data.CUST_SERL = exec.getString("CUST_SERL");
		data.CUST_TYPE = exec.getString("CUST_TYPE");
		data.TEL 	   = exec.getString("TEL");
		data.MAIL	   = exec.getString("MAIL");
		data.VRSN	   = exec.getString("VRSN");
		data.STTS	   = exec.getString("STTS");
		data.STTS_DTTM = exec.getString("STTS_DTTM");
		data.CRET_DTTM = exec.getString("CRET_DTTM");
		data.MDFY_USER = exec.getString("MDFY_USER");
		data.MDFY_DTTM = exec.getString("MDFY_DTTM");
	}
	
}
