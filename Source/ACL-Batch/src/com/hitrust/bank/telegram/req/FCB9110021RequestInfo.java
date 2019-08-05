/*
 * @(#)FCB9110021RequestInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req;

public class FCB9110021RequestInfo extends GenericHostRequestInfo {
	
	private String QryCode;

	public String getQryCode() {
		return QryCode;
	}

	public void setQryCode(String qryCode) {
		QryCode = qryCode;
	}
}
