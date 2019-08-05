/**
 * @(#) CustPltf.java
 *
 * Directions: 會員平台資料檔
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/30, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;

public class CustPltf extends GenericBean {

	private static final long serialVersionUID = -8305219030856724112L;
	
	// =============== Table Attribute ===============
	public String CUST_ID;	 // 身分證字號
	public String EC_ID;	 // 平台代碼
	public String STTS;		 // 狀態
	public String STTS_DTTM; // 狀態異動時間
	public String CRET_DTTM; // 建立時間
	public String MDFY_USER; // 最後異動人員

	@Override
	protected void toXML() {
		this.putTableName("CUST_PLTF");
		this.putField("CUST_ID", 	 this.CUST_ID, 		"S", "Y");
		this.putField("EC_ID", 	 	 this.EC_ID, 		"S", "Y");
		this.putField("STTS", 	 	 this.STTS, 		"S", "N");
		this.putField("STTS_DTTM", 	 this.STTS_DTTM, 	"S", "N");
		this.putField("CRET_DTTM", 	 this.CRET_DTTM, 	"S", "N");
		this.putField("MDFY_USER", 	 this.MDFY_USER, 	"S", "N");

	}

	@Override
	protected void fromXML() {
		this.CUST_ID   = this.getFieldString("CUST_ID");
		this.EC_ID 	   = this.getFieldString("EC_ID");
		this.STTS 	   = this.getFieldString("STTS");
		this.STTS_DTTM = this.getFieldString("STTS_DTTM");
		this.CRET_DTTM = this.getFieldString("CRET_DTTM");
		this.MDFY_USER = this.getFieldString("MDFY_USER");
	}

}
