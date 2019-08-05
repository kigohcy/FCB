/**
 * @(#)AbstractCustAcntLink.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員帳號連結檔 (CUST_ACNT_LINK)
 * 
 * Modify History:
 *  v1.00, 2017/09/13, Yann
 *   1) First release
 *  
 */
package com.hitrust.acl.model.base;

import java.io.Serializable;
import java.util.Date;
import com.hitrust.framework.model.BaseCommand;

public class AbstractCustAcntLink extends BaseCommand implements Serializable {
	
	private static final long serialVersionUID = -3386987996604698528L;

	// =============== KEY ===============
	private AbstractCustAcntLink.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private String gradType; // 身分認證方式
	private String grad;	 // 等級
	private String stts;	 // 狀態
	private Date sttsDttm;   // 狀態異動時間
	private Long trnsLimt;   // 單筆自訂限額
	private Long dayLimt;	 // 每日自訂限額
	private Long mnthLimt;	 // 每月自訂限額
	private Date cretDttm;	 // 建立時間
	private String mdfyUser; // 最後異動人員
	private Date mdfyDttm;	 // 最後異動時間
	private String acntIndt; // 帳號識別碼
	
	// =============== Getter & Setter ===============
	public AbstractCustAcntLink.Id getId() {
		return this.id;
	}
	public void setId(AbstractCustAcntLink.Id id) {
		this.id = id;
	}
	public String getGradType() {
		return this.gradType;
	}
	public void setGradType(String gradType) {
		this.gradType = gradType;
	}
	public String getGrad() {
		return this.grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
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
	public String getAcntIndt() {
		return this.acntIndt;
	}
	public void setAcntIndt(String acntIndt) {
		this.acntIndt = acntIndt;
	}
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = 4837049542470523536L;
		
		// =============== Table Attribute ===============
		private String custId;	 // 份證字號 
		private String ecId;	 // 平台代碼
		private String ecUser;	 // 平台會員代碼
		private String realAcnt; // 實體帳號
		
		// =============== Getter & Setter ===============
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
	}
}
