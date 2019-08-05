/*
 * @(#)HeaderHostRequestInfo.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Description:
 *		Header 上行電文 JavaBean
 *
 * Modify History:
 * v1.00, 2016/04/22, HiTRUST
 *	1) First release
 */

package com.hitrust.bank.telegram.req;

/**
 * Header 上行電文欄位 Java Bean
 * @author HiTRUST
 */
public class HeaderHostRequestInfo extends GenericHostRequestInfo {

	private String H_FILLER1;    //Filler
	private String H_MSG_TYPE;   //Message Type
	private String H_MSG_LEN;    //Message Length
	private String H_CYCL_NO;    //Cycle Number
	private String H_SEG_NO;     //Segment Number
	private String H_FROND_NO;   //Front End Number
	private String H_TERM_NO;    //Terminal Number
	private String H_INST_NO;    //Institution Number
	private String H_BRCH_NO;    //Branch Number
	private String H_WORK_NO;    //Workstation Number
	private String H_TELR_NO;    //Teller Number
	private String H_TXN_CODE;   //Transaction Code
	private String H_JRNL_NO;    //Journal Number
	private String H_DATE;       //Date
	private String H_FILLER2;    //Filler
	private String H_TERM_TYPE;  //Terminal Type
	private String H_FILLER3;    //Filler
	private String H_FLAG1;      //FLAG1
	private String H_FLAG2;      //FLAG2
	private String H_FLAG3;      //FLAG3
	private String H_FLAG4;      //FLAG4
	private String H_SUB_CHNL;   //Subsystem Channel
	private String H_SUP_ID;     //Supervisor ID
	private String H_DEBUG_FLAG; //Debug Flag
	private String H_DEBUG_QUEU; //Debug Queue

