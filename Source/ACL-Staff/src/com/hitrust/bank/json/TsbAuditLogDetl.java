/**
 * @(#) TsbAuditLogDetl.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/04/06, Yann
 * 	 1)First release
 *  v1.01, 2016/07/07, Yann
 *   1) 新增二階欄位
 *  
 */
package com.hitrust.bank.json;

import java.io.Serializable;

public class TsbAuditLogDetl implements Serializable {
	private static final long serialVersionUID = 1385220405925808603L;
	
	private String U;     //ecUser平台會員代號
	private String A;     //realAcnt實體帳號
	private String T;     //trnsLimt每筆自訂限額
	private String D;     //dayLimt每日自訂限額
	private String M;     //mnthLimt每月自訂限額
	private String stts;  //服務狀態
	private String fnctId;//功能代碼
	private String seq;   //v1.01,公告序號
	
	
	// =============== Getters & Setters ===============
	public String getU() {
		return U;
	}
	public void setU(String u) {
		U = u;
	}
	public String getA() {
		return A;
	}
	public void setA(String a) {
		A = a;
	}
	public String getT() {
		return T;
	}
	public void setT(String t) {
		T = t;
	}
	public String getD() {
		return D;
	}
	public void setD(String d) {
		D = d;
	}
	public String getM() {
		return M;
	}
	public void setM(String m) {
		M = m;
	}
	public String getStts() {
		return stts;
	}
	public void setStts(String stts) {
		this.stts = stts;
	}
	public String getFnctId() {
		return fnctId;
	}
	public void setFnctId(String fnctId) {
		this.fnctId = fnctId;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
}
