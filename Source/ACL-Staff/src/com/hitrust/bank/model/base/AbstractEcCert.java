/**
 * @(#) AbstractEcCert.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/21, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.model.base;

import java.io.Serializable;

import com.hitrust.bank.model.AclCommand;

public class AbstractEcCert extends AclCommand implements Serializable {

	private static final long serialVersionUID = -176514597610665006L;

	// =============== Table Attribute ===============
	private AbstractEcCert.Id id; // 復合 KEY
	private String certSn;		  // 憑證序號
	private Integer certId;		  // 憑證編號
	private String strtDttm; 	  // 生效日期
	private String endDttm; 	  // 到期日期
	private String raAcnt; 		  // RA帳號
	private byte[] certFeeData;   // 繳費通知
	
	// =============== Getter & Setter ===============
	public AbstractEcCert.Id getId() {
		return id;
	}
	public void setId(AbstractEcCert.Id id) {
		this.id = id;
	}
	public Integer getCertId() {
		return certId;
	}
	public String getCertSn() {
		return certSn;
	}
	public void setCertSn(String certSn) {
		this.certSn = certSn;
	}
	public void setCertId(Integer certId) {
		this.certId = certId;
	}
	public String getStrtDttm() {
		return strtDttm;
	}
	public void setStrtDttm(String strtDttm) {
		this.strtDttm = strtDttm;
	}
	public String getEndDttm() {
		return endDttm;
	}
	public void setEndDttm(String endDttm) {
		this.endDttm = endDttm;
	}
	public String getRaAcnt() {
		return raAcnt;
	}
	public void setRaAcnt(String raAcnt) {
		this.raAcnt = raAcnt;
	}
	public byte[] getCertFeeData() {
		return certFeeData;
	}
	public void setCertFeeData(byte[] certFeeData) {
		this.certFeeData = certFeeData;
	}

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {

		private static final long serialVersionUID = -6461335216242720402L;

		// =============== Table Attribute ===============
		private String ecId;   // 平台代碼
		private String certCn; // 憑證識別碼
		
		// =============== Getter & Setter ===============
		public String getEcId() {
			return ecId;
		}
		public void setEcId(String ecId) {
			this.ecId = ecId;
		}
		public String getCertCn() {
			return certCn;
		}
		public void setCertCn(String certCn) {
			this.certCn = certCn;
		}
		
	}

}
