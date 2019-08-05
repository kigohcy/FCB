/**
 * @(#) CustAcntLinkHome.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntLinkHome
 * 
 * Modify History:
 *  v1.00, 2016/03/28, Yann
 *   1) First release*   
 *  V2.00, 2018/02/05
 *   1) 新增可設定電商最大可綁定人數(先借用 EC_DATA.NOTE 欄位)，超過綁定人數，則回覆1999-超過電商最大可綁定人數
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
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.dao.beans.CustAcntLink;
import com.hitrust.framework.util.StringUtil;

public class CustAcntLinkHome extends BeanHome {
	//log4j
    static Logger LOG = Logger.getLogger(CustAcntLinkHome.class);
	
	public CustAcntLinkHome(Connection conn){
		super();
		this.conn = conn;
	}
	
	/**
	 * 依Primary Key 取得 會員帳號連結檔
	 * @param custId 身分證字號
	 * @param ecId 平台代碼 
	 * @param ecUser 平台會員代碼
	 * @param realAcnt 實體帳號
	 * @return CustAcntLink
	 * @throws DBException
	 */
	public CustAcntLink getCustAcntLinkByPk(String custId, String ecId, 
			String ecUser, String realAcnt) throws DBException {
		DBExec exec = null;
		CustAcntLink data = null;
		LOG.info("查詢CUST_ACNT_LINK");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from CUST_ACNT_LINK where CUST_ID = ? and EC_ID = ? ");
		sql.append(" and EC_USER = ? and REAL_ACNT = ? ");
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, custId);
			exec.setString(2, ecId);
			exec.setString(3, ecUser);
			exec.setString(4, realAcnt);
			exec.executeQuery();
			if (exec.next()) {
				data = new CustAcntLink();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[getCustAcntLinkByPk SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			exec.close();
		}
		
		return data;
	}
	
	/**
	 * 依 帳號識別碼 取得 會員帳號連結檔
	 * @param custId 身分證字號
	 * @param ecId 平台代碼 
	 * @param ecUser 平台會員代碼
	 * @param acntIdnt 帳號識別碼 
	 * @return CustAcntLink
	 * @throws DBException
	 */
	public CustAcntLink getCustAcntLinkByAcntIdnt(String custId, String ecId, 
			String ecUser, String acntIdnt) throws DBException {
		DBExec exec = null;
		CustAcntLink data = null;
		
		LOG.info("查詢CUST_ACNT_LINK");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from CUST_ACNT_LINK where CUST_ID = ? and EC_ID = ? ");
		sql.append(" and EC_USER = ? and ACNT_INDT = ? ");
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, custId);
			exec.setString(2, ecId);
			exec.setString(3, ecUser);
			exec.setString(4, acntIdnt);
			exec.executeQuery();
			if (exec.next()) {
				data = new CustAcntLink();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[getCustAcntLinkByAcntIdnt SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			exec.close();
		}
		
		return data;
	}
	
	/**
	 * 依據 平台代碼 & 身分證字號 取得會員連結帳號
	 * @param ecId	 平台代碼
	 * @param custId 身分證字號
	 * @return
	 * @throws DBException
	 */
	public List<CustAcntLink> fetchCustAcntLink(String ecId, String custId) throws DBException {
		DBExec exec = null;
		List<CustAcntLink> acntLinks = new ArrayList<CustAcntLink>();
		
		LOG.info("查詢CUST_ACNT_LINK");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM dbo.CUST_ACNT_LINK WHERE CUST_ID = ? and EC_ID = ?");
		
		LOG.info("[fetchCustAcntLink SQL]: " + sb.toString());
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, custId);
			exec.setString(2, ecId);
			exec.executeQuery();
			
			while (exec.next()) {
				CustAcntLink acntLink = new CustAcntLink();
				fillBean(exec, acntLink);
				acntLinks.add(acntLink);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchCustAcntLink SQLException]: ", e);
			throw new DBException("DB_QUERY");
		
		} finally {
			exec.close();
		}
		
		return acntLinks;
	}
	
	/**
	 * 取得客戶所有綁定帳號
	 * @param custId
	 * @param ecId
	 * @param ecUser
	 * @return
	 * @throws DBException
	 */
	public CustAcntLink[] getCustAllRealAcnt(String custId, String ecId, String ecUser) throws DBException {
		DBExec exec = null;
		CustAcntLink data = null;
		ArrayList list = new ArrayList();
		
		LOG.info("查詢CUST_ACNT_LINK");
		StringBuffer sql = new StringBuffer();
		sql.append("select * from CUST_ACNT_LINK where CUST_ID = ? and EC_ID = ? ");
		sql.append(" and EC_USER = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, custId);
			exec.setString(2, ecId);
			exec.setString(3, ecUser);
			exec.executeQuery();
			while (exec.next()) {
				data = new CustAcntLink();
				fillBean(exec, data);
				list.add(data);
			}
			
		} catch (SQLException e) {
			LOG.error("[getCustAllRealAcnt SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			exec.close();
		}
		
		return (CustAcntLink[])list.toArray(new CustAcntLink[0]);
	}
	
	/**
	 * 依據 平台代碼, 身分證字號  & 帳號識別碼 取得會員連結帳號
	 * @param ecId	   平台代碼
	 * @param custId   身分證字號
	 * @param acntIndt 帳號識別碼
	 * @return List<CustAcntLink>
	 * @throws DBException
	 */
	public List<CustAcntLink> fetchCustAcntLink(String ecId, String custId, String acntIndt) throws DBException {
		DBExec exec = null;
		List<CustAcntLink> acntLinks = new ArrayList<CustAcntLink>();
		
		LOG.info("查詢CUST_ACNT_LINK");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM dbo.CUST_ACNT_LINK WHERE EC_ID = ? AND CUST_ID = ? AND ACNT_INDT = ? ");
		
		LOG.info("[fetchCustAcntLink SQL]: " + sb.toString());
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, custId);
			exec.setString(3, acntIndt);
			exec.executeQuery();
			
			while (exec.next()) {
				CustAcntLink acntLink = new CustAcntLink();
				fillBean(exec, acntLink);
				acntLinks.add(acntLink);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchCustAcntLink SQLException]: ", e);
			throw new DBException("DB_QUERY");
		
		} finally {
			exec.close();
		}
		
		return acntLinks;
	}
	
	/**
	 * 依據 身分證字號, 平台代碼, 平台會員代碼 & 實體帳號 更新 會員帳號連結資料
	 * @param custId   身分證字號
	 * @param ecId	   平台代碼
	 * @param ecUser   平台會員代碼
	 * @param realAcnt 實體帳號
	 * @param stts	   00: 啟用, 01: 暫停, 02: 終止
	 * @return 更新筆數
	 * @throws DBException
	 * @throws UtilException 
	 */
	public int updateCustAcntLinkSttsByKey(String custId, String ecId, String ecUser, String realAcnt, String stts) throws DBException, UtilException {
		DBExec exec = null;
		int count = 0;
		
		LOG.info("更新CUST_ACNT_LINK");
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE CUST_ACNT_LINK SET STTS = ?, STTS_DTTM = ? WHERE CUST_ID = ? AND EC_ID = ? AND EC_USER = ? AND REAL_ACNT = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, stts);
			exec.setString(2, DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD")));
			exec.setString(3, custId);
			exec.setString(4, ecId);
			exec.setString(5, ecUser);
			exec.setString(6, realAcnt);
			count = exec.executeUpdate();
			
		} catch (SQLException e) {
			LOG.error("[updateCustAcntLinkSttsByKey SQLException]: ", e);
			throw new DBException("DB_UPD");
			
		} finally {
			exec.close();
		}
		
		return count;
	} 
	
	/**
	 * 
	 * @param exec
	 * @param data
	 * @throws SQLException
	 */
	private void fillBean(DBExec exec, CustAcntLink data) throws SQLException {
		data.CUST_ID   = exec.getString("CUST_ID");
		data.EC_ID     = exec.getString("EC_ID");
		data.EC_USER   = exec.getString("EC_USER");
		data.REAL_ACNT = exec.getString("REAL_ACNT");
		data.GRAD_TYPE = exec.getString("GRAD_TYPE");
		data.GRAD      = exec.getString("GRAD");
		data.STTS      = exec.getString("STTS");
		data.STTS_DTTM = exec.getString("STTS_DTTM");
		data.TRNS_LIMT = exec.getLong("TRNS_LIMT");
		data.DAY_LIMT  = exec.getLong("DAY_LIMT");
		data.MNTH_LIMT = exec.getLong("MNTH_LIMT");
		data.CRET_DTTM = exec.getString("CRET_DTTM");
		data.MDFY_USER = exec.getString("MDFY_USER");
		data.MDFY_DTTM = exec.getString("MDFY_DTTM");
		data.ACNT_INDT = exec.getString("ACNT_INDT");
	}
	
	//V2.01, 2018/02/05 新增可設定電商最大可綁定人數(先借用 EC_DATA.NOTE 欄位)，超過綁定人數，則回覆1999-超過電商最大可綁定人數 Begin
	/**
	 * 依據 平台代碼 取得已綁定總數
	 * @param ecId	   平台代碼
	 * @return 綁定筆數
	 * @throws DBException
	 * @throws UtilException 
	 */
	public int fetchCustAcntLinkedCountByKey(String ecId, String custId) throws DBException, UtilException {
		DBExec exec = null;
		String linked_count = "0";
		
		LOG.info("查詢CUST_ACNT_LINK");
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) as LINKED_COUNT FROM CUST_ACNT_LINK WHERE EC_ID = ? AND CUST_ID = ? AND STTS = '00' ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());	
			exec.setString(1, ecId);
			exec.setString(2, custId);
			exec.executeQuery();
			
			while (exec.next()) {
				linked_count = exec.getString("LINKED_COUNT");
			}						
			
			if (StringUtil.isBlank(linked_count)) {
				linked_count = "0";
			}
		} catch (SQLException e) {
			LOG.error("[fetchCustAcntLinkCountByKey SQLException]: ", e);
			throw new DBException("DB_UPD");
			
		} finally {
			exec.close();
		}
		
		return Integer.parseInt(linked_count);
	} 
	//V2.01, 2018/02/05 新增可設定電商最大可綁定人數(先借用 EC_DATA.NOTE 欄位)，超過綁定人數，則回覆1999-超過電商最大可綁定人數 End  
}
