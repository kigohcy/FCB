/**
 * @(#) ACLSysFnct.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/18, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.response;

public class AbstractResponseBean {
	
	private String MSG_NO = "";		// 訊息序號
	private String RTN_CODE = "";	// 回應代號
	private String RTN_MSG = "";	// 回應訊息
	private String EC_ID = "";		// 平台代碼
	private String EC_USER = "";	// 平台會員代號
	private String RTN_DIGEST = "";	// 訊息雜湊值
	
	/**
	 * @param url
	 * @param name
	 */
	public AbstractResponseBean() {
	}

	/**
	 * @return the MSG_NO
	 */
	public String getMSG_NO() {
		return this.MSG_NO;
	}

	/**
	 * @param msgNo the MSG_NO to set
	 */
	public void setMSG_NO(String msgNo) {
		this.MSG_NO = msgNo;
	}

	/**
	 * @return the RTN_CODE
	 */
	public String getRTN_CODE() {
		return this.RTN_CODE;
	}

	/**
	 * @param rtnCode the RTN_CODE to set
	 */
	public void setRTN_CODE(String rtnCode) {
		this.RTN_CODE = rtnCode;
	}

	/**
	 * @return the RTN_MSG
	 */
	public String getRTN_MSG() {
		return this.RTN_MSG;
	}

	/**
	 * @param rtnMsg the RTN_MSG to set
	 */
	public void setRTN_MSG(String rtnMsg) {
		this.RTN_MSG = rtnMsg;
	}

	/**
	 * @return the EC_ID
	 */
	public String getEC_ID() {
		return this.EC_ID;
	}

	/**
	 * @param ecId the EC_ID to set
	 */
	public void setEC_ID(String ecId) {
		this.EC_ID = ecId;
	}

	/**
	 * @return the EC_USER
	 */
	public String getEC_USER() {
		return this.EC_USER;
	}

	/**
	 * @param ecUser the EC_USER to set
	 */
	public void setEC_USER(String ecUser) {
		this.EC_USER = ecUser;
	}

	/**
	 * @return the RTN_DIGEST
	 */
	public String getRTN_DIGEST() {
		return this.RTN_DIGEST;
	}

	/**
	 * @param rtnDigest the RTN_DIGEST to set
	 */
	public void setRTN_DIGEST(String rtnDigest) {
		this.RTN_DIGEST = rtnDigest;
	}

}


