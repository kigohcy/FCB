/**
 * @(#) CustData.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/15, Yann
 * 	 1)First release
 *  v1.01, 2016/06/08, Yann
 *   1) 會員服務統計
 *  
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;

import com.hitrust.bank.model.base.AbstractCustData;

public class CustData extends AbstractCustData implements Serializable {
	private static final long serialVersionUID = -3712796315364618587L;
	
	// =============== Not Table Attribute ===============
	private String strtDate; 	     //查詢起始日期
	private String endDate;          //查詢結束日期
	private String rptType;	         //查詢統計方式
	private String dStts; 			// 明細查詢狀態
	private List<CustData> reportData;  //報表資料
	private boolean initFlag = true; // 查詢初始化狀態
	private String DATE;		 //統計日期/月份
	private int CNT_00;			 //啟用交易筆數
	private int CNT_01;			 //暫停交易筆數
	private int CNT_02;			 //終止交易筆數
	private int CNT_TOTL;		 //帳號總數
	private String reportDate;   //報表日期
	
	private List<CustData> reportDetailData; 
	private String runDate; 
	//constructor
	public CustData(){
	}
	
	// =============== Getters & Setters ===============
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
	public String getRptType() {
		return rptType;
	}
	public void setRptType(String rptType) {
		this.rptType = rptType;
	}
	public String getdStts() {
		return dStts;
	}
	public void setdStts(String dStts) {
		this.dStts = dStts;
	}
	public List<CustData> getReportData() {
		return reportData;
	}
	public void setReportData(List<CustData> reportData) {
		this.reportData = reportData;
	}
	public boolean isInitFlag() {
		return initFlag;
	}
	public void setInitFlag(boolean initFlag) {
		this.initFlag = initFlag;
	}
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String dATE) {
		DATE = dATE;
	}
	public int getCNT_00() {
		return CNT_00;
	}
	public void setCNT_00(int cNT_00) {
		CNT_00 = cNT_00;
	}
	public int getCNT_01() {
		return CNT_01;
	}
	public void setCNT_01(int cNT_01) {
		CNT_01 = cNT_01;
	}
	public int getCNT_02() {
		return CNT_02;
	}
	public void setCNT_02(int cNT_02) {
		CNT_02 = cNT_02;
	}
	public int getCNT_TOTL() {
		return CNT_TOTL;
	}
	public void setCNT_TOTL(int cNT_TOTL) {
		CNT_TOTL = cNT_TOTL;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public List<CustData> getReportDetailData() {
		return reportDetailData;
	}

	public void setReportDetailData(List<CustData> reportDetailData) {
		this.reportDetailData = reportDetailData;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
}
