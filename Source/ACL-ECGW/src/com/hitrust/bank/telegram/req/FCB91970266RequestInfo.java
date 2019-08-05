/*
 * @(#)FCB91970266RequestInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req;

public class FCB91970266RequestInfo extends GenericHostRequestInfo {
	
	private String OBUCD;
	private String CustID;
	private String RepeatSeq;
	private String InqNo;
	
	public String getOBUCD() {
		return OBUCD;
	}
	public void setOBUCD(String oBUCD) {
		OBUCD = oBUCD;
	}
	public String getCustID() {
		return CustID;
	}
	public void setCustID(String custID) {
		CustID = custID;
	}
	public String getRepeatSeq() {
		return RepeatSeq;
	}
	public void setRepeatSeq(String repeatSeq) {
		RepeatSeq = repeatSeq;
	}
	public String getInqNo() {
		return InqNo;
	}
	public void setInqNo(String inqNo) {
		InqNo = inqNo;
	}
}
