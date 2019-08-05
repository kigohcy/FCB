/**
 * @(#) CustAcnt.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/18, Yann
 * 	 1)First release
 * 
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.hitrust.bank.model.base.AbstractCustAcnt;

public class CustAcnt extends AbstractCustAcnt implements Serializable {
	private static final long serialVersionUID = 8499380648434327594L;
	
	private boolean initQuery;	//判斷是否為查詢初始頁
	private String custId;		//身分證
	private CustData custData;	//會員資料
	private List<CustPltf> custPltfList;	//綁定的平台資料
	private List<CustAcnt> custAcntList;	//會員帳號資料
	private HashMap<String, BaseLimt> baseLimt;	//限額資料
	private HashMap<String, List<CustAcntLink>> custAcntLink;	//平台對應的綁定帳號
	private String selectEcId;	//欲修改限額的平台
	private String[] custAcntRealAcnt;	//key:欲修改實體帳號 (CUST_ACNT) 
	private String[] custAcntTrnsLmt;	//欲修改單筆限額 (CUST_ACNT) 
	private String[] custAcntDayLmt;	//欲修改每日限額 (CUST_ACNT) 
	private String[] custAcntMnthLmt;	//欲修改每月限額(CUST_ACNT) 
	private String[] custAcntLinkEcUser;	//key:欲修改會員代碼(CUST_ACNT_LINK)
	private String[] custAcntLinkRealAcnt;	//key:欲修改實體帳號(CUST_ACNT_LINK)
	private String[] custAcntLinkTrnsLmt;	//欲修改實體單筆自定限額(CUST_ACNT_LINK)
	private String[] custAcntLinkDayLmt;	//欲修改每日自定限額(CUST_ACNT_LINK)
	private String[] custAcntLinkMnthLmt;	//欲修改每月自定限額(CUST_ACNT_LINK)
	//for 操作記錄
	private List<CustAcntLink> optCustAcntLink;	//平台對應的綁定帳號
	private List<CustAcnt> optCustAcntList;	//會員帳號資料
	
	private String[] custAcntLinkGrad;      //交易限額-等級(CUST_ACNT_LINK) 操作記錄使用
	private List<CustAcntLink> custAcntLinkList; //平台對應的綁定帳號list(會員操作記錄)
	private String ecName;                  //電商平台名稱
	private Long availableBalanceMonth;			//月可用餘額
	private Long usedAmountMonth;				//月已使用金額
	private Long availableBalanceDay;			//日可用餘額
	private Long usedAmountDay;					//日已使用金額
	
	// constructor
	public CustAcnt() {}
	
	public boolean getInitQuery() {
		return initQuery;
	}

	public void setInitQuery(boolean initQuery) {
		this.initQuery = initQuery;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public CustData getCustData() {
		return custData;
	}

	public void setCustData(CustData custData) {
		this.custData = custData;
	}

	public List<CustPltf> getCustPltfList() {
		return custPltfList;
	}

	public void setCustPltfList(List<CustPltf> custPltfList) {
		this.custPltfList = custPltfList;
	}

	public HashMap<String, BaseLimt> getBaseLimt() {
		return baseLimt;
	}

	public void setBaseLimt(HashMap<String, BaseLimt> baseLimt) {
		this.baseLimt = baseLimt;
	}

	public HashMap<String, List<CustAcntLink>> getCustAcntLink() {
		return custAcntLink;
	}

	public void setCustAcntLink(HashMap<String, List<CustAcntLink>> custAcntLink) {
		this.custAcntLink = custAcntLink;
	}

	public List<CustAcnt> getCustAcntList() {
		return custAcntList;
	}

	public void setCustAcntList(List<CustAcnt> custAcntList) {
		this.custAcntList = custAcntList;
	}

	public String getSelectEcId() {
		return selectEcId;
	}

	public void setSelectEcId(String selectEcId) {
		this.selectEcId = selectEcId;
	}

	public String[] getCustAcntRealAcnt() {
		return custAcntRealAcnt;
	}

	public void setCustAcntRealAcnt(String[] custAcntRealAcnt) {
		this.custAcntRealAcnt = custAcntRealAcnt;
	}

	public String[] getCustAcntTrnsLmt() {
		return custAcntTrnsLmt;
	}

	public void setCustAcntTrnsLmt(String[] custAcntTrnsLmt) {
		this.custAcntTrnsLmt = custAcntTrnsLmt;
	}

	public String[] getCustAcntDayLmt() {
		return custAcntDayLmt;
	}

	public void setCustAcntDayLmt(String[] custAcntDayLmt) {
		this.custAcntDayLmt = custAcntDayLmt;
	}

	public String[] getCustAcntMnthLmt() {
		return custAcntMnthLmt;
	}

	public void setCustAcntMnthLmt(String[] custAcntMnthLmt) {
		this.custAcntMnthLmt = custAcntMnthLmt;
	}

	public String[] getCustAcntLinkEcUser() {
		return custAcntLinkEcUser;
	}

	public void setCustAcntLinkEcUser(String[] custAcntLinkEcUser) {
		this.custAcntLinkEcUser = custAcntLinkEcUser;
	}

	public String[] getCustAcntLinkRealAcnt() {
		return custAcntLinkRealAcnt;
	}

	public void setCustAcntLinkRealAcnt(String[] custAcntLinkRealAcnt) {
		this.custAcntLinkRealAcnt = custAcntLinkRealAcnt;
	}

	public String[] getCustAcntLinkTrnsLmt() {
		return custAcntLinkTrnsLmt;
	}

	public void setCustAcntLinkTrnsLmt(String[] custAcntLinkTrnsLmt) {
		this.custAcntLinkTrnsLmt = custAcntLinkTrnsLmt;
	}

	public String[] getCustAcntLinkDayLmt() {
		return custAcntLinkDayLmt;
	}

	public void setCustAcntLinkDayLmt(String[] custAcntLinkDayLmt) {
		this.custAcntLinkDayLmt = custAcntLinkDayLmt;
	}

	public String[] getCustAcntLinkMnthLmt() {
		return custAcntLinkMnthLmt;
	}

	public void setCustAcntLinkMnthLmt(String[] custAcntLinkMnthLmt) {
		this.custAcntLinkMnthLmt = custAcntLinkMnthLmt;
	}

	public List<CustAcntLink> getOptCustAcntLink() {
		return optCustAcntLink;
	}

	public void setOptCustAcntLink(List<CustAcntLink> optCustAcntLink) {
		this.optCustAcntLink = optCustAcntLink;
	}

	public List<CustAcnt> getOptCustAcntList() {
		return optCustAcntList;
	}

	public void setOptCustAcntList(List<CustAcnt> optCustAcntList) {
		this.optCustAcntList = optCustAcntList;
	}
	public String[] getCustAcntLinkGrad() {
		return custAcntLinkGrad;
	}
	public void setCustAcntLinkGrad(String[] custAcntLinkGrad) {
		this.custAcntLinkGrad = custAcntLinkGrad;
	}
	public List<CustAcntLink> getCustAcntLinkList() {
		return custAcntLinkList;
	}
	public void setCustAcntLinkList(List<CustAcntLink> custAcntLinkList) {
		this.custAcntLinkList = custAcntLinkList;
	}
	public String getEcName() {
		return ecName;
	}
	public void setEcName(String ecName) {
		this.ecName = ecName;
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
}
