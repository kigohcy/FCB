/**
 * @(#) AbstractRfci301Svip.java
 *
 * Directions: 限閱戶名單檔
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/07/27, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

public class AbstractRfci301Svip implements Serializable {
	private static final long serialVersionUID = 1339286364776565842L;
	
	// =============== KEY ===============
	private AbstractRfci301Svip.Id id; //複合 KEY
	
	// =============== Getter & Setter ===============
	public AbstractRfci301Svip.Id getId() {
		return id;
	}
	public void setId(AbstractRfci301Svip.Id id) {
		this.id = id;
	}
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		private static final long serialVersionUID = 1004652670041084395L;
		
		private String id1Type;    //
		private String id1No;      //自然人是11碼
		private Date dataDate;     //資料日
		private String custAcctNo; //BANCS客戶編號
		
		// =============== Getter & Setter ===============
		public String getId1Type() {
			return this.id1Type;
		}
		
		public void setId1Type(String id1Type) {
			this.id1Type = id1Type;
		}
		
		public String getId1No() {
			return this.id1No;
		}
		
		public void setId1No(String id1No) {
			this.id1No = id1No;
		}
		
		public Date getDataDate() {
			return this.dataDate;
		}
		
		public void setDataDate(Date dataDate) {
			this.dataDate = dataDate;
		}
		
		public String getCustAcctNo() {
			return this.custAcctNo;
		}
		
		public void setCustAcctNo(String custAcctNo) {
			this.custAcctNo = custAcctNo;
		}
	}
}
