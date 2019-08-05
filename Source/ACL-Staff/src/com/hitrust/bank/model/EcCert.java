/**
 * @(#) EcCert.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/21, Eason Hsu
 *    1) JIRA-Number, First release
 *   V1.10, 2019/02/26
 *    1) 增加驗章測試功能
 */

package com.hitrust.bank.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitrust.acl.security.ra.RaCertInfo;
import com.hitrust.bank.model.base.AbstractEcCert;

public class EcCert extends AbstractEcCert {

	private static final long serialVersionUID = -9029941310120542096L;
	
	// =============== request Attribute ===============
	private String ecId; 	 	// 電商平台
	private String certCn;		// 憑證識別碼
	private String csr;			// CSR 資訊
	private transient MultipartFile file; // 繳費通知
	private String signatureVal;// 簽章值 
	//V1.10, 2019/02/26 增加驗章測試功能 Begin
	private String plainText;   // 簽章明文 
	//V1.10, 2019/02/26 增加驗章測試功能 End
	
	// =============== response Attribute ===============
	private String ecName;			    // 電商平台名稱
	private String fileName;			// 繳費通知檔名(for 操作記錄查詢顯示使用)
	private boolean queryFlag = false;  // 判斷是否執行查詢 
	private List<EcData> datas; 	  	// 電商平台物件
	private transient List<RaCertInfo> certInfos; // 電商平台憑證物件
	private transient RaCertInfo certInfo; // 憑證資訊
	private String hasFile;				   // 是否有上傳檔
	private List<EcCert> ecCertList; // 電商憑證物件(不可加transient, 會無法紀錄歷史紀錄)
	private EcCert ecCert;
	private String ecId4Update; 	 		// 電商平台 Update
	
	// =============== Getter & Setter ===============
	public String getEcId() {
		return ecId;
	}
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	public String getCertCn() {
		return certCn;
	}
	public void setCertCn(String certCn) {
		this.certCn = certCn;
	}
	public String getCsr() {
		return csr;
	}
	public void setCsr(String csr) {
		this.csr = csr;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getSignatureVal() {
		return signatureVal;
	}
	public void setSignatureVal(String signatureVal) {
		this.signatureVal = signatureVal;
	}
	public String getEcName() {
		return ecName;
	}
	public void setEcName(String ecName) {
		this.ecName = ecName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isQueryFlag() {
		return queryFlag;
	}
	public void setQueryFlag(boolean queryFlag) {
		this.queryFlag = queryFlag;
	}
	public List<EcData> getDatas() {
		return datas;
	}
	public void setDatas(List<EcData> datas) {
		this.datas = datas;
	}
	public List<RaCertInfo> getCertInfos() {
		return certInfos;
	}
	public void setCertInfos(List<RaCertInfo> certInfos) {
		this.certInfos = certInfos;
	}
	public RaCertInfo getCertInfo() {
		return certInfo;
	}
	public void setCertInfo(RaCertInfo certInfo) {
		this.certInfo = certInfo;
	}
	public String getHasFile() {
		return hasFile;
	}
	public void setHasFile(String hasFile) {
		this.hasFile = hasFile;
	}
	public List<EcCert> getEcCertList() {
		return ecCertList;
	}
	public void setEcCertList(List<EcCert> ecCertList) {
		this.ecCertList = ecCertList;
	}
	public EcCert getEcCert() {
		return ecCert;
	}
	public void setEcCert(EcCert ecCert) {
		this.ecCert = ecCert;
	}
	public String getEcId4Update() {
		return ecId4Update;
	}
	public void setEcId4Update(String ecId4Update) {
		this.ecId4Update = ecId4Update;
	}
	//V1.10, 2019/02/26 增加驗章測試功能 Begin
	public String getPlainText() {
		return plainText;
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	//V1.10, 2019/02/26 增加驗章測試功能 End
	
}
