/*
 * @(#)BeanHome.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/04/14, Tim Cao
 *   1) First release
 */
package com.hitrust.acl.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;

/**
 * Super class of all DAO Home. 
 */
public abstract class BeanHome {
	//log4j
	static Logger LOG = Logger.getLogger(BeanHome.class);
	
	// JDBC Connection
	protected Connection conn;
	
	/**
	 * Check if data exist in DB
	 * @param bean
	 * @param conn
	 * @return
	 * @throws DBException
	 */
	public boolean isExist(GenericBean bean) throws DBException {
		bean.setConnection(this.conn);
		LOG.info("查詢"+bean.getTableName()+"是否存在資料");
		return bean.isExist();
	}
	
	/**
	 * Load data from DB
	 * @param bean
	 * @param conn
	 * @throws DBException
	 */
	public void load(GenericBean bean) throws DBException {
		bean.setConnection(this.conn);
		bean.load();
	}
   
	/**
	 * Insert data to DB
	 * @param bean
	 * @param conn
	 * @throws DBException
	 */
	public void insert(GenericBean bean) throws DBException {
		bean.setConnection(this.conn);
		LOG.info("新增"+bean.getTableName());
		bean.insert();
	}
	
	/**
	 * Update data to DB
	 * @param bean
	 * @param conn
	 * @throws DBException
	 */
	public void update(GenericBean bean) throws DBException {
		bean.setConnection(this.conn);
		LOG.info("修改"+bean.getTableName());
		bean.update();
	}
	
	/**
	 * Delete data in DB
	 * @param bean
	 * @param conn
	 * @throws DBException
	 */
	public void delete(GenericBean bean) throws DBException {
		bean.setConnection(this.conn);
		LOG.info("刪除"+bean.getTableName());
		bean.delete();
	}
	
	/**
	 * Set connection
	 * @param conn
	 */
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	/**
	 * 
	 * @param startPos
	 * @param parameteList
	 * @param exe
	 * @throws SQLException
	 */
	protected void setParameter(int startPos ,ArrayList parameterList,DBExec exec) throws SQLException {
		try {
			for (int i =0; parameterList!= null && i < parameterList.size(); i++) {
				String value = (String)parameterList.get(i);
				exec.setString(startPos+i,value);
			}
		} catch (SQLException e) {
			LOG.debug("--set Parameter error!");
			LOG.error("SQLException:"+e.toString());
			throw e;
		}
	}
}