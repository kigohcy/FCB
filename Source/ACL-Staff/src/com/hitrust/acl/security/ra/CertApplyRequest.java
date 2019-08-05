/*
 * @(#)CertApplyRequest.java
 *
 * Copyright (c) 2013 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/03/18, Ada Chen
 *   1) First release
 */
package com.hitrust.acl.security.ra;

public class CertApplyRequest {
 
	private String certType;
	private String email;
	private String cn;
	private String csr;
	 
		
	/**
	 * @param certType 憑證種類
	 * @param email 電子郵件
	 * @param cn Common Name(憑證識別碼) 
	 * @param csr 憑證請求檔(PKCS10)
	 */
	public CertApplyRequest(String certType, String email, String cn, String csr) {
		this.certType = certType;
		this.email    = email;
		this.cn       = cn;
		this.csr      = csr;
	}
	 
	public String getCertType() {
		return this.certType;
	}
	 
	public String getEmail() {
		return this.email;
	}
	 
	public String getCn() {
		return this.cn;
	}
	 
	public String getCsr() {
		return this.csr;
	}

}
 
