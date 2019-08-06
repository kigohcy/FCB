/**
 * @(#)AbstractDayCustEcCont.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員綁定電商數日終累計 (DAY_CUST_EC_CONT)
 * 
 * Modify History:
 *  v1.00, 2017/09/22, Yann
 *   1) First release
 *  v1.01, 2017/09/27, Caleb
 *   2) 會員服務統計改為繼承DownloadCommand
 */
package com.hitrust.acl.model.base;

import java.io.Serializable;
import java.util.Date;
import com.hitrust.framework.model.BaseCommand;

/**
 * DayCustEcCont generated by hbm2java
 */
public class AbstractDayCustEcCont extends BaseCommand implements Serializable {
	
	private static final long serialVersionUID = -630254787818227698L;
	
	// =============== KEY ===============
	private AbstractDayCustEcCont.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private Integer totlCont;
	private Date mdfyDttm;
	
	// =============== Getter & Setter ===============
	public AbstractDayCustEcCont.Id getId() {
		return this.id;
	}
	
	public void setId(AbstractDayCustEcCont.Id id) {
		this.id = id;
	}
	
	public Integer getTotlCont() {
		return this.totlCont;
	}

	public void setTotlCont(Integer totlCont) {
		this.totlCont = totlCont;
	}

	public Date getMdfyDttm() {
		return this.mdfyDttm;
	}

	public void setMdfyDttm(Date mdfyDttm) {
		this.mdfyDttm = mdfyDttm;
	}
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		private static final long serialVersionUID = 8270906395789706161L;
		
		// =============== Table Attribute ===============
		private String setlDate;
		private int ecCont;
		
		// =============== Getter & Setter ===============
		public String getSetlDate() {
			return this.setlDate;
		}
		public void setSetlDate(String setlDate) {
			this.setlDate = setlDate;
		}
		public int getEcCont() {
			return this.ecCont;
		}
		public void setEcCont(int ecCont) {
			this.ecCont = ecCont;
		}
	}
}