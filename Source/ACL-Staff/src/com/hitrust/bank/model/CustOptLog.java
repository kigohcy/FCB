/**
 * @(#) CustOptLog.java
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
import java.util.List;

import com.hitrust.bank.model.base.AbstractCustOptLog;
import com.hitrust.framework.model.Command;

public class CustOptLog extends AbstractCustOptLog implements Serializable {

	private static final long serialVersionUID = -5003524578783418256L;

	private String startDate;
	private String endDate;
	private String queryLimt;
	private List<CustSysFnct> custSysFnct; // fnct清單
	private boolean initFlag = true;
	private String qUserId;
	private String qFnctId;
	private Command before;   //異動前記錄
	private Command after;    //異動後記錄
	private String q_fnctId;  //功能代碼 for 異動明細
	private String q_action;  //執行動作 for 異動明細
	private String optCurrentPage;	//記錄操作頁數

	public String getqUserId() {
		return qUserId;
	}

	public String getqFnctId() {
		return qFnctId;
	}

	public void setqUserId(String qUserId) {
		this.qUserId = qUserId;
	}

	public void setqFnctId(String qFnctId) {
		this.qFnctId = qFnctId;
	}

	public boolean isInitFlag() {
		return initFlag;
	}

	public void setInitFlag(boolean initFlag) {
		this.initFlag = initFlag;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getQueryLimt() {
		return queryLimt;
	}

	public void setQueryLimt(String queryLimt) {
		this.queryLimt = queryLimt;
	}

	public List<CustSysFnct> getCustSysFnct() {
		return custSysFnct;
	}

	public void setCustSysFnct(List<CustSysFnct> custSysFnct) {
		this.custSysFnct = custSysFnct;
	}

	public Command getBefore() {
		return before;
	}

	public void setBefore(Command before) {
		this.before = before;
	}

	public Command getAfter() {
		return after;
	}

	public void setAfter(Command after) {
		this.after = after;
	}

	public String getQ_fnctId() {
		return q_fnctId;
	}

	public void setQ_fnctId(String q_fnctId) {
		this.q_fnctId = q_fnctId;
	}

	public String getQ_action() {
		return q_action;
	}

	public void setQ_action(String q_action) {
		this.q_action = q_action;
	}
	
	public String getOptCurrentPage() {
		return optCurrentPage;
	}

	public void setOptCurrentPage(String optCurrentPage) {
		this.optCurrentPage = optCurrentPage;
	}
}
