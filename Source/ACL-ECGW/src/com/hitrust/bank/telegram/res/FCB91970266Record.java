/*
 * @(#)FCB91970266Record.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res;

public class FCB91970266Record {

	private String OBUCD;
	private String custID;
	private String repeatSeq;
	private String custName;
	private String birthday;
	private String bossName;
	private String bossBirth;
	private String bossID;
	private String nationality;
	private String bizType;
	private String bizType2;
	private String mainAplyBnk;
	private String homeTel;
	private String comTel;
	private String mobile;
	private String fax;

	// 20190725 雙因子認證 Begin
	private String TFATelNo;
	// 20190725 雙因子認證 End

	public String getOBUCD() {
		return OBUCD;
	}

	public void setOBUCD(String oBUCD) {
		OBUCD = oBUCD;
	}

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getRepeatSeq() {
		return repeatSeq;
	}

	public void setRepeatSeq(String repeatSeq) {
		this.repeatSeq = repeatSeq;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

	public String getBossBirth() {
		return bossBirth;
	}

	public void setBossBirth(String bossBirth) {
		this.bossBirth = bossBirth;
	}

	public String getBossID() {
		return bossID;
	}

	public void setBossID(String bossID) {
		this.bossID = bossID;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBizType2() {
		return bizType2;
	}

	public void setBizType2(String bizType2) {
		this.bizType2 = bizType2;
	}

	public String getMainAplyBnk() {
		return mainAplyBnk;
	}

	public void setMainAplyBnk(String mainAplyBnk) {
		this.mainAplyBnk = mainAplyBnk;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getComTel() {
		return comTel;
	}

	public void setComTel(String comTel) {
		this.comTel = comTel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	
	
	 // 20190725 雙因子認證 Begin  --> XXX
	public String getTFATelNo() {
		return TFATelNo;
	}

	public void setTFATelNo(String tFATelNo) {
		TFATelNo = tFATelNo;
	}
	//20190725 雙因子認證 End
}
