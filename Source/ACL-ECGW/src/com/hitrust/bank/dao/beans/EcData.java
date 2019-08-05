/**
 * @(#) EcData.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 電商平台資料檔
 * 
 * Modify History:
 *  v1.00, 2016/03/25, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class EcData extends GenericBean {
	private static final long serialVersionUID = -560657160332410115L;
	
	public String EC_ID;
	public String EC_NAME_CH;
	public String EC_NAME_EN;
	public String SORC_IP;
	public String FEE_TYPE;
	public double FEE_RATE;
	public String STTS;
	public String REAL_ACNT;
	public String ENTR_NO;
	public String ENTR_ID;
	public String CNTC;
	public String TEL;
	public String MAIL;
	public String NOTE;
	public String CRET_USER;
	public String CRET_DTTM;
	public String MDFY_USER;
	public String MDFY_DTTM;
	public int SHOW_SERL;
	public String SHOW_REAL_ACNT;
	public Integer MIN_FEE;
	public Integer MAX_FEE;
	public Integer LINK_LIMIT;
	
	public String TAX_TYPE;
	public double TAX_RATE;
	public Integer MIN_TAX;
	public Integer MAX_TAX;

	public EcData() {
		super();
	}

	public EcData(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("EC_DATA");
		this.putField("EC_ID"     , this.EC_ID     , "S", "Y");
		this.putField("EC_NAME_CH", this.EC_NAME_CH, "S", "N");
		this.putField("EC_NAME_EN", this.EC_NAME_EN, "S", "N");
		this.putField("SORC_IP"   , this.SORC_IP   , "S", "N");
		this.putField("FEE_TYPE"  , this.FEE_TYPE  , "S", "N");
		this.putField("FEE_RATE"  , this.FEE_RATE  , "D", "N");
		this.putField("STTS"      , this.STTS      , "S", "N");
		this.putField("REAL_ACNT" , this.REAL_ACNT , "S", "N");
		this.putField("ENTR_NO"   , this.ENTR_NO   , "S", "N");
		this.putField("ENTR_ID"   , this.ENTR_ID   , "S", "N");
		this.putField("CNTC"      , this.CNTC      , "S", "N");
		this.putField("TEL"       , this.TEL       , "S", "N");
		this.putField("MAIL"      , this.MAIL      , "S", "N");
		this.putField("NOTE"      , this.NOTE      , "S", "N");
		this.putField("CRET_USER" , this.CRET_USER , "S", "N");
		this.putField("CRET_DTTM" , this.CRET_DTTM , "S", "N");
		this.putField("MDFY_USER" , this.MDFY_USER , "S", "N");
		this.putField("MDFY_DTTM" , this.MDFY_DTTM , "S", "N");
		this.putField("SHOW_SERL" , this.SHOW_SERL , "I", "N");
		this.putField("SHOW_REAL_ACNT" , this.SHOW_REAL_ACNT , "S", "N");
		this.putField("MIN_FEE" , this.MIN_FEE , "I", "N");
		this.putField("MAX_FEE" , this.MAX_FEE , "I", "N");
		this.putField("LINK_LIMIT" , this.LINK_LIMIT , "I", "N");
		
		this.putField("TAX_TYPE"  , this.TAX_TYPE  , "S", "N");
		this.putField("TAX_RATE"  , this.TAX_RATE  , "D", "N");
		this.putField("MIN_TAX" , this.MIN_TAX , "I", "N");
		this.putField("MAX_TAX" , this.MAX_TAX , "I", "N");
	}

	protected void fromXML() {
		this.EC_ID      = this.getFieldString("EC_ID");
		this.EC_NAME_CH = this.getFieldString("EC_NAME_CH");
		this.EC_NAME_EN = this.getFieldString("EC_NAME_EN");
		this.SORC_IP    = this.getFieldString("SORC_IP");
		this.FEE_TYPE   = this.getFieldString("FEE_TYPE");
		this.FEE_RATE   = this.getFieldDouble("FEE_RATE");
		this.STTS       = this.getFieldString("STTS");
		this.REAL_ACNT  = this.getFieldString("REAL_ACNT");
		this.ENTR_NO    = this.getFieldString("ENTR_NO");
		this.ENTR_ID    = this.getFieldString("ENTR_ID");
		this.CNTC       = this.getFieldString("CNTC");
		this.TEL        = this.getFieldString("TEL");
		this.MAIL       = this.getFieldString("MAIL");
		this.NOTE       = this.getFieldString("NOTE");
		this.CRET_USER  = this.getFieldString("CRET_USER");
		this.CRET_DTTM  = this.getFieldString("CRET_DTTM");
		this.MDFY_USER  = this.getFieldString("MDFY_USER");
		this.MDFY_DTTM  = this.getFieldString("MDFY_DTTM");
		this.SHOW_SERL  = this.getFieldInt("SHOW_SERL");
		this.SHOW_REAL_ACNT  = this.getFieldString("SHOW_REAL_ACNT");
		this.MIN_FEE  = this.getFieldInt("MIN_FEE");
		this.MAX_FEE  = this.getFieldInt("MAX_FEE");
		this.LINK_LIMIT  = this.getFieldInt("LINK_LIMIT");
		
		this.TAX_TYPE   = this.getFieldString("TAX_TYPE");
		this.TAX_RATE   = this.getFieldDouble("TAX_RATE");
		this.MIN_TAX  = this.getFieldInt("MIN_TAX");
		this.MAX_TAX  = this.getFieldInt("MAX_TAX");
	}
}
