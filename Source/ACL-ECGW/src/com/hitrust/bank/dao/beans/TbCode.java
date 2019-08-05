/**
 * @(#) TbCode.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : TbCode bean
 * 
 * Modify History:
 *  v1.00, 2016/03/25, Yann
 *   1) First release
 *  v1.01, 2016/12/28, Eason Hsu
 *   1) TSBACL-144, 網銀 & 晶片卡認證失敗, 錯誤訊息 Mapping 調整
 */

package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class TbCode extends GenericBean {
	private static final long serialVersionUID = 1678712449926967542L;
	
	public String LNGN;      //語系 
	public String CODE_ID;   //訊息代碼
	public String CODE_TYPE; //訊息類別
	public String CODE_DESC; //訊息說明
	public String SHOW_DESC; //顯示訊息 
	
	public String REF_CODE_ID; // v1.01, 參考訊息代碼

	public TbCode() {
		super();
	}

	public TbCode(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("TB_CODE");
		this.putField("LNGN"     , this.LNGN     , "S", "Y");
		this.putField("CODE_ID"  , this.CODE_ID  , "S", "Y");
		this.putField("CODE_TYPE", this.CODE_TYPE, "S", "N");
		this.putField("CODE_DESC", this.CODE_DESC, "S", "N");
		this.putField("SHOW_DESC", this.SHOW_DESC, "S", "N");
		
		this.putField("REF_CODE_ID", this.REF_CODE_ID, "S", "N"); // v1.01, 參考訊息代碼
	}

	protected void fromXML() {
		this.LNGN      = this.getFieldString("LNGN");
		this.CODE_ID   = this.getFieldString("CODE_ID");
		this.CODE_TYPE = this.getFieldString("CODE_TYPE");
		this.CODE_DESC = this.getFieldString("CODE_DESC");
		this.SHOW_DESC = this.getFieldString("SHOW_DESC");
		
		this.REF_CODE_ID = this.getFieldString("REF_CODE_ID"); // v1.01, 參考訊息代碼
	}
}
