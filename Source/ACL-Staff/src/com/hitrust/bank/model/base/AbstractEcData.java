/**
 * @(#) AbstractEcData.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/28, Eason Hsu
 * 	 1) First release
 * 
 *  v1.01, 2019/06/19, Organ  
 *   1) Add 繳費稅收費方式及費率 
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;

public class AbstractEcData extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = 3201301242330591345L;
	
	// =============== Table Attribute ===============
	private String ecId;	 		// 平台代碼
	private String ecNameCh; 		// 平台中文名稱
	private String ecNameEn; 		// 平台英文名稱
	private String sorcIp;	 		// 來源IP
	private String feeType;  		// 收費方式
	private Double feeRate;  		// 費率
	private String stts;	 		// 狀態
	private String realAcnt;    	// 實體帳號
	private String entrNo;      	// 企業編號
	private String showRealAcnt;	// 實體帳號是否遮罩
	private Integer linkLimit;		// 使用者可綁定帳戶數
	private String entrId;	 		// 公司統編
	private String cntc;	 		// 聯絡人
	private String tel;		 		// 聯絡電話
	private String mail;	 		// 電子郵件
	private String note;	 		// 備註說明
	private String cretUser; 		// 建立人員
	private Date cretDttm;	 		// 建立時間
	private String mdfyUser; 		// 最後異動人員
	private Date mdfyDttm;	 		// 最後異動時間
	private Integer showSerl;		// 顯示順序 
	private Integer maxFee;  		// 比率收費上限
	private Integer minFee;  		// 比率收費下限
	private String actvSendId;    	// 啟用建檔人員
	private String actvAprvId;    	// 啟用核可人員 
	private Date actvAprvDttm;    	// 啟用時間
	private String trmnSendId;    	// 終止建檔人員 
	private String trmnAprvId;    	// 終止核可人員
	private Date trmnAprvDttm;    	// 終止時間
	
	// 20190619 Add 繳費稅收費方式及費率 Begin
	private String taxType; // 繳費稅收費方式
	private Double taxRate; // 繳費稅費率
	private Integer maxTax; // 繳費稅比率收費上限
	private Integer minTax; // 繳費稅比率收費下限
	// 20190619 Add 繳費稅收費方式及費率 End
	
	
	// =============== Getter & Setter ===============
	public String getEcId() {
		return this.ecId;
	}
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	public String getEcNameCh() {
		return this.ecNameCh;
	}
	public void setEcNameCh(String ecNameCh) {
		this.ecNameCh = ecNameCh;
	}
	public String getEcNameEn() {
		return this.ecNameEn;
	}
	public void setEcNameEn(String ecNameEn) {
		this.ecNameEn = ecNameEn;
	}
	public String getSorcIp() {
		return this.sorcIp;
	}
	public void setSorcIp(String sorcIp) {
		this.sorcIp = sorcIp;
	}
	public String getFeeType() {
		return this.feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public Double getFeeRate() {
		return this.feeRate;
	}
	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}
	public String getStts() {
		return this.stts;
	}
	public void setStts(String stts) {
		this.stts = stts;
	}
	public String getRealAcnt() {
		return this.realAcnt;
	}
	public void setRealAcnt(String realAcnt) {
		this.realAcnt = realAcnt;
	}
	public String getEntrNo() {
		return this.entrNo;
	}
	public void setEntrNo(String entrNo) {
		this.entrNo = entrNo;
	}	
	public String getShowRealAcnt() {
		return this.showRealAcnt;
	}
	public void setShowRealAcnt(String showRealAcnt) {
		this.showRealAcnt = showRealAcnt;
	}
	public Integer getLinkLimit() {
		return linkLimit;
	}
	public void setLinkLimit(Integer linkLimit) {
		this.linkLimit = linkLimit;
	}
	public String getEntrId() {
		return this.entrId;
	}
	public void setEntrId(String entrId) {
		this.entrId = entrId;
	}
	public String getCntc() {
		return this.cntc;
	}
	public void setCntc(String cntc) {
		this.cntc = cntc;
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
	public String getNote() {
		return this.note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCretUser() {
		return this.cretUser;
	}
	public void setCretUser(String cretUser) {
		this.cretUser = cretUser;
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
	public Integer getShowSerl() {
		return this.showSerl;
	}
	public void setShowSerl(Integer showSerl) {
		this.showSerl = showSerl;
	}
	public Integer getMaxFee() {
		return maxFee;
	}
	public void setMaxFee(Integer maxFee) {
		this.maxFee = maxFee;
	}
	public Integer getMinFee() {
		return minFee;
	}
	public void setMinFee(Integer minFee) {
		this.minFee = minFee;
	}
	public String getActvSendId() {
		return actvSendId;
	}
	public void setActvSendId(String actvSendId) {
		this.actvSendId = actvSendId;
	}
	public String getActvAprvId() {
		return actvAprvId;
	}
	public void setActvAprvId(String actvAprvId) {
		this.actvAprvId = actvAprvId;
	}
	public Date getActvAprvDttm() {
		return actvAprvDttm;
	}
	public void setActvAprvDttm(Date actvAprvDttm) {
		this.actvAprvDttm = actvAprvDttm;
	}
	public String getTrmnSendId() {
		return trmnSendId;
	}
	public void setTrmnSendId(String trmnSendId) {
		this.trmnSendId = trmnSendId;
	}
	public String getTrmnAprvId() {
		return trmnAprvId;
	}
	public void setTrmnAprvId(String trmnAprvId) {
		this.trmnAprvId = trmnAprvId;
	}
	public Date getTrmnAprvDttm() {
		return trmnAprvDttm;
	}
	public void setTrmnAprvDttm(Date trmnAprvDttm) {
		this.trmnAprvDttm = trmnAprvDttm;
	}
	

	// 20190619 Add 繳費稅收費方式及費率 Begin
	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	
	public Integer getMaxTax() {
		return maxTax;
	}
	public void setMaxTax(Integer maxTax) {
		this.maxTax = maxTax;
	}
	public Integer getMinTax() {
		return minTax;
	}
	public void setMinTax(Integer minTax) {
		this.minTax = minTax;
	}
	// 20190619 Add 繳費稅收費方式及費率 End
}
