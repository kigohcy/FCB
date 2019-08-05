/**
 * @(#) BaseLimt.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : BaseLimt bean
 * 
 * Modify History:
 *  v1.00, 2016/03/25, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class BaseLimt extends GenericBean {
	private static final long serialVersionUID = 968712265151342395L;
	
	public String GRAD;      //等級
	public Long TRNS_LIMT; //每筆限額
	public Long DAY_LIMT;  //每日限額
	public Long MNTH_LIMT; //每月限額

	public BaseLimt() {
		super();
	}

	public BaseLimt(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("BASE_LIMT");
		this.putField("GRAD"     , this.GRAD     , "S", "Y");
		this.putField("TRNS_LIMT", this.TRNS_LIMT, "L", "N");
		this.putField("DAY_LIMT" , this.DAY_LIMT , "L", "N");
		this.putField("MNTH_LIMT", this.MNTH_LIMT, "L", "N");
	}

	protected void fromXML() {
		this.GRAD      = this.getFieldString("GRAD");
		this.TRNS_LIMT = this.getFieldLong("TRNS_LIMT");
		this.DAY_LIMT  = this.getFieldLong("DAY_LIMT");
		this.MNTH_LIMT = this.getFieldLong("MNTH_LIMT");
	}
}
