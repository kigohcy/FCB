/**
 * @(#)AbstractBaseLimt.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : BaseLimt base model
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

public class AbstractBaseLimt extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = 6405505355381762096L;
	
	// =============== Table Attribute ===============
	private String grad;   // 等級
	private Long trnsLimt; // 單筆限額
	private Long mnthLimt; // 每月限額
	private Long dayLimt;  // 每日限額 
	
	// =============== Getter & Setter ===============
	public String getGrad() {
		return this.grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}
	public Long getTrnsLimt() {
		return this.trnsLimt;
	}
	public void setTrnsLimt(Long trnsLimt) {
		this.trnsLimt = trnsLimt;
	}
	public Long getMnthLimt() {
		return this.mnthLimt;
	}
	public void setMnthLimt(Long mnthLimt) {
		this.mnthLimt = mnthLimt;
	}
	public Long getDayLimt() {
		return this.dayLimt;
	}
	public void setDayLimt(Long dayLimt) {
		this.dayLimt = dayLimt;
	}

}
