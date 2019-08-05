/**
 * @(#)AbstractCustPltf.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員平台資料檔 (CUST_PLTF)
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

public class AbstractCustPltf extends BaseCommand implements Serializable {
	
	private static final long serialVersionUID = -4610190417819543227L;
	
	// =============== KEY ===============
	private AbstractCustPltf.Id id;
	
	private String stts;	 // 狀態
	private Date sttsDttm;	 // 狀態異動時間 
	private Date cretDttm;	 // 建立時間
	private String mdfyUser; // 最後異動人員
	
	// =============== Getter & Setter ===============
	public AbstractCustPltf.Id getId() {
		return this.id;
	}
	public void setId(AbstractCustPltf.Id id) {
		this.id = id;
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
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = -9207219204009937718L;
		
		// =============== Table Attribute ===============
		private String custId; // 身分證字號
		private String ecId;   // 平台代碼
		
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
	}
}
