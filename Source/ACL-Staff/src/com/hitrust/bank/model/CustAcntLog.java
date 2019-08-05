/**
 * @(#) CustAcntLog.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/15, Evan
 * 	 1)First release
 * 
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.hitrust.bank.model.base.AbstractCustAcntLog;

public class CustAcntLog extends AbstractCustAcntLog implements Serializable {
	
	private static final long serialVersionUID = -2477735137762756937L;

	//接收頁面參數
	private String strtDate; 		//開始時間 
	private String endDate;   		//結束時間
	private String queryType; 		//查詢類別
	private String custId;			//身分證號
	private String ecId;			//平台代號
	private String custName;		//for join
	private String ecNameCh;		//for join
	private String ecNameEn;		//for join
	private List<EcData> ecData; 	//平台代號清單
	private List<CustAcntLog> custAcntLogList; 	//會員連結記錄
	private boolean initQuery;					//判斷是否為查詢初始頁
	private String sysParam;					//系統參數
	
	// constructor
	public CustAcntLog() {}
	
	public String getSysParam() {
		return sysParam;
	}

	public void setSysParam(String sysParam) {
		this.sysParam = sysParam;
	}

	//for join talbe
	public CustAcntLog(CustAcntLog custAcntLog, String ecNameCh, String ecNameEn, String custName) {
		BeanUtils.copyProperties(custAcntLog, this);
		this.ecNameCh = ecNameCh;
		this.ecNameEn = ecNameEn;
		this.custName = custName;
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
	
	public String getQueryType() {
		return queryType;
	}
	
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	
	public List<EcData> getEcData() {
		return ecData;
	}
	
	public void setEcData(List<EcData> ecData) {
		this.ecData = ecData;
	}
	
	public List<CustAcntLog> getCustAcntLogList() {
		return custAcntLogList;
	}
	
	public void setCustAcntLogList(List<CustAcntLog> custAcntLogList) {
		this.custAcntLogList = custAcntLogList;
	}
	
	public String getCustId() {
		return custId;
	}
	
	public void setCustId(String custId) {
		this.custId = custId;
	}
	
	public String getEcId() {
		return ecId;
	}
	
	
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	
	public String getCustName() {
		return custName;
	}
	
	public void setCustName(String custName) {
		this.custName = custName;
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

	public boolean isInitQuery() {
		return initQuery;
	}

	public void setInitQuery(boolean initQuery) {
		this.initQuery = initQuery;
	}
}
