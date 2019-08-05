/**
 * @(#) TrnsData.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : TrnsData bean
 * 
 * Modify History:
 *  v1.00, 2016/03/25, Yann
 *   1) First release
 *  v1.01, 2018/03/20
 *   1) 新增手續費上限, 手續費下限, IP欄位
 *  
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class TrnsData extends GenericBean {
	private static final long serialVersionUID = 5964377032040734204L;
	
	public String EC_ID;     //平台代碼
	public String EC_MSG_NO; //平台訊息序號
	public String CUST_ID;   //身分證字號
	public String CUST_SERL; //會員服務序號
	public String EC_USER;   //平台會員代碼
	public String TRNS_TYPE; //交易類別 A:扣款, B:退款, C:提領, D:儲值
	public String REAL_ACNT; //實體帳號
	public String RECV_ACNT; //入帳帳號
	public String TRNS_DTTM; //交易日期
	public int TRNS_AMNT;    //交易金額
	public String TRNS_STTS; //交易狀態
	public String ORDR_NO;   //訂單編號
	public int BACK_AMNT;    //訂單餘額
	public String TRNS_NOTE; //交易備註
	public String ERR_CODE;  //錯誤代碼 
	public String HOST_CODE; //主機回應代碼
	public String HOST_SEQ;  //主機處理序號
	public String FEE_TYPE;  //收費方式
	public double FEE_RATE;  //費率
	public int FEE_AMNT;     //手續費金額
	public String ACNT_INDT; //帳號識別碼
	public String MDFY_DTTM; //最後異動時間 
	public String MAIL_NOTC; //MAIL通知註記
	public String MAIL_DTTM; //MAIL通知時間
	public String TELE_NO;   //電文序號
	public Integer MIN_FEE;		 //手續費上限
	public Integer MAX_FEE;		 //手續費下限
	public String IP; 		 // IP位置
	//20190619 Add 交易失敗時記錄上下行電文 Begin
    public String TITA;       // 上行電文
    public String TOTA;       //下行電文
  //20190619 Add 交易失敗時記錄上下行電文 End
	
	public TrnsData() {
		super();
	}

	public TrnsData(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("TRNS_DATA");
		this.putField("EC_ID"     , this.EC_ID     , "S", "Y");
		this.putField("EC_MSG_NO" , this.EC_MSG_NO , "S", "Y");
		this.putField("CUST_ID"   , this.CUST_ID   , "S", "N");
		this.putField("CUST_SERL" , this.CUST_SERL , "S", "N");
		this.putField("EC_USER"   , this.EC_USER   , "S", "N");
		this.putField("TRNS_TYPE" , this.TRNS_TYPE , "S", "N");
		this.putField("REAL_ACNT" , this.REAL_ACNT , "S", "N");
		this.putField("RECV_ACNT" , this.RECV_ACNT , "S", "N");
		this.putField("TRNS_DTTM" , this.TRNS_DTTM , "S", "N");
		this.putField("TRNS_AMNT" , this.TRNS_AMNT , "I", "N");
		this.putField("TRNS_STTS" , this.TRNS_STTS , "S", "N");
		this.putField("ORDR_NO"   , this.ORDR_NO   , "S", "N");
		this.putField("BACK_AMNT" , this.BACK_AMNT , "I", "N");
		this.putField("TRNS_NOTE" , this.TRNS_NOTE , "S", "N");
		this.putField("ERR_CODE"  , this.ERR_CODE  , "S", "N");
		this.putField("HOST_CODE" , this.HOST_CODE , "S", "N");
		this.putField("HOST_SEQ"  , this.HOST_SEQ  , "S", "N");
		this.putField("FEE_TYPE"  , this.FEE_TYPE  , "S", "N");
		this.putField("FEE_RATE"  , this.FEE_RATE  , "D", "N");
		this.putField("FEE_AMNT"  , this.FEE_AMNT  , "I", "N");
		this.putField("ACNT_INDT" , this.ACNT_INDT , "S", "N");
		this.putField("MDFY_DTTM" , this.MDFY_DTTM , "S", "N");
		this.putField("MAIL_NOTC" , this.MAIL_NOTC , "S", "N");
		this.putField("MAIL_DTTM" , this.MAIL_DTTM , "S", "N");
		this.putField("TELE_NO"  , this.TELE_NO    , "S", "N");
		this.putField("MIN_FEE" , this.MIN_FEE , "I", "N");
		this.putField("MAX_FEE" , this.MAX_FEE , "I", "N");
		this.putField("IP", this.IP, "S", "N");
		//20190619 Add 交易失敗時記錄上下行電文 Begin
		this.putField("TITA", this.TITA, "S", "N");
		this.putField("TOTA", this.TOTA, "S", "N");
		//20190619 Add 交易失敗時記錄上下行電文 End
	}

	protected void fromXML() {
		this.EC_ID     = this.getFieldString("EC_ID");
		this.EC_MSG_NO = this.getFieldString("EC_MSG_NO");
		this.CUST_ID   = this.getFieldString("CUST_ID");
		this.CUST_SERL = this.getFieldString("CUST_SERL");
		this.EC_USER   = this.getFieldString("EC_USER");
		this.TRNS_TYPE = this.getFieldString("TRNS_TYPE");
		this.REAL_ACNT = this.getFieldString("REAL_ACNT");
		this.RECV_ACNT = this.getFieldString("RECV_ACNT");
		this.TRNS_DTTM = this.getFieldString("TRNS_DTTM");
		this.TRNS_AMNT = this.getFieldInt("TRNS_AMNT");
		this.TRNS_STTS = this.getFieldString("TRNS_STTS");
		this.ORDR_NO   = this.getFieldString("ORDR_NO");
		this.BACK_AMNT = this.getFieldInt("BACK_AMNT");
		this.TRNS_NOTE = this.getFieldString("TRNS_NOTE");
		this.ERR_CODE  = this.getFieldString("ERR_CODE");
		this.HOST_CODE = this.getFieldString("HOST_CODE");
		this.HOST_SEQ  = this.getFieldString("HOST_SEQ");
		this.FEE_TYPE  = this.getFieldString("FEE_TYPE");
		this.FEE_RATE  = this.getFieldDouble("FEE_RATE");
		this.FEE_AMNT  = this.getFieldInt("FEE_AMNT");
		this.ACNT_INDT = this.getFieldString("ACNT_INDT");
		this.MDFY_DTTM = this.getFieldString("MDFY_DTTM");
		this.MAIL_NOTC = this.getFieldString("MAIL_NOTC");
		this.MAIL_DTTM = this.getFieldString("MAIL_DTTM");
		this.TELE_NO   = this.getFieldString("TELE_NO");
		this.MIN_FEE  = this.getFieldInt("MIN_FEE");
		this.MAX_FEE  = this.getFieldInt("MAX_FEE");
		this.IP 	   = this.getFieldString("IP");
		//20190619 Add 交易失敗時記錄上下行電文 Begin
		this.TITA 	   = this.getFieldString("TITA");
		this.TOTA 	   = this.getFieldString("TOTA");
		//20190619 Add 交易失敗時記錄上下行電文 End
	}
}
