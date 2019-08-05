/**
 * @(#) ACLink.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/04/08, Eason Hsu
 *   1) First release
 *	v1.01, 2016/10/28, Eason Hsu
 *	 1) TSBACL-122, 平台閘道、會員平台，同意條款 PDF 版本號依據 DB 參數(PRVS_VRSN)取得
 *	v1.02, 2016/12/02, Eason Hsu
 *	 1) TSBACL-137, ECGW 綁定連結帳號驗證方式調整
 *	v1.03, 2017/09/05, Eason Hsu
 *	 1) TSBACL-159, [Fortify] J2EE Bad Practices: Non-Serializable Object Stored in Session
 *  v1.04, 2018/03/20
 *   1) 新增ip
 */

package com.hitrust.bank.json;

import java.io.Serializable;

import com.hitrust.bank.bean.BindingAcnt;

public class ACLink implements Serializable {
	
	// v1.03, 修正 Fortify 白箱掃描(J2EE Bad Practices: Non-Serializable Object Stored in Session)
	private static final long serialVersionUID = -5289157695929689072L;

	public static final String ACLINK = "link"; // v1.02 session 名稱
	
	private String _error = "";			// v1.02
	
	private String msgNo = "";		    // 訊息序號
	private String ecId = "";		    // 平台代碼
	private String ecUser = "";		    // 平台會員代號
	private String custId = "";		    // 使用者身分證字號
	private String rsltUrl = ""; 	    // 綁定結果回傳URL
	private String succUrl = "";	    // 綁定成功導向頁
	private String failUrl = "";	    // 綁定失敗導向頁
	private String ecName = "";			// 平台名稱
	private String ecStts = "";		    // 平台狀態
	private String custStts = "";	    // 會員服務狀態
	private String pltfStts = ""; 		// 會員平台狀態
	private String linkStts = "";	    // 會員帳號連結狀態
	private String enableService = "";  // 是否已開通服務 (Y: 已開通, N: 未開通)
	private String identityType = "";   // 身分類別
	private String idetityAuthType = "";// 身分認證方式	
	private String surName = "";		// 會員姓名
	private String tlxNo = "";		    // 行動電話
	private String emailAddr = "";	    // 電子郵件
	private String linkAcnt = "";		// 綁定連結帳號
	private String custSerl = "";		// 會員服務序號
	private String sessionKey = "";	    // sessionKey
	private String userAgent = "";		// 使用者 Browser user-agent
	private String isMobile = "";		// 是否為行動裝置
	private BindingAcnt binding; 		//  
	private String prvsVrsn;			// v1.01, 同意條款版本號
	private String merchantId; 	// merchantId
	private String sign;	  	// sign
	private String txReqId;  	// txReqId
	private String uri0202;
	private String ip;
	
	// ========== 晶片卡登入參數 ==========
	private String iframeUrl = "";
	private String sysId = "";
	private String param = "";
	
	// =============== Getter & Setter ===============
	// v1.02
	public String getMsgNo() {
		return msgNo;
	}
	public String get_error() {
		return _error;
	}
	public void clean(){
		this._error = "";
	} 
	
	public void set_error(String _error) {
		this._error = _error;
	}
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
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
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getRsltUrl() {
		return rsltUrl;
	}
	public void setRsltUrl(String rsltUrl) {
		this.rsltUrl = rsltUrl;
	}
	public String getSuccUrl() {
		return succUrl;
	}
	public void setSuccUrl(String succUrl) {
		this.succUrl = succUrl;
	}
	public String getFailUrl() {
		return failUrl;
	}
	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}
	public String getEcName() {
		return ecName;
	}
	public void setEcName(String ecName) {
		this.ecName = ecName;
	}
	public String getEcStts() {
		return ecStts;
	}
	public void setEcStts(String ecStts) {
		this.ecStts = ecStts;
	}
	public String getCustStts() {
		return custStts;
	}
	public void setCustStts(String custStts) {
		this.custStts = custStts;
	}
	public String getPltfStts() {
		return pltfStts;
	}
	public void setPltfStts(String pltfStts) {
		this.pltfStts = pltfStts;
	}
	public String getLinkStts() {
		return linkStts;
	}
	public void setLinkStts(String linkStts) {
		this.linkStts = linkStts;
	}
	public String getEnableService() {
		return enableService;
	}
	public void setEnableService(String enableService) {
		this.enableService = enableService;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	public String getIdetityAuthType() {
		return idetityAuthType;
	}
	public void setIdetityAuthType(String idetityAuthType) {
		this.idetityAuthType = idetityAuthType;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public String getTlxNo() {
		return tlxNo;
	}
	public void setTlxNo(String tlxNo) {
		this.tlxNo = tlxNo;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public String getLinkAcnt() {
		return linkAcnt;
	}
	public void setLinkAcnt(String linkAcnt) {
		this.linkAcnt = linkAcnt;
	}
	public String getCustSerl() {
		return custSerl;
	}
	public void setCustSerl(String custSerl) {
		this.custSerl = custSerl;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getIsMobile() {
		return isMobile;
	}
	public void setIsMobile(String isMobile) {
		this.isMobile = isMobile;
	}
	public BindingAcnt getBinding() {
		return binding;
	}
	public void setBinding(BindingAcnt binding) {
		this.binding = binding;
	}
	// v1.01
	public String getPrvsVrsn() {
		return prvsVrsn;
	}
	public void setPrvsVrsn(String prvsVrsn) {
		this.prvsVrsn = prvsVrsn;
	}
	public String getIframeUrl() {
		return iframeUrl;
	}
	public void setIframeUrl(String iframeUrl) {
		this.iframeUrl = iframeUrl;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTxReqId() {
		return txReqId;
	}
	public void setTxReqId(String txReqId) {
		this.txReqId = txReqId;
	}
	public String getUri0202() {
		return uri0202;
	}
	public void setUri0202(String uri0202) {
		this.uri0202 = uri0202;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
