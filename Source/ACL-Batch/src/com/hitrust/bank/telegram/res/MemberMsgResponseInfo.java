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
public class MemberMsgResponseInfo extends HeaderHostResponseInfo {
	
	private String OUTPUT_CODE; //Output Code
	private String NAME;        //姓名
	private String TELE;        //行動電話
	private String STATUS;      //客戶狀態
	private String EMAIL;       //EMAIL-ADDR
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
	/**
	 * 欄位:NAME 的 getter
	 * @return NAME 姓名
	 */
	public String getNAME() {
		return this.NAME;
	}
	/**
	 * 欄位:NAME(姓名) 的 setter
	 * @param nAME 姓名
	 */
	public void setNAME(String nAME) {
		this.NAME = nAME;
	}
	/**
	 * 欄位:TELE 的 getter
	 * @return TELE 行動電話
	 */
	public String getTELE() {
		return this.TELE;
	}
	/**
	 * 欄位:TELE(行動電話) 的 setter
	 * @param tELE 行動電話
	 */
	public void setTELE(String tELE) {
		this.TELE = tELE;
	}
	/**
	 * 欄位:STATUS 的 getter
	 * @return STATUS 客戶狀態
	 */
	public String getSTATUS() {
		return this.STATUS;
	}
	/**
	 * 欄位:STATUS(客戶狀態) 的 setter<br>
	 * 備註:"A/C Link 只針對 000:正常 取用資料
	 * @param sTATUS 客戶狀態
	 */
	public void setSTATUS(String sTATUS) {
		this.STATUS = sTATUS;
	}
	/**
	 * 欄位:EMAIL 的 getter
	 * @return EMAIL EMAIL-ADDR
	 */
	public String getEMAIL() {
		return this.EMAIL;
	}
	/**
	 * 欄位:EMAIL(EMAIL-ADDR) 的 setter
	 * @param eMAIL EMAIL-ADDR
	 */
	public void setEMAIL(String eMAIL) {
		this.EMAIL = eMAIL;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}
