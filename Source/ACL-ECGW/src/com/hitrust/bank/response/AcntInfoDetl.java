/**
 * @(#) AcntInfoDetl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 查詢可使用連結帳戶帳號資訊明細 bean
 * 
 * Modify History:
 *  v1.00, 2016/03/30, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.response;

public class AcntInfoDetl {
	
	private String INDT_ACNT  = "";   //帳號識別碼
	private String ACNT_STTS = "";    //狀態
	private String AVA_BALANCE = "";  //可用餘額
	private String ACNT_BALANCE = ""; //存款餘額
	
	
	// =============== Getter & Setter ===============
	public String getINDT_ACNT() {
		return INDT_ACNT;
	}
	
	public void setINDT_ACNT(String indtAcnt) {
		INDT_ACNT = indtAcnt;
	}
	
	public String getACNT_STTS() {
		return ACNT_STTS;
	}
	
	public void setACNT_STTS(String acntStts) {
		ACNT_STTS = acntStts;
	}
	
	public String getAVA_BALANCE() {
		return AVA_BALANCE;
	}
	
	public void setAVA_BALANCE(String avaBalance) {
		AVA_BALANCE = avaBalance;
	}
	
	public String getACNT_BALANCE() {
		return ACNT_BALANCE;
	}
	
	public void setACNT_BALANCE(String acntBalance) {
		ACNT_BALANCE = acntBalance;
	}
}
