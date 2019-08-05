/**
 * @(#) AbstractTsbApauditlog.java
 *
 * Directions: 應用系統日誌
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/18, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

public class AbstractTsbApauditlog extends AclCommand implements Serializable {

	private static final long serialVersionUID = 5683637986500684304L;
	
	private AbstractTsbApauditlog.Id id;
	
	// =============== Getter & Setter ===============
	public AbstractTsbApauditlog.Id getId() {
		return id;
	}
	public void setId(AbstractTsbApauditlog.Id id) {
		this.id = id;
	}

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = -3534028945474092733L;
		
		// =============== Table Attribute ===============
		private String projCode;	 // 系統代碼
		private String userId;		 // 登入帳號
		private String procDatetime; // 日期時間
		private String clientIp;	 // User端IP
		private String fnType;		 // 紀錄類別
		private String fnName;		 // 交易代號
		private String fnStts;		 // 交易執行結果
		private String fnKeyvalue;	 // 輸入交易關鍵值
		private String fnProc;		 // 輸入欄位
		
		// =============== Getter & Setter ===============
		public String getProjCode() {
			return projCode;
		}
		public void setProjCode(String projCode) {
			this.projCode = projCode;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getProcDatetime() {
			return procDatetime;
		}
		public void setProcDatetime(String procDatetime) {
			this.procDatetime = procDatetime;
		}
		public String getClientIp() {
			return clientIp;
		}
		public void setClientIp(String clientIp) {
			this.clientIp = clientIp;
		}
		public String getFnType() {
			return fnType;
		}
		public void setFnType(String fnType) {
			this.fnType = fnType;
		}
		public String getFnName() {
			return fnName;
		}
		public void setFnName(String fnName) {
			this.fnName = fnName;
		}
		public String getFnStts() {
			return fnStts;
		}
		public void setFnStts(String fnStts) {
			this.fnStts = fnStts;
		}
		public String getFnKeyvalue() {
			return fnKeyvalue;
		}
		public void setFnKeyvalue(String fnKeyvalue) {
			this.fnKeyvalue = fnKeyvalue;
		}
		public String getFnProc() {
			return fnProc;
		}
		public void setFnProc(String fnProc) {
			this.fnProc = fnProc;
		}
	}
	
	
}
