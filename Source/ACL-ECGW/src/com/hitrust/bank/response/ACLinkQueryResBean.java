/**
 * @(#) ACLinkQueryResBean.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/28, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */
package com.hitrust.bank.response;

import com.hitrust.acl.response.AbstractResponseBean;

public class ACLinkQueryResBean extends AbstractResponseBean {
	
	private String INDT_ACNT = ""; // 帳號識別碼
	private String LINK_ACNT = ""; // 綁定實體帳號
	private String LINK_GRAD = ""; // 身分認證等級
	
	// =============== Getter & Setter ===============
	public String getINDT_ACNT() {
		return INDT_ACNT;
	}
	public void setINDT_ACNT(String iNDT_ACNT) {
		INDT_ACNT = iNDT_ACNT;
	}
	public String getLINK_ACNT() {
		return LINK_ACNT;
	}
	public void setLINK_ACNT(String lINK_ACNT) {
		LINK_ACNT = lINK_ACNT;
	}
	public String getLINK_GRAD() {
		return LINK_GRAD;
	}
	public void setLINK_GRAD(String lINK_GRAD) {
		LINK_GRAD = lINK_GRAD;
	}
	
}
