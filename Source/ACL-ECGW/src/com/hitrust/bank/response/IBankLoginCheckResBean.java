/**
 * @(#) ACLinkCancelResBean.java
 *
 * Directions: 取消連結帳戶綁定 回傳值
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/27, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.response;

import com.hitrust.acl.response.AbstractResponseBean;

public class IBankLoginCheckResBean extends AbstractResponseBean{
	private String autoRedirectWaitSec = "";
	private String ack = "";
	
	public String getAutoRedirectWaitSec() {
		return autoRedirectWaitSec;
	}
	public void setAutoRedirectWaitSec(String autoRedirectWaitSec) {
		this.autoRedirectWaitSec = autoRedirectWaitSec;
	}
	public String getAck() {
		return ack;
	}
	public void setAck(String ack) {
		this.ack = ack;
	}
	
	
}
