/**
 * @(#) SignatureLog.java
 *
 * Directions: 訊息簽章記錄
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/27, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;

public class SignatureLog extends GenericBean {

	private static final long serialVersionUID = -7298367661919949730L;
	
	// =============== Table Attribute ===============
	public String EC_ID;	 // 平台代碼
	public String EC_MSG_NO; // 平台訊息序號
	public String SIG_STTS;	 // 驗章結果
	public String SIG_VALU;  // 交易簽章值

	@Override
	protected void toXML() {
		this.putTableName("SIGNATURE_LOG");
		this.putField("EC_ID", 	   this.EC_ID, 	   "S", "Y");
		this.putField("EC_MSG_NO", this.EC_MSG_NO, "S", "Y");
		this.putField("SIG_STTS",  this.SIG_STTS,  "S", "N");
		this.putField("SIG_VALU",  this.SIG_VALU,  "S", "N");
		
	}

	@Override
	protected void fromXML() {
		this.EC_ID 	   = this.getFieldString("EC_ID");
		this.EC_MSG_NO = this.getFieldString("EC_MSG_NO");
		this.SIG_STTS  = this.getFieldString("SIG_STTS");
		this.SIG_VALU  = this.getFieldString("SIG_VALU");
		
	}

}
