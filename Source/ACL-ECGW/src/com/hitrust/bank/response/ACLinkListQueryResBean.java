/**
 * @(#) ACLinkListQueryResBean.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 查詢可使用連結帳戶回傳 bean
 * 
 * Modify History:
 *  v1.00, 2016/03/30, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.response;

import java.util.ArrayList;
import java.util.List;
import com.hitrust.acl.response.AbstractResponseBean;

public class ACLinkListQueryResBean extends AbstractResponseBean {
	
	private List ACNT_INFO = new ArrayList();  //帳號資訊(多筆)
	
	
	// =============== Getter & Setter ===============
	public List getACNT_INFO() {
		return ACNT_INFO;
	}
	
	public void setACNT_INFO(List acntInfo) {
		ACNT_INFO = acntInfo;
	}
	
}
