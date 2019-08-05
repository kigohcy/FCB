/**
 * @(#) AbstractStaffSysFnct.java
 * 銀行端系統功能檔
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/11/13, Eason Hsu
 * 	 1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

public class AbstractStaffSysFnct extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = 4544610101667175438L;

	// =============== KEY ===============
	private AbstractStaffSysFnct.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private String menuId;	 // 功能模組代碼
	private String fnctName; // 功能名稱
	private String isUse;	 // 是否使用
	private String isSet;	 // 是否使
	private String url;		 // URL
	private Integer serl;	 // 顯示順序

	// =============== Getter & Setter ===============
	public AbstractStaffSysFnct.Id getId() {
		return this.id;
	}
	public void setId(AbstractStaffSysFnct.Id id) {
		this.id = id;
	}
	public String getMenuId() {
		return this.menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getFnctName() {
		return this.fnctName;
	}
	public void setFnctName(String fnctName) {
		this.fnctName = fnctName;
	}
	public String getIsUse() {
		return this.isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getIsSet() {
		return this.isSet;
	}
	public void setIsSet(String isSet) {
		this.isSet = isSet;
	}
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getSerl() {
		return this.serl;
	}
	public void setSerl(Integer serl) {
		this.serl = serl;
	}
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = 9063428286366090114L;
		
		// =============== Table Attribute ===============
		private String lngn;	// 語系 
		private String fnctId;	// 功能代碼
		
		// =============== Getter & Setter ===============
		public String getLngn() {
			return this.lngn;
		}
		public void setLngn(String lngn) {
			this.lngn = lngn;
		}
		public String getFnctId() {
			return this.fnctId;
		}
		public void setFnctId(String fnctId) {
			this.fnctId = fnctId;
		}
	}
}
