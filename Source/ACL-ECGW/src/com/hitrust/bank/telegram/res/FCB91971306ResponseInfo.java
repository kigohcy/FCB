/*
 * @(#)FCB91971306ResponseInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res;


public class FCB91971306ResponseInfo  extends GenericHostResponseInfo {
	
	private String busType;	//帳 務 別
	private String wsID;	//工作站代號
	private String hostSeqNo;	//中心交易序號
	private String txID;	//交易代號
	private String education;
	private String marriage;
	private String occupation;
	private String tel1;
	private String tel2;
	private String CPhone;
	private String email;
	private String zip;
	private String address;
	private String birthday;
	private String WHVouDel;
	private String compreSTDel;
	private String iPAddress;
	private String isEZAcc;
	private String OTPFlag;
	private String OTPPhoneNo;
	private String postAddSetYear;
	private String inGuardAndAssist;
	private String completeTstCust;
	private String mainBranch;
	private String tstDecFiled;
	private String OLAgrQryJCICFlag;
	private String OLAgrQryJCICFlagAuthQryBrh;
	private String OLAgrQryJCICDate;
	private String OLAgrQryJCICPhone;
	private String interCustInfoCode;
	private String freePredFlag;
	private String OLPredInAcctNoFlag;
	private String isROC;
	private String isAmerican;
	private String isHighRisk;
	private String isBlacklist;
	private String NHICatFlag;
	private String isWealth;
	private String PRLM;
	private String WARD;
	private String isAlertOrDerConAccNo;
	private String isCredOrDepAlertAccNo;
	
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getWsID() {
		return wsID;
	}
	public void setWsID(String wsID) {
		this.wsID = wsID;
	}
	public String getHostSeqNo() {
		return hostSeqNo;
	}
	public void setHostSeqNo(String hostSeqNo) {
		this.hostSeqNo = hostSeqNo;
	}
	public String getTxID() {
		return txID;
	}
	public void setTxID(String txID) {
		this.txID = txID;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getTel1() {
		return tel1;
	}
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public String getCPhone() {
		return CPhone;
	}
	public void setCPhone(String cPhone) {
		CPhone = cPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getWHVouDel() {
		return WHVouDel;
	}
	public void setWHVouDel(String wHVouDel) {
		WHVouDel = wHVouDel;
	}
	public String getCompreSTDel() {
		return compreSTDel;
	}
	public void setCompreSTDel(String compreSTDel) {
		this.compreSTDel = compreSTDel;
	}
	public String getiPAddress() {
		return iPAddress;
	}
	public void setiPAddress(String iPAddress) {
		this.iPAddress = iPAddress;
	}
	public String getIsEZAcc() {
		return isEZAcc;
	}
	public void setIsEZAcc(String isEZAcc) {
		this.isEZAcc = isEZAcc;
	}
	public String getOTPFlag() {
		return OTPFlag;
	}
	public void setOTPFlag(String oTPFlag) {
		OTPFlag = oTPFlag;
	}
	public String getOTPPhoneNo() {
		return OTPPhoneNo;
	}
	public void setOTPPhoneNo(String oTPPhoneNo) {
		OTPPhoneNo = oTPPhoneNo;
	}
	public String getPostAddSetYear() {
		return postAddSetYear;
	}
	public void setPostAddSetYear(String postAddSetYear) {
		this.postAddSetYear = postAddSetYear;
	}
	public String getInGuardAndAssist() {
		return inGuardAndAssist;
	}
	public void setInGuardAndAssist(String inGuardAndAssist) {
		this.inGuardAndAssist = inGuardAndAssist;
	}
	public String getCompleteTstCust() {
		return completeTstCust;
	}
	public void setCompleteTstCust(String completeTstCust) {
		this.completeTstCust = completeTstCust;
	}
	public String getMainBranch() {
		return mainBranch;
	}
	public void setMainBranch(String mainBranch) {
		this.mainBranch = mainBranch;
	}
	public String getTstDecFiled() {
		return tstDecFiled;
	}
	public void setTstDecFiled(String tstDecFiled) {
		this.tstDecFiled = tstDecFiled;
	}
	public String getOLAgrQryJCICFlag() {
		return OLAgrQryJCICFlag;
	}
	public void setOLAgrQryJCICFlag(String oLAgrQryJCICFlag) {
		OLAgrQryJCICFlag = oLAgrQryJCICFlag;
	}
	public String getOLAgrQryJCICFlagAuthQryBrh() {
		return OLAgrQryJCICFlagAuthQryBrh;
	}
	public void setOLAgrQryJCICFlagAuthQryBrh(String oLAgrQryJCICFlagAuthQryBrh) {
		OLAgrQryJCICFlagAuthQryBrh = oLAgrQryJCICFlagAuthQryBrh;
	}
	public String getOLAgrQryJCICDate() {
		return OLAgrQryJCICDate;
	}
	public void setOLAgrQryJCICDate(String oLAgrQryJCICDate) {
		OLAgrQryJCICDate = oLAgrQryJCICDate;
	}
	public String getOLAgrQryJCICPhone() {
		return OLAgrQryJCICPhone;
	}
	public void setOLAgrQryJCICPhone(String oLAgrQryJCICPhone) {
		OLAgrQryJCICPhone = oLAgrQryJCICPhone;
	}
	public String getInterCustInfoCode() {
		return interCustInfoCode;
	}
	public void setInterCustInfoCode(String interCustInfoCode) {
		this.interCustInfoCode = interCustInfoCode;
	}
	public String getFreePredFlag() {
		return freePredFlag;
	}
	public void setFreePredFlag(String freePredFlag) {
		this.freePredFlag = freePredFlag;
	}
	public String getOLPredInAcctNoFlag() {
		return OLPredInAcctNoFlag;
	}
	public void setOLPredInAcctNoFlag(String oLPredInAcctNoFlag) {
		OLPredInAcctNoFlag = oLPredInAcctNoFlag;
	}
	public String getIsROC() {
		return isROC;
	}
	public void setIsROC(String isROC) {
		this.isROC = isROC;
	}
	public String getIsAmerican() {
		return isAmerican;
	}
	public void setIsAmerican(String isAmerican) {
		this.isAmerican = isAmerican;
	}
	public String getIsHighRisk() {
		return isHighRisk;
	}
	public void setIsHighRisk(String isHighRisk) {
		this.isHighRisk = isHighRisk;
	}
	public String getIsBlacklist() {
		return isBlacklist;
	}
	public void setIsBlacklist(String isBlacklist) {
		this.isBlacklist = isBlacklist;
	}
	public String getNHICatFlag() {
		return NHICatFlag;
	}
	public void setNHICatFlag(String nHICatFlag) {
		NHICatFlag = nHICatFlag;
	}
	public String getIsWealth() {
		return isWealth;
	}
	public void setIsWealth(String isWealth) {
		this.isWealth = isWealth;
	}
	public String getPRLM() {
		return PRLM;
	}
	public void setPRLM(String pRLM) {
		PRLM = pRLM;
	}
	public String getWARD() {
		return WARD;
	}
	public void setWARD(String wARD) {
		WARD = wARD;
	}
	public String getIsAlertOrDerConAccNo() {
		return isAlertOrDerConAccNo;
	}
	public void setIsAlertOrDerConAccNo(String isAlertOrDerConAccNo) {
		this.isAlertOrDerConAccNo = isAlertOrDerConAccNo;
	}
	public String getIsCredOrDepAlertAccNo() {
		return isCredOrDepAlertAccNo;
	}
	public void setIsCredOrDepAlertAccNo(String isCredOrDepAlertAccNo) {
		this.isCredOrDepAlertAccNo = isCredOrDepAlertAccNo;
	}
}
