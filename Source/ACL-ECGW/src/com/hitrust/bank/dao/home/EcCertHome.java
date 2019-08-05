/**
 * @(#) EcCertHome.java
 *
 * Directions: 電商平台憑證檔
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/25 Eason Hsu
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
import com.hitrust.bank.dao.beans.EcCert;

public class EcCertHome extends BeanHome {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(EcCertHome.class);

	public EcCertHome(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 依據 平台代碼 & 憑證識別碼 取得平台憑證資訊
	 * @param ecId   平台代碼
	 * @param certCn 憑證識別碼
	 * @return EcCert or null
	 * @throws DBException 
	 */
	public EcCert fetchEcCertByKey(String ecId, String certCn) throws DBException {
		DBExec exec = null;
		EcCert cert = null;
		
		LOG.info("查詢EC_CERT");
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM EC_CERT WHERE EC_ID = ? AND CERT_CN = ?");
		
		LOG.info("[fetchEcCertByKey SQL]: " + sb.toString());
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, certCn);
			exec.executeQuery();
			
			while (exec.next()) {
				cert = new EcCert();
				fillBean(exec, cert);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchEcCertByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return cert;
	}
	
	/**
	 * 依據 平台代碼 & 憑證序號 取得平台憑證資訊
	 * @param ecId	 平台代碼
	 * @param certSn 憑證序號
	 * @return EcCert or null
	 * @throws DBException
	 */
	public EcCert fetchEcCertByCertSn(String ecId, String certSn) throws DBException {
		DBExec exec = null;
		EcCert cert = null;
		
		LOG.info("查詢EC_CERT");
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM EC_CERT WHERE EC_ID = ? AND CERT_SN = ? ");
		
		LOG.info("[fetchEcCertByKey SQL]: " + sb.toString());
		LOG.info("[EC_ID]: " + ecId);
		LOG.info("[certSn]: " + certSn);
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, certSn);
			exec.executeQuery();
			
			while (exec.next()) {
				cert = new EcCert();
				fillBean(exec, cert);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchEcCertByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return cert;
	}
	
	private void fillBean(DBExec exec, EcCert cert) throws SQLException {
		cert.EC_ID 		   = exec.getString("EC_ID");
		cert.CERT_CN 	   = exec.getString("CERT_CN");
		cert.CERT_SN 	   = exec.getString("CERT_SN");
		cert.CERT_ID 	   = exec.getInt("CERT_ID");
		cert.STRT_DTTM 	   = exec.getString("STRT_DTTM");
		cert.END_DTTM 	   = exec.getString("END_DTTM");
		cert.RA_ACNT 	   = exec.getString("RA_ACNT");
		cert.CERT_FEE_DATA = exec.getBytes("CERT_FEE_DATA");
	}
	
}
