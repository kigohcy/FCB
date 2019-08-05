/*
 * @(#)FCB91103WResponseInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res;


public class FCB91103WResponseInfo  extends GenericHostResponseInfo {
	
	private String busType;	//帳 務 別
	private String wsID;	//工作站代號
	private String hostSeqNo;	//中心交易序號
	private String txID;	//交易代號
	private String payerAcctNo;
	private String txDate;
	private String txTime;
	private String payerBank;
	private String custID;
	private String txSeqNo;
	private String txSubSeq;
	private String payeeBank;
	private String payeeAcctNo;
	private String txAmt;
	private String serviceFee;
	private String sign;
	private String pBBalAmt;
	private String sign1;
	private String avlBalAmt;
	private String cMemo;
	private String sNo;
	private String delayFlag;
	private String fiscSeqNo;
	private String hostSeqNo1;
	
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
	public String getPayerAcctNo() {
		return payerAcctNo;
	}
	public void setPayerAcctNo(String payerAcctNo) {
		this.payerAcctNo = payerAcctNo;
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
	public String getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
	}
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String getTxSeqNo() {
		return txSeqNo;
	}
	public void setTxSeqNo(String txSeqNo) {
		this.txSeqNo = txSeqNo;
	}
	public String getTxSubSeq() {
		return txSubSeq;
	}
	public void setTxSubSeq(String txSubSeq) {
		this.txSubSeq = txSubSeq;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}
	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}
	public String getTxAmt() {
		return txAmt;
	}
	public void setTxAmt(String txAmt) {
		this.txAmt = txAmt;
	}
	public String getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getpBBalAmt() {
		return pBBalAmt;
	}
	public void setpBBalAmt(String pBBalAmt) {
		this.pBBalAmt = pBBalAmt;
	}
	public String getSign1() {
		return sign1;
	}
	public void setSign1(String sign1) {
		this.sign1 = sign1;
	}
	public String getAvlBalAmt() {
		return avlBalAmt;
	}
	public void setAvlBalAmt(String avlBalAmt) {
		this.avlBalAmt = avlBalAmt;
	}
	public String getcMemo() {
		return cMemo;
	}
	public void setcMemo(String cMemo) {
		this.cMemo = cMemo;
	}
	public String getsNo() {
		return sNo;
	}
	public void setsNo(String sNo) {
		this.sNo = sNo;
	}
	public String getDelayFlag() {
		return delayFlag;
	}
	public void setDelayFlag(String delayFlag) {
		this.delayFlag = delayFlag;
	}
	public String getFiscSeqNo() {
		return fiscSeqNo;
	}
	public void setFiscSeqNo(String fiscSeqNo) {
		this.fiscSeqNo = fiscSeqNo;
	}
	public String getHostSeqNo1() {
		return hostSeqNo1;
	}
	public void setHostSeqNo1(String hostSeqNo1) {
		this.hostSeqNo1 = hostSeqNo1;
	}
}
