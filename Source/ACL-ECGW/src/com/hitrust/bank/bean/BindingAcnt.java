/**
 * @(#) BindingAcnt.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/13, Eason Hsu
 *    1) JIRA-Number, First release
 *	 v1.01, 2017/09/05, Eason Hsu
 *	  1) TSBACL-159, [Fortify] J2EE Bad Practices: Non-Serializable Object Stored in Session
 */

package com.hitrust.bank.bean;

import java.io.Serializable;
import java.util.List;

public class BindingAcnt implements Serializable {
	
	// v1.01, 修正 Fortify 白箱掃描(J2EE Bad Practices: Non-Serializable Object Stored in Session)
	private static final long serialVersionUID = 1053262612874651047L;
	
	private String idetityAuthType;// 身分認證方式 
	private String linkAcnt;	   // 連結綁定帳號
	private String tlxNo;		   // 行動電話
	private String emailAddr;	   // 電子郵件
	private List<Accounts> acnts;  // 連結綁定帳號清單
	
	// =============== Getter & Setter ===============
	public String getIdetityAuthType() {
		return idetityAuthType;
	}
	public void setIdetityAuthType(String idetityAuthType) {
		this.idetityAuthType = idetityAuthType;
	}
	public String getLinkAcnt() {
		return linkAcnt;
	}
	public void setLinkAcnt(String linkAcnt) {
		this.linkAcnt = linkAcnt;
	}
	public String getTlxNo() {
		return tlxNo;
	}
	public void setTlxNo(String tlxNo) {
		this.tlxNo = tlxNo;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public List<Accounts> getAcnts() {
		return acnts;
	}
	public void setAcnts(List<Accounts> acnts) {
		this.acnts = acnts;
	}
	
}
