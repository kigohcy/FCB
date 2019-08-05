/**
 * @(#) TsbAuditLog.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/04/06, Yann
 * 	 1) First release
 *  v1.01, 2016/07/07, Yann
 *   1) 新增二階欄位
 *  
 */
package com.hitrust.bank.json;

import java.io.Serializable;
import java.util.List;

public class TsbAuditLog implements Serializable {
	private static final long serialVersionUID = -4655731926784210L;
	
	private String custId;        //身分證字號
	private String ecId;          //平台代號
	private String ecNameCh;      //平台中文名稱
	private String ecNameEn;      //平台英文名稱
	private String feeType;       //收費方式
	private String feeRate;       //費率
	private String stts;          //狀態
	private String realAcnt;      //實體帳號
	private String entrNo;        //企業編號
	private String entrId;        //公司統編
	private String cntc;          //聯絡人
	private String tel;           //聯絡電話
	private String mail;          //電子郵件
	private String note;          //備註說明
	private String roleId;        //角色代碼
	private String roleName;      //角色名稱
	private String startDate;     //查詢起日
	private String endDate;       //查詢迄日
	private String queryType;     //查詢類別
	private String queryResult;   //執行結果
	private String execSrc;       //執行來源
	private String acnt;          //銀行存款帳號
	private String trnsType;      //交易類別
	private String userId;        //操作人員代號
	private String fnctId;        //功能代碼
	private String certCn;        //憑證識別碼
	private List list;            //多筆明細資料
	private String newsType;      //v1.01,公告類型
	private String title;         //v1.01,公告標題
	private String serl;          //v1.01,置頂
	
	// 20190619 Add 繳費稅收費方式及費率 Begin
	public String taxType; // 繳費稅收費方式
	public Double taxRate; // 繳費稅費率
	// 20190619 Add 繳費稅收費方式及費率 End
	
	
	// =============== Getters & Setters ===============
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
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}
	public String getStts() {
		return stts;
	}
	public void setStts(String stts) {
		this.stts = stts;
	}
	public String getRealAcnt() {
		return realAcnt;
	}
	public void setRealAcnt(String realAcnt) {
		this.realAcnt = realAcnt;
	}
	public String getEntrNo() {
		return entrNo;
	}
	public void setEntrNo(String entrNo) {
		this.entrNo = entrNo;
	}
	public String getEntrId() {
		return entrId;
	}
	public void setEntrId(String entrId) {
		this.entrId = entrId;
	}
	public String getCntc() {
		return cntc;
	}
	public void setCntc(String cntc) {
		this.cntc = cntc;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
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
	public String getQueryResult() {
		return queryResult;
	}
	public void setQueryResult(String queryResult) {
		this.queryResult = queryResult;
	}
	public String getExecSrc() {
		return execSrc;
	}
	public void setExecSrc(String execSrc) {
		this.execSrc = execSrc;
	}
	public String getAcnt() {
		return acnt;
	}
	public void setAcnt(String acnt) {
		this.acnt = acnt;
	}
	public String getTrnsType() {
		return trnsType;
	}
	public void setTrnsType(String trnsType) {
		this.trnsType = trnsType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFnctId() {
		return fnctId;
	}
	public void setFnctId(String fnctId) {
		this.fnctId = fnctId;
	}
	public String getCertCn() {
		return certCn;
	}
	public void setCertCn(String certCn) {
		this.certCn = certCn;
	}
	public List getTsbAuditLogDetl() {
		return list;
	}
	public void setTsbAuditLogDetl(List tsbAuditLogDetl) {
		this.list = tsbAuditLogDetl;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public String getNewsType() {
		return newsType;
	}
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSerl() {
		return serl;
	}
	public void setSerl(String serl) {
		this.serl = serl;
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
	
	
	//test
	/*public static void main(String args[]){
		//
		TsbAuditLog log = new TsbAuditLog();
		log.setEcId("1234");
		List list = new ArrayList();
		for(int i=0; i<5; i++){ //5筆
			TsbAuditLogDetl detl = new TsbAuditLogDetl();
			detl.setU("123456789012");
			detl.setA("1234567890123456");
			detl.setT("12345678");
			detl.setD("12345678");
			detl.setM("12345678");
			//detl.setStts("00");
			list.add(detl);
		}
		log.setTsbAuditLogDetl(list);
		String str = JsonUtil.object2Json(log, false);
		System.out.println("str="+str);
		System.out.println("len="+str.length());
	}*/
}
