/**
 * @(#) TbCode.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/03/03, Yann
 * 	 1)First release
 * 
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;

import com.hitrust.bank.model.base.AbstractTbCode;

public class TbCode extends AbstractTbCode implements Serializable {
	
	private static final long serialVersionUID = -869988694360020353L;
	private String qCodeId;	//查詢條件-訊息代碼
	private List<TbCode> tbCodeList; //查詢結果
	private String codeId; //輸入-訊息代碼
	private TbCode tbCode;
	private boolean queryFlag=false;
	public String getqCodeId() {
		return qCodeId;
	}
	public void setqCodeId(String qCodeId) {
		this.qCodeId = qCodeId;
	}
	public List<TbCode> getTbCodeList() {
		return tbCodeList;
	}
	public void setTbCodeList(List<TbCode> tbCodeList) {
		this.tbCodeList = tbCodeList;
	}
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	public TbCode getTbCode() {
		return tbCode;
	}
	public void setTbCode(TbCode tbCode) {
		this.tbCode = tbCode;
	}
	public boolean isQueryFlag() {
		return queryFlag;
	}
	public void setQueryFlag(boolean queryFlag) {
		this.queryFlag = queryFlag;
	}
	
	
}
