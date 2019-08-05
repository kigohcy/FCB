/*
 * @(#)Host001045ResponseInfo.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Description:
 *		001045 下行電文 JavaBean
 *
 * Modify History:
 * v1.00, 2016/04/22, HiTRUST
 *	1) First release
 */

package com.hitrust.bank.telegram.res;

/**
 * 001045 下行電文欄位 Java Bean
 * @author HiTRUST
 */
public class TransactionResponseInfo extends HeaderHostResponseInfo {

	private String OUTPUT_CODE; //Output Code
	private String ERR_CODE;    //Error Code
	private String HOST_DATE;   //主機帳務日期
	private String HOST_TIME;   //主機帳務時間
	private String HostSeqNo; 	//中心處理序號
	private String JRNL_NO; 	//主機交易序號
	
	//20190619 Add 交易失敗時記錄上下行電文 Begin
	private String TITA; 		//主機上行電文
	private String TOTA; 		//主機下行電文
	//20190619 Add 交易失敗時記錄上下行電文 End
	
	/**
	 * 欄位:OUTPUT_CODE 的 getter
	 * @return OUTPUT_CODE Output Code
	 */
	public String getOUTPUT_CODE() {
		return this.OUTPUT_CODE;
	}
	/**
	 * 欄位:OUTPUT_CODE(Output Code) 的 setter<br>
	 * 備註:A/C Link 判斷必需為 'RF' 才視為成功
	 * @param oUTPUT_CODE Output Code
	 */
	public void setOUTPUT_CODE(String oUTPUT_CODE) {
		this.OUTPUT_CODE = oUTPUT_CODE;
	}
	/**
	 * 欄位:ERR_CODE 的 getter
	 * @return ERR_CODE Error Code
	 */
	public String getERR_CODE() {
		return this.ERR_CODE;
	}
	/**
	 * 欄位:ERR_CODE(Error Code) 的 setter<br>
	 * 備註:A/C Link 判斷必需為 '0000' 才視為成功
	 * @param eRR_CODE Error Code
	 */
	public void setERR_CODE(String eRR_CODE) {
		this.ERR_CODE = eRR_CODE;
	}
	/**
	 * 欄位:HOST_DATE 的 getter
	 * @return HOST_DATE 主機帳務日期
	 */
	public String getHOST_DATE() {
		return this.HOST_DATE;
	}
	/**
	 * 欄位:HOST_DATE(主機帳務日期) 的 setter<br>
	 * 備註:CCYYMMDD
	 * @param hOST_DATE 主機帳務日期
	 */
	public void setHOST_DATE(String hOST_DATE) {
		this.HOST_DATE = hOST_DATE;
	}
	/**
	 * 欄位:HOST_TIME 的 getter
	 * @return HOST_TIME 主機帳務時間
	 */
	public String getHOST_TIME() {
		return this.HOST_TIME;
	}
	/**
	 * 欄位:HOST_TIME(主機帳務時間) 的 setter<br>
	 * 備註:HHMMSSHS
	 * @param hOST_TIME 主機帳務時間
	 */
	public void setHOST_TIME(String hOST_TIME) {
		this.HOST_TIME = hOST_TIME;
	}
	public String getHostSeqNo() {
		return HostSeqNo;
	}
	public void setHostSeqNo(String hostSeqNo) {
		HostSeqNo = hostSeqNo;
	}
	public String getJRNL_NO() {
		return JRNL_NO;
	}
	public void setJRNL_NO(String jRNL_NO) {
		JRNL_NO = jRNL_NO;
	}
	
	//20190619 Add 交易失敗時記錄上下行電文 Begin
	public String getTiTa() {
		return TITA;
	}
	public void setTiTa(String tiTa) {
		this.TITA = tiTa;
	}
	public String getToTa() {
		return TOTA;
	}
	public void setToTa(String toTa) {
		this.TOTA = toTa;
	}
	//20190619 Add 交易失敗時記錄上下行電文 End
}
