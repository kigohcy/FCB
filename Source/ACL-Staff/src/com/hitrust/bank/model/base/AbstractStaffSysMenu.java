/**
 * @(#) AbstractStaffSysMenu.java
 * 銀行端系統模組檔 
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

public class AbstractStaffSysMenu extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = -4790639871885686726L;
	
	// =============== KEY ===============
	private AbstractStaffSysMenu.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private String menuName; // 功能模組名稱 
	private Integer serl;	 // 顯示順序 
	
	// =============== Getter & Setter ===============
	public AbstractStaffSysMenu.Id getId() {
		return this.id;
	}
	public void setId(AbstractStaffSysMenu.Id id) {
		this.id = id;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public Integer getSerl() {
		return serl;
	}
	public void setSerl(Integer serl) {
		this.serl = serl;
	}
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = -8886015282634618285L;
		
		// =============== Table Attribute ===============
		private String lngn;   // 語系 
		private String menuId; // 功能模組代碼 
		
		// =============== Getter & Setter ===============
		public String getLngn() {
			return this.lngn;
		}
		public void setLngn(String lngn) {
			this.lngn = lngn;
		}
		public String getMenuId() {
			return this.menuId;
		}
		public void setMenuId(String menuId) {
			this.menuId = menuId;
		}
	}
}
