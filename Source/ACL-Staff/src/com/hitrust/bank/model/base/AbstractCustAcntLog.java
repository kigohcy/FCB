/**
 * @(#)AbstractCustAcntLog.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcntLog base model
 * 
 * Modify History:
 *  v1.00, 2016/02/05, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;

public class AbstractCustAcntLog extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = 4099858990769878699L;
	
	// =============== Table Attribute ===============
	private String logNo;	 // 序號
	private String custId;	 // 身分證字號
	private String ecId;	 // 平台代碼
	private String ecUser;   // 平台會員代碼
	private String realAcnt; // 平台會員代碼
	private Date cretDttm;	 // 異動時間
	private String grad;	 // 等級
	private String gradType; // 身分認證方式
	private String stts;	 // 行狀態
	private String errCode;  // 錯誤代碼
	private String hostCode; // 主機回應代碼
	private String custSerl; // 會員服務序號
	private String acntIndt; // 帳號識別碼
	private String execSrc;	 // 執行來源
	private String execUser; // 執行人員
	private String ecMsgNo;	 // 平台訊息序號 
	private String ip;		 // IP位置
	
	// =============== Getter & Setter ===============
	public String getLogNo() {
		return this.logNo;
	}
	public void setLogNo(String logNo) {
		this.logNo = logNo;
	}
	public String getCustId() {
		return this.custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getEcId() {
		return this.ecId;
	}
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	public String getEcUser() {
		return this.ecUser;
	}
	public void setEcUser(String ecUser) {
		this.ecUser = ecUser;
	}
	public String getRealAcnt() {
		return this.realAcnt;
	}
	public void setRealAcnt(String realAcnt) {
		this.realAcnt = realAcnt;
	}
	public Date getCretDttm() {
		return this.cretDttm;
	}
	public void setCretDttm(Date cretDttm) {
		this.cretDttm = cretDttm;
	}
	public String getGrad() {
		return this.grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}
	public String getGradType() {
		return this.gradType;
	}
	public void setGradType(String gradType) {
		this.gradType = gradType;
	}
	public String getStts() {
		return this.stts;
	}
	public void setStts(String stts) {
		this.stts = stts;
	}
	public String getErrCode() {
		return this.errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getHostCode() {
		return this.hostCode;
	}
	public void setHostCode(String hostCode) {
		this.hostCode = hostCode;
	}
	public String getCustSerl() {
		return this.custSerl;
	}
	public void setCustSerl(String custSerl) {
		this.custSerl = custSerl;
	}
	public String getAcntIndt() {
		return this.acntIndt;
	}
	public void setAcntIndt(String acntIndt) {
		this.acntIndt = acntIndt;
	}
	public String getExecSrc() {
		return this.execSrc;
	}
	public void setExecSrc(String execSrc) {
		this.execSrc = execSrc;
	}
	public String getExecUser() {
		return this.execUser;
	}
	public void setExecUser(String execUser) {
		this.execUser = execUser;
	}
	public String getEcMsgNo() {
		return this.ecMsgNo;
	}
	public void setEcMsgNo(String ecMsgNo) {
		this.ecMsgNo = ecMsgNo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

}