	/**
	 * 欄位:H_FILLER1 的 getter
	 * @return H_FILLER1 Filler
	 */
	public String getH_FILLER1() {
		return this.H_FILLER1;
	}
	/**
	 * 欄位說明:H_FILLER1(Filler) 的 setter<br>
	 * 資料型態:X*2
	 * @param h_FILLER1 Filler
	 */
	public void setH_FILLER1(String h_FILLER1) {
		this.H_FILLER1 = h_FILLER1;
	}
	/**
	 * 欄位:H_MSG_TYPE 的 getter
	 * @return H_MSG_TYPE Message Type
	 */
	public String getH_MSG_TYPE() {
		return this.H_MSG_TYPE;
	}
	/**
	 * 欄位說明:H_MSG_TYPE(Message Type) 的 setter<br>
	 * 資料型態:X*2
	 * @param h_MSG_TYPE Message Type
	 */
	public void setH_MSG_TYPE(String h_MSG_TYPE) {
		this.H_MSG_TYPE = h_MSG_TYPE;
	}
	/**
	 * 欄位:H_MSG_LEN 的 getter
	 * @return H_MSG_LEN Message Length
	 */
	public String getH_MSG_LEN() {
		return this.H_MSG_LEN;
	}
	/**
	 * 欄位說明:H_MSG_LEN(Message Length) 的 setter<br>
	 * 資料型態:X*4<br>
	 * 備註:INTERNAL Header+Transaction Data 總長度
	 * @param h_MSG_LEN Message Length
	 */
	public void setH_MSG_LEN(String h_MSG_LEN) {
		this.H_MSG_LEN = h_MSG_LEN;
	}
	/**
	 * 欄位:H_CYCL_NO 的 getter
	 * @return H_CYCL_NO Cycle Number
	 */
	public String getH_CYCL_NO() {
		return this.H_CYCL_NO;
	}
	/**
	 * 欄位說明:H_CYCL_NO(Cycle Number) 的 setter<br>
	 * 資料型態:X*12<br>
	 * 備註:"共12碼
	 * @param h_CYCL_NO Cycle Number
	 */
	public void setH_CYCL_NO(String h_CYCL_NO) {
		this.H_CYCL_NO = h_CYCL_NO;
	}
	/**
	 * 欄位:H_SEG_NO 的 getter
	 * @return H_SEG_NO Segment Number
	 */
	public String getH_SEG_NO() {
		return this.H_SEG_NO;
	}
	/**
	 * 欄位說明:H_SEG_NO(Segment Number) 的 setter<br>
	 * 資料型態:X*4
	 * @param h_SEG_NO Segment Number
	 */
	public void setH_SEG_NO(String h_SEG_NO) {
		this.H_SEG_NO = h_SEG_NO;
	}
	/**
	 * 欄位:H_FROND_NO 的 getter
	 * @return H_FROND_NO Front End Number
	 */
	public String getH_FROND_NO() {
		return this.H_FROND_NO;
	}
	/**
	 * 欄位說明:H_FROND_NO(Front End Number) 的 setter<br>
	 * 資料型態:X*4
	 * @param h_FROND_NO Front End Number
	 */
	public void setH_FROND_NO(String h_FROND_NO) {
		this.H_FROND_NO = h_FROND_NO;
	}
	/**
	 * 欄位:H_TERM_NO 的 getter
	 * @return H_TERM_NO Terminal Number
	 */
	public String getH_TERM_NO() {
		return this.H_TERM_NO;
	}
	/**
	 * 欄位說明:H_TERM_NO(Terminal Number) 的 setter<br>
	 * 資料型態:X*6
	 * @param h_TERM_NO Terminal Number
	 */
	public void setH_TERM_NO(String h_TERM_NO) {
		this.H_TERM_NO = h_TERM_NO;
	}
	/**
	 * 欄位:H_INST_NO 的 getter
	 * @return H_INST_NO Institution Number
	 */
	public String getH_INST_NO() {
		return this.H_INST_NO;
	}
	/**
	 * 欄位說明:H_INST_NO(Institution Number) 的 setter<br>
	 * 資料型態:X*3
	 * @param h_INST_NO Institution Number
	 */
	public void setH_INST_NO(String h_INST_NO) {
		this.H_INST_NO = h_INST_NO;
	}
	/**
	 * 欄位:H_BRCH_NO 的 getter
	 * @return H_BRCH_NO Branch Number
	 */
	public String getH_BRCH_NO() {
		return this.H_BRCH_NO;
	}
	/**
	 * 欄位說明:H_BRCH_NO(Branch Number) 的 setter<br>
	 * 資料型態:X*4
	 * @param h_BRCH_NO Branch Number
	 */
	public void setH_BRCH_NO(String h_BRCH_NO) {
		this.H_BRCH_NO = h_BRCH_NO;
	}
	/**
	 * 欄位:H_WORK_NO 的 getter
	 * @return H_WORK_NO Workstation Number
	 */
	public String getH_WORK_NO() {
		return this.H_WORK_NO;
	}
	/**
	 * 欄位說明:H_WORK_NO(Workstation Number) 的 setter<br>
	 * 資料型態:X*3
	 * @param h_WORK_NO Workstation Number
	 */
	public void setH_WORK_NO(String h_WORK_NO) {
		this.H_WORK_NO = h_WORK_NO;
	}
	/**
	 * 欄位:H_TELR_NO 的 getter
	 * @return H_TELR_NO Teller Number
	 */
	public String getH_TELR_NO() {
		return this.H_TELR_NO;
	}
	/**
	 * 欄位說明:H_TELR_NO(Teller Number) 的 setter<br>
	 * 資料型態:X*5<br>
	 * 備註:"74861~74880
	 * @param h_TELR_NO Teller Number
	 */
	public void setH_TELR_NO(String h_TELR_NO) {
		this.H_TELR_NO = h_TELR_NO;
	}
	/**
	 * 欄位:H_TXN_CODE 的 getter
	 * @return H_TXN_CODE Transaction Code
	 */
	public String getH_TXN_CODE() {
		return this.H_TXN_CODE;
	}
	/**
	 * 欄位說明:H_TXN_CODE(Transaction Code) 的 setter<br>
	 * 資料型態:X*6<br>
	 * 備註:"電文代號
	 * @param h_TXN_CODE Transaction Code
	 */
	public void setH_TXN_CODE(String h_TXN_CODE) {
		this.H_TXN_CODE = h_TXN_CODE;
	}
	/**
	 * 欄位:H_JRNL_NO 的 getter
	 * @return H_JRNL_NO Journal Number
	 */
	public String getH_JRNL_NO() {
		return this.H_JRNL_NO;
	}
	/**
	 * 欄位說明:H_JRNL_NO(Journal Number) 的 setter<br>
	 * 資料型態:X*6<br>
	 * 備註:"主機處理序號
	 * @param h_JRNL_NO Journal Number
	 */
	public void setH_JRNL_NO(String h_JRNL_NO) {
		this.H_JRNL_NO = h_JRNL_NO;
	}
	/**
	 * 欄位:H_DATE 的 getter
	 * @return H_DATE Date
	 */
	public String getH_DATE() {
		return this.H_DATE;
	}
	/**
	 * 欄位說明:H_DATE(Date) 的 setter<br>
	 * 資料型態:X*4
	 * @param h_DATE Date
	 */
	public void setH_DATE(String h_DATE) {
		this.H_DATE = h_DATE;
	}
	/**
	 * 欄位:H_FILLER2 的 getter
	 * @return H_FILLER2 Filler
	 */
	public String getH_FILLER2() {
		return this.H_FILLER2;
	}
	/**
	 * 欄位說明:H_FILLER2(Filler) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_FILLER2 Filler
	 */
	public void setH_FILLER2(String h_FILLER2) {
		this.H_FILLER2 = h_FILLER2;
	}
	/**
	 * 欄位:H_TERM_TYPE 的 getter
	 * @return H_TERM_TYPE Terminal Type
	 */
	public String getH_TERM_TYPE() {
		return this.H_TERM_TYPE;
	}
	/**
	 * 欄位說明:H_TERM_TYPE(Terminal Type) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_TERM_TYPE Terminal Type
	 */
	public void setH_TERM_TYPE(String h_TERM_TYPE) {
		this.H_TERM_TYPE = h_TERM_TYPE;
	}
	/**
	 * 欄位:H_FILLER3 的 getter
	 * @return H_FILLER3 Filler
	 */
	public String getH_FILLER3() {
		return this.H_FILLER3;
	}
	/**
	 * 欄位說明:H_FILLER3(Filler) 的 setter<br>
	 * 資料型態:X*2
	 * @param h_FILLER3 Filler
	 */
	public void setH_FILLER3(String h_FILLER3) {
		this.H_FILLER3 = h_FILLER3;
	}
	/**
	 * 欄位:H_FLAG1 的 getter
	 * @return H_FLAG1 FLAG1
	 */
	public String getH_FLAG1() {
		return this.H_FLAG1;
	}
	/**
	 * 欄位說明:H_FLAG1(FLAG1) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_FLAG1 FLAG1
	 */
	public void setH_FLAG1(String h_FLAG1) {
		this.H_FLAG1 = h_FLAG1;
	}
	/**
	 * 欄位:H_FLAG2 的 getter
	 * @return H_FLAG2 FLAG2
	 */
	public String getH_FLAG2() {
		return this.H_FLAG2;
	}
	/**
	 * 欄位說明:H_FLAG2(FLAG2) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_FLAG2 FLAG2
	 */
	public void setH_FLAG2(String h_FLAG2) {
		this.H_FLAG2 = h_FLAG2;
	}
	/**
	 * 欄位:H_FLAG3 的 getter
	 * @return H_FLAG3 FLAG3
	 */
	public String getH_FLAG3() {
		return this.H_FLAG3;
	}
	/**
	 * 欄位說明:H_FLAG3(FLAG3) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_FLAG3 FLAG3
	 */
	public void setH_FLAG3(String h_FLAG3) {
		this.H_FLAG3 = h_FLAG3;
	}
	/**
	 * 欄位:H_FLAG4 的 getter
	 * @return H_FLAG4 FLAG4
	 */
	public String getH_FLAG4() {
		return this.H_FLAG4;
	}
	/**
	 * 欄位說明:H_FLAG4(FLAG4) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_FLAG4 FLAG4
	 */
	public void setH_FLAG4(String h_FLAG4) {
		this.H_FLAG4 = h_FLAG4;
	}
	/**
	 * 欄位:H_SUB_CHNL 的 getter
	 * @return H_SUB_CHNL Subsystem Channel
	 */
	public String getH_SUB_CHNL() {
		return this.H_SUB_CHNL;
	}
	/**
	 * 欄位說明:H_SUB_CHNL(Subsystem Channel) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_SUB_CHNL Subsystem Channel
	 */
	public void setH_SUB_CHNL(String h_SUB_CHNL) {
		this.H_SUB_CHNL = h_SUB_CHNL;
	}
	/**
	 * 欄位:H_SUP_ID 的 getter
	 * @return H_SUP_ID Supervisor ID
	 */
	public String getH_SUP_ID() {
		return this.H_SUP_ID;
	}
	/**
	 * 欄位說明:H_SUP_ID(Supervisor ID) 的 setter<br>
	 * 資料型態:X*6
	 * @param h_SUP_ID Supervisor ID
	 */
	public void setH_SUP_ID(String h_SUP_ID) {
		this.H_SUP_ID = h_SUP_ID;
	}
	/**
	 * 欄位:H_DEBUG_FLAG 的 getter
	 * @return H_DEBUG_FLAG Debug Flag
	 */
	public String getH_DEBUG_FLAG() {
		return this.H_DEBUG_FLAG;
	}
	/**
	 * 欄位說明:H_DEBUG_FLAG(Debug Flag) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_DEBUG_FLAG Debug Flag
	 */
	public void setH_DEBUG_FLAG(String h_DEBUG_FLAG) {
		this.H_DEBUG_FLAG = h_DEBUG_FLAG;
	}
	/**
	 * 欄位:H_DEBUG_QUEU 的 getter
	 * @return H_DEBUG_QUEU Debug Queue
	 */
	public String getH_DEBUG_QUEU() {
		return this.H_DEBUG_QUEU;
	}
	/**
	 * 欄位說明:H_DEBUG_QUEU(Debug Queue) 的 setter<br>
	 * 資料型態:X*1
	 * @param h_DEBUG_QUEU Debug Queue
	 */
	public void setH_DEBUG_QUEU(String h_DEBUG_QUEU) {
		this.H_DEBUG_QUEU = h_DEBUG_QUEU;
	}
}
