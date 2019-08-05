/**
 * @(#) CustAcntLink.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntLink bean
 * 
 * Modify History:
 *  v1.00, 2016/03/28, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class CustAcntLink extends GenericBean {
	private static final long serialVersionUID = -5031705914438137171L;
	
	public String CUST_ID;   //身分證字號
	public String EC_ID;     //平台代碼
	public String EC_USER;   //平台會員代碼
	public String REAL_ACNT; //實體帳號
	public String GRAD_TYPE; //身分認證方式
	public String GRAD;      //等級
	public String STTS;      //狀態
	public String STTS_DTTM; //狀態異動時間
	public Long TRNS_LIMT;    //單筆自訂限額
	public Long DAY_LIMT;     //每日自訂限額
	public Long MNTH_LIMT;    //每月自訂限額
	public String CRET_DTTM; //建立時間
	public String MDFY_USER; //最後異動人員
	public String MDFY_DTTM; //最後異動時間
	public String ACNT_INDT; //帳號識別碼
	

	public CustAcntLink() {
		super();
	}

	public CustAcntLink(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("CUST_ACNT_LINK");
		this.putField("CUST_ID"    ,this.CUST_ID   , "S", "Y");
		this.putField("EC_ID"     , this.EC_ID     , "S", "Y");
		this.putField("EC_USER"   , this.EC_USER   , "S", "Y");
		this.putField("REAL_ACNT" , this.REAL_ACNT , "S", "Y");
		this.putField("GRAD_TYPE" , this.GRAD_TYPE , "S", "N");
		this.putField("GRAD"      , this.GRAD      , "S", "N");
		this.putField("STTS"      , this.STTS      , "S", "N");
		this.putField("STTS_DTTM" , this.STTS_DTTM , "S", "N");
		this.putField("TRNS_LIMT" , this.TRNS_LIMT , "L", "N");
		this.putField("DAY_LIMT"  , this.DAY_LIMT  , "L", "N");
		this.putField("MNTH_LIMT" , this.MNTH_LIMT , "L", "N");
		this.putField("CRET_DTTM" , this.CRET_DTTM , "S", "N");
		this.putField("MDFY_USER" , this.MDFY_USER , "S", "N");
		this.putField("MDFY_DTTM" , this.MDFY_DTTM , "S", "N");
		this.putField("ACNT_INDT" , this.ACNT_INDT , "S", "N");
	}

	protected void fromXML() {
		this.CUST_ID   = this.getFieldString("CUST_ID");
		this.EC_ID     = this.getFieldString("EC_ID");
		this.EC_USER   = this.getFieldString("EC_USER");
		this.REAL_ACNT = this.getFieldString("REAL_ACNT");
		this.GRAD_TYPE = this.getFieldString("GRAD_TYPE");
		this.GRAD      = this.getFieldString("GRAD");
		this.STTS      = this.getFieldString("STTS");
		this.STTS_DTTM = this.getFieldString("STTS_DTTM");
		this.TRNS_LIMT = this.getFieldLong("TRNS_LIMT");
		this.DAY_LIMT  = this.getFieldLong("DAY_LIMT");
		this.MNTH_LIMT = this.getFieldLong("MNTH_LIMT");
		this.CRET_DTTM = this.getFieldString("CRET_DTTM");
		this.MDFY_USER = this.getFieldString("MDFY_USER");
		this.MDFY_DTTM = this.getFieldString("MDFY_DTTM");
		this.ACNT_INDT = this.getFieldString("ACNT_INDT");
	}
}
