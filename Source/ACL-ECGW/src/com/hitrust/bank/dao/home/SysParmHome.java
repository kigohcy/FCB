/**
 * @(#) SysParmHome.java
 *
 * Directions: 系統參數檔DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/30, Eason Hsu
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
import com.hitrust.bank.dao.beans.SysParm;

public class SysParmHome extends BeanHome {

	// Log4j
	private static Logger LOG = Logger.getLogger(SysParmHome.class);

	public SysParmHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 參數名稱 取得系統參數資料
	 * @param parmCode 參數名稱
	 * @return SysParm or null
	 * @throws DBException
	 */
	public SysParm fetchSysParmByKey(String parmCode) throws DBException {
		DBExec exec = null;
		SysParm parm = null;
		
		LOG.info("查詢SYS_PARM"); 
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM SYS_PARM WHERE PARM_CODE = ? ");
		
		LOG.info("[fetchSysParmByKey SQL]:" + sb.toString());
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, parmCode);
			exec.executeQuery();
			
			while (exec.next()) {
				parm = new SysParm();
				fillBean(exec, parm);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchSysParmByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return parm;
	}
	
	public void updateSysParmByKey(String parmCode, String value) throws DBException {
		DBExec exec = null;
		SysParm parm = null;
		
		StringBuffer sb = new StringBuffer();
		LOG.info("更新SYS_PARM"); 
		sb.append("update SYS_PARM set PARM_VALUE = ? WHERE PARM_CODE = ? ");
		
		LOG.info("[updateSysParmByKey SQL]:" + sb.toString());
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, value);
			exec.setString(2, parmCode);
			exec.executeUpdate();
		} catch (SQLException e) {
			LOG.error("[updateSysParmByKey SQLException]: ", e);
			throw new DBException("DB_UPDATE");
			
		} finally {
			exec.close();
		}
	}
	
	public SysParm fetchSysParmByName(String parmCode) throws DBException {
		DBExec exec = null;
		SysParm parm = null;
		
		StringBuffer sb = new StringBuffer();
		LOG.info("查詢SYS_PARM"); 
		sb.append("SELECT * FROM SYS_PARM WHERE PARM_CODE = ? ");
		
		LOG.info("[fetchSysParmByKey SQL]:" + sb.toString());
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, parmCode);
			exec.executeQuery();
			
			while (exec.next()) {
				parm = new SysParm();
				fillBean(exec, parm);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchSysParmByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return parm;
	}
	
	
	private void fillBean(DBExec exec, SysParm parm) throws SQLException {
		parm.PARM_CODE  = exec.getString("PARM_CODE");
		parm.PARM_NAME  = exec.getString("PARM_NAME");
		parm.PARM_VALUE = exec.getString("PARM_VALUE");
	}
	
}
