/**
 * @(#) CustAcntLog.java
 *
 * Directions: 會員帳號連結記錄檔
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/28, Eason Hsu
 *    1) JIRA-Number, First release
 *   v1.01, 2018/03/20
 *   1) 新增IP欄位
 *
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;

public class CustAcntLog extends GenericBean {

	private static final long serialVersionUID = -6719431698666666193L;
	
	// =============== Table Attribute ===============
	public String LOG_NO;	 // 序號
	public String CUST_ID;	 // 身分證字號
	public String EC_ID;	 // 平台代碼
	public String EC_USER;   // 平台會員代碼
	public String REAL_ACNT; // 實體帳號
	public String CRET_DTTM; // 異動時間
	public String GRAD;		 // 等級
	public String GRAD_TYPE; // 身分認證方式
	public String STTS;		 // 執行狀態
	public String ERR_CODE;  // 錯誤代碼
	public String HOST_CODE; // 主機回應代碼
	public String CUST_SERL; // 會員服務序號
	public String ACNT_INDT; // 帳號識別碼
	public String EXEC_SRC;  // 執行來源
	public String EXEC_USER; // 執行人員
	public String EC_MSG_NO; // 平台訊息序號
	public String IP; 		 // IP位置

	@Override
	protected void toXML() {
		this.putTableName("CUST_ACNT_LOG");
		this.putField("LOG_NO"   , this.LOG_NO   , "S", "Y");
		this.putField("CUST_ID"  , this.CUST_ID  , "S", "N");
		this.putField("EC_ID"    , this.EC_ID    , "S", "N");
		this.putField("EC_USER"  , this.EC_USER  , "S", "N");
		this.putField("REAL_ACNT", this.REAL_ACNT, "S", "N");
		this.putField("CRET_DTTM", this.CRET_DTTM, "S", "N");
		this.putField("GRAD"     , this.GRAD     , "S", "N");
		this.putField("GRAD_TYPE", this.GRAD_TYPE, "S", "N");
		this.putField("STTS"     , this.STTS     , "S", "N");
		this.putField("ERR_CODE" , this.ERR_CODE , "S", "N");
		this.putField("HOST_CODE", this.HOST_CODE, "S", "N");
		this.putField("CUST_SERL", this.CUST_SERL, "S", "N");
		this.putField("ACNT_INDT", this.ACNT_INDT, "S", "N");
		this.putField("EXEC_SRC" , this.EXEC_SRC , "S", "N");
		this.putField("EXEC_USER", this.EXEC_USER, "S", "N");
		this.putField("EC_MSG_NO", this.EC_MSG_NO, "S", "N");
		this.putField("IP", this.IP, "S", "N");
	}

	@Override
	protected void fromXML() {
		this.LOG_NO    = this.getFieldString("LOG_NO");
		this.CUST_ID   = this.getFieldString("CUST_ID");
		this.EC_ID 	   = this.getFieldString("EC_ID");
		this.EC_USER   = this.getFieldString("EC_USER");
		this.REAL_ACNT = this.getFieldString("REAL_ACNT");
		this.CRET_DTTM = this.getFieldString("CRET_DTTM");
		this.GRAD 	   = this.getFieldString("GRAD");
		this.GRAD_TYPE = this.getFieldString("GRAD_TYPE");
		this.STTS 	   = this.getFieldString("STTS");
		this.ERR_CODE  = this.getFieldString("ERR_CODE");
		this.HOST_CODE = this.getFieldString("HOST_CODE");
		this.CUST_SERL = this.getFieldString("CUST_SERL");
		this.ACNT_INDT = this.getFieldString("ACNT_INDT");
		this.EXEC_SRC  = this.getFieldString("EXEC_SRC");
		this.EXEC_USER = this.getFieldString("EXEC_USER");
		this.EC_MSG_NO = this.getFieldString("EC_MSG_NO");
		this.IP 	   = this.getFieldString("IP");
	}

}
