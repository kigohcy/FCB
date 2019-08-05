/**
 * @(#) SessionTempTableHome.java
 *
 * Directions: 帳號連結綁定 Temp Table DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/29, Eason Hsu
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
import com.hitrust.bank.dao.beans.SessionTempTable;

public class SessionTempTableHome extends BeanHome {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(SessionTempTableHome.class);

	public SessionTempTableHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 sessionKey 取得 session Data
	 * @param sessionKey
	 * @return SessionTempTable or null
	 * @throws DBException
	 */
	public SessionTempTable fetchSessionDataByKey(String sessionKey) throws DBException {
		DBExec exec = null;
		SessionTempTable tempTable = null;
		LOG.info("查詢SESSION_TEMP_TABLE"); 
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM SESSION_TEMP_TABLE WHERE SESSION_KEY = ?");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, sessionKey);
			exec.executeQuery();	
			
			while (exec.next()) {
				tempTable = new SessionTempTable();
				fillBean(exec, tempTable);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchSessionDataByKey SQLException]: ", e);
			throw new DBException("DB_QRY");
		} finally {
			exec.close();
		}
		
		return tempTable;
	}
	
	/**
	 * 依據 sessionKey 刪除 Temp Table data
	 * @param sessionKey
	 * @return 執行結果
	 * @throws DBException 
	 */
	public int deleteSessionTempTableByKey(String sessionKey) throws DBException {
		DBExec exec = null;
		int result = 0;
		
		LOG.info("刪除SESSION_TEMP_TABLE"); 
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM SESSION_TEMP_TABLE WHERE SESSION_KEY = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, sessionKey);
			result = exec.executeUpdate();
			
		} catch (SQLException e) {
			LOG.info("========== SessionTempTable 資料刪除失敗 sessionKey: " + sessionKey + " ==========");
			throw new DBException("DB_DEL");
			
		} finally {
			
			exec.close();
		}
		
		return result; 
	}
	
	
	private void fillBean(DBExec exec, SessionTempTable tempTable) throws SQLException {
		tempTable.SESSION_KEY = exec.getString("SESSION_KEY");
		tempTable.SESSION_DATA = exec.getString("SESSION_DATA");
		
	}

}
