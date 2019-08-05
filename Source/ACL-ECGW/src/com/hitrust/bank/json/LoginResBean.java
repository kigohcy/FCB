/**
 * @(#) ACLinkResBeanOne.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/01/04, JeffLin
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.json;

public class LoginResBean {
	private String merchantId; 	// merchantId
	private String sign;	  	// sign
	private String txReqId;  	// txReqId
	private String uri0202;		//
	private String custId;
	
	// =============== Getter & Setter ===============
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTxReqId() {
		return txReqId;
	}
	public void setTxReqId(String txReqId) {
		this.txReqId = txReqId;
	}
	public String getUri0202() {
		return uri0202;
	}
	public void setUri0202(String uri0202) {
		this.uri0202 = uri0202;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
}
