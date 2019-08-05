/**
 * @(#) EcCert.java
 *
 * Directions: 電商平台憑證檔
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

public class EcCert extends GenericBean {

	private static final long serialVersionUID = 234956936502198353L;
	
	// =============== Table Attribute ===============
	public String EC_ID; 		 // 平台代碼
	public String CERT_CN ; 	 // 憑證識別碼
	public String CERT_SN;		 // 憑證序號
	public int CERT_ID; 		 // 憑證編號
	public String STRT_DTTM;	 // 生效日期
	public String END_DTTM;		 // 到期日期
	public String RA_ACNT;		 // RA帳號
	public byte[] CERT_FEE_DATA; // 繳費通知

	@Override
	protected void toXML() {
		this.putTableName("EC_CERT");
		this.putField("EC_ID", 		   this.EC_ID, 		   "S", "Y");
		this.putField("CERT_CN", 	   this.CERT_CN, 	   "S", "Y");
		this.putField("CERT_SN", 	   this.CERT_SN, 	   "S", "N");
		this.putField("CERT_ID", 	   this.CERT_ID, 	   "I", "N");
		this.putField("STRT_DTTM", 	   this.STRT_DTTM, 	   "S", "N");
		this.putField("END_DTTM", 	   this.EC_ID, 		   "S", "N");
		this.putField("RA_ACNT", 	   this.RA_ACNT, 	   "S", "N");
		this.putField("CERT_FEE_DATA", this.CERT_FEE_DATA, "B", "N");
		
	}

	@Override
	protected void fromXML() {
		this.EC_ID 	       = this.getFieldString("");
		this.CERT_CN       = this.getFieldString("CERT_CN");
		this.CERT_SN 	   = this.getFieldString("CERT_SN");
		this.CERT_ID 	   = this.getFieldInt("CERT_ID");
		this.STRT_DTTM 	   = this.getFieldString("STRT_DTTM");
		this.END_DTTM 	   = this.getFieldString("END_DTTM");
		this.RA_ACNT 	   = this.getFieldString("RA_ACNT");
		this.CERT_FEE_DATA = this.getFieldBytes("CERT_FEE_DATA");
		
	}

}
