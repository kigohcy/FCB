/**
 * @(#)AbstractTbCode.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : TbCode base model
 * 
 * Modify History:
 *  v1.00, 2016/03/03, Yann
 *   1) First release
 *  v1.01, 2016/12/29, Eason Hsu
 *   1) TSBACL-144, 網銀 & 晶片卡認證失敗, 錯誤訊息 Mapping 調整
 */

package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

public class AbstractTbCode extends AclCommand implements Serializable {

	private static final long serialVersionUID = 3625689111215975559L;

	// =============== KEY ===============
	private AbstractTbCode.Id id; // 複合 KEY
	
	private String codeType; // 訊息類別
	private String codeDesc; // 訊息說明
	private String showDesc; // 顯示訊息
	private String refCodeId;// v1.01, 參考訊息代碼

	// =============== Getter & Setter ===============
	public AbstractTbCode.Id getId() {
		return this.id;
	}
	public void setId(AbstractTbCode.Id id) {
		this.id = id;
	}
	public String getCodeType() {
		return this.codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getCodeDesc() {
		return this.codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	public String getShowDesc() {
		return this.showDesc;
	}
	public void setShowDesc(String showDesc) {
		this.showDesc = showDesc;
	}
	
	// v1.01
	public String getRefCodeId() {
		return refCodeId;
	}
	public void setRefCodeId(String refCodeId) {
		this.refCodeId = refCodeId;
	}


	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = -6040490286657458552L;
		
		// =============== Table Attribute ===============
		private String lngn;   // 語系
		private String codeId; // 訊息代碼
		
		// =============== Getter & Setter ===============
		public String getLngn() {
			return this.lngn;
		}
		public void setLngn(String lngn) {
			this.lngn = lngn;
		}
		public String getCodeId() {
			return this.codeId;
		}
		public void setCodeId(String codeId) {
			this.codeId = codeId;
		}
	}
}
