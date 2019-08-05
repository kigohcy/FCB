/**
 * @(#) EcReqBean.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/05/18, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.bean;

public class EcReqBean {
	
	private String msgNo = "";		// 訊息序號
	private String ecId = "";		// 平台代碼
	private String ecUser = "";		// 平台會員代號
	private String certSn = "";		// 憑證序號
	private String signValue = "";	// 簽章值
	
	// ========== Getter & Setter ==========
	public String getMsgNo() {
		return msgNo;
	}
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}
	public String getEcId() {
		return ecId;
	}
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	public String getEcUser() {
		return ecUser;
	}
	public void setEcUser(String ecUser) {
		this.ecUser = ecUser;
	}
	public String getCertSn() {
		return certSn;
	}
	public void setCertSn(String certSn) {
		this.certSn = certSn;
	}
	public String getSignValue() {
		return signValue;
	}
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}
	
}
