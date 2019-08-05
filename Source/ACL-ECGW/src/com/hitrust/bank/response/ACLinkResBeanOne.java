/**
 * @(#) ACLinkResBeanOne.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/04/11, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.response;

import com.hitrust.acl.response.AbstractResponseBean;

public class ACLinkResBeanOne extends AbstractResponseBean {
	
	private String S_KEY = "";	  // Session Key
	private String LINK_URL = ""; // 連結綁定URL
	
	// =============== Getter & Setter ===============
	public String getS_KEY() {
		return S_KEY;
	}
	public void setS_KEY(String S_KEY) {
		this.S_KEY = S_KEY;
	}
	public String getLINK_URL() {
		return LINK_URL;
	}
	public void setLINK_URL(String lINK_URL) {
		this.LINK_URL = lINK_URL;
	}

}
