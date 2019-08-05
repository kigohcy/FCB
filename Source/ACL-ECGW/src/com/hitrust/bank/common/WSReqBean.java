/**
 * @(#) WSReqBean.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : WSReqBean bean
 * 
 * Modify History:
 *  v1.00, 2016/04/08, Ada
 *   1) First release
 *  
 */
package com.hitrust.bank.common;

public class WSReqBean {
	
	private String ID;    //身分證號
	private String PWD;   //密碼
	private String USERCODE;  //使用者代號
	
	
	// =============== Getter & Setter ===============
	/**
	 * @return the ID
	 */
	public String getID() {
		return this.ID;
	}
	/**
	 * @param iD the ID to set
	 */
	public void setID(String iD) {
		this.ID = iD;
	}
	/**
	 * @return the PWD
	 */
	public String getPWD() {
		return this.PWD;
	}
	/**
	 * @param pWD the PWD to set
	 */
	public void setPWD(String pWD) {
		this.PWD = pWD;
	}
	/**
	 * @return the USERCODE
	 */
	public String getUSERCODE() {
		return this.USERCODE;
	}
	/**
	 * @param uSERCODE the USERCODE to set
	 */
	public void setUSERCODE(String uSERCODE) {
		this.USERCODE = uSERCODE;
	}
}
