/**
 * @(#)AbstractVwTrnsData.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : VwTrnsData base model
 * 
 * Modify History:
 *  v1.00, 2016/06/02, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;
import com.hitrust.bank.model.DownloadCommand;

public class AbstractVwTrnsData extends DownloadCommand implements Serializable {

	private static final long serialVersionUID = 6000487301289694640L;

	// =============== KEY ===============
	private AbstractVwTrnsData.Id id; // 複合 KEY

	// =============== Table Attribute ===============
	private String trnsMnth; // 交易月份(YYYY-MM)
	private String trnsDate; // 交易日(YYYY-MM-DD)
	private String custId;   // 身分證字號
	private String custSerl; // 會員服務序號
	private String ecUser;   // 平台會員代碼
	private String trnsType; // 交易類別
	private String realAcnt; // 實體帳號
	private String recvAcnt; // 入帳帳號
	private Date trnsDttm;   // 交易日期
	private Long trnsAmnt;   // 交易金額
	private String trnsStts; // 交易狀態
	private String ordrNo;   // 訂單編號
	private Long feeAmnt;    // 手續費金額
	private String acntIndt; // 帳號識別碼
	private String ecNameCh; // 平台中文名稱
	private String feeType;  // 收費方式 
	private Double feeRate;  // 費率
	// 20190619 Add 繳費稅收費方式及費率 Begin
	private String taxType; // 繳費稅收費方式
	private Double taxRate; // 繳費稅費率
	// 20190619 Add 繳費稅收費方式及費率 End
	
	// =============== Getter & Setter ===============
	public AbstractVwTrnsData.Id getId() {
		return this.id;
	}
	public void setId(AbstractVwTrnsData.Id id) {
		this.id = id;
	}
	public String getTrnsMnth() {
		return this.trnsMnth;
	}
	public void setTrnsMnth(String trnsMnth) {
		this.trnsMnth = trnsMnth;
	}
	public String getTrnsDate() {
		return this.trnsDate;
	}
	public void setTrnsDate(String trnsDate) {
		this.trnsDate = trnsDate;
	}
	public String getCustId() {
		return this.custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustSerl() {
		return this.custSerl;
	}
	public void setCustSerl(String custSerl) {
		this.custSerl = custSerl;
	}
	public String getEcUser() {
		return this.ecUser;
	}
	public void setEcUser(String ecUser) {
		this.ecUser = ecUser;
	}
	public String getTrnsType() {
		return this.trnsType;
	}
	public void setTrnsType(String trnsType) {
		this.trnsType = trnsType;
	}
	public String getRealAcnt() {
		return this.realAcnt;
	}
	public void setRealAcnt(String realAcnt) {
		this.realAcnt = realAcnt;
	}
	public String getRecvAcnt() {
		return this.recvAcnt;
	}
	public void setRecvAcnt(String recvAcnt) {
		this.recvAcnt = recvAcnt;
	}
	public Date getTrnsDttm() {
		return this.trnsDttm;
	}
	public void setTrnsDttm(Date trnsDttm) {
		this.trnsDttm = trnsDttm;
	}
	public Long getTrnsAmnt() {
		return this.trnsAmnt;
	}
	public void setTrnsAmnt(Long trnsAmnt) {
		this.trnsAmnt = trnsAmnt;
	}
	public String getTrnsStts() {
		return this.trnsStts;
	}
	public void setTrnsStts(String trnsStts) {
		this.trnsStts = trnsStts;
	}
	public String getOrdrNo() {
		return this.ordrNo;
	}
	public void setOrdrNo(String ordrNo) {
		this.ordrNo = ordrNo;
	}
	public Long getFeeAmnt() {
		return this.feeAmnt;
	}
	public void setFeeAmnt(Long feeAmnt) {
		this.feeAmnt = feeAmnt;
	}
	public String getAcntIndt() {
		return this.acntIndt;
	}
	public void setAcntIndt(String acntIndt) {
		this.acntIndt = acntIndt;
	}
	public String getEcNameCh() {
		return this.ecNameCh;
	}
	public void setEcNameCh(String ecNameCh) {
		this.ecNameCh = ecNameCh;
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
	// 20190619 Add 繳費稅收費方式及費率 End
	
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = -5411327004995132811L;
		
		// =============== Table Attribute ===============
		private String ecId;	// 平台代碼
		private String ecMsgNo;	// 平台訊息序號 
		
		// =============== Getter & Setter ===============
		public String getEcId() {
			return this.ecId;
		}
		public void setEcId(String ecId) {
			this.ecId = ecId;
		}
		public String getEcMsgNo() {
			return this.ecMsgNo;
		}
		public void setEcMsgNo(String ecMsgNo) {
			this.ecMsgNo = ecMsgNo;
		}
	}
}
