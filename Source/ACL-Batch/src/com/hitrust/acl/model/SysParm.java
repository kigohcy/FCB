/**
 * @(#) SysParm.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : SysParm model
 * 
 * Modify History:
 *  v1.00, 2016/02/24, Yann
 *   1) First release
 *  
 */
package com.hitrust.acl.model;

import java.util.List;

import com.hitrust.acl.model.base.AbstractSysParm;

public class SysParm extends AbstractSysParm {

	private static final long serialVersionUID = 8600598411631638658L;

	// =============== Not Table Attribute ===============
	private List<SysParm> sysParmList; // SysParm設定清單
	private SysParm sysParm; // SysParm設定資料
	// =============== Getters & Setters ===============

	public List<SysParm> getSysParmList() {
		return sysParmList;
	}

	public void setSysParmList(List<SysParm> sysParmList) {
		this.sysParmList = sysParmList;
	}

	public SysParm getSysParm() {
		return sysParm;
	}

	public void setSysParm(SysParm sysParm) {
		this.sysParm = sysParm;
	}

}
