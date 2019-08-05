/**
 * @(#)AbstractVwHostErrorLog.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 主機錯誤訊息記錄 (VW_HOST_ERROR_LOG)
 * 
 * Modify History:
 *  v1.00, 2017/09/13, Yann
 *   1) First release
 *  
 */
package com.hitrust.acl.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.framework.model.BaseCommand;

public class AbstractVwHostErrorLog extends BaseCommand implements Serializable {
	
private static final long serialVersionUID = -4859721985366875533L;
	
	// =============== KEY ===============
	private AbstractVwHostErrorLog.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	//private String trnsType;	// 交易類別
	private String ordrNo;		// 訂單編號
	private Date trnsDttm;		// 交易日期
	//private String errCode;	    // 錯誤代碼
	private String hostCode;	// 主機回應代碼
	private String codeDesc; // 訊息說明
	
	// =============== Getter & Setter ===============
	public AbstractVwHostErrorLog.Id getId() {
		return this.id;
	}
	public void setId(AbstractVwHostErrorLog.Id id) {
		this.id = id;
	}
	public String getOrdrNo() {
		return this.ordrNo;
	}
	public void setOrdrNo(String ordrNo) {
		this.ordrNo = ordrNo;
	}
	public Date getTrnsDttm() {
		return this.trnsDttm;
	}
	public void setTrnsDttm(Date trnsDttm) {
		this.trnsDttm = trnsDttm;
	}
	public String getHostCode() {
		return this.hostCode;
	}
	public void setHostCode(String hostCode) {
		this.hostCode = hostCode;
	}
	public String getCodeDesc() {
		return this.codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = 544669580105787321L;
		
		// =============== Table Attribute ===============
		private String ecId;	// 平台代碼
		private String ecMsgNo;	// 平台訊息序號 
		
		// =============== Getter & Setter ===============
		public String getEcId() {
			return this.ecId;
		}
		public void setEcId(String ecId) {
			this.ecId = ecId;
		}
		public String getEcMsgNo() {
			return this.ecMsgNo;
		}
		public void setEcMsgNo(String ecMsgNo) {
			this.ecMsgNo = ecMsgNo;
		}
	}
}
