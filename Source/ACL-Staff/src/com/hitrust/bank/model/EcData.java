/**
 * @(#) EcData.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/28, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;

import com.hitrust.bank.model.base.AbstractEcData;

public class EcData extends AbstractEcData implements Serializable {

	private static final long serialVersionUID = -2477735137762756937L;

	// =============== Not Table Attribute ===============
	private List<EcData> ecDataList; 	// 平台代號清單
	private EcData ecData; 				// 平台代號資料

	// =============== Getters & Setters ===============
	public List<EcData> getEcDataList() {
		return ecDataList;
	}

	public void setEcDataList(List<EcData> ecDataList) {
		this.ecDataList = ecDataList;
	}

	public EcData getEcData() {
		return ecData;
	}

	public void setEcData(EcData ecData) {
		this.ecData = ecData;
	}
}
