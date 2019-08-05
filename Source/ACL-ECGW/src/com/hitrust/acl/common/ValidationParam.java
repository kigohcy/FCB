/**
 * @(#) ValidationParam.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/01, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.acl.common;

import java.util.Map;

public class ValidationParam {
	
	private int paramSize;	// 參數數量
	private Map<String, Map<String, String>> fields;
	private Map<String, String> signFields;
	private Map<String, String> digestFields;
	private Map<String, String> acntInfoFields;
	
	public ValidationParam() {}

	public ValidationParam(int paramSize, Map<String, Map<String, String>> fields, Map<String, String> signFields,
			Map<String, String> digestFields, Map<String, String> acntInfoFields) {
		super();
		this.paramSize = paramSize;
		this.fields = fields;
		this.signFields = signFields;
		this.digestFields = digestFields;
		this.acntInfoFields = acntInfoFields;
	}



	// =============== Getter & Setter ===============
	public int getParamSize() {
		return paramSize;
	}
	public void setParamSize(int paramSize) {
		this.paramSize = paramSize;
	}
	public Map<String, Map<String, String>> getFields() {
		return fields;
	}
	public void setFields(Map<String, Map<String, String>> fields) {
		this.fields = fields;
	}
	public Map<String, String> getSignFields() {
		return signFields;
	}
	public void setSignFields(Map<String, String> signFields) {
		this.signFields = signFields;
	}
	public Map<String, String> getDigestFields() {
		return digestFields;
	}
	public void setDigestFields(Map<String, String> digestFields) {
		this.digestFields = digestFields;
	}
	public Map<String, String> getAcntInfoFields() {
		return acntInfoFields;
	}
	public void setAcntInfoFields(Map<String, String> acntInfoFields) {
		this.acntInfoFields = acntInfoFields;
	}
	
}
