/**
 * @(#) SignatureLogHome.java
 *
 * Directions: 訊息簽章記錄 DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/27, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.dao.home;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.BeanHome;

public class SignatureLogHome extends BeanHome {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(SignatureLogHome.class);

	public SignatureLogHome(Connection conn) {
		this.conn = conn;
	}

}
