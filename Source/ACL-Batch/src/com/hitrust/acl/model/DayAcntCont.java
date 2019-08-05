/**
 * @(#) DayAcntCont.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/06/06, Yann
 * 	 1)First release, 二階
 * 
 */
package com.hitrust.acl.model;

import java.io.Serializable;
import org.springframework.beans.BeanUtils;

import com.hitrust.acl.model.base.AbstractDayAcntCont;

public class DayAcntCont extends AbstractDayAcntCont implements Serializable {
	private static final long serialVersionUID = -29751714610785328L;
	
	// =============== Not Table Attribute ===============
	private String ecNameCh;	     //join EC_DATA field 平台名稱
	
	//constructor
	public DayAcntCont(){
	}
	
	public DayAcntCont(DayAcntCont dayAcntCont, String ecNameCh) {
		BeanUtils.copyProperties(dayAcntCont, this);
		this.ecNameCh = ecNameCh;
	}
	
	// =============== Getters & Setters ===============
	public String getEcNameCh() {
		return ecNameCh;
	}
	public void setEcNameCh(String ecNameCh) {
		this.ecNameCh = ecNameCh;
	}
}
