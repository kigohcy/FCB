/*
 * @(#)FCB91970266ResponseInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res;

import java.util.List;

public class FCB91970266ResponseInfo  extends GenericHostResponseInfo {
	
	private List records;
	private String addressPostCode;
	private String address;
	private String comAddressPostCode;
	private String comAddress;
	private String custBiz;
	private String custSales;
	private String custType;
	private String sex;
	private String eduRecord;
	private String marriageStatus;
	private String OBUID;
	private String comType;
	private String riskFlag;
	private String warningFlag;
	
	//20190725 雙因子認證 Begin
	private String riskScore;
	private String riskLevel;
	private String riskChangeDate;
	private String TFATelNo;
	//20190725 雙因子認證 End
	
	public List getRecords() {
		return records;
	}
	public void setRecords(List records) {
		this.records = records;
	}
	public String getAddressPostCode() {
		return addressPostCode;
	}
	public void setAddressPostCode(String addressPostCode) {
		this.addressPostCode = addressPostCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getComAddressPostCode() {
		return comAddressPostCode;
	}
	public void setComAddressPostCode(String comAddressPostCode) {
		this.comAddressPostCode = comAddressPostCode;
	}
	public String getComAddress() {
		return comAddress;
	}
	public void setComAddress(String comAddress) {
		this.comAddress = comAddress;
	}
	public String getCustBiz() {
		return custBiz;
	}
	public void setCustBiz(String custBiz) {
		this.custBiz = custBiz;
	}
	public String getCustSales() {
		return custSales;
	}
	public void setCustSales(String custSales) {
		this.custSales = custSales;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEduRecord() {
		return eduRecord;
	}
	public void setEduRecord(String eduRecord) {
		this.eduRecord = eduRecord;
	}
	public String getMarriageStatus() {
		return marriageStatus;
	}
	public void setMarriageStatus(String marriageStatus) {
		this.marriageStatus = marriageStatus;
	}
	public String getOBUID() {
		return OBUID;
	}
	public void setOBUID(String oBUID) {
		OBUID = oBUID;
	}
	public String getComType() {
		return comType;
	}
	public void setComType(String comType) {
		this.comType = comType;
	}
	public String getRiskFlag() {
		return riskFlag;
	}
	public void setRiskFlag(String riskFlag) {
		this.riskFlag = riskFlag;
	}
	public String getWarningFlag() {
		return warningFlag;
	}
	public void setWarningFlag(String warningFlag) {
		this.warningFlag = warningFlag;
	}
	
	//20190725 雙因子認證 Begin
	public String getRiskScore() {
		return riskScore;
	}
	public void setRiskScore(String riskScore) {
		this.riskScore = riskScore;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getRiskChangeDate() {
		return riskChangeDate;
	}
	public void setRiskChangeDate(String riskChangeDate) {
		this.riskChangeDate = riskChangeDate;
	}
	public String getTFATelNo() {
		return TFATelNo;
	}

	public void setTFATelNo(String tFATelNo) {
		TFATelNo = tFATelNo;
	}
	//20190725 雙因子認證 End
	
}
