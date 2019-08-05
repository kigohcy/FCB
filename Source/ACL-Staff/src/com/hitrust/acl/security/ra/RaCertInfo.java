/**
 * @(#)RaCertInfo.java
 *
 * Copyright (c) 2013 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2013/10/28, Ada Chen
 *   1) First release
 */
package com.hitrust.acl.security.ra;

public class RaCertInfo {
 
	private int certId;
	 
	private String applyTime;
	 
	private String approveTime;
	 
	private String certStatus;
	 
	private String caApplyId;
	 
	private String certSerial;
	 
	private String certIssuser;
	 
	private String certType;
	 
	private String cn;
	 
	private String email;
	 
	private String certNotBefore;
	 
	private String certNotAfter;
	
	private String certSubject; //���ҥD��
	
	private String certFinger; //���ҩi��
	 
	public RaCertInfo(int certId) {
		this.certId = certId;
	}
	
	
	public String getApplyTime() {
		return applyTime;
	}
	
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	
	public String getApproveTime() {
		return approveTime;
	}
	
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}
	
	public String getCaApplyId() {
		return caApplyId;
	}
	
	public void setCaApplyId(String caApplyId) {
		this.caApplyId = caApplyId;
	}
	
	public int getCertId() {
		return certId;
	}
	
	public void setCertId(int certId) {
		this.certId = certId;
	}
	
	public String getCertIssuser() {
		return certIssuser;
	}
	
	public void setCertIssuser(String certIssuser) {
		this.certIssuser = certIssuser;
	}
	
	public String getCertNotAfter() {
		return certNotAfter;
	}
	
	public void setCertNotAfter(String certNotAfter) {
		this.certNotAfter = certNotAfter;
	}
	
	public String getCertNotBefore() {
		return certNotBefore;
	}
	
	public void setCertNotBefore(String certNotBefore) {
		this.certNotBefore = certNotBefore;
	}
	
	public String getCertSerial() {
		return certSerial;
	}
	
	public void setCertSerial(String certSerial) {
		this.certSerial = certSerial;
	}
	
	public String getCertStatus() {
		return certStatus;
	}
	
	public void setCertStatus(String certStatus) {
		this.certStatus = certStatus;
	}
	
	public String getCertType() {
		return certType;
	}
	
	public void setCertType(String certType) {
		this.certType = certType;
	}
	
	public String getCn() {
		return cn;
	}
	
	public void setCn(String cn) {
		this.cn = cn;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}


	public String getCertSubject() {
		return certSubject;
	}


	public void setCertSubject(String certSubject) {
		this.certSubject = certSubject;
	}


	public String getCertFinger() {
		return certFinger;
	}


	public void setCertFinger(String certFinger) {
		this.certFinger = certFinger;
	}
	
	
}
 
