/**
 * @(#) SysParm.java
 *
 * Directions: 系統參數檔
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

public class SysParm extends GenericBean {

	private static final long serialVersionUID = -4821929593390455732L;
	
	// =============== Table Attribute ===============
	public String PARM_CODE;  // 參數名稱
	public String PARM_NAME;  // 參數說明
	public String PARM_VALUE; // 參數值

	@Override
	protected void toXML() {
		this.putTableName("SYS_PARM");
		this.putField("PARM_CODE",  this.PARM_CODE,  "S", "Y");
		this.putField("PARM_NAME",  this.PARM_NAME,  "S", "N");
		this.putField("PARM_VALUE", this.PARM_VALUE, "S", "N");
		
	}

	@Override
	protected void fromXML() {
		this.PARM_CODE  = this.getFieldString("PARM_CODE");
		this.PARM_NAME  = this.getFieldString("PARM_NAME");
		this.PARM_VALUE = this.getFieldString("PARM_VALUE");
		
	}

}
