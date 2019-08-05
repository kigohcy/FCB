package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;

import com.hitrust.bank.model.base.AbstractVwTrnsData;

public class VwTrnsData extends AbstractVwTrnsData implements Serializable {
	private static final long serialVersionUID = 165332289757943552L;
	
	private boolean initFlag = true; 						// 查詢初始化狀態
	
	//查詢條件
	private String strtDate; 								// 查詢起始日期
	private String endDate; 								// 查詢結束日期
	private String rptType; 								// 報表型別
	private String qEcId; 									// 查詢:平台代號
	private String qTrnsStts; 								// 查詢交易狀態
	private String dEcId; 									// 明細查詢:平台代號
	private String reportDate;   							// 報表:日期
	private String reportEcName; 	 						// 報表:電商平台
	private List<EcData> ecData; 							// 平台代號清單

	//查詢結果
	private List<VwTrnsData> report;						// 報表交易資料清單
	private String trnsTime;								// 交易時間
	private String ecId;									// 報表平台代號
	private String countA;									// 報表扣款筆數
	private String amuntA;									// 報表扣款金額
	private String countB;									// 報表退款筆數
	private String amuntB;									// 報表退款金額
	private String countC;									// 報表提領筆數
	private String amuntC;									// 報表提領金額
	private String countD;									// 報表扣款筆數
	private String amuntD;									// 報表扣款金額
	//20190625 交易量統計增加繳費稅 Begin
	private String amuntE; // 繳費稅扣款金額
	private String countE; // 繳費稅扣款筆數
	//20190625 交易量統計增加繳費稅 End
	
	private String amuntT;									// 報表交易淨額
	private String feeAmount;
	private String currentFeeType;							// 報表"目前收費方式"欄位內容
	
	private String SUMMARY_COUNT_A;							// 扣款總筆數
	private String SUMMARY_COUNT_B;							// 退款總筆數
	private String SUMMARY_COUNT_C;							// 提領總筆數
	private String SUMMARY_COUNT_D;							// 提領總筆數
	private String SUMMARY_AMUNT_A; 						// 扣款總金額
	private String SUMMARY_AMUNT_B; 						// 退款總金額
	private String SUMMARY_AMUNT_C; 						// 提領總金額
	private String SUMMARY_AMUNT_D; 						// 提領總金額
	
    //20190625 交易量統計增加繳費稅 Begin
	private String SUMMARY_AMUNT_E;                         //繳費稅總金額
	private String SUMMARY_COUNT_E; 						//繳費稅總筆數
	//20190625 交易量統計增加繳費稅 End 
	
