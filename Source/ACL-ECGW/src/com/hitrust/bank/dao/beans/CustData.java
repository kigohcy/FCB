/**
 * @(#) CustData.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustData bean
 * 
 * Modify History:
 *  v1.00, 2016/03/28, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class CustData extends GenericBean {
	private static final long serialVersionUID = 4045420734225903448L;
	
	public String CUST_ID;   //身分證字號
	public String CUST_NAME; //客戶姓名
	public String CUST_SERL; //會員服務序號
	public String CUST_TYPE; //會員身分
	public String TEL;       //行動電話(目前系統不記錄此資訊, 此欄位最新資訊需從CIF電文取得)
	public String MAIL;      //電子郵件(目前系統不記錄此資訊, 此欄位最新資訊需從CIF電文取得)
	public String VRSN;      //條款版本
	public String STTS;      //狀態
	public String STTS_DTTM; //狀態異動時間
	public String CRET_DTTM; //建立時間 
	public String MDFY_USER; //最後異動人員
	public String MDFY_DTTM; //最後異動時間

	public CustData() {
		super();
	}

	public CustData(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("CUST_DATA");
		this.putField("CUST_ID"   , this.CUST_ID   , "S", "Y");
		this.putField("CUST_NAME" , this.CUST_NAME , "S", "N");
		this.putField("CUST_SERL" , this.CUST_SERL , "S", "N");
		this.putField("CUST_TYPE" , this.CUST_TYPE , "S", "N");
		this.putField("TEL"       , this.TEL       , "S", "N");
		this.putField("MAIL"      , this.MAIL      , "S", "N");
		this.putField("VRSN"      , this.VRSN      , "S", "N");
		this.putField("STTS"      , this.STTS      , "S", "N");
		this.putField("STTS_DTTM" , this.STTS_DTTM , "S", "N");
		this.putField("CRET_DTTM" , this.CRET_DTTM , "S", "N");
		this.putField("MDFY_USER" , this.MDFY_USER , "S", "N");
		this.putField("MDFY_DTTM" , this.MDFY_DTTM , "S", "N");
	}

	protected void fromXML() {
		this.CUST_ID   = this.getFieldString("CUST_ID");
		this.CUST_NAME = this.getFieldString("CUST_NAME");
		this.CUST_SERL = this.getFieldString("CUST_SERL");
		this.CUST_TYPE = this.getFieldString("CUST_TYPE");
		this.TEL       = this.getFieldString("TEL");
		this.MAIL      = this.getFieldString("MAIL");
		this.VRSN      = this.getFieldString("VRSN");
		this.STTS      = this.getFieldString("STTS");
		this.STTS_DTTM = this.getFieldString("STTS_DTTM");
		this.CRET_DTTM = this.getFieldString("CRET_DTTM");
		this.MDFY_USER = this.getFieldString("MDFY_USER");
		this.MDFY_DTTM = this.getFieldString("MDFY_DTTM");
	}
}
