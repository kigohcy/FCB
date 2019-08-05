/**
 * @(#)AbstractCustData.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : SysParm base model
 * 
 * Modify History:
 *  v1.00, 2016/02/24, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

public class AbstractSysParm extends AclCommand implements Serializable {

	private static final long serialVersionUID = -8463159361032851856L;

	// =============== Table Attribute ===============
	private String parmCode;  // 參數名稱
	private String parmName;  // 參數說明
	private String parmValue; // 參數值
	
	// =============== Getter & Setter ===============
	public String getParmCode() {
		return this.parmCode;
	}
	public void setParmCode(String parmCode) {
		this.parmCode = parmCode;
	}
	public String getParmName() {
		return this.parmName;
	}
	public void setParmName(String parmName) {
		this.parmName = parmName;
	}
	public String getParmValue() {
		return this.parmValue;
	}
	public void setParmValue(String parmValue) {
		this.parmValue = parmValue;
	}
	
}
