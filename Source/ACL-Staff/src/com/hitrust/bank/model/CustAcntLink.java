/**
 * @(#) CustAcntLink.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/16, Evan
 * 	 1) First release
 *  v1.01, 2016/06/06, Yann
 *   1) 約定帳號統計
 *  
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.BeanUtils;

import com.hitrust.bank.model.base.AbstractCustAcntLink;

public class CustAcntLink extends AbstractCustAcntLink implements Serializable {
	private static final long serialVersionUID = -2622274322171918464L;
	
	// =============== Not Table Attribute ===============
	private String strtDate; 	     //查詢起始日期
	private String endDate;          //查詢結束日期
	private String rptType;	         //查詢統計方式
	private String qEcId; 		     //查詢電商平台代號
	private String dEcId; 			// 明細查詢平台代號
	private String dStts; 			// 明細查詢狀態
	private List<EcData> ecDataList; //電商平台代號清單
	private String ecNameCh;	     //join EC_DATA field 平台名稱
	private List<CustAcntLink> reportData;  //報表資料
	private boolean initFlag = true; // 查詢初始化狀態
	
	private String EC_ID;		 //平台代號
	private String EC_NAME;		 //平台名稱
	private String DATE;		 //統計日期/月份
	private int CNT_00;			 //啟用交易筆數
	private int CNT_01;			 //暫停交易筆數
	private int CNT_02;			 //終止交易筆數
	private int CNT_TOTL;		 //帳號總數
	private String reportDate;   //報表日期
	private String reportEcId; 	 //報表電商平台
	private String runDate;
	private List<CustAcntLink> reportDetailData;
	private Long availableBalanceMonth;			//月可用餘額
	private Long usedAmountMonth;				//月已使用金額
	private Long availableBalanceDay;			//日可用餘額
	private Long usedAmountDay;					//日已使用金額
	//constructor
	public CustAcntLink(){
	}
	
	public CustAcntLink(CustAcntLink custAcntLink, String ecNameCh) {
		BeanUtils.copyProperties(custAcntLink, this);
		this.ecNameCh = ecNameCh;
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
	public String getqEcId() {
		return qEcId;
	}
	public void setqEcId(String qEcId) {
		this.qEcId = qEcId;
	}
	public String getdEcId() {
		return dEcId;
	}
	public void setdEcId(String dEcId) {
		this.dEcId = dEcId;
	}
	public String getdStts() {
		return dStts;
	}
	public void setdStts(String dStts) {
		this.dStts = dStts;
	}
	public String getRptType() {
		return rptType;
	}
	public void setRptType(String rptType) {
		this.rptType = rptType;
	}
	public List<EcData> getEcDataList() {
		return ecDataList;
	}
	public void setEcDataList(List<EcData> ecDataList) {
		this.ecDataList = ecDataList;
	}
	public String getEcNameCh() {
		return ecNameCh;
	}
	public void setEcNameCh(String ecNameCh) {
		this.ecNameCh = ecNameCh;
	}
	public List<CustAcntLink> getReportData() {
		return reportData;
	}
	public void setReportData(List<CustAcntLink> reportData) {
		this.reportData = reportData;
	}
	public boolean isInitFlag() {
		return initFlag;
	}
	public void setInitFlag(boolean initFlag) {
		this.initFlag = initFlag;
	}

	public String getEC_ID() {
		return EC_ID;
	}
	public void setEC_ID(String eC_ID) {
		EC_ID = eC_ID;
	}
	public String getEC_NAME() {
		return EC_NAME;
	}
	public void setEC_NAME(String eC_NAME) {
		EC_NAME = eC_NAME;
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
	public String getReportEcId() {
		return reportEcId;
	}
	public void setReportEcId(String reportEcId) {
		this.reportEcId = reportEcId;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public List<CustAcntLink> getReportDetailData() {
		return reportDetailData;
	}

	public void setReportDetailData(List<CustAcntLink> reportDetailData) {
		this.reportDetailData = reportDetailData;
	}

	public Long getAvailableBalanceDay() {
		return availableBalanceDay;
	}

	public void setAvailableBalanceDay(Long availableBalanceDay) {
		this.availableBalanceDay = availableBalanceDay;
	}

	public Long getUsedAmountDay() {
		return usedAmountDay;
	}

	public void setUsedAmountDay(Long usedAmountDay) {
		this.usedAmountDay = usedAmountDay;
	}

	public Long getAvailableBalanceMonth() {
		return availableBalanceMonth;
	}

	public void setAvailableBalanceMonth(Long availableBalanceMonth) {
		this.availableBalanceMonth = availableBalanceMonth;
	}

	public Long getUsedAmountMonth() {
		return usedAmountMonth;
	}

	public void setUsedAmountMonth(Long usedAmountMonth) {
		this.usedAmountMonth = usedAmountMonth;
	}
}
