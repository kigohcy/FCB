/*
 * @(#)FCB91970363RequestInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req;

public class FCB91970363RequestInfo extends GenericHostRequestInfo {
	
	private String OBUCD;
	private String CustID;
	private String CuCode;
	private String RptNo;
	private String InqType;
	
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
	public String getCuCode() {
		return CuCode;
	}
	public void setCuCode(String cuCode) {
		CuCode = cuCode;
	}
	public String getRptNo() {
		return RptNo;
	}
	public void setRptNo(String rptNo) {
		RptNo = rptNo;
	}
	public String getInqType() {
		return InqType;
	}
	public void setInqType(String inqType) {
		InqType = inqType;
	}
}
