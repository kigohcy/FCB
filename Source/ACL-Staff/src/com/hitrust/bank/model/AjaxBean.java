/**
 * @(#)AjaxBean.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.All rights reserved.
 *
 * Description :Ajax bean
 * 
 * Modify History:
 *  v1.00, 2016/03/03, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model;

import java.util.Date;

public class AjaxBean extends AclCommand {
	
	private static final long serialVersionUID = 1L;

	private String errorMsg; //錯誤訊息
	
	private String codeDesc; //訊息說明
	private String showDesc; //顯示訊息
	private String codeId;	 //錯誤代碼
	
	private String title;
	private String content;
	private Date bgnDate;
	private Date mdfyDttm;
	
	// =============== Getter & Setter ===============
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public String getCodeDesc() {
		return codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	public String getShowDesc() {
		return showDesc;
	}
	public void setShowDesc(String showDesc) {
		this.showDesc = showDesc;
	}
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getBgnDate() {
		return bgnDate;
	}
	public void setBgnDate(Date bgnDate) {
		this.bgnDate = bgnDate;
	}
	public Date getMdfyDttm() {
		return mdfyDttm;
	}
	public void setMdfyDttm(Date mdfyDttm) {
		this.mdfyDttm = mdfyDttm;
	}
}
