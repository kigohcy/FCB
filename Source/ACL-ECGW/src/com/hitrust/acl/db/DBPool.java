/*

 * @(#)DBPool.java
 *
 * Copyright (c) 2004 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2004/07/20, Jackie Yang
 *   1) First release
 *
 *  v1.01, 2004/07/28, Jackie Yang
 *   1) Modify open(), not throws Exception when initial connect to DB failure.
 *   2) Modify getConnection(), change the method of testing connection is OK.
 *  v1.02, 2017/09/13, Eason Hsu
 *   1) TSBACL-161, [Fortify] Null Dereference
 *
 */
package com.hitrust.acl.db;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;

/**
 * Databse connection pool for manage connect between DBMS.
 *
 * @author  Jackie Yang
 * @version 1.00, 2004/07/20
 */
public class DBPool {
	// log4J category
	static Logger LOG = Logger.getLogger(DBPool.class);

	// Pool for store connections
	private Hashtable connectionPool = null;

	// Connections allocation control
	private int bldConnections	= 0;	// Current builded connections 
	private int maxConnections	= 3;	// Maximum number of connections can be builded
	private int iniConnections	= 1;	// Initial number of connections to be builded when open()
	private int incConnections	= 1;	// Increase number of connections to build each time

	// Properties for DBMS connection
	private String driverName;			// Driver name of JDBC
	private String jdbcURL;				// Database URL location
	private String loginUser;			// Database login id
	private String loginPswd;			// Database login password

	// Character encoding convert control
	private String	dbEncoding	= "UTF-8";	// Database character encoding
	private String	vmEncoding	= "UTF-8";	// Java VM character encoding
	private boolean	sendEncode	= true;		// Encode flag when send to database
	private boolean	recvEncode	= false;	// Encode flag when recveive from database

	/**
	 * Constructor for JSP bean, must setXXX() and open() before using.
	 */
	public DBPool() {}

	/**
	* Constructor with DB properities and connections control for initial 
	* the database connections.
	 *
	* @param   drv  Driver name of JDBC.
	* @param   url  Database URL location.
	* @param   user Database login id.
	* @param   pswd Database login password.
	* @param   max  Maximum number of connections can be builded.
	* @param   ini  Initial number of connections to be builded when open().
	* @param   inc  Increase number of connections to build each time.
	*/
	public DBPool(String drv, String url, String user, String pswd,
			int max, int ini, int inc)  {
		// Properties for DBMS connection
		this.driverName	= drv;
		this.jdbcURL	= url;
		this.loginUser	= user;
		this.loginPswd	= pswd;

		// Connections allocation control
		this.maxConnections = max;
		this.iniConnections = ini;
		this.incConnections = inc;
	}

	/**
	 * Enable connection Pool for initial connections established
	 * 
	 * @throws  SQLException  If connect to DB error.
	 */
	public void open() throws SQLException {
		// Check connectionPool has been established
		if(connectionPool != null) return;
		
		// Initial connectionPool connections
		Connection conn = null;
		try {
			// Load the specified driver class
			Class.forName(driverName);
			connectionPool = new Hashtable(iniConnections, incConnections);
			bldConnections = 0;
			// Get connections from DriverManager
			for(int i=0; i < iniConnections && bldConnections < maxConnections; i++) {
				// Add the connection into connectionPool
				conn = DriverManager.getConnection(jdbcURL, loginUser, loginPswd);
				connectionPool.put(conn, new DBFlag(false));
				bldConnections++;
			}
		} catch(Exception ex) {
			// 2004/07/28-Jackie, Keep open to wait DB started.
			LOG.debug("open() failure - " + ex.getMessage());
			// Reset the connectionPool
//			try {close();} catch(Exception ex1) {}
//			throw new SQLException(ex.getMessage());
		} finally {
			if(conn!=null) {
				this.safeClose(conn);
			}
		}
	}

	/**
	 * Close all connections and release connecttion pool
	 */
	public void close() {
		// Check connectionPool has been established
		if(connectionPool == null) return;
		
		// Close coonections in pool
		Enumeration conns = connectionPool.keys();
		while(conns.hasMoreElements()) {
        	Connection conn = (Connection)conns.nextElement();
			try{conn.close();}catch(Exception ex){}
		}

		// Free connectionPool Hashtable contents
		connectionPool.clear();
		connectionPool = null;
	}
	
