/**
 * @(#) ACLSysModel.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/18, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.json;

import java.util.List;

public class AclSysModel {
	
	private String title;		    // 系統模組 
	private List<AclSysFnct> detail;// 系統功能
	
	public AclSysModel () {}
	
	/**
	 * @param title
	 * @param detail
	 */
	public AclSysModel(String title, List<AclSysFnct> detail) {
		super();
		this.title = title;
		this.detail = detail;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<AclSysFnct> getDetail() {
		return detail;
	}
	public void setDetail(List<AclSysFnct> detail) {
		this.detail = detail;
	}
	
}
