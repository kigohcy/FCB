/**
 * @(#) EcMsgLog.java
 *
 * Directions: 平台訊息收送記錄
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/25, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;

public class EcMsgLog extends GenericBean {

	private static final long serialVersionUID = -2269903501874347694L;
	
	// =============== Table Attribute ===============
	public String EC_ID;	 // 平台代碼
	public String EC_MSG_NO; // 平台訊息序號
	public String MSG_TYPE;  // 訊息類別
	public String CRET_DTTM; // 接收時間
	public String STTS;		 // 訊息狀態
	public String MSG_CNTN;	 // 訊息內容

	@Override
	protected void toXML() {
		this.putTableName("EC_MSG_LOG");
		this.putField("EC_ID", 		this.EC_ID, 	"S", "Y");
		this.putField("EC_MSG_NO", 	this.EC_MSG_NO, "S", "Y");
		this.putField("MSG_TYPE", 	this.MSG_TYPE, 	"S", "Y");
		this.putField("CRET_DTTM", 	this.CRET_DTTM, "S", "N");
		this.putField("STTS", 		this.STTS, 		"S", "N");
		this.putField("MSG_CNTN", 	this.MSG_CNTN, 	"S", "N");
	}

	@Override
	protected void fromXML() {
		this.EC_ID 	   = this.getFieldString("EC_ID");
		this.EC_MSG_NO = this.getFieldString("EC_MSG_NO");
		this.MSG_TYPE  = this.getFieldString("MSG_TYPE");
		this.CRET_DTTM = this.getFieldString("CRET_DTTM");
		this.STTS 	   = this.getFieldString("STTS");
		this.MSG_CNTN  = this.getFieldString("MSG_CNTN");
	}

}
