/**
 * @(#) SessionTempTable.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History: 帳號連結綁定 Table DB
 *   v1.00, 2016/04/29, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;

public class SessionTempTable extends GenericBean {

	private static final long serialVersionUID = 9000860972414422827L;
	
	// =============== Table Attribute ===============
	public String SESSION_KEY;
	public String SESSION_DATA;
	
	@Override
	protected void toXML() {
		this.putTableName("SESSION_TEMP_TABLE");
		this.putField("SESSION_KEY", 	this.SESSION_KEY, 	"S", "Y");
		this.putField("SESSION_DATA", 	this.SESSION_DATA, 	"S", "Y");
		
	}

	@Override
	protected void fromXML() {
		this.SESSION_KEY = this.getFieldString("SESSION_KEY");
		this.SESSION_DATA = this.getFieldString("SESSION_DATA");
		
	}

}
