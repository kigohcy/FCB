/**
 * @(#) CustPltfHome.java
 *
 * Directions: 會員平台資料檔DAO
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
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.dao.beans.CustPltf;

public class CustPltfHome extends BeanHome {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(CustPltfHome.class);

	public CustPltfHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 平台代碼 & 身分證字號 取得會員平台資料
	 * @param ecId   平台代碼
	 * @param custId 身分證字號
	 * @return CustPltf or null
	 * @throws DBException
	 */
	public CustPltf fetchCustPltfByKey(String ecId, String custId) throws DBException {
		DBExec exec = null;
		CustPltf pltf = null;
		
		LOG.info("查詢CUST_PLTF");
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM CUST_PLTF WHERE EC_ID = ? AND CUST_ID = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, custId);
			exec.executeQuery();
			
			while (exec.next()) {
				pltf = new CustPltf();
				fillBean(exec, pltf);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchCustPltfByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return pltf;
	}
	
	/**
	 * 依據 平台代碼 & 身分證字號 更新會員平台資料狀態
	 * @param ecId	 平台代碼
	 * @param custId 身分證字號
	 * @param stts	 狀態 00: 啟用, 01: 暫停, 02: 終止
	 * @return 更新筆數
	 * @throws DBException
	 * @throws UtilException
	 */
	public int updateCustPltfSttsByKey(String ecId, String custId, String stts) throws DBException, UtilException {
		DBExec exec = null;
		int count = 0;
		
		LOG.info("更新CUST_PLTF");
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE dbo.CUST_PLTF SET STTS = ?, STTS_DTTM = ? WHERE EC_ID = ? AND CUST_ID = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, stts);
			exec.setString(2, DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD")));
			exec.setString(3, ecId);
			exec.setString(4, custId);
			count = exec.executeUpdate();
			
		} catch (SQLException e) {
			LOG.error("[updateCustPltfSttsByKey SQLException]", e);
			throw new DBException("");
			
		} finally {
			exec.close();
		}
		
		return count;
	}
	
	private void fillBean(DBExec exec, CustPltf pltf) throws SQLException {
		pltf.CUST_ID   = exec.getString("CUST_ID");
		pltf.EC_ID 	   = exec.getString("EC_ID");
		pltf.STTS 	   = exec.getString("STTS");
		pltf.STTS_DTTM = exec.getString("STTS_DTTM");
		pltf.CRET_DTTM = exec.getString("CRET_DTTM");
		pltf.MDFY_USER = exec.getString("MDFY_USER");
		
	}
	
}
