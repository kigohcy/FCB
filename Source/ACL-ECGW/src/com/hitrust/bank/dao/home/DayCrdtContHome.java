/**
 * @(#) DayCrdtContHome.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayCrdtContHome
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
import com.hitrust.bank.dao.beans.DayCrdtCont;

public class DayCrdtContHome extends BeanHome {
    static Logger LOG = Logger.getLogger(DayCrdtContHome.class);
	
	public DayCrdtContHome(Connection conn){
		super();
		this.conn = conn;
	}
	
	/**
	 * fillBean
	 * @param exec DBExec
	 * @param data DayCrdtCont
	 * @throws SQLException
	 */
	private void fillBean(DBExec exec, DayCrdtCont data) throws SQLException {
		data.ACNT_INDT = exec.getString("ACNT_INDT");
		data.TRNS_DATE = exec.getString("TRNS_DATE");
		data.CUST_ID   = exec.getString("CUST_ID");
		data.EC_ID     = exec.getString("EC_ID");
		data.EC_USER   = exec.getString("EC_USER");
		data.REAL_ACNT = exec.getString("REAL_ACNT");
		data.DAY_CONT  = exec.getLong("DAY_CONT");
	}
	
	/**
	 * 依 Primary Key 取得 日額度累計檔
	 * @param acntIndt
	 * @param trnsDate
	 * @return DayCrdtCont
	 * @throws DBException
	 */
	public DayCrdtCont getDayCrdtContByPk(String acntIndt, String trnsDate) throws DBException {
		DBExec exec = null;
		DayCrdtCont data = null;
		
		LOG.info("查詢DAY_CRDT_CONT");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from DAY_CRDT_CONT where ACNT_INDT=? and TRNS_DATE=? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, acntIndt);
			exec.setString(2, trnsDate);
			exec.executeQuery();
			if (exec.next()) {
				data = new DayCrdtCont();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[getDayCrdtContByPk SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			if(exec!=null) exec.close();
		}
		
		return data;
	}
	
	/**
	 * 取得 日額度累計金額 by 交易日期+身分證字號+實體帳號+會員服務序號
	 * @param trnsDate
	 * @param custId
	 * @param realAcnt
	 * @param custSerl
	 * @return long
	 * @throws DBException
	 */
	public long getDaySumByAcnt(String trnsDate, String custId, String realAcnt, String custSerl) throws DBException {
		DBExec exec = null;
		StringBuffer sql = new StringBuffer();
		LOG.info("查詢DAY_CRDT_CONT");
		
		sql.append("select sum(DAY_CONT) as SUM_DAY_CONT from DAY_CRDT_CONT ");
		sql.append(" where TRNS_DATE=? and CUST_ID=? and REAL_ACNT=? and CUST_SERL=? ");
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, trnsDate);
			exec.setString(2, custId);
			exec.setString(3, realAcnt);
			exec.setString(4, custSerl);
			exec.executeQuery();
			if (exec.next()) {
				return exec.getLong("SUM_DAY_CONT");
			}
			
		} catch (SQLException e) {
			LOG.error("[getDaySumByAcnt SQLException]: ", e);
			throw new DBException("DB_QUERY");
		} finally {
			if(exec!=null) exec.close();
		}
		
		return 0;
	}
	
	/**
	 * 累計 日額度金額
	 * @param dayCrdtCont
	 * @param amount
	 * @return boolean
	 * @throws DBException
	 */
	public boolean calculate(DayCrdtCont dayCrdtCont, int amount) throws DBException {
		DBExec exec = null;
		boolean dayCrdtContExist = false;
		StringBuffer sql = new StringBuffer();
		LOG.info("更新DAY_CRDT_CONT");
		
		dayCrdtCont.setConnection(conn);
		if(dayCrdtCont.isExist()){
			dayCrdtContExist = true;
			sql.append("Update DAY_CRDT_CONT set DAY_CONT = DAY_CONT + ? ");
			sql.append(" where ACNT_INDT = ? and TRNS_DATE = ? ");
		}
		
		try {
			if(dayCrdtContExist){
				LOG.info("calculate sql:"+sql.toString());
				exec = new DBExec(this.conn);
				exec.prepareStatement(sql.toString());
				exec.setLong(1, (long)amount);
				exec.setString(2, dayCrdtCont.ACNT_INDT);
				exec.setString(3, dayCrdtCont.TRNS_DATE);
				
				int cnt = exec.executeUpdate();
				if(cnt==0){
					return false;
				}
			}else{
				dayCrdtCont.DAY_CONT = amount;
				dayCrdtCont.insert();
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
	 * 回沖 日累計額度
	 * @param acntIndt 帳號識別碼
	 * @param trnsDate 交易日期 (YYYYMMDD)
	 * @param amount 金額
	 * @return boolean
	 * @throws DBException
	 */
	public boolean backflush(String acntIndt, String trnsDate, int amount) throws DBException {
		DBExec exec = null;
		StringBuffer sql = new StringBuffer();
		LOG.info("更新DAY_CRDT_CONT");
		sql.append("Update DAY_CRDT_CONT set DAY_CONT = DAY_CONT - ? ");
		sql.append(" where ACNT_INDT = ? and TRNS_DATE = ?");
		try {
			LOG.info("backflush sql:" + sql.toString());
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setLong(1, (long)amount);
			exec.setString(2, acntIndt);
			exec.setString(3, trnsDate);
			
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
