/*
 * @(#)Host085083Record.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Description:
 *		085083 下行電文多筆欄位 JavaBean
 *
 * Modify History:
 * v1.00, 2016/04/22, HiTRUST
 *	1) First release
 */

package com.hitrust.bank.telegram.res;

/**
 * 085083 下行電文多筆欄位  Java Bean
 * @author HiTRUST
 */
public class AccountListRecord extends GenericHostResponseInfo {

	private String ACNT_NO;   //帳號
	private String ACNT_TYPE; //產品代號
	private String CURT_AMT;  //帳戶餘額
	/**
	 * 欄位 ACNT_NO 的 getter
	 * @return ACNT_NO 帳號
	 */
	public String getACNT_NO() {
		return ACNT_NO;
	}
	/**
	 * 欄位 ACNT_NO(帳號) 的 setter
	 * @param aCNT_NO 帳號
	 */
	public void setACNT_NO(String aCNT_NO) {
		this.ACNT_NO = aCNT_NO;
	}
	/**
	 * 欄位 ACNT_TYPE 的 getter
	 * @return ACNT_TYPE 產品代號
	 */
	public String getACNT_TYPE() {
		return ACNT_TYPE;
	}
	/**
	 * 欄位 ACNT_TYPE(產品代號) 的 setter
	 * @param aCNT_TYPE 產品代號
	 */
	public void setACNT_TYPE(String aCNT_TYPE) {
		this.ACNT_TYPE = aCNT_TYPE;
	}
	/**
	 * 欄位 CURT_AMT 的 getter
	 * @return CURT_AMT 帳戶餘額
	 */
	public String getCURT_AMT() {
		return CURT_AMT;
	}
	/**
	 * 欄位 CURT_AMT(帳戶餘額) 的 setter
	 * @param cURT_AMT 帳戶餘額
	 */
	public void setCURT_AMT(String cURT_AMT) {
		this.CURT_AMT = cURT_AMT;
	}
}
