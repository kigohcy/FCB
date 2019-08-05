/**
 * @(#)AbstractBlobData.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : AbstractBlobData base model
 * 
 * Modify History:
 *  v1.00, 2016/02/02, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;

public class AbstractBlobData extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = -8760849065756559401L;
	
	// =============== Table Attribute ===============
	private String logNo;    //交易序號
	private String logType;  //類型
	private Date oprtDttm;   //執行時間
	private byte[] logData;  //操作記錄XML資料
	
	// =============== Getter & Setter ===============
	public String getLogNo() {
		return logNo;
	}
	public void setLogNo(String logNo) {
		this.logNo = logNo;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public Date getOprtDttm() {
		return oprtDttm;
	}
	public void setOprtDttm(Date oprtDttm) {
		this.oprtDttm = oprtDttm;
	}
	public byte[] getLogData() {
		return logData;
	}
	public void setLogData(byte[] logData) {
		this.logData = logData;
	}

}
