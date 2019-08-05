/**
 * @(#) CustAcnt.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcnt bean
 * 
 * Modify History:
 *  v1.00, 2016/03/28, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class CustAcnt extends GenericBean {
	private static final long serialVersionUID = 2433213223133286539L;
	
	public String CUST_ID;   //身分證字號
	public String REAL_ACNT; //實體帳號
	public Long TRNS_LIMT;    //單筆自訂限額
	public Long DAY_LIMT;     //每日自訂限額
	public Long MNTH_LIMT;    //每月自訂限額 
	public String CRET_DTTM; //建立時間 
	public String MDFY_USER; //最後異動人員
	public String MDFY_DTTM; //最後異動時間

	public CustAcnt() {
		super();
	}

	public CustAcnt(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("CUST_ACNT");
		this.putField("CUST_ID"   , this.CUST_ID   , "S", "Y");
		this.putField("REAL_ACNT" , this.REAL_ACNT , "S", "Y");
		this.putField("TRNS_LIMT" , this.TRNS_LIMT , "L", "N");
		this.putField("DAY_LIMT"  , this.DAY_LIMT  , "L", "N");
		this.putField("MNTH_LIMT" , this.MNTH_LIMT , "L", "N");
		this.putField("CRET_DTTM" , this.CRET_DTTM , "S", "N");
		this.putField("MDFY_USER" , this.MDFY_USER , "S", "N");
		this.putField("MDFY_DTTM" , this.MDFY_DTTM , "S", "N");
	}

	protected void fromXML() {
		this.CUST_ID   = this.getFieldString("CUST_ID");
		this.REAL_ACNT = this.getFieldString("REAL_ACNT");
		this.TRNS_LIMT = this.getFieldLong("TRNS_LIMT");
		this.DAY_LIMT  = this.getFieldLong("DAY_LIMT");
		this.MNTH_LIMT = this.getFieldLong("MNTH_LIMT");
		this.CRET_DTTM = this.getFieldString("CRET_DTTM");
		this.MDFY_USER = this.getFieldString("MDFY_USER");
		this.MDFY_DTTM = this.getFieldString("MDFY_DTTM");
	}
}
