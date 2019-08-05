/*
 * @(#)FCB91970363Record.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res;


public class FCB91970363Record{
	
	private String acctNo;
	private String iniDate;
	private String tAcctNo;
	private String belongBranch;
	private String natureType;
	private String ncsFlag;
	private String crossFlag;
	
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getIniDate() {
		return iniDate;
	}
	public void setIniDate(String iniDate) {
		this.iniDate = iniDate;
	}
	public String gettAcctNo() {
		return tAcctNo;
	}
	public void settAcctNo(String tAcctNo) {
		this.tAcctNo = tAcctNo;
	}
	public String getBelongBranch() {
		return belongBranch;
	}
	public void setBelongBranch(String belongBranch) {
		this.belongBranch = belongBranch;
	}
	public String getNatureType() {
		return natureType;
	}
	public void setNatureType(String natureType) {
		this.natureType = natureType;
	}
	public String getNcsFlag() {
		return ncsFlag;
	}
	public void setNcsFlag(String ncsFlag) {
		this.ncsFlag = ncsFlag;
	}
	public String getCrossFlag() {
		return crossFlag;
	}
	public void setCrossFlag(String crossFlag) {
		this.crossFlag = crossFlag;
	}
}
