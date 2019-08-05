/**
 * @(#) MnthCrdtContHome.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : MnthCrdtContHome
 * 
 * Modify History:
 *  v1.00, 2016/03/28, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.home;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.BeanHome;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.bank.dao.beans.MnthCrdtCont;

public class MnthCrdtContHome extends BeanHome {
    static Logger LOG = Logger.getLogger(MnthCrdtContHome.class);
	
	public MnthCrdtContHome(Connection conn){
		super();
		this.conn = conn;
	}
	
	/**
	 * fillBean
	 * @param exec DBExec
	 * @param data MnthCrdtCont
	 * @throws SQLException
	 */
	private void fillBean(DBExec exec, MnthCrdtCont data) throws SQLException {
		data.ACNT_INDT = exec.getString("ACNT_INDT");
		data.TRNS_MNTH = exec.getString("TRNS_MNTH");
		data.CUST_ID   = exec.getString("CUST_ID");
		data.EC_ID     = exec.getString("EC_ID");
		data.EC_USER   = exec.getString("EC_USER");
		data.REAL_ACNT = exec.getString("REAL_ACNT");
		data.MNTH_CONT = exec.getLong("MNTH_CONT");
	}
	
	/**
	 * 依 Primary Key 取得 月額度累計檔
	 * @param acntIndt: 帳號識別碼
	 * @param trnsMnth: 交易月份 YYYYMM
	 * @return MnthCrdtCont
	 * @throws DBException
	 */
	public MnthCrdtCont getMnthCrdtContByPk(String acntIndt, String trnsMnth) throws DBException {
		DBExec exec = null;
		MnthCrdtCont data = null;
		
		LOG.info("查詢MNTH_CRDT_CONT");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from MNTH_CRDT_CONT where ACNT_INDT=? and TRNS_MNTH=?");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, acntIndt);
			exec.setString(2, trnsMnth);
			exec.executeQuery();
			if (exec.next()) {
				data = new MnthCrdtCont();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[getMnthCrdtContByPk SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			if(exec!=null) exec.close();
		}
		
		return data;
	}
	
	/**
	 * 取得 月額度累計金額 by 交易日期+身分證字號+實體帳號+會員服務序號
	 * @param trnsMnth
	 * @param custId
	 * @param realAcnt
	 * @param custSerl
	 * @return long
	 * @throws DBException
	 */
	public long getMnthSumByAcnt(String trnsMnth, String custId, String realAcnt, String custSerl) throws DBException {
		DBExec exec = null;
		MnthCrdtCont data = null;
		
		LOG.info("查詢MNTH_CRDT_CONT");
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(MNTH_CONT) as SUM_MNTH_CONT from MNTH_CRDT_CONT ");
		sql.append(" where TRNS_MNTH=? and CUST_ID=? and REAL_ACNT=? and CUST_SERL=? ");
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, trnsMnth);
			exec.setString(2, custId);
			exec.setString(3, realAcnt);
			exec.setString(4, custSerl);
			exec.executeQuery();
			if (exec.next()) {
				return exec.getLong("SUM_MNTH_CONT");
			}
			
		} catch (SQLException e) {
			LOG.error("[getMnthSumByAcnt SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			if(exec!=null) exec.close();
		}
		
		return 0;
	}
	
	/**
	 * 取得 月額度累計金額 by 交易日期+身分證字號+會員服務序號
	 * @param trnsMnth
	 * @param custId
	 * @param custSerl
	 * @return long
	 * @throws DBException
	 */
	public long getMnthSumByCustId(String trnsMnth, String custId, String custSerl) throws DBException {
		DBExec exec = null;
		
		StringBuffer sql = new StringBuffer();
		LOG.info("查詢MNTH_CRDT_CONT");
		sql.append("select sum(MNTH_CONT) as SUM_MNTH_CONT from MNTH_CRDT_CONT ");
		sql.append(" where TRNS_MNTH=? and CUST_ID=? and CUST_SERL=? ");
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, trnsMnth);
			exec.setString(2, custId);
			exec.setString(3, custSerl);
			exec.executeQuery();
			if (exec.next()) {
				return exec.getLong("SUM_MNTH_CONT");
			}
			
		} catch (SQLException e) {
			LOG.error("[getMnthSumByAcnt SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			if(exec!=null) exec.close();
		}
		
		return 0;
	}
	
	
	/**
	 * 累計 月額度金額
	 * @param mnthCrdtCont
	 * @param amount
	 * @return boolean
	 * @throws DBException
	 */
	public boolean calculate(MnthCrdtCont mnthCrdtCont, int amount) throws DBException {
		DBExec exec = null;
		boolean mnthCrdtContExist = false;
		StringBuffer sql = new StringBuffer();
		
		mnthCrdtCont.setConnection(conn);
		if(mnthCrdtCont.isExist()){
			mnthCrdtContExist = true;
			LOG.info("更新MNTH_CRDT_CONT");
			sql.append("Update MNTH_CRDT_CONT set MNTH_CONT = MNTH_CONT + ? ");
			sql.append(" where ACNT_INDT = ? and TRNS_MNTH = ? ");
		}
		
		try {
			if(mnthCrdtContExist){
				LOG.info("sql:"+sql.toString());
				exec = new DBExec(this.conn);
				exec.prepareStatement(sql.toString());
				exec.setLong(1, (long)amount);
				exec.setString(2, mnthCrdtCont.ACNT_INDT);
				exec.setString(3, mnthCrdtCont.TRNS_MNTH);
				
				int cnt = exec.executeUpdate();
				if(cnt==0){
					return false;
				}
			}else{
				mnthCrdtCont.MNTH_CONT = amount;
				mnthCrdtCont.insert();
			}
			
			return true;
			
		} catch (SQLException e) {
			LOG.error("[calculate SQLException]:", e);
			throw new DBException("DB_UPD");
		} finally {
			if(exec!=null) exec.close();
		}
	}
	
	/**
	 * 回沖 月累計額度
	 * @param acntIndt 帳號識別碼
	 * @param trnsMnth 交易月份 (YYYYMM)
	 * @param amount 金額
	 * @return boolean
	 * @throws DBException
	 */
	public boolean backflush(String acntIndt, String trnsMnth, int amount) throws DBException {
		DBExec exec = null;
		StringBuffer sql = new StringBuffer();
		LOG.info("更新MNTH_CRDT_CONT");
		sql.append("Update MNTH_CRDT_CONT set MNTH_CONT = MNTH_CONT - ? ");
		sql.append(" where ACNT_INDT = ? and TRNS_MNTH = ?");
		try {
			LOG.info("backflush sql:" + sql.toString());
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setLong(1, (long)amount);
			exec.setString(2, acntIndt);
			exec.setString(3, trnsMnth);
			
			int cnt = exec.executeUpdate();
			if(cnt==0){
				return false;
			}
		} catch (SQLException e) {
			LOG.error("[backflush SQLException]:", e);
			throw new DBException("DB_UPD");
		} finally {
			if(exec!=null) exec.close();
		}
		return true;
	}
}
