/*
 * @(#)TransactionControl.java
 * Description : Transaction共用程式。
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/03/21, Ada Chen
 *   1) First release
 */
package com.hitrust.acl.common;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hitrust.acl.exception.DBException;

/**
 *
 * Control DB transaction.
 */
public class TransactionControl {

	// Log4j
	static Logger LOG = Logger.getLogger(TransactionControl.class);

	/**
	 * Begin a db transaction.
	 * @param conn1
	 * @param conn2
	 * @param conn3
	 * @throws DBException
	 */
	public static void transactionBegin(Connection conn) throws DBException {
		try {
			
			if (conn!=null) conn.setAutoCommit(false);
			
		} catch (SQLException e) {
			LOG.error("SQLException : " + e.getMessage(), e);
			throw new DBException(e.getMessage(), "DBP_TRAN_ERR");
		}
	}

	/**
	 * Commit a db transaction.
	 * @param conn1
	 * @param conn2
	 * @param conn3
	 * @throws DBException
	 */
	public static void trasactionCommit(Connection conn) throws DBException {
		try {
		
			if (conn!=null && !conn.getAutoCommit()) conn.commit();

		} catch (SQLException e) {
			LOG.error("SQLException : " + e.getMessage(), e);
			throw new DBException(e.getMessage(), "DBP_TRAN_ERR");
		}
	}

	/**
	 * Rollback a db transaction.
	 * @param conn1
	 * @param conn2
	 * @param conn3
	 * @throws DBException
	 */
	public static void transactionRollback(Connection conn) throws DBException {
		try {
		
			if (conn!=null && !conn.getAutoCommit()) conn.rollback();

		} catch (SQLException e) {
			LOG.error("SQLException : " + e.getMessage(), e);
			throw new DBException(e.getMessage(), "DBP_TRAN_ERR");
		}
	}

	/**
	 * End a db transaction.
	 * @param conn1
	 * @param conn2
	 * @param conn3
	 */
	public static void transactionEnd(Connection conn) {
		try {

			if (conn!=null) conn.setAutoCommit(true);

		} catch (SQLException e) {
			LOG.error("SQLException : " + e.getMessage(), e);
		}
	}
}
