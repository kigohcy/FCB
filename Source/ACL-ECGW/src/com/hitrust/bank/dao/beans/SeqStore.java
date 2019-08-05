/**
 * @(#) SeqStore.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : SeqStore bean
 * 
 * Modify History:
 *  v1.00, 2016/03/25, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.beans;

import com.hitrust.acl.dao.GenericBean;
import com.hitrust.acl.exception.DBException;

public class SeqStore extends GenericBean {
	private static final long serialVersionUID = -7786975133060189980L;
	
	public String TYPE;
	public String SEQ;
	public String REFLESHDATE;

	public SeqStore() {
		super();
	}

	public SeqStore(byte[] xmlBytes) throws DBException {
		super(xmlBytes);
	}

	protected void toXML() {
		this.putTableName("SEQ_STORE");
		this.putField("TYPE"       , this.TYPE       , "S", "Y");
		this.putField("SEQ"        , this.SEQ        , "S", "N");
		this.putField("REFLESHDATE", this.REFLESHDATE, "S", "N");
	}

	protected void fromXML() {
		this.TYPE        = this.getFieldString("TYPE");
		this.SEQ         = this.getFieldString("SEQ");
		this.REFLESHDATE = this.getFieldString("REFLESHDATE");
	}
}
