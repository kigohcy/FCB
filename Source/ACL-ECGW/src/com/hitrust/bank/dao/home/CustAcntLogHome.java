/**
 * @(#) CustAcntLogHome.java
 *
 * Directions: 會員帳號連結記錄檔 DAO
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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.BeanHome;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.bank.dao.beans.CustAcntLog;

public class CustAcntLogHome extends BeanHome {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(CustAcntLogHome.class);

	public CustAcntLogHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 平台代碼 & 查詢訊息序號 取得會員帳號連結記錄
	 * @param ecId	  平台代碼
	 * @param ecMsgNo 查詢訊息序號
	 * @return List<CustAcntLog>
	 * @throws DBException 
	 */
	public List<CustAcntLog> fetchCustAcntLog(String ecId, String ecMsgNo) throws DBException {
		DBExec exec = null;
		List<CustAcntLog> acntLogs = new ArrayList<CustAcntLog>();
		
		LOG.info("查詢CUST_ACNT_LOG");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM CUST_ACNT_LOG WHERE EC_ID = ? AND EC_MSG_NO = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, ecMsgNo);
			exec.executeQuery();
			
			while (exec.next()) {
				CustAcntLog acntLog = new CustAcntLog();
				fillBean(exec, acntLog);
				acntLogs.add(acntLog);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchCustAcntLog SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return acntLogs;
	}
	
	/**
	 * 依據 平台代碼, 使用者身分證字號 & 帳號識別碼 取得會員帳號連結記錄
	 * @param ecId	   平台代碼
	 * @param custId   使用者身分證字號
	 * @param indtAcnt 帳號識別碼
	 * @return List<CustAcntLog>
	 * @throws DBException
	 */
	public List<CustAcntLog> fetchCustAcntLog(String ecId, String custId, String indtAcnt) throws DBException {
		DBExec exec = null;
		List<CustAcntLog> acntLogs = new ArrayList<CustAcntLog>();
		LOG.info("查詢CUST_ACNT_LOG");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM CUST_ACNT_LOG WHERE EC_ID = ? AND CUST_ID = ? AND ACNT_INDT = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, custId);
			exec.setString(3, indtAcnt);
			exec.executeQuery();
			
			while (exec.next()) {
				CustAcntLog acntLog = new CustAcntLog();
				fillBean(exec, acntLog);
				acntLogs.add(acntLog);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchCustAcntLog SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return acntLogs;
	}
	
	
	private void fillBean(DBExec exec, CustAcntLog acntLog) throws SQLException {
		acntLog.LOG_NO    = exec.getString("LOG_NO");
		acntLog.CUST_ID   = exec.getString("CUST_ID");
		acntLog.EC_ID     = exec.getString("EC_ID");
		acntLog.EC_USER   = exec.getString("EC_USER");
		acntLog.REAL_ACNT = exec.getString("REAL_ACNT");
		acntLog.CRET_DTTM = exec.getString("CRET_DTTM");
		acntLog.GRAD      = exec.getString("GRAD");
		acntLog.GRAD_TYPE = exec.getString("GRAD_TYPE");
		acntLog.STTS      = exec.getString("STTS");
		acntLog.ERR_CODE  = exec.getString("ERR_CODE");
		acntLog.HOST_CODE = exec.getString("HOST_CODE");
		acntLog.CUST_SERL = exec.getString("CUST_SERL");
		acntLog.ACNT_INDT = exec.getString("ACNT_INDT");
		acntLog.EXEC_SRC  = exec.getString("EXEC_SRC");
		acntLog.EXEC_USER = exec.getString("EXEC_USER");
		acntLog.EC_MSG_NO = exec.getString("EC_MSG_NO");
	}
}
