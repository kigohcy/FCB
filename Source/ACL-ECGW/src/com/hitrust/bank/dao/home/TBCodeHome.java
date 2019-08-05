/*
 * @(#)TBCodeHome.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/04/25, Ben He
 *   1) First release
 *  v1.01, 2016/12/28, Eason Hsu
 *   1) TSBACL-144, 網銀 & 晶片卡認證失敗, 錯誤訊息 Mapping 調整
 */

package com.hitrust.bank.dao.home;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.BeanHome;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.bank.dao.beans.TbCode;

public class TBCodeHome extends BeanHome {
	
    static Logger LOG = Logger.getLogger(TBCodeHome.class);
	
	public TBCodeHome(){
		
	}
	
	public TBCodeHome(Connection conn){
		super();
		this.conn = conn;
	}
	
	/**
	 * 
	 * @param type
	 * @param id
	 * @param lngn
	 * @return
	 * @throws DBException
	 */
	public boolean existByTypeId(String type,String id,String lngn)throws DBException{
		TbCode tbCode = new TbCode(); 
		tbCode.setConnection(conn);
		return tbCode.isExist();
	}
	

	/**
	 * 依據 訊息代碼 & 訊息類別 取得訊息說明
	 * @param codeId   訊息代碼
	 * @param codeType 訊息類別
	 * @return TbCode or null
	 * @throws DBException
	 */
	public TbCode fetchTbCodeByKey(String codeId, String codeType) throws DBException {
		DBExec exec = null;
		TbCode code = null;
		LOG.info("查詢TB_CODE"); 
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM TB_CODE WHERE LNGN = ?  AND CODE_ID = ? AND CODE_TYPE = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, "zh_TW");
			exec.setString(2, codeId);
			exec.setString(3, codeType);
			exec.executeQuery();
			
			while (exec.next()) {
				code = new TbCode();
				this.fillBean(exec, code);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchTbCodeByKey SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return code;
	}
	
	/**
	 * 依據 錯誤訊息取得錯訊代碼
	 * @param errorMsg
	 * @return TbCode or null
	 * @throws DBException
	 * @since v1.01
	 */
	public TbCode fetchTbCodeByErrorMsg(String errorMsg) throws DBException {
		DBExec exec = null;
		TbCode code = null;
		
		LOG.info("查詢TB_CODE");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM TB_CODE WHERE LNGN = 'zh_TW' AND SHOW_DESC = ? ");
		
		try {
			exec = new DBExec(this.conn);
			exec.prepareStatement(sb.toString());
			exec.setString(1, errorMsg);
			exec.executeQuery();
			
			while (exec.next()) {
				code = new TbCode();
				this.fillBean(exec, code);
			}
			
		} catch (SQLException e) {
			LOG.error("[fetchTbCodeByErrorMsg SQLException]: ", e);
			throw new DBException("DB_QUERY");
			
		} finally {
			exec.close();
		}
		
		return code;
	}
	
	private void fillBean(DBExec exec, TbCode code) throws SQLException{
		code.LNGN 	   = exec.getString("LNGN");
		code.CODE_ID   = exec.getString("CODE_ID");
		code.CODE_TYPE = exec.getString("CODE_TYPE");
		code.CODE_DESC = exec.getString("CODE_DESC");
		code.SHOW_DESC = exec.getString("SHOW_DESC");
		
		code.REF_CODE_ID = exec.getString("REF_CODE_ID"); // v1.01, 參考訊息代碼
		
	}

}
