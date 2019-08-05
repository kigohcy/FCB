/**
 * @(#) EcMsgLogHome.java
 *
 * Directions: 平台訊息收送記錄 
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/25, Eason Hsu
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
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.beans.EcMsgLog;

public class EcMsgLogHome extends BeanHome {

	// Log4j
	private static Logger LOG = Logger.getLogger(EcMsgLogHome.class);

	public EcMsgLogHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 平台代碼, 平台訊息序號 & 訊息類別 取得平台收送記錄
	 * @param ecId	  平台代碼
	 * @param ecMsgNo 平台訊息序號
	 * @param msgType 訊息類別(optional)
	 * @return EcMsgLog or null
	 * @throws DBException
	 */
	public EcMsgLog fetchEcMsgLogByKey(String ecId, String ecMsgNo, String msgType) throws DBException {
		DBExec exec = null;
		EcMsgLog log = null;
		
		StringBuffer sb = new StringBuffer();
		
		LOG.info("查詢EC_MSG_LOG");
		
		sb.append("SELECT * FROM EC_MSG_LOG WHERE EC_ID = ? AND EC_MSG_NO= ? ");
		
		// 訊息類別是否為空白
		if (!StringUtil.isBlank(msgType)) {
			sb.append("AND MSG_TYPE = ? ");
		}
		
		LOG.info("[fetchEcMsgLogByKey SQL]: " + sb.toString());
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, ecMsgNo);
			
			// 訊息類別是否為空白
			if (!StringUtil.isBlank(msgType)) {
				exec.setString(3, msgType);
			}
			
			exec.executeQuery();
			
			while (exec.next()) {
				log = new EcMsgLog();
				this.fillBean(exec, log);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchEcMsgLogByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return log;
	}
	
	/**
	 * 依據訊息狀態 更新 平台訊息收送記錄
	 * @param ecId	  平台代碼
	 * @param msgNo	  平台訊息序號
	 * @param msgType 訊息類別
	 * @param stts	  訊息狀態 01: 成功, 02: 失敗
	 * @return 回傳異動結果筆數
	 * @throws DBException
	 */
	public int updateEcMsgLogByKey(String ecId, String msgNo, String msgType, String stts) throws DBException {
		DBExec exec = null;
		
		int count = 0;
		StringBuffer sb = new StringBuffer();
		
		LOG.info("更新EC_MSG_LOG");
		sb.append("UPDATE EC_MSG_LOG SET STTS = ? WHERE EC_ID = ? AND EC_MSG_NO = ? AND MSG_TYPE = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, stts);
			exec.setString(2, ecId);
			exec.setString(3, msgNo);
			exec.setString(4, msgType);
			
			count = exec.executeUpdate();
			
		} catch (SQLException e) {
			LOG.error("");
			throw new DBException(e, "DB_UPD");
		} finally {
			exec.close();
		}
		
		return count;
	}
	
	
	private void fillBean(DBExec exec, EcMsgLog log) throws SQLException {
		log.EC_ID 	  = exec.getString("EC_ID");
		log.EC_MSG_NO = exec.getString("EC_MSG_NO");
		log.MSG_TYPE  = exec.getString("MSG_TYPE");
		log.CRET_DTTM = exec.getString("CRET_DTTM");
		log.STTS 	  = exec.getString("STTS");
		log.MSG_CNTN  = exec.getString("MSG_CNTN");
	}
}
