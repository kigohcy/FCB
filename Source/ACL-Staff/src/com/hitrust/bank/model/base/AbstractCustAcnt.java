/**
 * @(#)AbstractCustAcnt.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustAcnt base model
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;

public class AbstractCustAcnt extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = -266566532565631926L;

	// =============== KEY ===============
	private AbstractCustAcnt.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private Long trnsLimt;	 // 單筆自訂限額
	private Long dayLimt;	 // 每日自訂限額
	private Long mnthLimt;	 // 每月自訂限額
	private Date cretDttm;   // 建立時間 
	private String mdfyUser; // 最後異動人員 
	private Date mdfyDttm;   // 最後異動時間
	
	// =============== Getter & Setter ===============
	public AbstractCustAcnt.Id getId() {
		return this.id;
	}
	public void setId(AbstractCustAcnt.Id id) {
		this.id = id;
	}
	public Long getTrnsLimt() {
		return this.trnsLimt;
	}
	public void setTrnsLimt(Long trnsLimt) {
		this.trnsLimt = trnsLimt;
	}
	public Long getDayLimt() {
		return this.dayLimt;
	}
	public void setDayLimt(Long dayLimt) {
		this.dayLimt = dayLimt;
	}
	public Long getMnthLimt() {
		return this.mnthLimt;
	}
	public void setMnthLimt(Long mnthLimt) {
		this.mnthLimt = mnthLimt;
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
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = 6428557583764795941L;
		
		// =============== Table Attribute ===============
		private String custId;   // 身分證字號
		private String realAcnt; // 實體帳號
		
		// =============== Getter & Setter ===============
		public String getCustId() {
			return this.custId;
		}
		public void setCustId(String custId) {
			this.custId = custId;
		}
		public String getRealAcnt() {
			return this.realAcnt;
		}
		public void setRealAcnt(String realAcnt) {
			this.realAcnt = realAcnt;
		}
	}
}
