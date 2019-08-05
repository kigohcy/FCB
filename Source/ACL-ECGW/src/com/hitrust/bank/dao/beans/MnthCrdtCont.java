/**
 * @(#) MnthCrdtCont.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 月額度累計 bean
 * 
 * Modify History:
 *  v1.00, 2016/03/28, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class MnthCrdtCont extends GenericBean {
	private static final long serialVersionUID = 5231108488514861473L;
	
	public String ACNT_INDT; //帳號識別碼
	public String TRNS_MNTH; //交易月份
	public String CUST_ID;   //身分證字號
	public String EC_ID;     //平台代碼
	public String EC_USER;   //平台會員代碼
	public String REAL_ACNT; //實體帳號
	public long   MNTH_CONT; //月累計金額
	public String CUST_SERL; //會員服務序號

	public MnthCrdtCont() {
		super();
	}

	public MnthCrdtCont(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("MNTH_CRDT_CONT");
		this.putField("ACNT_INDT", this.ACNT_INDT, "S", "Y");
		this.putField("TRNS_MNTH", this.TRNS_MNTH, "S", "Y");
		this.putField("CUST_ID"  , this.CUST_ID  , "S", "N");
		this.putField("EC_ID"    , this.EC_ID    , "S", "N");
		this.putField("EC_USER"  , this.EC_USER  , "S", "N");
		this.putField("REAL_ACNT", this.REAL_ACNT, "S", "N");
		this.putField("MNTH_CONT", this.MNTH_CONT, "L", "N");
		this.putField("CUST_SERL", this.CUST_SERL, "S", "N");
	}

	protected void fromXML() {
		this.ACNT_INDT = this.getFieldString("ACNT_INDT");
		this.TRNS_MNTH = this.getFieldString("TRNS_MNTH");
		this.CUST_ID   = this.getFieldString("CUST_ID");
		this.EC_ID     = this.getFieldString("EC_ID");
		this.EC_USER   = this.getFieldString("EC_USER");
		this.REAL_ACNT = this.getFieldString("REAL_ACNT");
		this.MNTH_CONT = this.getFieldLong("MNTH_CONT");
		this.CUST_SERL = this.getFieldString("CUST_SERL");
	}
}
