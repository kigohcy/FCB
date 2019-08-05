/*
 * @(#)DBImp.java
 *
 * Copyright (c) 2008 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v3.00, 2008/07/31, Ada Chen
 *   1) First release
 *   2) FUBONCR-417, (越南地區)多語系、多地區別 - 共用模組與Framework
 *  v3.01, 2008/08/18, Ada Chen
 *   1) FUBONCR-431, (越南地區)Log輸出調整
 *  v4.00, 2009/03/27, Jmiu Han
 *   1) FUBONCR-738, modify method:getDBName(),增加district為空值時的處理
 *  v4.01, 2017/09/08, Eason Hsu
 *   1) TSBACL-165, [Fortify] Unreleased Resource
 */
package com.hitrust.acl.db;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.util.LocaleUtil;
import com.hitrust.acl.util.MAC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBImp {
 
	//Log4j
	static Logger LOG = Logger.getLogger(DBImp.class);
	
	//Error Code
	//Global Error
	public static final String ERR_DB_INIT_FAIL = "1"; //Database連結初始化失敗
	public static final String ERR_DB_NOT_INIT = "2"; //Database連結尚未初始化
	public static final String ERR_DB_OVER_RETRY = "3"; //已超過Retry次數
	public static final String ERR_DB_RETURN_CONN = "4"; //Return Connection發生錯誤
	public static final String ERR_AP_NOT_CORR = "5"; //指定Application名稱不正確
	public static final String ERR_DSTR_NOT_CORR = "6";//指定區域別不正確的
	//JDBC Error
	public static final String ERR_JNDI_NAME = "11"; //無效的JNDI名稱
	//DBPool Error
	public static final String ERR_DBP_GET_CONN = "21"; //DBPool取得connection失敗
	public static final String ERR_DBP_NAME_NOT_CORR = "22"; //DBPool名稱錯誤
	
	//Database Type
	public static final String DATABASE_TYPE_DBPool = "DBPool";
	public static final String DATABASE_TYPE_JDBC = "JDBC";
	public static final String DATABASE_TYPE_DATASOURCE = "DataSource";

	//Default System Database District
	private static final String SYSTEM_DSTR = "TW";
	
	//Database config and parameter
	private Document databaseDoc;
	private String dbConnType;
	private int retryCount;
	private int interval;
	 

	/**
	 *DBPool environment
	 */
	private HashMap dbPoolParams;
	 
	/**
	 *JDBC environment
	 */
	private HashMap jdbcParams;
	 
	/**
	 *Datasource environment
	 */
	private HashMap dataSourceCol;
	 
	 
	/**
	 * Consturctor
	 * @param databaseDoc - Database配置檔(database.xml)
	 * @param dbConnType - Database連結方式
	 * @param retryCount - retry次數
	 * @param interval - retry間隔時間
	 * @throws DBException - if initial database environment error occurs
	 */
	public DBImp(Document databaseDoc, String dbConnType, int retryCount, int interval) throws DBException {
	 
		this.databaseDoc = databaseDoc;
		this.dbConnType = dbConnType;
		this.retryCount = retryCount;
		this.interval = interval;

		try {
			//依據連結方式初始化DB環境
			if (DATABASE_TYPE_DBPool.equals(dbConnType)) {
			    initialDBPoolEnvironment();
			} else if (DATABASE_TYPE_JDBC.equals(dbConnType)) {
			    initialJDBCEnvironment();
			} else if (DATABASE_TYPE_DATASOURCE.equals(dbConnType)) {
			    initialDatasourceEnvironment();
			}
		} catch (NamingException ex) {
			LOG.error("[dbConnType]"+dbConnType); //v3.01 Log輸出調整
			LOG.error("Initial DB Environment Error: " + ex.toString());
			throw new DBException(ex.toString(), ERR_JNDI_NAME);
		} catch (Exception ex) {
			LOG.error("[dbConnType]"+dbConnType); //v3.01 Log輸出調整
			LOG.error("Initial DB Environment Error: " + ex.toString());
			throw new DBException(ex.toString(), ERR_DB_INIT_FAIL);
		}
		
	}
	 
	/**
	 * 取得資料庫 Connection<br>
	 *
	 * 可依Application名稱及區域別，取得對應的資料庫Connection。<br>
	 * @param appName - Application名稱
	 * @param district - 區域別
	 * @return Connection - a connection from Database enviroment
	 * @throws DBException - if a database access error occurs
	 */
	public Connection getConnection(String dbName) throws DBException {
		
		try{
			//依據連結方式及DB Name取得Connection並回傳
			if (DATABASE_TYPE_DBPool.equals(dbConnType)) {
			    return getDBPoolConnection(dbName);
			} else if (DATABASE_TYPE_JDBC.equals(dbConnType)) {
			    return getJDBCConnection(dbName);
			} else if (DATABASE_TYPE_DATASOURCE.equals(dbConnType)) {
			    return getDSConnection(dbName);
			}
		}catch(DBException ex){
			//v3.01 Log輸出調整
			LOG.error("[dbName]"+dbName+"[dbConnType]"+dbConnType);
			throw ex;
		}

		return null;
	}

	/**
	 * Initial DBPool environment
	 * @throws SQLException - if initial DBPool object error occurs 
	 * @throws Exception - if a database configuration access error occurs
	 */
	private void initialDBPoolEnvironment() throws SQLException, Exception {
        this.dbPoolParams = new HashMap();
        
        //自database.xml取得DBPool設定值
        NodeList dbPoolList = this.databaseDoc.getElementsByTagName(DATABASE_TYPE_DBPool);
        for (int i=0; i<dbPoolList.getLength(); i++) {
            Element dbPoolItem = (Element) dbPoolList.item(i);
            
            String dbPoolName = dbPoolItem.getAttribute("name");

            //依據設定值初始化DBPool物件
            DBPool dbp = new DBPool();
            dbp.setDriverName(dbPoolItem.getAttribute("driverName"));
            dbp.setJdbcURL(dbPoolItem.getAttribute("jdbcURL"));
            dbp.setLoginUser(dbPoolItem.getAttribute("loginUser"));
            //登入DB密碼需做解密動作
            dbp.setLoginPswd(MAC.decryptePswd(dbPoolItem.getAttribute("loginPswd")));
            dbp.setMaxConnections(Integer.parseInt(dbPoolItem.getAttribute("maxConnections")));
            dbp.setIniConnections(Integer.parseInt(dbPoolItem.getAttribute("iniConnections")));
            dbp.setIncConnections(Integer.parseInt(dbPoolItem.getAttribute("incConnections")));
            dbp.setDbEncoding(dbPoolItem.getAttribute("dbEncoding"));
            dbp.setVmEncoding(dbPoolItem.getAttribute("vmEncoding"));
            dbp.setSendEncode(Integer.parseInt(dbPoolItem.getAttribute("sendEncode")));
            dbp.setRecvEncode(Integer.parseInt(dbPoolItem.getAttribute("recvEncode")));
            dbp.open();
            
            //以DBPool Name為Key, 將DBPool物件放置於HashMap中
            this.dbPoolParams.put(dbPoolName, dbp);
        }
	}
	 
	/**
	 * Initial JDBC environment
	 * @throws Exception - if a database configuration access error occurs
	 */
	private void initialJDBCEnvironment() throws Exception{
        this.jdbcParams = new HashMap();
        
        //自database.xml取得JDBC設定值
        NodeList jdbcList = this.databaseDoc.getElementsByTagName(DATABASE_TYPE_JDBC);
        for (int i=0; i<jdbcList.getLength(); i++) {
            Element jdbcItem = (Element) jdbcList.item(i);
            
            //以JDBC Name為Key, 將JDBC連結參數資訊放置於HashMap中
            String jdbcName = jdbcItem.getAttribute("name");
            HashMap params  = new HashMap();
            params.put("driverName", jdbcItem.getAttribute("driverName"));
            params.put("jdbcURL"   , jdbcItem.getAttribute("jdbcURL"));
            params.put("loginUser" , jdbcItem.getAttribute("loginUser"));
            params.put("loginPswd" , jdbcItem.getAttribute("loginPswd"));
            
            this.jdbcParams.put(jdbcName, params);
        }
	}
	 
	/**
	 * Initial datasource environment
	 * @throws NamingException - if JNDI name not correct
	 * @throws Exception - if a database configuration access error occurs
	 */
	private void initialDatasourceEnvironment() throws NamingException, Exception{
        dataSourceCol = new HashMap();
        
        Context jndiContext = new InitialContext();
        
        //自database.xml取得DataSouce設定值
        NodeList dsList = databaseDoc.getElementsByTagName(DATABASE_TYPE_DATASOURCE);
        for (int i=0; i<dsList.getLength(); i++) {
            Element dsItem = (Element) dsList.item(i);
            
            String dsName = dsItem.getAttribute("name");
            
            //以DataSource Name為Key, 將DataSource物件放置於HashMap中
            dataSourceCol.put(dsName, (DataSource) jndiContext.lookup(dsItem.getAttribute("jndiName")));
        }
	}
	 
	/**
	 * Get DBPool connection
	 * @param dbPoolName - DBPool name
	 * @return Connection - a connection from the DBPool 
	 * @throws DBException - if a database access error occurs
	 */
	private synchronized Connection getDBPoolConnection(String dbPoolName) throws DBException {
		
        Connection conn = null;
		
        //檢查DBPool環境是否initial成功
        if(this.dbPoolParams.isEmpty()) {
            throw new DBException("DBPool environment not initial! ", ERR_DB_NOT_INIT);
        }

        try {
            // 依據DBPool Name至HashMap中取出DBPool物件
            if (this.dbPoolParams.containsKey(dbPoolName)) {
            	DBPool dbPool = (DBPool) this.dbPoolParams.get(dbPoolName);
            	//透由DBPool物件取得Connection
                conn = dbPool.getConnection();
            } else {
            	// 在HashMap中找不到對應的DBPool物件
            	throw new DBException("DBPool name isn't exist! ", ERR_DBP_NAME_NOT_CORR);
            }
        } catch(DBException ex){
        	throw ex;
        } catch(Exception ex) {
        	LOG.error("DBPool get conneciton error! " + ex.toString());
            throw new DBException(ex.getMessage(), ERR_DBP_GET_CONN);
        }
        
        if (conn != null) {
            return conn;
        } else {
        	LOG.error("DB connect times is great than retry count! ");
            throw new DBException("DB connect times is great than retry count! ",ERR_DB_OVER_RETRY);
        }
	}
	 
	/**
	 * Get JDBC connection
	 * @param jdbcName - JDBC name
	 * @return Connection - a connection from the JDBC 
	 * @throws DBException - if a database access error occurs
	 */
	private synchronized Connection getJDBCConnection(String jdbcName) throws DBException {
        Connection conn = null;
        Statement stmt = null; // v4.01, 修正 Fortify 白箱掃描(Unreleased Resource: Database)
        
        //檢查JDBC環境是否initial成功
        if (this.jdbcParams.isEmpty()){
            throw new DBException("JDBC environment not initial! ", ERR_DB_NOT_INIT);
        }
        
        // for-each Retry次數
        for (int i=0; i<this.retryCount; i++) {
        	
            // 依據JDBC name至HashMap取得對應的JDBC參數
            if (this.jdbcParams.containsKey(jdbcName)) {
                HashMap params = (HashMap) jdbcParams.get(jdbcName);
                
                String url    = (String) params.get("jdbcURL");
                String driver = (String) params.get("driverName");
                String user   = (String) params.get("loginUser");
                // 登入DB密碼需做解密動作
                String pswd   = MAC.decryptePswd((String) params.get("loginPswd"));
                try {
                	// 透由DriverManager取得Connection
                    Class.forName(driver);
                    conn = DriverManager.getConnection(url, user, pswd);
                    // 執行connection測試, 確保connection為有效
                    stmt = conn.createStatement();
                    stmt.executeQuery("select getdate()");
                    break;
                } catch (ClassNotFoundException ex) {
                	LOG.error("JDBC Driver class not found! " + ex.toString());
                    // close bad connection before next connect.
                    try { if (conn != null) conn.close(); } catch (SQLException e) {}
                } catch (SQLException ex) {
                	LOG.error("Failed to get connection from JDBC! " + ex.toString());
                    // close bad connection before next connect.
                    try { if (conn != null) conn.close(); } catch (SQLException e) {}
                    
                } finally {
                	// v4.01, 修正 Fortify 白箱掃描(Unreleased Resource: Database)
                	if (stmt != null) {
						try { stmt.close(); } catch (SQLException e) { LOG.error("Failed to close Statement", e); }
					}
                }
                
                // sleep before next to get connection
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException ex) {
                	//TODO
                    ex.printStackTrace();
                }
            }
        }
        if (conn != null) {
            return conn;
        } else {
        	LOG.error("DB connect times is great than retry count! ");
            throw new DBException("DB connect times is great than retry count! ", ERR_DB_OVER_RETRY);
        }
	}
	 
	/**
	 * Get Datasource connection
	 * @param dsName - DataSource name
	 * @return Connection - a connection from the DataSource 
	 * @throws DBException - if a database access error occurs
	 */
	private synchronized Connection getDSConnection(String dsName) throws DBException {
        Connection conn = null;
        Statement stmt = null; // v4.01, 修正 Fortify 白箱掃描(Unreleased Resource: Database)
        
        //檢查DataSource環境是否initial成功
        if (this.dataSourceCol.isEmpty()){
            throw new DBException("DataSource environment not initial! ", ERR_DB_NOT_INIT);
        }
        
        // 依據DataSource name至HashMap取得對應的DataSource物件
        if (this.dataSourceCol.containsKey(dsName)) {
            DataSource dataSource = (DataSource) this.dataSourceCol.get(dsName);
            // for-each Retry次數
            for (int i=0; i<this.retryCount; i++) {
                try {
                	//透過DataSource物件取得connection
                    conn = dataSource.getConnection();
                    // 執行connection測試, 確保connection為有效
                    stmt = conn.createStatement();
                    stmt.executeQuery("select getdate()");
                    break;
                } catch (SQLException ex) {
                	LOG.error("Failed to get connection from JDBC! " + ex.toString());
                    // close bad connection before next connect.
                    try { if (conn != null) conn.close(); } catch (SQLException e) {}
                    
                } finally {
                	// v4.01, 修正 Fortify 白箱掃描(Unreleased Resource: Database)
                	if (stmt != null) {
						try { stmt.close(); } catch (SQLException e) { LOG.error("Failed to close Statement", e); }
					}
                }
                
                
                // sleep before next to get connection
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException ex) {
                	//TODO
                    ex.printStackTrace();
                }
            }
        }
        if (conn != null) {
            return conn;
        } else {
        	LOG.error("DB connect times is great than retry count! ");
            throw new DBException("DB connect times is great than retry count! ", ERR_DB_OVER_RETRY);
        }
	}
	 
	
    /**
     * Return connection to DBPool
     * @param conn - 欲歸還至DBPool的Connection
     * @param dbPoolName - DBPool name
     * @throws DBException - if DBPool retrun conneciton error occurs
     */
    private synchronized void returnConnectionToDBPool(Connection conn, String dbPoolName) throws DBException {
    	
        //檢查DBPool環境是否initial成功
        if(this.dbPoolParams.isEmpty()) {
            throw new DBException("DBPool environment not initial! ", ERR_DB_NOT_INIT);
        }

        try {
            // 依據DBPool Name至HashMap中取出DBPool物件
            if (this.dbPoolParams.containsKey(dbPoolName)) {
            	DBPool dbPool = (DBPool) this.dbPoolParams.get(dbPoolName);
            	//將connection還回至DBPool
                dbPool.returnConnection(conn);
            } else {
            	// 在HashMap中找不到對應的DBPool物件
            	throw new DBException("DBPool name isn't exist! ", ERR_DBP_NAME_NOT_CORR);
            }
        } catch(DBException ex){
        	throw ex;
        } catch(Exception ex) {
        	LOG.error("Failed to reutrn connection into DBPool! " + ex.toString());
            throw new DBException(ex.getMessage(), ERR_DB_RETURN_CONN);
        }
    }
	
	/**
	 * 歸還資料庫 Connection<br>
	 *
	 * 可依Application名稱及區域別，歸還對應的資料庫Connection。<br>
	 * @param conn - the connection to be retured
	 * @param appName - Application名稱
	 * @param district - 區域別
	 */
	public synchronized void returnConnection(Connection conn, String dbName) {
		
        try {
            if (conn!=null) {
                if (!conn.getAutoCommit()) {
                    conn.commit();
                    conn.setAutoCommit(true);
                }
                //若為連結方式為DBPool, 則將connecton歸還至DBPool中
        		if (DATABASE_TYPE_DBPool.equals(dbConnType)) {
        			returnConnectionToDBPool(conn, dbName);
        		}else{
        			conn.close();
        			conn = null;
        		}
            }
        } catch (DBException ex){
        	//v3.01 Log輸出調整
			LOG.error("[dbName]"+dbName+"[dbConnType]"+dbConnType);
        	LOG.error("Failed to reutrn connection! " + ex.toString());
        } catch (SQLException ex) {
        	//v3.01 Log輸出調整
			LOG.error("[dbName]"+dbName+"[dbConnType]"+dbConnType);
        	LOG.error("Failed to reutrn connection! " + ex.toString());
        } finally{
        	if (conn != null) try {conn.close();} catch (Exception ignore){}
        }

	}
	 
}
 
