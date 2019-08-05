/**
 * @(#) AbstractEcDataAprv.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/04/11
 * 	 1) First release
 * 
 *  v1.01, 2019/06/19, Organ  
 *   1) Add 繳費稅收費方式及費率
 * 
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;

public class AbstractEcDataAprv extends AclCommand implements Serializable {

	private static final long serialVersionUID = 3201301242330591345L;

	// =============== KEY ===============
	private AbstractEcDataAprv.Id id; // 複合key

	// =============== Table Attribute ===============
	private String ecNameCh; // 平台中文名稱
	private String ecNameEn; // 平台英文名稱
	private String sorcIp; // 來源IP
	private String feeType; // 收費方式
	private Double feeRate; // 費率
	private String stts; // 狀態
	private String realAcnt; // 實體帳號
	private String entrNo; // 企業編號
	private String showRealAcnt; // 實體帳號是否遮罩
	private String entrId; // 公司統編
	private String cntc; // 聯絡人
	private String tel; // 聯絡電話
	private String mail; // 電子郵件
	private String note; // 備註說明
	private String aprvUser; // 審核人員
	private Date aprvDttm; // 審核時間
	private Integer showSerl; // 顯示順序
	private Integer maxFee; // 比率收費上限
	private Integer minFee; // 比率收費下限
	private String dataStts; // 資料狀態
	private String oprtType; // 異動類別
	private Integer linkLimit; // 使用者可綁定帳戶數

	// 20190619 Add 繳費稅收費方式及費率 Begin
	private String taxType; // 繳費稅收費方式
	private Double taxRate; // 繳費稅費率
	private Integer maxTax; // 繳費稅比率收費上限
	private Integer minTax; // 繳費稅比率收費下限
	// 20190619 Add 繳費稅收費方式及費率 End

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		private static final long serialVersionUID = -192860865536138594L;
		private String ecId; // 平台代碼
		private String cretUser; // 建立人員
		private Date cretDttm; // 建立時間

		public String getEcId() {
			return ecId;
		}

		public void setEcId(String ecId) {
			this.ecId = ecId;
		}

		public String getCretUser() {
			return cretUser;
		}

		public void setCretUser(String cretUser) {
			this.cretUser = cretUser;
		}

		public Date getCretDttm() {
			return cretDttm;
		}

		public void setCretDttm(Date cretDttm) {
			this.cretDttm = cretDttm;
		}
	}

	public String getEcNameCh() {
		return ecNameCh;
	}

	public void setEcNameCh(String ecNameCh) {
		this.ecNameCh = ecNameCh;
	}

	public String getEcNameEn() {
		return ecNameEn;
	}

	public void setEcNameEn(String ecNameEn) {
		this.ecNameEn = ecNameEn;
	}

	public String getSorcIp() {
		return sorcIp;
	}

	public void setSorcIp(String sorcIp) {
		this.sorcIp = sorcIp;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Double getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}

	public String getStts() {
		return stts;
	}

	public void setStts(String stts) {
		this.stts = stts;
	}

	public String getRealAcnt() {
		return realAcnt;
	}

	public void setRealAcnt(String realAcnt) {
		this.realAcnt = realAcnt;
	}

	public String getEntrNo() {
		return entrNo;
	}

	public void setEntrNo(String entrNo) {
		this.entrNo = entrNo;
	}

	public String getShowRealAcnt() {
		return showRealAcnt;
	}

	public void setShowRealAcnt(String showRealAcnt) {
		this.showRealAcnt = showRealAcnt;
	}

	public String getEntrId() {
		return entrId;
	}

	public void setEntrId(String entrId) {
		this.entrId = entrId;
	}

	public String getCntc() {
		return cntc;
	}

	public void setCntc(String cntc) {
		this.cntc = cntc;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAprvUser() {
		return aprvUser;
	}

	public void setAprvUser(String aprvUser) {
		this.aprvUser = aprvUser;
	}

	public Date getAprvDttm() {
		return aprvDttm;
	}

	public void setAprvDttm(Date aprvDttm) {
		this.aprvDttm = aprvDttm;
	}

	public Integer getShowSerl() {
		return showSerl;
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

	public String getDataStts() {
		return dataStts;
	}

	public void setDataStts(String dataStts) {
		this.dataStts = dataStts;
	}

	public AbstractEcDataAprv.Id getId() {
		return id;
	}

	public void setId(AbstractEcDataAprv.Id id) {
		this.id = id;
	}

	public String getOprtType() {
		return oprtType;
	}

	public void setOprtType(String oprtType) {
		this.oprtType = oprtType;
	}

	public Integer getLinkLimit() {
		return linkLimit;
	}

	public void setLinkLimit(Integer linkLimit) {
		this.linkLimit = linkLimit;
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
