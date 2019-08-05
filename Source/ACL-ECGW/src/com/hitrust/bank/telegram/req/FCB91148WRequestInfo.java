/*
 * @(#)FCB91148WRequestInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req;

public class FCB91148WRequestInfo extends GenericHostRequestInfo {
	
	private String PayerBank;
	private String PayerAcctNo;
	private String PayerID;
	private String TxSeqNo;
	private String TxSubSeq;
	private String PayeeBank;
	private String PayeeAcctNo;
	private String TxAmt;
	private String PmtType;
	private String SNo;
	private String CMemo;
	private String DelayFlag;
	private String ClsDate;
	private String TransType;
	private String InMemo;
	private String EcFlag;
	
	
	public String getPayerBank() {
		return PayerBank;
	}
	public void setPayerBank(String payerBank) {
		PayerBank = payerBank;
	}
	public String getPayerAcctNo() {
		return PayerAcctNo;
	}
	public void setPayerAcctNo(String payerAcctNo) {
		PayerAcctNo = payerAcctNo;
	}
	public String getPayerID() {
		return PayerID;
	}
	public void setPayerID(String payerID) {
		PayerID = payerID;
	}
	public String getTxSeqNo() {
		return TxSeqNo;
	}
	public void setTxSeqNo(String txSeqNo) {
		TxSeqNo = txSeqNo;
	}
	public String getTxSubSeq() {
		return TxSubSeq;
	}
	public void setTxSubSeq(String txSubSeq) {
		TxSubSeq = txSubSeq;
	}
	public String getPayeeBank() {
		return PayeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		PayeeBank = payeeBank;
	}
	public String getPayeeAcctNo() {
		return PayeeAcctNo;
	}
	public void setPayeeAcctNo(String payeeAcctNo) {
		PayeeAcctNo = payeeAcctNo;
	}
	public String getTxAmt() {
		return TxAmt;
	}
	public void setTxAmt(String txAmt) {
		TxAmt = txAmt;
	}
	public String getPmtType() {
		return PmtType;
	}
	public void setPmtType(String pmtType) {
		PmtType = pmtType;
	}
	public String getSNo() {
		return SNo;
	}
	public void setSNo(String sNo) {
		SNo = sNo;
	}
	public String getCMemo() {
		return CMemo;
	}
	public void setCMemo(String cMemo) {
		CMemo = cMemo;
	}
	public String getDelayFlag() {
		return DelayFlag;
	}
	public void setDelayFlag(String delayFlag) {
		DelayFlag = delayFlag;
	}
	public String getClsDate() {
		return ClsDate;
	}
	public void setClsDate(String clsDate) {
		ClsDate = clsDate;
	}
	public String getTransType() {
		return TransType;
	}
	public void setTransType(String transType) {
		TransType = transType;
	}
	public String getInMemo() {
		return InMemo;
	}
	public void setInMemo(String inMemo) {
		InMemo = inMemo;
	}
	public String getEcFlag() {
		return EcFlag;
	}
	public void setEcFlag(String ecFlag) {
		EcFlag = ecFlag;
	}
}
