/**
 * @(#) Accounts.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/13, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.bean;

public class Accounts {

	private String acnt; // 連結帳號
	private String stts; // 綁定狀態
	
	public Accounts(String acnt, String stts) {
		this.acnt = acnt;
		this.stts = stts;
	}
	
	public String getAcnt() {
		return acnt;
	}
	public void setAcnt(String acnt) {
		this.acnt = acnt;
	}
	public String getStts() {
		return stts;
	}
	public void setStts(String stts) {
		this.stts = stts;
	}
	
}
