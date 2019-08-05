/**
 * @(#) AbstractTxncsSvip.java
 *
 * Directions: 敏感客戶名單檔
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

public class AbstractTxncsSvip implements Serializable {
	
	private static final long serialVersionUID = 8414516488693952375L;
	
	// =============== KEY ===============
	private AbstractTxncsSvip.Id id; // 複合 KEY
	
	// =============== Getter & Setter ===============
	public AbstractTxncsSvip.Id getId() {
		return id;
	}
	public void setId(AbstractTxncsSvip.Id id) {
		this.id = id;
	}

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = -4830456005179992952L;
		
		// =============== Table Attribute ===============
		private String name;	   // 客戶姓名
		private String idType;	   // ID 型態
		private String idNo;	   // 身分證號11碼
		private String idNo1;	   // 身分證號10碼
		private String custAcctNo; // 客戶編號
		private String actNo;	   // 客戶帳號
		private String flag;	   // 帳號總類
		private String lstMtnDt;   // 批次日
		
		// =============== Getter & Setter ===============
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIdType() {
			return idType;
		}
		public void setIdType(String idType) {
			this.idType = idType;
		}
		public String getIdNo() {
			return idNo;
		}
		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}
		public String getIdNo1() {
			return idNo1;
		}
		public void setIdNo1(String idNo1) {
			this.idNo1 = idNo1;
		}
		public String getCustAcctNo() {
			return custAcctNo;
		}
		public void setCustAcctNo(String custAcctNo) {
			this.custAcctNo = custAcctNo;
		}
		public String getActNo() {
			return actNo;
		}
		public void setActNo(String actNo) {
			this.actNo = actNo;
		}
		public String getFlag() {
			return flag;
		}
		public void setFlag(String flag) {
			this.flag = flag;
		}
		public String getLstMtnDt() {
			return lstMtnDt;
		}
		public void setLstMtnDt(String lstMtnDt) {
			this.lstMtnDt = lstMtnDt;
		}
		
	}
	

}