	/**
	 * Get free connection from connectionPool
	 * 
	 * @throws  SQLException  If connect to DB error or no free connection.
	 */
	public Connection getConnection() throws SQLException {
		// Check connectionPool has been established
		if(connectionPool == null) {
			throw new SQLException("DB_POOL_NOT_INIT");
		}
		
		// Synchronize control
		synchronized(this) {
			// Get connection from connectionPool which is not in use
			Enumeration conns = connectionPool.keys();
			while(conns.hasMoreElements()) {
				Connection conn = (Connection)conns.nextElement();
				DBFlag flag = (DBFlag)connectionPool.get(conn);
				if(!flag.isInUse()) {
					// Test its integrity with a quick setAutoCommit(true) call.
					try {
						// Test connection OK
						// 2004/07/28-Jackie, use dumy select to test.
						Statement stmt = conn.createStatement();
						stmt.executeQuery("select getdate()");
						stmt.close();
					} catch(SQLException e) {
						// Problem with the connection, replace it.
						LOG.debug("Problem connection, retry connect to DB.");
						connectionPool.remove(conn);
						try{conn.close();}catch(Exception ex){}
						conn = DriverManager.getConnection(jdbcURL, loginUser, loginPswd);
						connectionPool.put(conn, flag);
					}
					// Update the Hashtable to show this one's taken
					flag.setInUse(true);
					// Return the connection
					return conn;
				}
			}
			
			// No free connection, make more if maximun limited not reached
			if(bldConnections < maxConnections) {
				// Allowcate new connections into connectionPool of increse no
				Connection conn = null;
				try {
					for(int i=0; i < incConnections && bldConnections < maxConnections; i++) {
						// Add the connection into free pool
						conn = DriverManager.getConnection(jdbcURL, loginUser, loginPswd);
						connectionPool.put(conn, new DBFlag(false));
						bldConnections++;
					}
					if(conn!=null) {
						conn.isClosed();
					}
					// Recursive call to get connection
					return this.getConnection();
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					this.safeClose(conn);
				}
			}
			
			// Check is any connections is idle
			long minAccessTime = 0;
			DBFlag idleFlag = null;
			Connection idleConn = null;
			conns = connectionPool.keys();
			while(conns.hasMoreElements()) {
				Connection conn = (Connection)conns.nextElement();
				DBFlag flag = (DBFlag)connectionPool.get(conn);
				if(flag.isIdle()) {
					if( (flag.lastAccessTime() < minAccessTime) || (minAccessTime == 0) ) {
						idleFlag = flag;
						idleConn = conn;
						minAccessTime = flag.lastAccessTime();
					}
				}
			}
			// Return the idle connection
			// v1.02, 修正 Fortify 白箱掃描(Null Dereference)
			if(idleFlag != null && idleConn != null) {
				idleFlag.setInUse(true);
				idleConn.commit();
				idleConn.setAutoCommit(true);
				return idleConn;
			}
			// Max connection reached, throws error
			throw new SQLException("DB_POOL_MAX_LIMIT");
		} // end of synchronized
	}

    /**
     * Restore connection from user, mark it not in use
	 * 
     * @param   returned  The connection which get from getConnection().
     */
	public void returnConnection(Connection returned) {
		// Check is connection is null
		if(returned == null) return;
		
		// Check connectionPool has been established
		if(connectionPool == null) {
			try{returned.close();}catch(Exception ex){}
			return;
		}
		
		// Synchronize control
		synchronized(this) {
			// Find the current in use connection
			Enumeration conns = connectionPool.keys();
			while(conns.hasMoreElements()) {
				Connection conn = (Connection)conns.nextElement();
				if(conn == returned) {
					// Commit before restore
					try {
						conn.commit();
						conn.setAutoCommit(true);
					} catch (SQLException ex) {}
					// Mark it not in use
					DBFlag flag = (DBFlag)connectionPool.get(conn);
					flag.setInUse(false);
					return;
				}
			}
			// Close connection if not found in ConnectioPool
			try{returned.close();}catch(Exception ex){}
		} // end of synchronized
	}

	/**
	 * Set driver name of JDBC.
	 * @param   drv  Driver name of JDBC.
	 */
	public void setDriverName(String drv) {
		this.driverName = drv;
	}

	/**
	 * Set database URL location.
	 * @param   url  Database URL location.
	 */
	public void setJdbcURL(String url) {
		this.jdbcURL = url;
	}

