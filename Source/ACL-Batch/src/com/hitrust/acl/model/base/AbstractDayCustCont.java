/**
 * @(#) AbstractDayCustCont.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/03/28, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.acl.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.framework.model.BaseCommand;

public class AbstractDayCustCont extends BaseCommand implements Serializable {
	private static final long serialVersionUID = -1082393812414908501L;

	// =============== KEY ===============
	private AbstractDayCustCont.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private Integer totlCont;
	private Date mdfyDttm;
	
	// =============== Getter & Setter ===============
	public AbstractDayCustCont.Id getId() {
		return this.id;
	}
	
	public void setId(AbstractDayCustCont.Id id) {
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
		private String stts;
		
		// =============== Getter & Setter ===============
		public String getSetlDate() {
			return this.setlDate;
		}
		public void setSetlDate(String setlDate) {
			this.setlDate = setlDate;
		}
		public String getStts() {
			return this.stts;
		}
		public void setStts(String stts) {
			this.stts = stts;
		}
	}
}
