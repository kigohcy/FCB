package com.hitrust.bank.telegram.req;

import com.hitrust.telegram.HostRequestInfo;

public abstract class GenericHostRequestInfo implements HostRequestInfo {
	
	private String correlationID;
	// Header attributes
	protected String hTxID; 
	protected String hSystemKey; 
	protected String hTxSeqNo; 
	protected String hMsgSeqNo; 
	protected String hCltTimeStamp; 
	protected String hCustID; 
	protected String hUserID; 
	protected String hAcctNo; 
	protected String hMsgDirection;
	
	// Business attributes
	//protected Transaction transaction;
	//protected APLogin login;
	
	/**
	 * Get correlation id
	 */
	public String getCorrelationID() {
		return correlationID;
	}
	
	public void setCorrelationID(String str) {
		this.correlationID = str;
	}
	
	// Getter & Setter
	public String getHAcctNo() {
		return hAcctNo;
	}

	public void setHAcctNo(String acctNo) {
		hAcctNo = acctNo;
	}

	public String getHCltTimeStamp() {
		return hCltTimeStamp;
	}

	public void setHCltTimeStamp(String cltTimeStamp) {
		hCltTimeStamp = cltTimeStamp;
	}

	public String getHCustID() {
		return hCustID;
	}

	public void setHCustID(String custID) {
		hCustID = custID;
	}

	public String getHMsgDirection() {
		return hMsgDirection;
	}

	public void setHMsgDirection(String msgDirection) {
		hMsgDirection = msgDirection;
	}

	public String getHMsgSeqNo() {
		return hMsgSeqNo;
	}

	public void setHMsgSeqNo(String msgSeqNo) {
		hMsgSeqNo = msgSeqNo;
	}
	
	/**
	 * add by Arf 2011/11/15
	 */
	public void setHMsgSeqNo() {
		hMsgSeqNo = this.hTxSeqNo.substring(this.hTxSeqNo.length()-5);
	}

	public String getHSystemKey() {
		return hSystemKey;
	}

	public void setHSystemKey(String systemKey) {
		hSystemKey = systemKey;
	}

	public String getHTxID() {
		return hTxID;
	}

	public void setHTxID(String txID) {
		hTxID = txID;
	}

	public String getHTxSeqNo() {
		return hTxSeqNo;
	}

	public void setHTxSeqNo(String txSeqNo) {
		hTxSeqNo = txSeqNo;
	}

	public String getHUserID() {
		return hUserID;
	}

	public void setHUserID(String userID) {
		hUserID = userID;
	}

//	public APLogin getLogin() {
//		return login;
//	}
//
//	public void setLogin(APLogin login) {
//		this.login = login;
//	}
//
//	public Transaction getTransaction() {
//		return transaction;
//	}
//
//	public void setTransaction(Transaction transaction) {
//		this.transaction = transaction;
//	}

}
