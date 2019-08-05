/*
 * @(#)FCB91103WRequestInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.req;

public class FCB91103WRequestInfo extends GenericHostRequestInfo {
	
	private String PayerAcctNo;
	private String CustID;
	private String TxnDate;
	private String TxnSeqNo;
	private String TxnSubSeq;
	private String TxnAmt;
	private String EcFlag;
	public String getPayerAcctNo() {
		return PayerAcctNo;
	}
	public void setPayerAcctNo(String payerAcctNo) {
		PayerAcctNo = payerAcctNo;
	}
	public String getCustID() {
		return CustID;
	}
	public void setCustID(String custID) {
		CustID = custID;
	}
	public String getTxnDate() {
		return TxnDate;
	}
	public void setTxnDate(String txnDate) {
		TxnDate = txnDate;
	}
	public String getTxnSeqNo() {
		return TxnSeqNo;
	}
	public void setTxnSeqNo(String txnSeqNo) {
		TxnSeqNo = txnSeqNo;
	}
	public String getTxnSubSeq() {
		return TxnSubSeq;
	}
	public void setTxnSubSeq(String txnSubSeq) {
		TxnSubSeq = txnSubSeq;
	}
	public String getTxnAmt() {
		return TxnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		TxnAmt = txnAmt;
	}
	public String getEcFlag() {
		return EcFlag;
	}
	public void setEcFlag(String ecFlag) {
		EcFlag = ecFlag;
	}
}
