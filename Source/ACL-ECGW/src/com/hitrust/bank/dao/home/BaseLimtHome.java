/**
 * @(#) BaseLimtHome.java
 *
 * Directions: 法定限額資料檔 DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/14, Eason Hsu
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
import com.hitrust.bank.dao.beans.BaseLimt;

public class BaseLimtHome extends BeanHome {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(BaseLimtHome.class);
	
	public BaseLimtHome(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 依據 身分認證等級 取得法定限額
	 * @param grad 身分認證等級 (A: 網銀認證, B: 簡訊OTP, C: 晶片卡)
	 * @return BaseLimt or null
	 * @throws DBException 
	 */
	public BaseLimt fetchBaseLimtByKey(String grad) throws DBException {
		DBExec exec = null;
		BaseLimt limt = null;
		
		LOG.info("查詢BASE_LIMT");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM BASE_LIMT WHERE GRAD = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, grad);
			exec.executeQuery(); 
			
			while (exec.next()) {
				limt = new BaseLimt();
				fillBean(exec, limt);
			}
			
		} catch (SQLException e) {
			LOG.error("", e);
			throw new DBException("DB_QUERY");
		} finally {
			exec.close();
		}
		
		return limt;
	}
	
	private void fillBean(DBExec exec, BaseLimt limt) throws SQLException {
		limt.GRAD      = exec.getString("GRAD");
		limt.TRNS_LIMT = exec.getLong("TRNS_LIMT");
		limt.DAY_LIMT  = exec.getLong("DAY_LIMT");
		limt.MNTH_LIMT = exec.getLong("MNTH_LIMT");
	}

}