	/**
	 * Set database login id.
	 * @param   user Database login id.
	 */
	public void setLoginUser(String user) {
		this.loginUser = user;
	}

	/**
	 * Set database login password.
	 * @param   pswd Database login password.
	 */
	public void setLoginPswd(String pswd) {
		this.loginPswd = pswd;
	}

	/**
	 * Set maximum number of connections can be builded.
	 * @param   max  Maximum number of connections can be builded.
	 */
	public void setMaxConnections(int max) {
		this.maxConnections = max;
	}

	/**
	 * Set initial number of connections to be builded when open().
	 * @param   ini  Initial number of connections to be builded when open().
	 */
	public void setIniConnections(int ini) {
		this.iniConnections = ini;
	}

	/**
	 * Set increase number of connections to build each time.
	 * @param   inc  Increase number of connections to build each time.
	 */
	public void setIncConnections(int inc) {
		this.incConnections = inc;
	}

	/**
	 * Set database character encoding.
	 * @param   dbenc   Database character encoding.
	 */
	public void setDbEncoding(String dbenc) {
		this.dbEncoding = dbenc;
	}

	/**
	 * Set Java VM character encoding.
	 * @param   vmenc   Java VM character encoding.
	 */
	public void setVmEncoding(String vmenc) {
		this.vmEncoding = vmenc;
	}
	
	/**
	 * Set encode flag when send to database.
	 * @param   send    Encode flag when send to database.
	 */
	public void setSendEncode(int send) {
		this.sendEncode = (send == 1 ? true : false);
	}
	
	/**
	 * Set encode flag when recveive from database.
	 * @param   recv    Encode flag when recveive from database.
	 */
	public void setRecvEncode(int recv) {
		this.recvEncode = (recv == 1 ? true : false);
	}
	
	/**
	 * Get driver name of JDBC.
	 */
	public String getDriverName() {
		return this.driverName;
	}

	/**
	 * Get database URL location.
	 */
	public String getJdbcURL() {
		return this.jdbcURL;
	}

	/**
	 * Get database login id.
	 */
	public String getLoginUser() {
		return this.loginUser;
	}

	/**
	 * Get database login password.
	 */
	public String getLoginPswd() {
		return this.loginPswd;
	}

	/**
	 * Get maximum number of connections can be builded.
	 */
	public int getMaxConnections() {
		return this.maxConnections;
	}

	/**
	 * Get initial number of connections to be builded when open().
	 */
	public int getIniConnections() {
		return this.iniConnections;
	}

	/**
	 * Get increase number of connections to build each time.
	 */
	public int getIncConnections() {
		return this.incConnections;
	}

	/**
	 * Get database character encoding.
	 */
	public String getDbEncoding() {
		return this.dbEncoding;
	}

	/**
	 * Get Java VM character encoding.
	 */
	public String getVmEncoding() {
		return this.vmEncoding;
	}
	
	/**
	 * Get encode flag when send to database.
	 */
	public boolean getSendEncode() {
		return this.sendEncode;
	}
	
	/**
	 * Get encode flag when recveive from database.
	 */
	public boolean getRecvEncode() {
		return this.recvEncode;
	}

	/**
	 * Convert string charset after retrieve row data
	 * 
	 * @param   src  The string to be converted.
	 */
	public String recvDBMS(String src) {
		// Check convert setting
		if(!this.recvEncode) return src;

		// If db charset is 'big5', need not to convert
		if(dbEncoding.equalsIgnoreCase("UTF-8")) return src;

		// Convert string charset
		try {
			return new String(src.getBytes(dbEncoding), vmEncoding);
		} catch(UnsupportedEncodingException e) {
			return src;
		}
	}

	/**
	 * Convert string charset before update to DB
	 * 
	 * @param   src  The string to be converted.
	 */
	public String sendDBMS(String src) {
		// Check convert setting
		if(!this.sendEncode) return src;

		// If same charset, need not to convert
		if(dbEncoding.equalsIgnoreCase(vmEncoding)) return src;

		// Convert string charset
		try {
			return new String(src.getBytes(vmEncoding), dbEncoding);
		} catch(UnsupportedEncodingException e) {
			return src;
		}
	}
	
	/**
	 * Finalize - Close all connections before destroy
	 * 
	 * @throws  Throwable  If error occured.
	 */
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}
	
	public static void safeClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				
			}
		}
	}
}