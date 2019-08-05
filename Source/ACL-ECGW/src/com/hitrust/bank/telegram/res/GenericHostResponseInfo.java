package com.hitrust.bank.telegram.res;

import com.hitrust.telegram.HostResponseInfo;

public abstract class GenericHostResponseInfo implements HostResponseInfo {
	
	private String msgCode;//add by Arf for check return code
	// Header attributes
	private String htxID;
	private String hsystemKey;
	private String htxSeqNo;
	private String hmsgSeqNo;
	private String hsvrTimeStamp;
	private String hmsgDirection;
	private String hsystem;
	private String hstatusCode;
	private String hstatusDesc;
	
	// 20190619 Add 交易失敗時記錄上下行電文 Begin
	private String tiTa; // 上行電文
	private String toTa; // 下行電文
	// 20190619 Add 交易失敗時記錄上下行電文 End

	// Getter & Setter
	public String getHmsgDirection() {
		return hmsgDirection;
	}

	public void setHmsgDirection(String hmsgDirection) {
		this.hmsgDirection = hmsgDirection;
	}

	public String getHmsgSeqNo() {
		return hmsgSeqNo;
	}

	public void setHmsgSeqNo(String hmsgSeqNo) {
		this.hmsgSeqNo = hmsgSeqNo;
	}

	public String getHstatusCode() {
		return hstatusCode;
	}

	public void setHstatusCode(String hstatusCode) {
		this.hstatusCode = hstatusCode;
	}

	public String getHstatusDesc() {
		return hstatusDesc;
	}

	public void setHstatusDesc(String hstatusDesc) {
		this.hstatusDesc = hstatusDesc;
	}

	public String getHsvrTimeStamp() {
		return hsvrTimeStamp;
	}

	public void setHsvrTimeStamp(String hsvrTimeStamp) {
		this.hsvrTimeStamp = hsvrTimeStamp;
	}

	public String getHsystem() {
		return hsystem;
	}

	public void setHsystem(String hsystem) {
		this.hsystem = hsystem;
	}

	public String getHsystemKey() {
		return hsystemKey;
	}

	public void setHsystemKey(String hsystemKey) {
		this.hsystemKey = hsystemKey;
	}

	public String getHtxID() {
		return htxID;
	}

	public void setHtxID(String htxID) {
		this.htxID = htxID;
	}

	public String getHtxSeqNo() {
		return htxSeqNo;
	}

	public void setHtxSeqNo(String htxSeqNo) {
		this.htxSeqNo = htxSeqNo;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	//20190619 Add 交易失敗時記錄上下行電文 Begin
	public String getTiTa() {
		return tiTa;
	}

	public void setTiTa(String tiTa) {
		this.tiTa = tiTa;
	}

	public String getToTa() {
		return toTa;
	}

	public void setToTa(String toTa) {
		this.toTa = toTa;
	}
	//20190619 Add 交易失敗時記錄上下行電文 End
   
	 
}
