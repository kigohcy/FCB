/**
 * @(#) WSResBean.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : WSResBean bean
 * 
 * Modify History:
 *  v1.00, 2016/04/08, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.common;

public class WSResBean {
	
	private String RESULT;    //0:失敗,1:成功,2:首次登入
	private String ERRORMSG;  //錯誤訊息
	
	private String RANK;      //會員等級
	private String CODE;      //所屬客群代碼
	private String TYPE;      //身分別
	
	private int PWDFAILTIMES;   //密碼錯誤次數(0:正常, 1-4:密碼錯誤n次, 9:Lock)
	private int CODEFAILTIMES;  //使用者代碼錯誤次數(0:正常, 1-4:代碼錯誤n次, 9:Lock)
	
	
	// =============== Getter & Setter ===============
	public String getResult() {
		return RESULT;
	}
	public void setResult(String result) {
		this.RESULT = result;
	}
	public String getErrorMsg() {
		return ERRORMSG;
	}
	public void setErrorMsg(String errorMsg) {
		this.ERRORMSG = errorMsg;
	}
	public String getRank() {
		return RANK;
	}
	public void setRank(String rank) {
		this.RANK = rank;
	}
	public String getCode() {
		return CODE;
	}
	public void setCode(String code) {
		this.CODE = code;
	}
	public String getType() {
		return TYPE;
	}
	public void setType(String type) {
		this.TYPE = type;
	}
	public int getPWDFAILTIMES() {
		return PWDFAILTIMES;
	}
	public void setPWDFAILTIMES(int PWDFAILTIMES) {
		this.PWDFAILTIMES = PWDFAILTIMES;
	}
	public int getCODEFAILTIMES() {
		return CODEFAILTIMES;
	}
	public void setCODEFAILTIMES(int CODEFAILTIMES) {
		this.CODEFAILTIMES = CODEFAILTIMES;
	}
}
