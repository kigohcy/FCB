/**
 * @(#) TrnsData.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/16, Yann
 * 	 1)First release
 * 
 *  v1.01, 2019/06/19, Organ  
 *   1) Add 交易失敗時記錄上下行電文  
 * 
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;

import com.hitrust.bank.model.base.AbstractTrnsData;

public class TrnsData extends AbstractTrnsData implements Serializable {
	private static final long serialVersionUID = 1582593769695406714L;

	// ===============Not Table Attribute ===============
	private boolean initFlag = true;		// 查詢初始化狀態
	private String strtDate;				// 查詢起始日期
	private String endDate;					// 查詢結束日期
	private String custName;				// 客戶姓名
	private String ecNameEn;				// 平台英文平稱
	private String qEcId;					// 平台代號
	private String qTrnsType;				// 交易類別
	private String queryLimt;				// 查詢日期區間限制
	private String qTrnsStts;				// 查詢交易狀態
	private List<TrnsData> trnsDataList;	// 交易結果清單
	private List<EcData> ecData;			// 平台代號清單
	

    // 20190619 Add 交易失敗時記錄上下行電文 Begin
	private String tiTa; //上行電文
	private String toTa; //下行電文
	// 20190619 Add 交易失敗時記錄上下行電文 End 
	
	// =============== Getters & Setters ===============
	public boolean isInitFlag() {
		return initFlag;
	}
	public void setInitFlag(boolean initFlag) {
		this.initFlag = initFlag;
	}
	public String getStrtDate() {
		return strtDate;
	}
	public void setStrtDate(String strtDate) {
		this.strtDate = strtDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getEcNameEn() {
		return ecNameEn;
	}
	public void setEcNameEn(String ecNameEn) {
		this.ecNameEn = ecNameEn;
	}
	public String getqEcId() {
		return qEcId;
	}
	public void setqEcId(String qEcId) {
		this.qEcId = qEcId;
	}
	public String getqTrnsType() {
		return qTrnsType;
	}
	public void setqTrnsType(String qTrnsType) {
		this.qTrnsType = qTrnsType;
	}
	public String getQueryLimt() {
		return queryLimt;
	}
	public void setQueryLimt(String queryLimt) {
		this.queryLimt = queryLimt;
	}
	public List<TrnsData> getTrnsDataList() {
		return trnsDataList;
	}
	public void setTrnsDataList(List<TrnsData> trnsDataList) {
		this.trnsDataList = trnsDataList;
	}
	public List<EcData> getEcData() {
		return ecData;
	}
	public void setEcData(List<EcData> ecData) {
		this.ecData = ecData;
	}
	public String getqTrnsStts() {
		return qTrnsStts;
	}
	public void setqTrnsStts(String qTrnsStts) {
		this.qTrnsStts = qTrnsStts;
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
	
}
