/**
 * @(#) SvrSttings.java
 *
 * Directions: 服務設定
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/06/07, Eason Hsu
 *    1) First release
 *
 */

package com.hitrust.bank.model;

import java.util.List;

public class SrvSettings extends AclCommand {

	private static final long serialVersionUID = 943165732288315534L;
	
	// =============== 前端 request attributes ===============
	private String custId;	 // 身分證字號
	private String ecId;	 // 平台代碼
	private String ecUser;	 // 平台會員代碼
	private String realAcnt; // 實體帳號
	
	// =============== Response 物件 ===============
	private CustData custData;		 // 會員資料檔
	private List<CustPltf> pltfs;	 // 會員平台資料清單
	private List<CustPltf> pltfAcnts;// 會員帳號連結清單
	private String ecName;			 // 平台中文名稱
	
	// =============== Getter & Setter ===============
	public CustData getCustData() {
		return custData;
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
	public String getEcUser() {
		return ecUser;
	}
	public void setEcUser(String ecUser) {
		this.ecUser = ecUser;
	}
	public String getRealAcnt() {
		return realAcnt;
	}
	public void setRealAcnt(String realAcnt) {
		this.realAcnt = realAcnt;
	}
	public void setCustData(CustData custData) {
		this.custData = custData;
	}
	public List<CustPltf> getPltfs() {
		return pltfs;
	}
	public void setPltfs(List<CustPltf> pltfs) {
		this.pltfs = pltfs;
	}
	public List<CustPltf> getPltfAcnts() {
		return pltfAcnts;
	}
	public void setPltfAcnts(List<CustPltf> pltfAcnts) {
		this.pltfAcnts = pltfAcnts;
	}
	public String getEcName() {
		return ecName;
	}
	public void setEcName(String ecName) {
		this.ecName = ecName;
	}
}
