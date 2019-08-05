/*
 * @(#)FCB91920YResponseInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res;


public class FCB91920YResponseInfo  extends GenericHostResponseInfo {
	
	private String busType;	//帳 務 別
	private String wsID;	//工作站代號
	private String hostSeqNo;	//中心交易序號
	private String txID;	//交易代號
	private String movResult;
	
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
	public String getMovResult() {
		return movResult;
	}
	public void setMovResult(String movResult) {
		this.movResult = movResult;
	}
}
