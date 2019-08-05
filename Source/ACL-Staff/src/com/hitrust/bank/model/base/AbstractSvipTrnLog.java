/**
 * @(#) AbstractSvipTrnLog.java
 *
 * Directions: 敏感客戶查詢LOG檔
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/06/23, Eason Hsu
 *    1) First release
 *
 */

package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;

public class AbstractSvipTrnLog extends AclCommand implements Serializable {

	private static final long serialVersionUID = 1004018077822081638L;
	
	// =============== KEY ===============
	private AbstractSvipTrnLog.Id id; // 複合 KEY
	
	// =============== Getter & Setter ===============
	public AbstractSvipTrnLog.Id getId() {
		return id;
	}
	public void setId(AbstractSvipTrnLog.Id id) {
		this.id = id;
	}

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {

		private static final long serialVersionUID = -5918646424181322946L;
		
		// =============== Table Attribute ===============
		private String tranDate;   // 交易日期
		private String tranTime;   // 交易時間
		private String empNo;	   // 員工編號
		private String systemCode; // 系統代碼
		private String systemName; // 系統名稱
		private String custId;	   // 客戶ID
		private String custName;   // 客戶名稱
		private String tranType;   // 交易類型
		private String tranSource; // 交易來源
		private String sysAcct;	   // 登入帳號
		private String memo1;      // 備註1
		private String memo2;	   // 備註2
		private String memo3;	   // 備註3
		private Integer rowid;	   // 識別碼
		private Date procDate;	   // 報表日期
		private Integer flag;	   // FLAG
		
		// =============== Getter & Setter ===============
		public String getTranDate() {
			return tranDate;
		}
		public void setTranDate(String tranDate) {
			this.tranDate = tranDate;
		}
		public String getTranTime() {
			return tranTime;
		}
		public void setTranTime(String tranTime) {
			this.tranTime = tranTime;
		}
		public String getEmpNo() {
			return empNo;
		}
		public void setEmpNo(String empNo) {
			this.empNo = empNo;
		}
		public String getSystemCode() {
			return systemCode;
		}
		public void setSystemCode(String systemCode) {
			this.systemCode = systemCode;
		}
		public String getSystemName() {
			return systemName;
		}
		public void setSystemName(String systemName) {
			this.systemName = systemName;
		}
		public String getCustId() {
			return custId;
		}
		public void setCustId(String custId) {
			this.custId = custId;
		}
		public String getCustName() {
			return custName;
		}
		public void setCustName(String custName) {
			this.custName = custName;
		}
		public String getTranType() {
			return tranType;
		}
		public void setTranType(String tranType) {
			this.tranType = tranType;
		}
		public String getTranSource() {
			return tranSource;
		}
		public void setTranSource(String tranSource) {
			this.tranSource = tranSource;
		}
		public String getSysAcct() {
			return sysAcct;
		}
		public void setSysAcct(String sysAcct) {
			this.sysAcct = sysAcct;
		}
		public String getMemo1() {
			return memo1;
		}
		public void setMemo1(String memo1) {
			this.memo1 = memo1;
		}
		public String getMemo2() {
			return memo2;
		}
		public void setMemo2(String memo2) {
			this.memo2 = memo2;
		}
		public String getMemo3() {
			return memo3;
		}
		public void setMemo3(String memo3) {
			this.memo3 = memo3;
		}
		public Integer getRowid() {
			return rowid;
		}
		public void setRowid(Integer rowid) {
			this.rowid = rowid;
		}
		public Date getProcDate() {
			return procDate;
		}
		public void setProcDate(Date procDate) {
			this.procDate = procDate;
		}
		public Integer getFlag() {
			return flag;
		}
		public void setFlag(Integer flag) {
			this.flag = flag;
		}
		
	}

}
