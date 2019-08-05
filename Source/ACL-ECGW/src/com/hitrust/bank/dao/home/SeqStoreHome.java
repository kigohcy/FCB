/**
 * @(#) SeqStoreHome.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : SeqStoreHome
 * 
 * Modify History:
 *  v1.00, 2016/03/25, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.home;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.BeanHome;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.beans.SeqStore;

public class SeqStoreHome extends BeanHome {
    static Logger LOG = Logger.getLogger(SeqStoreHome.class);
	
	public SeqStoreHome(Connection conn){
		super();
		this.conn = conn;
	}
	
	/**
	 * 依據 Primary Key 取得 序號儲存表 
	 * @param type 序號類別
	 * @return SeqStore or null
	 * @throws DBException
	 */
	public SeqStore fetchSeqStoreByKey(String type) throws DBException {
		DBExec exec = null;
		SeqStore data = null;
		
		LOG.info("查詢SEQ_STORE");
		StringBuffer sql = new StringBuffer();
		sql.append("select * from SEQ_STORE where TYPE = ?");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, type);
			exec.executeQuery();
			
			if (exec.next()) {
				data = new SeqStore();
				this.fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchSeqStoreByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			if(exec!=null) exec.close();
		}
		
		return data;
	}
	
	/**
	 * 更新 SEQ_STORE by type and seq
	 * @param type 序號類別
	 * @param newSeq 新序號
	 * @param oldSeq 原序號
	 * @param refleshDate 序號重編日期(若不需異動則帶空值)
	 * @return boolean true:成功, false:失敗
	 * @throws DBException
	 */
	public boolean updateSeqStore(String type, String newSeq, String oldSeq, 
			String refleshDate) throws DBException {
		DBExec exec = null;
		SeqStore data = null;
		ArrayList parList = new ArrayList();
		
		LOG.info("更新SEQ_STORE");
		StringBuffer sql = new StringBuffer();
		sql.append("update SEQ_STORE set SEQ=? ");
		parList.add(newSeq);
		if(!StringUtil.isBlank(refleshDate)){
			sql.append(",REFLESHDATE=? ");
			parList.add(refleshDate);
		}
		sql.append(" where TYPE=? and SEQ=?");
		parList.add(type);
		parList.add(oldSeq);
		
		LOG.info("updateSeqStore:"+sql.toString());
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			super.setParameter(1, parList, exec);
			int row = exec.executeUpdate();
			
			if (row > 0) {
				return true;
			}
			
		} catch (SQLException e) {
			LOG.error("[updateSeqStore SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			if(exec!=null) exec.close();
		}
		
		return false;
	}
	
	private void fillBean(DBExec exec, SeqStore data) throws SQLException{
		data.TYPE 	     = exec.getString("TYPE");
		data.SEQ         = exec.getString("SEQ");
		data.REFLESHDATE = exec.getString("REFLESHDATE");
	}
}
