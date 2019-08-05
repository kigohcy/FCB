/**
 * @(#) CustPltf.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/16, Evan
 * 	 1)First release
 *  v1.01, 2016/12/21, Yann
 *   1) TSBACL-143, 未綁定解鎖
 *  
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.BeanUtils;

import com.hitrust.bank.model.base.AbstractCustPltf;

public class CustPltf extends AbstractCustPltf implements Serializable {
	
	private static final long serialVersionUID = 2823922993256305756L;
	private boolean initQuery;	//判斷是否為查詢初始頁
	private String selectEcId;	//欲編輯的平台
	private String custId;	//身分證
	private String ecNameCh;	//join EC_DATA field 平台名稱
	private String ecNameEn;	//join EC_DATA field 平台名稱
	private CustData custData;	//會員資料
	private List<CustPltf> custPltfList;	//綁定的平台資料
	private HashMap<String, List<CustAcntLink>> custAcntLink;	//平台對應的綁定帳號
	private String[] ecKey;		//綁定平台的checkBox
	private String[] ecStts;	//綁定的平台的狀態
	private String[] acntKey;	//綁定帳號的checkBox
	private String[] acntStts;	//綁定帳號的狀態
	private List<CustAcntLink> afterAcntLink;	//更新後的acntLink
	private CustPltf afterCustPltf;	//更新後的 custPltf
	private CustData afterCustData; //更新後的 custData
	private String whoUpdate;	//更新平台EC 或 是  帳號 ACNT
	private String custStts;	// 會員狀態(call web service)
	private String qCustId;	    //v1.01, 查詢身分證字號
	
	// constructor
	public CustPltf() {}
	
	public CustPltf(CustPltf custPltf, String ecNameCh, String ecNameEn) {
		BeanUtils.copyProperties(custPltf, this);
		this.ecNameCh = ecNameCh;
		this.ecNameEn = ecNameEn;
	}

	// =============== Getter & Setter ===============
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
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
	public HashMap<String, List<CustAcntLink>> getCustAcntLink() {
		return custAcntLink;
	}
	public void setCustAcntLink(HashMap<String, List<CustAcntLink>> custAcntLink) {
		this.custAcntLink = custAcntLink;
	}
	public boolean isInitQuery() {
		return initQuery;
	}
	public void setInitQuery(boolean initQuery) {
		this.initQuery = initQuery;
	}
	public String getSelectEcId() {
		return selectEcId;
	}
	public void setSelectEcId(String selectEcId) {
		this.selectEcId = selectEcId;
	}
	public String[] getEcKey() {
		return ecKey;
	}
	public void setEcKey(String[] ecKey) {
		this.ecKey = ecKey;
	}
	public String[] getEcStts() {
		return ecStts;
	}
	public void setEcStts(String[] ecStts) {
		this.ecStts = ecStts;
	}
	public String[] getAcntKey() {
		return acntKey;
	}
	public void setAcntKey(String[] acntKey) {
		this.acntKey = acntKey;
	}
	public String[] getAcntStts() {
		return acntStts;
	}
	public void setAcntStts(String[] acntStts) {
		this.acntStts = acntStts;
	}
	public List<CustAcntLink> getAfterAcntLink() {
		return afterAcntLink;
	}
	public void setAfterAcntLink(List<CustAcntLink> afterAcntLink) {
		this.afterAcntLink = afterAcntLink;
	}
	public CustPltf getAfterCustPltf() {
		return afterCustPltf;
	}
	public void setAfterCustPltf(CustPltf afterCustPltf) {
		this.afterCustPltf = afterCustPltf;
	}
	public CustData getAfterCustData() {
		return afterCustData;
	}
	public void setAfterCustData(CustData afterCustData) {
		this.afterCustData = afterCustData;
	}

	public String getWhoUpdate() {
		return whoUpdate;
	}

	public void setWhoUpdate(String whoUpdate) {
		this.whoUpdate = whoUpdate;
	}
	public String getCustStts() {
		return custStts;
	}
	public void setCustStts(String custStts) {
		this.custStts = custStts;
	}
	//v1.01
	public String getqCustId() {
		return qCustId;
	}
	public void setqCustId(String qCustId) {
		this.qCustId = qCustId;
	}
}
