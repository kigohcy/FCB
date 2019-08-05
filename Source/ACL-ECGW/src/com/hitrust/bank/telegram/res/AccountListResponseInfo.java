/*
 * @(#)Host085083ResponseInfo.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Description:
 *		085083 下行電文 JavaBean
 *
 * Modify History:
 * v1.00, 2016/04/22, HiTRUST
 *	1) First release
 */

package com.hitrust.bank.telegram.res;

import java.util.ArrayList;
import java.util.List;

/**
 * 085083 下行電文欄位 Java Bean
 * @author HiTRUST
 */
public class AccountListResponseInfo extends HeaderHostResponseInfo {

	private String OUTPUT_CODE; //Output Code
	private List records;
	private String statusCode;
	/**
	 * 欄位:OUTPUT_CODE 的 getter
	 * @return OUTPUT_CODE Output Code
	 */
	public String getOUTPUT_CODE() {
		return this.OUTPUT_CODE;
	}
	/**
	 * 欄位:OUTPUT_CODE(Output Code) 的 setter<br>
	 * 備註:A/C Link 判斷必須為 '03' 才視為成功
	 * @param oUTPUT_CODE Output Code
	 */
	public void setOUTPUT_CODE(String oUTPUT_CODE) {
		this.OUTPUT_CODE = oUTPUT_CODE;
	}
	
	public List getRecords() {
		return records;
	}
	public void setRecords(List records) {
		this.records = records;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}
