/*
 * @(#)Host067050ResponseInfo.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Description:
 *		067050 下行電文 JavaBean
 *
 * Modify History:
 * v1.00, 2016/04/22, HiTRUST
 *	1) First release
 */

package com.hitrust.bank.telegram.res;

/**
 * 067050 下行電文欄位 Java Bean
 * @author HiTRUST
 */
public class NotificationHostMsgResponseInfo extends HeaderHostResponseInfo {

	private String OUTPUT_CODE; //Output Code
	private String MovResult;
	private String statusCode;
	
	public String getOUTPUT_CODE() {
		return OUTPUT_CODE;
	}
	public void setOUTPUT_CODE(String oUTPUT_CODE) {
		OUTPUT_CODE = oUTPUT_CODE;
	}
	public String getMovResult() {
		return MovResult;
	}
	public void setMovResult(String movResult) {
		MovResult = movResult;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}
