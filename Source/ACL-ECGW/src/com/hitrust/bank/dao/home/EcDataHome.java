/**
 * @(#) EcDataHome.java
 *
 * Directions: 電商平台資料檔 DAO
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
import com.hitrust.bank.dao.beans.EcData;

public class EcDataHome extends BeanHome {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(EcDataHome.class);

	public EcDataHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 Primary Key 取得平台資料
	 * @param ecId 平台代碼
	 * @return EcData or null
	 * @throws DBException 
	 */
	public EcData fetchEcDataByKey(String ecId) throws DBException {
		DBExec exec = null;
		EcData data = null;
		
		LOG.info("查詢EC_DATA");
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM EC_DATA WHERE EC_ID = ?");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.executeQuery();
			
			while (exec.next()) {
				data = new EcData();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchEcDataByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return data;
	}
	

	private void fillBean(DBExec exec, EcData data) throws SQLException {
		data.EC_ID 		= exec.getString("EC_ID");
		data.EC_NAME_CH = exec.getString("EC_NAME_CH");
		data.EC_NAME_EN = exec.getString("EC_NAME_EN");
		data.SORC_IP    = exec.getString("SORC_IP");
		data.FEE_TYPE   = exec.getString("FEE_TYPE");
		data.FEE_RATE   = exec.getDouble("FEE_RATE");
		data.STTS 	    = exec.getString("STTS");
		data.REAL_ACNT  = exec.getString("REAL_ACNT");
		data.ENTR_NO    = exec.getString("ENTR_NO");
		data.ENTR_ID    = exec.getString("ENTR_ID");
		data.CNTC       = exec.getString("CNTC");
		data.TEL        = exec.getString("TEL");
		data.MAIL 	    = exec.getString("MAIL");
		data.NOTE 	    = exec.getString("NOTE");
		data.CRET_USER  = exec.getString("CRET_USER");
		data.CRET_DTTM  = exec.getString("CRET_DTTM");
		data.MDFY_USER  = exec.getString("MDFY_USER");
		data.MDFY_DTTM  = exec.getString("MDFY_DTTM");
		data.SHOW_SERL  = exec.getInt("SHOW_SERL");
		data.SHOW_REAL_ACNT  = exec.getString("SHOW_REAL_ACNT");
		data.MIN_FEE = exec.getInt("MIN_FEE");
		data.MAX_FEE = exec.getInt("MAX_FEE");
		data.LINK_LIMIT = exec.getInt("LINK_LIMIT");
		//20190619 Add 繳費稅 Begin
		data.TAX_TYPE   = exec.getString("TAX_TYPE");
		data.TAX_RATE   = exec.getDouble("TAX_RATE");
		data.MIN_TAX	= exec.getInt("MIN_TAX");
		data.MAX_TAX	= exec.getInt("MAX_TAX");
		//20190619 Add 繳費稅 End
		
	}
}
