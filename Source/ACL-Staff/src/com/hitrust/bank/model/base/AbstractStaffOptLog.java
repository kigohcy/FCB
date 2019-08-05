/**
 * @(#)AbstractStaffOptLog.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 行員操作記錄model
 * 
 * Modify History:
 *  v1.00, 2016/01/25, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;

public class AbstractStaffOptLog extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = -4343909260286847752L;
	
	// =============== Table Attribute ===============
	private String logNo;	 // 序號 
	private String userId;	 // 使用者代碼
	private String userName; // 使用者名稱
	private String fnctId;	 // 功能代碼
	private Date oprtDttm;	 // 執行時間
	private String action;	 // 執行動作
	private String rslt;	 // 執行結果
	private String ipAddr;	 // IP_ADDR 
	private String beforeId; // 異動前資料
	private String afterId;  // 異動後資料
	
	// =============== Getter & Setter ===============
	public String getLogNo() {
		return this.logNo;
	}
	public void setLogNo(String logNo) {
		this.logNo = logNo;
	}
	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFnctId() {
		return this.fnctId;
	}
	public void setFnctId(String fnctId) {
		this.fnctId = fnctId;
	}
	public Date getOprtDttm() {
		return this.oprtDttm;
	}
	public void setOprtDttm(Date oprtDttm) {
		this.oprtDttm = oprtDttm;
	}
	public String getAction() {
		return this.action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getRslt() {
		return this.rslt;
	}
	public void setRslt(String rslt) {
		this.rslt = rslt;
	}
	public String getIpAddr() {
		return this.ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getBeforeId() {
		return this.beforeId;
	}
	public void setBeforeId(String beforeId) {
		this.beforeId = beforeId;
	}
	public String getAfterId() {
		return this.afterId;
	}
	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}
}
