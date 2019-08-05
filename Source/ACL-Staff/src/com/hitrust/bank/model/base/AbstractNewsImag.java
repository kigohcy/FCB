/**
 * @(#)AbstractNewsImag.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : NewsImag base model
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
 * NewsImag generated by hbm2java
 */
public class AbstractNewsImag extends AclCommand implements Serializable {
	
	private static final long serialVersionUID = -1518853508622185050L;
	
	// =============== KEY ===============
	private AbstractNewsImag.Id id; // 複合 KEY
	
	// =============== Table Attribute ===============
	private byte[] imag;
	private String mdfyUser;
	private Date mdfyDttm;
	private String deltFlag;

	// =============== Getter & Setter ===============
	public AbstractNewsImag.Id getId() {
		return this.id;
	}
	
	public void setId(AbstractNewsImag.Id id) {
		this.id = id;
	}

	public byte[] getImag() {
		return this.imag;
	}

	public void setImag(byte[] imag) {
		this.imag = imag;
	}

	public String getMdfyUser() {
		return this.mdfyUser;
	}

	public void setMdfyUser(String mdfyUser) {
		this.mdfyUser = mdfyUser;
	}

	public Date getMdfyDttm() {
		return this.mdfyDttm;
	}

	public void setMdfyDttm(Date mdfyDttm) {
		this.mdfyDttm = mdfyDttm;
	}
	
	public String getDeltFlag() {
		return this.deltFlag;
	}

	public void setDeltFlag(String deltFlag) {
		this.deltFlag = deltFlag;
	}
	
	// =============== 複合 KEY ===============
	public static class Id implements Serializable {
		private static final long serialVersionUID = 3214722489735820605L;
		
		// =============== Table Attribute ===============
		private String seq;
		private String fileName;
		
		// =============== Getter & Setter ===============
		public String getSeq() {
			return this.seq;
		}

		public void setSeq(String seq) {
			this.seq = seq;
		}

		public String getFileName() {
			return this.fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}
}
