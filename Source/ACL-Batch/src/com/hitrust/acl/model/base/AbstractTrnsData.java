/**
 * @(#)AbstractTrnsData.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 交易資料 (TRNS_DATA)
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

public class AbstractTrnsData extends BaseCommand implements Serializable {
	
private static final long serialVersionUID = -4859721985366874433L;
	
	// =============== KEY ===============
	private AbstractTrnsData.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private String custId;		// 身分證字號
	private String custSerl;	// 會員服務序號
	private String ecUser;		// 平台會員代碼
	private String trnsType;	// 交易類別
	private String realAcnt;	// 實體帳號 
	private String recvAcnt;	// 入帳帳號
	private Date trnsDttm;		// 交易日期
	private Long trnsAmnt;		// 交易金額
	private String trnsStts;	// 交易狀態
	private String ordrNo;		// 訂單編號
	private Long backAmnt;		// 訂單餘額
	private String trnsNote;	// 交易備註
	private String errCode;	    // 錯誤代碼
	private String hostCode;	// 主機回應代碼
	private String hostSeq;		// 主機處理序號
	private String feeType;		// 收費方式
	private Double feeRate;		// 費率
	private Long feeAmnt;		// 手續費金額
	private String acntIndt;	// 帳號識別碼
	private Date mdfyDttm;		// 最後異動時間
	private String mailNotc;	// MAIL通知註記
	private Date mailDttm;		// MAIL通知時間
	private String teleNo;		// 電文序號
	
	// 20190619 Add 交易失敗時記錄上下行電文 Begin
	private String tiTa; // 上行電文
	private String toTa; // 下行電文
	// 20190619 Add 交易失敗時記錄上下行電文 End
	
	// =============== Getter & Setter ===============
	public AbstractTrnsData.Id getId() {
		return this.id;
	}
	public void setId(AbstractTrnsData.Id id) {
		this.id = id;
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
	public Long getBackAmnt() {
		return this.backAmnt;
	}
	public void setBackAmnt(Long backAmnt) {
		this.backAmnt = backAmnt;
	}
	public String getTrnsNote() {
		return this.trnsNote;
	}
	public void setTrnsNote(String trnsNote) {
		this.trnsNote = trnsNote;
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
	public String getHostSeq() {
		return this.hostSeq;
	}
	public void setHostSeq(String hostSeq) {
		this.hostSeq = hostSeq;
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
	public Date getMdfyDttm() {
		return this.mdfyDttm;
	}
	public void setMdfyDttm(Date mdfyDttm) {
		this.mdfyDttm = mdfyDttm;
	}
	public String getMailNotc() {
		return this.mailNotc;
	}
	public void setMailNotc(String mailNotc) {
		this.mailNotc = mailNotc;
	}
	public Date getMailDttm() {
		return this.mailDttm;
	}
	public void setMailDttm(Date mailDttm) {
		this.mailDttm = mailDttm;
	}
	public String getTeleNo() {
		return teleNo;
	}
	public void setTeleNo(String teleNo) {
		this.teleNo = teleNo;
	}
	
	// 20190619 Add 交易失敗時記錄上下行電文 Begin
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
	// 20190619 Add 交易失敗時記錄上下行電文 End

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = 544669580105787341L;
		
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
