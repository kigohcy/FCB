/**
 * @(#)AbstractDayAcntCont.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : DayAcntCont base model
 * 
 * Modify History:
 *  v1.00, 2016/06/06, Yann
 *   1) First release, 二階
 *  
 */
package com.hitrust.bank.model.base;

import java.io.Serializable;
import java.util.Date;

import com.hitrust.bank.model.AclCommand;

/**
 * DayAcntCont generated by hbm2java
 */
public class AbstractDayAcntCont extends AclCommand implements Serializable {
	private static final long serialVersionUID = -6135879783226130648L;

	// =============== KEY ===============
	private AbstractDayAcntCont.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private Integer totlCont;
	private Date mdfyDttm;

	// =============== Getter & Setter ===============
	public AbstractDayAcntCont.Id getId() {
		return this.id;
	}
	
	public void setId(AbstractDayAcntCont.Id id) {
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
		private static final long serialVersionUID = -507575486553050961L;
		
		// =============== Table Attribute ===============
		private String setlDate;
		private String ecId;
		private String stts;
		
		// =============== Getter & Setter ===============
		public String getSetlDate() {
			return this.setlDate;
		}
		public void setSetlDate(String setlDate) {
			this.setlDate = setlDate;
		}
		public String getEcId() {
			return this.ecId;
		}
		public void setEcId(String ecId) {
			this.ecId = ecId;
		}
		public String getStts() {
			return this.stts;
		}
		public void setStts(String stts) {
			this.stts = stts;
		}
	}
}
