/**
 * @(#) DayCustCont.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/06/06, Yann
 * 	 1)First release, 二階
 * 
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import org.springframework.beans.BeanUtils;

import com.hitrust.bank.model.base.AbstractDayCustCont;

public class DayCustCont extends AbstractDayCustCont implements Serializable {
	private static final long serialVersionUID = 9207910520316993519L;

	//
	public DayCustCont() {
	}
	
	public DayCustCont(DayCustCont dayCustCont) {
		BeanUtils.copyProperties(dayCustCont, this);
	}
	
}
