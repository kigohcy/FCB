/*
 * @(#)FCB9110021ResponseInfo.java 
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2017/12/06, JeffLin
 *   1)first release
 */
package com.hitrust.bank.telegram.res;


public class FCB9110021ResponseInfo  extends GenericHostResponseInfo {
	
	private String qryCode;	
	private String hostSysDate;
	private String nextOpDate;
	private String delayCode;
	private String hostSystime;
	private String kEY;
	private String keepField;
	
	public String getQryCode() {
		return qryCode;
	}
	public void setQryCode(String qryCode) {
		this.qryCode = qryCode;
	}
	public String getHostSysDate() {
		return hostSysDate;
	}
	public void setHostSysDate(String hostSysDate) {
		this.hostSysDate = hostSysDate;
	}
	public String getNextOpDate() {
		return nextOpDate;
	}
	public void setNextOpDate(String nextOpDate) {
		this.nextOpDate = nextOpDate;
	}
	public String getDelayCode() {
		return delayCode;
	}
	public void setDelayCode(String delayCode) {
		this.delayCode = delayCode;
	}
	public String getHostSystime() {
		return hostSystime;
	}
	public void setHostSystime(String hostSystime) {
		this.hostSystime = hostSystime;
	}
	public String getkEY() {
		return kEY;
	}
	public void setkEY(String kEY) {
		this.kEY = kEY;
	}
	public String getKeepField() {
		return keepField;
	}
	public void setKeepField(String keepField) {
		this.keepField = keepField;
	}
}