	private String SUMMARY_AMUNT_T; 						// 交易總淨額
	private String SUMMARY_FEE_AMUNT;						// 手續費總額
	
	
	private List<VwTrnsData> reportDetail;					
	private String runDate;
	
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
	public String getRptType() {
		return rptType;
	}
	public void setRptType(String rptType) {
		this.rptType = rptType;
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
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportEcName() {
		return reportEcName;
	}
	public void setReportEcName(String reportEcName) {
		this.reportEcName = reportEcName;
	}
	public List<EcData> getEcData() {
		return ecData;
	}
	public void setEcData(List<EcData> ecData) {
		this.ecData = ecData;
	}
	public List<VwTrnsData> getReport() {
		return report;
	}
	public void setReport(List<VwTrnsData> report) {
		this.report = report;
	}
	public String getTrnsTime() {
		return trnsTime;
	}
	public void setTrnsTime(String trnsTime) {
		this.trnsTime = trnsTime;
	}
	public String getEcId() {
		return ecId;
	}
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	public String getCountA() {
		return countA;
	}
	public void setCountA(String countA) {
		this.countA = countA;
	}
	public String getAmuntA() {
		return amuntA;
	}
	public void setAmuntA(String amuntA) {
		this.amuntA = amuntA;
	}
	public String getCountB() {
		return countB;
	}
	public void setCountB(String countB) {
		this.countB = countB;
	}
	public String getAmuntB() {
		return amuntB;
	}
	public void setAmuntB(String amuntB) {
		this.amuntB = amuntB;
	}
	public String getCountC() {
		return countC;
	}
	public void setCountC(String countC) {
		this.countC = countC;
	}
	public String getAmuntC() {
		return amuntC;
	}
	public void setAmuntC(String amuntC) {
		this.amuntC = amuntC;
	}
	public String getAmuntT() {
		return amuntT;
	}
	public void setAmuntT(String amuntT) {
		this.amuntT = amuntT;
	}
	public String getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getCurrentFeeType() {
		return currentFeeType;
	}
	public void setCurrentFeeType(String currentFeeType) {
		this.currentFeeType = currentFeeType;
	}
	public String getSUMMARY_COUNT_A() {
		return SUMMARY_COUNT_A;
	}
	public void setSUMMARY_COUNT_A(String sUMMARY_COUNT_A) {
		SUMMARY_COUNT_A = sUMMARY_COUNT_A;
	}
	public String getSUMMARY_COUNT_B() {
		return SUMMARY_COUNT_B;
	}
	public void setSUMMARY_COUNT_B(String sUMMARY_COUNT_B) {
		SUMMARY_COUNT_B = sUMMARY_COUNT_B;
	}
	public String getSUMMARY_COUNT_C() {
		return SUMMARY_COUNT_C;
	}
	public void setSUMMARY_COUNT_C(String sUMMARY_COUNT_C) {
		SUMMARY_COUNT_C = sUMMARY_COUNT_C;
	}
	public String getSUMMARY_AMUNT_A() {
		return SUMMARY_AMUNT_A;
	}
	public void setSUMMARY_AMUNT_A(String sUMMARY_AMUNT_A) {
		SUMMARY_AMUNT_A = sUMMARY_AMUNT_A;
	}
	public String getSUMMARY_AMUNT_B() {
		return SUMMARY_AMUNT_B;
	}
	public void setSUMMARY_AMUNT_B(String sUMMARY_AMUNT_B) {
		SUMMARY_AMUNT_B = sUMMARY_AMUNT_B;
	}
	public String getSUMMARY_AMUNT_C() {
		return SUMMARY_AMUNT_C;
	}
	public void setSUMMARY_AMUNT_C(String sUMMARY_AMUNT_C) {
		SUMMARY_AMUNT_C = sUMMARY_AMUNT_C;
	}
	public String getSUMMARY_AMUNT_T() {
		return SUMMARY_AMUNT_T;
	}
	public void setSUMMARY_AMUNT_T(String sUMMARY_AMUNT_T) {
		SUMMARY_AMUNT_T = sUMMARY_AMUNT_T;
	}
	public String getSUMMARY_FEE_AMUNT() {
		return SUMMARY_FEE_AMUNT;
	}
	public void setSUMMARY_FEE_AMUNT(String sUMMARY_FEE_AMUNT) {
		SUMMARY_FEE_AMUNT = sUMMARY_FEE_AMUNT;
	}
	public List<VwTrnsData> getReportDetail() {
		return reportDetail;
	}
	public void setReportDetail(List<VwTrnsData> reportDetail) {
		this.reportDetail = reportDetail;
	}
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	public String getCountD() {
		return countD;
	}
	public void setCountD(String countD) {
		this.countD = countD;
	}
	public String getAmuntD() {
		return amuntD;
	}
	public void setAmuntD(String amuntD) {
		this.amuntD = amuntD;
	}
	public String getSUMMARY_COUNT_D() {
		return SUMMARY_COUNT_D;
	}
	public void setSUMMARY_COUNT_D(String sUMMARY_COUNT_D) {
		SUMMARY_COUNT_D = sUMMARY_COUNT_D;
	}
	public String getSUMMARY_AMUNT_D() {
		return SUMMARY_AMUNT_D;
	}
	public void setSUMMARY_AMUNT_D(String sUMMARY_AMUNT_D) {
		SUMMARY_AMUNT_D = sUMMARY_AMUNT_D;
	}
	
	
	//20190625 交易量統計增加繳費稅 Begin
	public String getAmuntE() {
		return amuntE;
	}
	public void setAmuntE(String amuntE) {
		this.amuntE = amuntE;
	}
	public String getCountE() {
		return countE;
	}
	public void setCountE(String countE) {
		this.countE = countE;
	}
	public String getSUMMARY_AMUNT_E() {
		return SUMMARY_AMUNT_E;
	}
	public void setSUMMARY_AMUNT_E(String sUMMARY_AMUNT_E) {
		SUMMARY_AMUNT_E = sUMMARY_AMUNT_E;
	}
	public String getSUMMARY_COUNT_E() {
		return SUMMARY_COUNT_E;
	}
	public void setSUMMARY_COUNT_E(String sUMMARY_COUNT_E) {
		SUMMARY_COUNT_E = sUMMARY_COUNT_E;
	}
	//20190625 交易量統計增加繳費稅 End
	
	public String getqTrnsStts() {
		return qTrnsStts;
	}
	public void setqTrnsStts(String qTrnsStts) {
		this.qTrnsStts = qTrnsStts;
	}
	
	
	

	
	
	
}
