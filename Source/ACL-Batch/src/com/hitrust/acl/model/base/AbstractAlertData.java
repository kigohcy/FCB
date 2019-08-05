/**
 * @(#)AbstractAlertData.java
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

public class AbstractAlertData extends BaseCommand implements Serializable {
	
private static final long serialVersionUID = -4849721985366875533L;
	
	// =============== KEY ===============
	private AbstractAlertData.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private String ordrNo;		// 訂單編號
	private Date trnsDttm;		// 交易日期
	
	// =============== Getter & Setter ===============
	public AbstractAlertData.Id getId() {
		return this.id;
	}
	public void setId(AbstractAlertData.Id id) {
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
	

	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		
		private static final long serialVersionUID = 544639580105787321L;
		
		// =============== Table Attribute ===============
		private String ecId;	// 平台代碼
		private String ecMsgNo;	// 平台訊息序號 
		private String alertType; //通知訊息型態 M:e-mail, S:SMS簡訊
		
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
		public String getAlertType() {
			return alertType;
		}
		public void setAlertType(String alertType) {
			this.alertType = alertType;
		}
		
	}
}
