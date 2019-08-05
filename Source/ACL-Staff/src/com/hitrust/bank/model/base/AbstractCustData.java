/**
 * @(#)AbstractCustData.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustData base model
 * 
 * Modify History:
 *  v1.00, 2016/02/05, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.DownloadCommand;

public class AbstractCustData extends DownloadCommand implements Serializable {
	
	private static final long serialVersionUID = 6003764564864964941L;
	
	// =============== Table Attribute ===============
	private String custId;	 // 身分證字號
	private String custName; // 客戶姓名
	private String custSerl; // 客戶姓名
	private String tel;		 // 會員服務序號
	private String mail;	 // 電子郵件
	private String vrsn;	 // 條款版本
	private String stts;	 // 狀態
	private Date sttsDttm;	 // 狀態異動時間
	private Date cretDttm;	 // 建立時間 
	private String mdfyUser; // 最後異動人員
	private Date mdfyDttm;   // 最後異動時間
	
	// =============== Getter & Setter ===============
	public String getCustId() {
		return this.custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustName() {
		return this.custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustSerl() {
		return this.custSerl;
	}
	public void setCustSerl(String custSerl) {
		this.custSerl = custSerl;
	}
	public String getTel() {
		return this.tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMail() {
		return this.mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getVrsn() {
		return this.vrsn;
	}
	public void setVrsn(String vrsn) {
		this.vrsn = vrsn;
	}
	public String getStts() {
		return this.stts;
	}
	public void setStts(String stts) {
		this.stts = stts;
	}
	public Date getSttsDttm() {
		return this.sttsDttm;
	}
	public void setSttsDttm(Date sttsDttm) {
		this.sttsDttm = sttsDttm;
	}
	public Date getCretDttm() {
		return this.cretDttm;
	}
	public void setCretDttm(Date cretDttm) {
		this.cretDttm = cretDttm;
	}
	public String getMdfyUser() {
		return this.mdfyUser;
	}
	public void setMdfyUser(String mdfyUser) {
		this.mdfyUser = mdfyUser;
	}
	public Date getMdfyDttm() {
		return this.mdfyDttm;
	}
	public void setMdfyDttm(Date mdfyDttm) {
		this.mdfyDttm = mdfyDttm;
	}

}
