/**
 * @(#)StaffOptLog.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 操作記錄查詢model
 * 
 * Modify History:
 *  v1.00, 2016/01/25, XXXX
 *   1) First release
 *  
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.hitrust.bank.model.base.AbstractStaffOptLog;
import com.hitrust.framework.model.Command;

public class StaffOptLog extends AbstractStaffOptLog implements Serializable{
	
	private static final long serialVersionUID = -2457201167118397576L;
	
	private String startDate; //開始時間 接收頁面參數
	private String endDate;   //結束時間
	private String quserId;   //操作人員代號
	private String qfnctId;   //功能代碼
	private String fnctName;  //功能名稱
	private Command before;   //異動前記錄
	private Command after;    //異動後記錄
	private String q_fnctId;  //功能代碼 for 異動明細
	private String q_action;  //執行動作 for 異動明細
    private boolean initQuery;	//判斷是否為查詢初始頁
    private String optCurrentPage;	//記錄操作頁數
	private List<StaffSysFnct> staffSysFnct; //fnct清單
	private String sysParam;	//系統參數物件

	// =============== Constructor ===============
	public StaffOptLog(){}
	
	// StaffOptLogDAO for queryByCondition 調用
	public StaffOptLog(StaffOptLog staffOptLog,String  fnctName ){
		BeanUtils.copyProperties(staffOptLog, this);
		this.fnctName = fnctName;
	}

	// =============== Getter & Setter ===============
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getQuserId() {
		return quserId;
	}
	public void setQuserId(String quserId) {
		this.quserId = quserId;
	}
	public String getQfnctId() {
		return qfnctId;
	}
	public void setQfnctId(String qfnctId) {
		this.qfnctId = qfnctId;
	}
	public String getFnctName() {
		return fnctName;
	}
	public void setFnctName(String fnctName) {
		this.fnctName = fnctName;
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
	public boolean isInitQuery() {
		return initQuery;
	}
	public void setInitQuery(boolean initQuery) {
		this.initQuery = initQuery;
	}
	public List<StaffSysFnct> getStaffSysFnct() {
		return staffSysFnct;
	}
	public void setStaffSysFnct(List<StaffSysFnct> staffSysFnct) {
		this.staffSysFnct = staffSysFnct;
	}
	public String getSysParam() {
		return sysParam;
	}
	public void setSysParam(String sysParam) {
		this.sysParam = sysParam;
	}

	public String getOptCurrentPage() {
		return optCurrentPage;
	}

	public void setOptCurrentPage(String optCurrentPage) {
		this.optCurrentPage = optCurrentPage;
	}
	
}
