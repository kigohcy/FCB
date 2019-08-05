package com.hitrust.bank.model;

import java.util.List;

import com.hitrust.bank.model.base.AbstractEcDataAprv;

public class EcDataAprv extends AbstractEcDataAprv {
	private static final long serialVersionUID = 8221486540205764407L;
	private List<EcDataAprv> ecDataAprvList; // 覆核平台代號清單
	private EcDataAprv ecDataAprv; // 覆核平台代號資料
	private List<EcData> ecDataList; // 平台代號清單
	private String ecId;
	private String cretUser;
	private String cretDttm;
	private String strtDate; // 查詢起始日期
	private String endDate; // 查詢結束日期
	private boolean queryFlag = false;

	// 20190619 Add 繳費稅收費方式及費率 Begin
	private String taxType; // 繳費稅收費方式
	private Double taxRate; // 繳費稅費率
	private Integer maxTax; // 繳費稅比率收費上限
	private Integer minTax; // 繳費稅比率收費下限
	// 20190619 Add 繳費稅收費方式及費率 End

	public List<EcDataAprv> getEcDataAprvList() {
		return ecDataAprvList;
	}

	public void setEcDataAprvList(List<EcDataAprv> ecDataAprvList) {
		this.ecDataAprvList = ecDataAprvList;
	}

	public EcDataAprv getEcDataAprv() {
		return ecDataAprv;
	}

	public void setEcDataAprv(EcDataAprv ecDataAprv) {
		this.ecDataAprv = ecDataAprv;
	}

	public List<EcData> getEcDataList() {
		return ecDataList;
	}

	public void setEcDataList(List<EcData> ecDataList) {
		this.ecDataList = ecDataList;
	}

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

	public String getCretDttm() {
		return cretDttm;
	}

	public void setCretDttm(String cretDttm) {
		this.cretDttm = cretDttm;
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

	public boolean isQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(boolean queryFlag) {
		this.queryFlag = queryFlag;
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
