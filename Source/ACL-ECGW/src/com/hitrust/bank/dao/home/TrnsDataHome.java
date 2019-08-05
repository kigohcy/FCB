/**
 * @(#) TrnsDataHome.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : TrnsDataHome
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
import java.util.List;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.BeanHome;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.dao.beans.TrnsData;

public class TrnsDataHome extends BeanHome {
	//log4j
    static Logger LOG = Logger.getLogger(TrnsDataHome.class);
	
	public TrnsDataHome(Connection conn){
		super();
		this.conn = conn;
	}
	
	/**
	 * 
	 * @param ecId
	 * @param ordrNo
	 * @return
	 * @throws DBException
	 */
	public boolean isOrdrNoExist(String ecId, String ordrNo) throws DBException {
		DBExec exec = null;
		
		LOG.info("查詢TRNS_DATA");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as cnt from TRNS_DATA where EC_ID=? ");
		sql.append("and TRNS_TYPE='A' and (TRNS_STTS='01' or TRNS_STTS='02') and ORDR_NO=?");
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sql.toString());
			exec.setString(1, ecId);
			exec.setString(2, ordrNo);
			exec.executeQuery();
			if (exec.next()) {
				if(exec.getInt("cnt") > 0) return true;
			}
		} catch (SQLException e) {
            LOG.error("[isOrdrNoExist] Exception " + e.getMessage());
            throw new DBException(e, "DB_QUERY");
		} finally {
			if(exec!=null) exec.close();
		}
		return false;
	}
	
	/**
	 * 依據 平台代碼 & 平台訊息序號 取得交易資料
	 * @param ecId	  平台代碼
	 * @param ecMsgNo 平台訊息序號
	 * @return TrnsData or null
	 * @throws DBException 
	 */
	public TrnsData fetchTrnsDataByKey(String ecId, String ecMsgNo) throws DBException {
		DBExec exec = null;
		TrnsData data = null;
		
		StringBuffer sb = new StringBuffer();
		LOG.info("查詢TRNS_DATA");
		sb.append("SELECT * FROM TRNS_DATA WHERE EC_ID = ? AND EC_MSG_NO = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, ecMsgNo);
			exec.executeQuery();
			
			while (exec.next()) {
				data = new TrnsData();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchTrnsDataByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			if(exec!=null) exec.close();
		}
		
		return data;
	}
	
	/**
	 * 依據 訂單編號取得原訂單資料
	 * @param ecId	  平台代碼
	 * @param custId  身分證字號
	 * @param orderNo 訂單編號
	 * @return TrnsData or null
	 * @throws DBException 
	 */
	public TrnsData fetchOrderDataByOrderNo(String ecId, String custId, String orderNo) throws DBException {
		DBExec exec = null;
		TrnsData data = null;
		
		StringBuffer sb = new StringBuffer();
		LOG.info("查詢TRNS_DATA");
		sb.append("SELECT * FROM TRNS_DATA WHERE EC_ID = ? AND CUST_ID = ? AND ORDR_NO = ? AND TRNS_STTS = '02' ");
		sb.append(" and TRNS_TYPE in ('A','E') ");
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, ecId);
			exec.setString(2, custId);
			exec.setString(3, orderNo);
			exec.executeQuery();
			
			while (exec.next()) {
				data = new TrnsData();
				fillBean(exec, data);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchOrderData SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			if(exec!=null) exec.close();
		}
		
		return data;
	}
	
	/**
	 * 依據 平台代碼與訊息序號 更新交易資料
	 * @param ecId		 平台代碼
	 * @param ecMsgNo	 原訊息序號
	 * @param trnsStts 交易狀態: 02-交易成功/03-交易失敗
	 * @param hostCode 主機回應錯誤代碼
	 * @param journalNum 主機交易序號
	 * @return 回傳更新筆數
	 * @throws UtilException
	 * @throws DBException
	 */
	public int updateTrnsDataByKey(String ecId, String ecMsgNo, String trnsStts, String errCode, String hostCode, 
			String journalNum) throws UtilException, DBException {
		DBExec exec = null;
		int count = 0;
		
		StringBuffer sb = new StringBuffer();
		LOG.info("更新TRNS_DATA");
		sb.append("UPDATE TRNS_DATA SET TRNS_STTS=?, ERR_CODE=?, HOST_CODE=?, HOST_SEQ=?, MDFY_DTTM=? ");
		sb.append(" WHERE EC_ID = ? AND EC_MSG_NO = ? ");
		
		LOG.info("[updateTrnsDataByKey SQL]: " + sb.toString());
		LOG.info("[TRNS_STTS]: " + trnsStts + " [ERR_CODE]:" + errCode);
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, trnsStts);
			exec.setString(2, errCode);
			exec.setString(3, hostCode);
			exec.setString(4, journalNum);
			exec.setString(5, DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD")));
			exec.setString(6, ecId);
			exec.setString(7, ecMsgNo);
			count = exec.executeUpdate();
			
		} catch (SQLException e) {
			LOG.error("[updateTrnsDataByKey SQLException]: ", e);
			throw new DBException("DB_UPD");
			
		} finally {
			if(exec!=null) exec.close();
		}
		
		return count;
	}
	
	/**
	 * 依據 PK 更新訂單餘額
	 * @param ecId	   平台代碼
	 * @param ecMsgNo  平台訊息序號
	 * @param backAmnt (訂單餘額 - 交易金額)
	 * @return 更新筆數
	 * @throws DBException 
	 * @throws UtilException 
	 */
	public int updateTrnsDataBackAmntByKey(String ecId, String ecMsgNo, int backAmnt) throws DBException, UtilException {
		DBExec exec = null;
		int count = 0;
		
		StringBuffer sb = new StringBuffer();
		LOG.info("更新TRNS_DATA");
		sb.append("UPDATE TRNS_DATA SET BACK_AMNT = ?, MDFY_DTTM = ? WHERE EC_ID = ? AND EC_MSG_NO = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setInt(1, backAmnt);
			exec.setString(2, DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD")));
			exec.setString(3, ecId);
			exec.setString(4, ecMsgNo);
			count = exec.executeUpdate();
			
		} catch (SQLException e) {
			LOG.error("[updateTrnsDataBackAmntByKey SQLException]: ", e);
			throw new DBException("DB_UPD");
			
		} finally {
			if(exec!=null) exec.close();
		}
		
		return count;
	}
	
	/**
	 * 依據 交易日期取得狀態未明交易
	 * @param trnsDateList	  交易日期(yyyyMMdd)
	 * @return List<TrnsData>
	 * @throws DBException 
	 */
	public List<TrnsData> fetchNukonwSttsTrns(List<String> trnsDateList) throws DBException {
		DBExec exec = null;
		List<String> arguList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		List<TrnsData> resultList = new ArrayList<TrnsData>();
		LOG.info("查詢TRNS_DATA");
		sb.append("SELECT * FROM TRNS_DATA WHERE TRNS_STTS = '03' ");
		for(String trnsDate:trnsDateList){
			sb.append(" and TRNS_DTTM BETWEEB ? AND ? '");
			arguList.add(trnsDate.substring(0, 4)+"-"+trnsDate.substring(4,6)+"-"+trnsDate.substring(6) + " 00:00:00.000");
			arguList.add(trnsDate.substring(0, 4)+"-"+trnsDate.substring(4,6)+"-"+trnsDate.substring(6) + " 23:59:59.999");
		}
		sb.append(" ORDER BY TRNS_DTTM");
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			int count = 0;
			for(String argu:arguList){
				exec.setString(++count, argu);
			}
			exec.executeQuery();
			
			while (exec.next()) {
				TrnsData data = new TrnsData();
				fillBean(exec, data);
				resultList.add(data);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchNukonwSttsTrns SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			if(exec!=null) exec.close();
		}
		
		return resultList;
	}
	
	private void fillBean(DBExec exec, TrnsData data) throws SQLException {
		data.EC_ID     = exec.getString("EC_ID");
		data.EC_MSG_NO = exec.getString("EC_MSG_NO");
		data.CUST_ID   = exec.getString("CUST_ID");
		data.CUST_SERL = exec.getString("CUST_SERL");
		data.EC_USER   = exec.getString("EC_USER");
		data.TRNS_TYPE = exec.getString("TRNS_TYPE");
		data.REAL_ACNT = exec.getString("REAL_ACNT");
		data.RECV_ACNT = exec.getString("RECV_ACNT");
		data.TRNS_DTTM = exec.getString("TRNS_DTTM");
		data.TRNS_AMNT = exec.getInt("TRNS_AMNT");
		data.TRNS_STTS = exec.getString("TRNS_STTS");
		data.ORDR_NO   = exec.getString("ORDR_NO");
		data.BACK_AMNT = exec.getInt("BACK_AMNT");
		data.TRNS_NOTE = exec.getString("TRNS_NOTE");
		data.ERR_CODE  = exec.getString("ERR_CODE");
		data.HOST_CODE = exec.getString("HOST_CODE");
		data.HOST_SEQ  = exec.getString("HOST_SEQ");
		data.FEE_TYPE  = exec.getString("FEE_TYPE");
		data.FEE_RATE  = exec.getDouble("FEE_RATE");
		data.FEE_AMNT  = exec.getInt("FEE_AMNT");
		data.ACNT_INDT = exec.getString("ACNT_INDT");
		data.MDFY_DTTM = exec.getString("MDFY_DTTM");
		data.MAIL_NOTC = exec.getString("MAIL_NOTC");
		data.MAIL_DTTM = exec.getString("MAIL_DTTM");
		data.TELE_NO   = exec.getString("TELE_NO");
		data.MIN_FEE   = exec.getInt("MIN_FEE");
		data.MAX_FEE   = exec.getInt("MAX_FEE");
		data.IP        = exec.getString("IP");
		//20190619 Add 交易失敗時記錄上下行電文 Begin
		data.TITA 	   = exec.getString("TITA");
		data.TOTA      = exec.getString("TOTA");
		//20190619 Add 交易失敗時記錄上下行電文 End
	}
}
