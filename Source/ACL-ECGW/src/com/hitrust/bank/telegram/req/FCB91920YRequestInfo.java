/*
 * @(#)FCB91920YRequestInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req;

public class FCB91920YRequestInfo extends GenericHostRequestInfo {
	
	private String DesAccNo;
	private String MovCode;
	private String ApplyCat;
	private String AgencyCode;
	private String CustNo;
	private String InsuredUnitCode;
	private String CompCustID;
	private String InsuredCustID;
	private String PhoneNo;
	private String SeqNo;
	private String ChannelType;
	private String TPCInAccNo;
	
	public String getDesAccNo() {
		return DesAccNo;
	}
	public void setDesAccNo(String desAccNo) {
		DesAccNo = desAccNo;
	}
	public String getMovCode() {
		return MovCode;
	}
	public void setMovCode(String movCode) {
		MovCode = movCode;
	}
	public String getApplyCat() {
		return ApplyCat;
	}
	public void setApplyCat(String applyCat) {
		ApplyCat = applyCat;
	}
	public String getAgencyCode() {
		return AgencyCode;
	}
	public void setAgencyCode(String agencyCode) {
		AgencyCode = agencyCode;
	}
	public String getCustNo() {
		return CustNo;
	}
	public void setCustNo(String custNo) {
		CustNo = custNo;
	}
	public String getInsuredUnitCode() {
		return InsuredUnitCode;
	}
	public void setInsuredUnitCode(String insuredUnitCode) {
		InsuredUnitCode = insuredUnitCode;
	}
	public String getCompCustID() {
		return CompCustID;
	}
	public void setCompCustID(String compCustID) {
		CompCustID = compCustID;
	}
	public String getInsuredCustID() {
		return InsuredCustID;
	}
	public void setInsuredCustID(String insuredCustID) {
		InsuredCustID = insuredCustID;
	}
	public String getPhoneNo() {
		return PhoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		PhoneNo = phoneNo;
	}
	public String getSeqNo() {
		return SeqNo;
	}
	public void setSeqNo(String seqNo) {
		SeqNo = seqNo;
	}
	public String getChannelType() {
		return ChannelType;
	}
	public void setChannelType(String channelType) {
		ChannelType = channelType;
	}
	public String getTPCInAccNo() {
		return TPCInAccNo;
	}
	public void setTPCInAccNo(String tPCInAccNo) {
		TPCInAccNo = tPCInAccNo;
	}
}
