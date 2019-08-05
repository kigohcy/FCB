/**
 * @(#) MsgSour.java
 *
 * Directions: 簡訊資料檔
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/13, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;

public class MsgSour extends GenericBean {

	private static final long serialVersionUID = -5153409027575104084L;
	
	// =============== Table Attribute ===============
	public String GroupID;		// 簡訊中心的群組代號 (測試環境：TSCARD 正式環境：TSBANK)
	public String UserName;		// 使用者帳號
	public String UserPassword;
	public String OrderTime;	// 簡訊預約時間
	public String ExpireTime;	// 簡訊有效期限
	public String MsgType;		// 宵禁延遲發送旗標
	public String DestCategory; // 分行代碼或是部門代碼
	public String DestName;		// 收訊人名稱
	public String DestNo;		// 手機號碼 (請填入09帶頭的手機號碼)
	public String MsgData;		// 簡訊內容 (若有換行的需求，請填入ASCII Code 6代表換行)
	public String StatusFlag;
	public String Filler;		// 簡訊優先權 (有效值為 1~5，5 的優先權最高)
	public int SerialNo;		// 資料序號
	public String StatusTime;	
	public String MID;			// 客戶端留欄位
	public String ObjectID;		// 客戶端留欄位

	@Override
	protected void toXML() {
		this.putTableName("MsgSour");
		this.putField("GroupID", 	  this.GroupID, 	 "S", "N");
		this.putField("UserName", 	  this.GroupID, 	 "S", "N");
		this.putField("UserPassword", this.GroupID, 	 "S", "N");
		this.putField("OrderTime", 	  this.OrderTime, 	 "S", "N");
		this.putField("ExpireTime",   this.ExpireTime, 	 "S", "N");
		this.putField("MsgType", 	  this.MsgType, 	 "S", "N");
		this.putField("DestCategory", this.DestCategory, "S", "N");
		this.putField("DestName", 	  this.DestName, 	 "S", "N");
		this.putField("DestNo", 	  this.DestNo,		 "S", "N");
		this.putField("MsgData", 	  this.MsgData, 	 "S", "N");
		this.putField("StatusFlag",   this.StatusFlag, 	 "S", "N");
		this.putField("Filler", 	  this.Filler, 		 "S", "N");
		this.putField("StatusTime",   this.StatusTime, 	 "S", "N");
		this.putField("SerialNo", 	  this.SerialNo, 	 "I", "N");
		this.putField("MID", 		  this.MID, 		 "S", "N");
		this.putField("ObjectID", 	  this.ObjectID, 	 "S", "N");
		
	}

	@Override
	protected void fromXML() {
		this.GroupID 	  = this.getFieldString("GroupID");
		this.UserName 	  = this.getFieldString("UserName");
		this.UserPassword = this.getFieldString("UserPassword");
		this.OrderTime    = this.getFieldString("OrderTime");
		this.ExpireTime   = this.getFieldString("ExpireTime");
		this.MsgType 	  = this.getFieldString("MsgType");
		this.DestCategory = this.getFieldString("DestCategory");
		this.DestName 	  = this.getFieldString("DestName");
		this.DestNo 	  = this.getFieldString("DestNo");
		this.MsgData 	  = this.getFieldString("MsgData");
		this.StatusFlag   = this.getFieldString("StatusFlag");
		this.Filler 	  = this.getFieldString("Filler");
		this.SerialNo 	  = this.getFieldInt("SerialNo");
		this.StatusTime   = this.getFieldString("StatusTime");
		this.MID 		  = this.getFieldString("MID");
		this.ObjectID 	  = this.getFieldString("ObjectID");
	}

}
