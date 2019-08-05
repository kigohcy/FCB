/*
 * @(#)FCB91970363ResponseInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res;

import java.util.List;

public class FCB91970363ResponseInfo  extends GenericHostResponseInfo {
	
	private String busType;	//帳 務 別
	private String wsID;	//工作站代號
	private String hostSeqNo;	//中心交易序號
	private String txID;	//交易代號
	private List records;
	private String txDate;
	private String txTime;
	private String custID;
	private String eCode;
	private String custName;

	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getWsID() {
		return wsID;
	}
	public void setWsID(String wsID) {
		this.wsID = wsID;
	}
	public String getHostSeqNo() {
		return hostSeqNo;
	}
	public void setHostSeqNo(String hostSeqNo) {
		this.hostSeqNo = hostSeqNo;
	}
	public String getTxID() {
		return txID;
	}
	public void setTxID(String txID) {
		this.txID = txID;
	}
	public List getRecords() {
		return records;
	}
	public void setRecords(List records) {
		this.records = records;
	}
	public String getTxDate() {
		return txDate;
	}
	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}
	public String getTxTime() {
		return txTime;
	}
	public void setTxTime(String txTime) {
		this.txTime = txTime;
	}
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
}
