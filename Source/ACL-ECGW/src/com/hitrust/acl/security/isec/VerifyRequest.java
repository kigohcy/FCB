/*
 * @(#)VerifyRequest
 *
 * Copyright (c) 2013 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/03/18, Ada Chen
 *   1) First release
 */

package com.hitrust.acl.security.isec;

public class VerifyRequest {
	
	private String commonName = null;// 簽章時所用的憑證的 commName
	private String signature = null; // 簽章值
	private String plainText = null; // 簽章原文
	private int verifyFlag = Secure.VERIFY_CERT;	// 驗章參數
													// 1 - verify() 驗證簽章、憑證以及檢查CRL
													// 0 - verifyNoCertVerify() 只驗證簽章值是否正確
	private boolean bRtn = true;	// 是(true)否(false)傳回 Issuer, Serial, Subject, Cert 等值

	/**
     * 取得前端簽章相關資料
     * @param signature XML簽章值
     * @param commonName 簽章時所用的憑證的 commName
     * @param plainText 簽章原文
     */
	public VerifyRequest(String signature, String commonName, String plainText) {
		this.signature = signature;
		this.commonName = commonName;
		this.plainText = plainText;
	}

    /**
     * Getter method for signature
     * @return
     */
	public String getSignature() {
		return this.signature;
	}
	
    /**
     * Getter method for plain text
	 * @return the plainText
	 */
	public String getPlainText() {
		return plainText;
	}

    /**
     * Getter method for common name
	 * @return the commonName
	 */
	public String getCommonName() {
		return commonName;
	}

	/**
     * Setter method for flags
     * @param flags 1-驗證簽章、憑證以及檢查CRL, 2-只驗證簽章值是否正確  
     * @return
     */	
	public void setVerifyFlag(int verifyFlag) {
		this.verifyFlag = verifyFlag;
	}
	
	/**
     * Getter method for flags
     * @return
     */
	public int getVerifyFlag() {
		return this.verifyFlag;
	}
	
    /**
     * Setter method for bRtn
     * @return
     */	
	public void setReturn(boolean bRtn) {
		this.bRtn = bRtn;
	}
	
    /**
     * Getter method for bRtn
     * @return
     */	
	public boolean getReturn() {
		return this.bRtn;
	}	
	
}
 
