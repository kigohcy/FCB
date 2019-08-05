/*
 * @(#)APSystem.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 * 
 * Modify History:
 *  v1.00, 2016/03/21, Ada Chen
 *   1) First release
 *  v1.01, 2016/12/02, Eason Hsu
 *   1) TSBACL-137, ECGW 綁定連結帳號驗證方式調整
 *  v1.02, 2017/09/08, Eason Hsu
 *   1) TSBACL-165, [Fortify] Unreleased Resource
 *   2) TSBACL-164, [Fortify] Path Manipulation
 *  v1.03, 2017/09/27, Eason Hsu
 *   1) TSBACL-164, [Fortify] Path Manipulation
 */
package com.hitrust.acl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hitrust.acl.common.FilePathUtil;
import com.hitrust.acl.common.ValidationParam;
import com.hitrust.acl.db.DBImp;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.util.DOM;
import com.hitrust.acl.util.MAC;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.acl.util.XmlConfig;

public class APSystem {
    // log4J category
	static Logger LOG = Logger.getLogger(APSystem.class);

    // Global pulic variables for system access
    public static final String PROPERTY_FILE    = "ecgw"; // Application properitiy file name
    public static final String SESSION_KEY 		= "lib.sessionpk"; // v4.51 User's session PK 
    
    // Flow control URL for forward when error occured
    public static final String INDEX_URL = "/index.jsp";
    public static final String ERROR_URL = "/message/error.jsp";
    public static final String CLOSE_URL = "/message/close.jsp";
    public static final String WARN_URL = "/message/warn.jsp";
    public static final String INFO_URL = "/message/info.jsp";
    public static final String AUTO_ERROR_URL = "/message/error_auto.jsp";
    public static final String POP_TIME_OUT_URL = "/message/pop_close.jsp";
    public static final String FRAME_TIME_OUT_URL = "/message/timeout_auto.jsp";
    
    // Database parameters
    public static final String DB_ACLINK = "ACLINK";
    public static final String DB_SMS = "SMSDB";

    // Request/Response parameters with EC
    public static final String DEF_SIGN = "sign";
    public static final String DEF_DIGEST = "hash";
    public static final String SERVICE_UNAVAILABLE = "503";
    public static final String REQUEST_ENCODING = "Big5";
    public static final String RESPONSE_ENCODING = "Big5";
    public static final String USER_AGENT = "Mozilla/5.0";
    
    // Private properities for service
    private static String sysDir = null;
    private static String sysErr = null;
    private static Document messageDoc;
    private static XmlConfig systemConfig;
    private static Document serviceConfigDoc;   
    
    // Project parameters
    private static Document projectDoc;
    private static HashMap  projectParams;

    // Validation parameters
    private static Document validationDoc;
    private static Map<String, Map<String, ValidationParam>> validationParams;
    private static Map<String, HashMap> signatureParams;
    private static Map<String, HashMap> digestParams;

    // Request encoding & Response content type
    private static String encoding; 
    
    // System menus
    private static HashMap sysServiceDef;
    
    // System message
    private static HashMap sysMessage;
    
    // v3.01 DataBase Implement object
    private static DBImp dbImp;
	
    // Database conifguration
    private static String dbConnType;
    private static int retryCount;
    private static int interval;
    
    // Initial values for application's properities
    static {
        try {
            // Get application root directory
        	//sysDir = APSystem.class.getClassLoader().getResource("/").getPath().replace("WEB-INF/classes/", "");
        	
//        	LOG.info("jboss.home.dir: " + System.getProperty("jboss.home.dir"));
//        	
//        	LOG.info("jboss.server.home.dir: " + System.getProperty("jboss.server.home.dir"));
//        	
//        	LOG.info("jboss.server.base.dir: " + System.getProperty("jboss.server.base.dir"));
//        
//        	String classpath = Thread.currentThread().getContextClassLoader().getResource("hitrustUtil.jar").getPath();
//        	
//        	Thread.currentThread().getContextClassLoader().getResource("/").getPath(); 
//        	
//        	LOG.info("jeff1: " + System.getProperty(Thread.currentThread().getContextClassLoader().getResource("hitrustUtil.jar").getPath()));
//        	
//        	LOG.info("jeff2: " + System.getProperty(Thread.currentThread().getContextClassLoader().getResource("/").getPath()));
//        	
//        	LOG.info("jeff3: " + System.getProperty(Thread.currentThread().getContextClassLoader().getResource("").getPath()));
        	sysDir = "D:/";

    		
        	ResourceBundle bundle = ResourceBundle.getBundle(PROPERTY_FILE);
			
			// get Character Encoding
            encoding = bundle.getString("request.CharacterEncoding");
            
            // outside config directory
            String outsideCfgDir = bundle.getString("outside.config.dir");
            
            // the config directory
            String cfgDir = sysDir + bundle.getString("config.directory");
            
            // loading configure XML files
//          systemConfig  = new XmlConfig(cfgDir + "/" + prop.getProperty("file.system"));
            serviceConfigDoc = DOM.loadDoc(cfgDir + File.separator + bundle.getString("file.service"));
            loadSysServiceDef();
            
            // load project parameter
            projectDoc = DOM.loadDoc(outsideCfgDir + File.separator + bundle.getString("file.project"));
            loadProjectParameters();

            // load validation parameter
            validationDoc = DOM.loadDoc(cfgDir + File.separator + bundle.getString("file.validation"));
            loadValidationParameters();
            
            // loading system message
            messageDoc = DOM.loadDoc(cfgDir + File.separator + bundle.getString("file.message"));
            loadSysMessage();
           
            // Initial system directories
            initialSystemDirectories(outsideCfgDir + File.separator + bundle.getString("file.logconfig"));
            
            // log4j configuration
            PropertyConfigurator.configure(outsideCfgDir + File.separator + bundle.getString("file.logconfig"));
           
            // Setup the Connection pool            
            // 傳入相關參數以供DBImp初始化使用
            dbConnType = bundle.getString("database.pool");
            LOG.info("Connection pool type : " + dbConnType); 
            retryCount = Integer.parseInt(APSystem.getProjectParam("DB_CONN_RETRY_COUNT"));
            interval =  Integer.parseInt(APSystem.getProjectParam("DB_CONN_WAIT_TIME"));
            initialDatabase(outsideCfgDir + "/" + bundle.getString("file.database"));
            
            LOG.info("APSystem initial successfully! ");
        }
        catch(Exception ex) {
        	ex.printStackTrace();
            // store the initial error message for display.
            sysErr = ex.getMessage();
            System.out.println("APSystem initial exception:"+sysErr);
        }
    }
    
    
    /**
     * Initial database environment
     * @param dbConfigFile - Database配置檔(database.xml)
     * @throws Exception - if initial database environment error occurs
     */    
    static private void initialDatabase(String dbConfigFile) throws Exception {
    	
    	// 1.Get database configuration
    	Document databaseDoc = DOM.loadDoc(dbConfigFile);
    	
    	try {
        	// 2.Initila DataBase Implement Object
    		dbImp = new DBImp(databaseDoc, dbConnType, retryCount, interval);
    	} catch(DBException ex) {
			LOG.error("Initial DB Environment Error: " + ex.getMessage()+ ",[ErrorCode:" + ex.getErrorCode() +"]");
			throw new DBException(ex.getMessage(), "DBP_NOT_INIT");
    	}
    } 

    /**
     * Get Connection
     * @param dbName - Database name
	 * @return Connection - a connection 
     * @throws DBException - if a database access error occurs
     * 調整透過DBImp物件來取得DB Connection
     */
    static public Connection getConnection(String dbName) throws DBException {
    	
    	try {
    		return dbImp.getConnection(dbName);
    	} catch(DBException ex) {
			LOG.error("Get connection Error: " + ex.getMessage()+ ",[ErrorCode:" + ex.getErrorCode() +"]");
			throw new DBException(ex.getMessage(), "DBP_GET_CONN_ERR");
    	}
    } 
    
    /**
     * Return Connection 
     * @param conn	the connection to be retured
	 * @param dbName - Database name
     */
    static public void returnConnection(Connection conn, String dbName) {

    	dbImp.returnConnection(conn, dbName);
    }
    
    /**
     * Get system default encoding
     * @return
     */
    static public String getSystemDefaultEncoding() {
        return getProjectParam("DEFAULT_ENCODING");
    }    
    /**
     * Get application root directory.
     * @return the application root directory.
     */
    static public String getSysDir() {
        return sysDir;
    }
    
    static public void setSysDir(String sysDir) {
		APSystem.sysDir = sysDir;
	}

	/**
     * Get application initial ststus error message.
     * @return the initial error message, null means OK.
     */
    static public String getSysErr() {
        return sysErr;
    }
    /**
     * put application initial ststus error message.
     */
    static public void setSysErr(String err) {
        sysErr = err;
    }
    /**
     * Get application message by specify return code.
     * @return the message string.
     */
    static public HashMap getServiceItem(String name) {
    	HashMap serviceItemMap = null;
    	if (sysServiceDef.containsKey(name)) {
    		serviceItemMap = (HashMap)sysServiceDef.get(name);
    	}
    	return serviceItemMap;
    }

    /**
     * Get application Character Encoding
     * @return the application Character Encoding
     */
    static public String getEncoding() {
        return encoding;
    }    

    /**
     * Get application message by specify return code.
     * @return the message string.
     */
    static public String codeMessage(String language, String retCode) {
    	HashMap messageItemMap = (HashMap) sysMessage.get(language);
    	if (messageItemMap == null) {
    		return "";
    	} else if (!messageItemMap.containsKey(retCode)) {
    		return retCode + " " + codeMessage(language, "SYS16"); //當Error Code未被定義在message.xml中, 拋出預設訊息
    	} else {
    		return messageItemMap.get(retCode).toString();
    	}
    }

    /**
     * Get application message by specify return code.
     * @return a String array{error code,error message}.
     */
    static public String[] codeMessageAndCode(String language, String retCode) {
        String[] tmpErr = new String[2];
        tmpErr[0] = retCode;
        tmpErr[1] = codeMessage(language, retCode);
        
        //if message isn't exist
        if(tmpErr[1] == null)
        	tmpErr[1] = "";
        
        return tmpErr;
    }
    
    /**
     * Get parameter value of specified project
     * @param param_name
     * @return
     */
    static public String getProjectParam(String param_name) { 
        try {
        	HashMap attrMap = null;

            Iterator keyIter = projectParams.keySet().iterator();
            while (keyIter.hasNext()) {
            	String key = (String) keyIter.next();
            	if (key.equalsIgnoreCase(param_name)) {
            		attrMap = (HashMap) projectParams.get(key);
            		break;
            	}
            }
            if (attrMap == null) {  //參數不存在, 記錄LOG並回傳空字串
                LOG.warn("Param isn't exist:" + param_name); 
                return "";	
            }
            //取得加密屬性 
            String encrypt = (String) attrMap.get("encrypt"); 
            String value = ""; 
            //取得其參數值 
            if("Y".equalsIgnoreCase(encrypt)) { //加密屬性="Y',對其值做解密 
                value = MAC.decryptePswd((String)attrMap.get("value")); 
            } else { 
                value = (String)attrMap.get("value"); 
            } 
             
            return value; 
        } catch(Exception e) { 
            LOG.warn("Get Project Param Error: " + e.getMessage()); 
            return ""; 
        } 
    }

    /**
     * Get parameter value of specified Service URL
     */
    static public Map<String, ValidationParam> getValidationParam(String url) {
    	if (validationParams.containsKey(url)) {
    		return validationParams.get(url);
    	} else {
    		return null;
    	}
    }
    
    /**
     * Get application file directory location.
     * @param dirKind	the file pool knd(upload/sqlfile/...).
     * @return the file located directory.
     */
    static public String getFileDir(String dirKind) {
        systemConfig.setTagNode("directory", dirKind);
        return sysDir + systemConfig.getString("path");
    }

    /**
     * Get application unaccess time out control time.
     * @return the session time out control in second.
     */
    static public int getTimeoutSec() {
        systemConfig.setTagNode("system", "control");
        return systemConfig.getInt("timeout");
    }

    /**
     * Get application login retry times before lock.
     * @return the session time out control in second.
     */
    static public int getLoginTry() {
        systemConfig.setTagNode("system", "control");
        return systemConfig.getInt("logintry");
    }
    
    /**
     * Get system default locale
     * @return
     */
    static public String getDefaultLocale() {
    	return getProjectParam("default_locale");
    }
    
    /**
     * Get system default district
     * @return
     */
    static public String getDefaultDistrict() {
    	return getProjectParam("default_district");
    }
    
    /**
     * Load the serviceDef message document to a hash map
     */
    static private void loadSysServiceDef() {
    	sysServiceDef = new HashMap();
    	HashMap serviceItem = new HashMap();
    	NodeList commonList = serviceConfigDoc.getElementsByTagName("param");
    	for (int i=0; i<commonList.getLength(); i++) {
    		Node common = commonList.item(i);
			if (common.getNodeType() == Node.TEXT_NODE) {
				continue;
			} else {
				serviceItem.put((String)((Element)common).getAttribute("name") , (String)((Element)common).getAttribute("value"));
			}
    	}
    	sysServiceDef.put("commPackage", serviceItem);
    	NodeList serviceList = serviceConfigDoc.getElementsByTagName("service");
    	for (int i=0; i<serviceList.getLength(); i++) {
    		Node service = serviceList.item(i);
			if (service.getNodeType() == Node.TEXT_NODE) {
				continue;
			} else { 
				Element element = (Element)service;
				serviceItem = new HashMap();
				serviceItem.put("function" , (String)(element).getAttribute("function"));
				serviceItem.put("operation" , (String)(element).getAttribute("operation"));
				serviceItem.put("resbean" , (String)(element).getAttribute("resbean"));
				serviceItem.put("process" , (String)(element).getAttribute("process"));
				serviceItem.put("result" , (String)(element).getAttribute("result"));
				// v1.01, 增加 error 頁面
				serviceItem.put("error" , (String)(element).getAttribute("error"));
				serviceItem.put("process" , (String)(element).getAttribute("process"));
				sysServiceDef.put((((Element)service).getAttribute("name")).toUpperCase(), serviceItem);
			}
    	}   
    }
    
    /**
     * Load the system message document to a hash map
     */
    static private void loadSysMessage() {
    	sysMessage = new HashMap();
    	
    	NodeList languageList = messageDoc.getElementsByTagName("language");
    	for (int i=0; i<languageList.getLength(); i++) {
    		Node language = languageList.item(i);
    		
    		NodeList messageList = language.getChildNodes();    		
    		HashMap messageItemMap = new HashMap();
    		for (int j=0; j<messageList.getLength(); j++) {
    			Node message = messageList.item(j);
    			if (message.getNodeType() == Node.TEXT_NODE) {
    				continue;
    			} else { 
    				String name = ((Element)message).getAttribute("name");
    				String msg  = ((Element)message).getAttribute("msg");
    				messageItemMap.put(name, msg);
    			}    			
    		}
    		sysMessage.put(((Element)language).getAttribute("name"), messageItemMap);
    	}  
    }
    
    /**
     * Get all project parameter to a hash map
     */
    static private void loadProjectParameters() {
    	projectParams = new HashMap();
    	
    	NodeList projectParamList = projectDoc.getElementsByTagName("param");
    	for (int i=0; i<projectParamList.getLength(); i++) {
    		Element paramItem = (Element) projectParamList.item(i);
    		
    		String paramName  = paramItem.getAttribute("name");
    		
    		NamedNodeMap namedNodeMap = paramItem.getAttributes();
    		HashMap attrMap  = new HashMap();
    		for (int j=0; j<namedNodeMap.getLength(); j++) {
    			Node attrItem = namedNodeMap.item(j);
    			attrMap.put(attrItem.getNodeName(), attrItem.getNodeValue());
    		}   
    		projectParams.put(paramName, attrMap);
    	}
    }
    
    /**
     * Get all validation parameter to a hash map
     */
    static private void loadValidationParameters() {
    	validationParams = new HashMap<String, Map<String, ValidationParam>>();
    	
		NodeList pageList = validationDoc.getElementsByTagName("page");
		ValidationParam validParam = null; 
		String pageUrl = null; // 對應 URL
		int paramSize = 0;	   // 參數數量
		
		for (int i = 0; pageList != null && i < pageList.getLength(); i++) {
			Map<String, ValidationParam> params = new HashMap<String, ValidationParam>();

			Element pageNode = (Element) pageList.item(i);
			NodeList directList = pageNode.getElementsByTagName("direct");
			
			pageUrl = pageNode.getAttribute("url");
			
			// 讀取欄位定義之屬性
			for (int j = 0; j < directList.getLength(); j++) {
				Map<String, Map<String, String>> fields = new HashMap<String, Map<String, String>>();
				Map<String, String> field = null;										 // 欄位定義
				Map<String, String> signFields = new LinkedHashMap<String, String>();    // 簽章本文欄位
				Map<String, String> digestFields = new LinkedHashMap<String, String>();  // 雜湊值欄位
				Map<String, String> acntInfoFields = new LinkedHashMap<String, String>();// 帳號資訊雜湊值欄位
				
				Element directNode = (Element) directList.item(j);
				NodeList fieldList = directNode.getElementsByTagName("field");		  // 取得 direct 下所有 field List
				NodeList acntInfoList = directNode.getElementsByTagName("acnt_info"); // 取得 acnt_info 下所有 field List
				
				String directType = directNode.getAttribute("type");
				
				if (!StringUtil.isBlank(directType)) {
					
					for (int k = 0; k < fieldList.getLength(); k++) {
						Element fieldsNode = (Element) fieldList.item(k);
						NamedNodeMap fieldMap = fieldsNode.getAttributes();
						
						field = new HashMap<String, String>();
						
						String elementName = fieldsNode.getAttribute("name");
						
						for (int l = 0; l < fieldMap.getLength(); l++) {

							Node attr = fieldMap.item(l);
							String name = attr.getNodeName();
							String value = attr.getNodeValue();
							
							// 各 direct 下 field 屬性名稱 & 屬性值
							field.put(name, value);
							
							// 被定義為簽章本文欄位
							if(DEF_SIGN.equalsIgnoreCase(name)){
								signFields.put(value, elementName);
							}
							
							// 被定義為雜湊值欄位
							if(DEF_DIGEST.equalsIgnoreCase(name)){
								digestFields.put(value, elementName);
							}
						}
						
						fields.put(elementName, field);
					}
					
					// 取得 acnt_info 雜湊順序
					if (acntInfoList.getLength() != 0) {
						for (int m = 0; m < acntInfoList.getLength(); m++) {
							Element acntInfoNode = (Element) acntInfoList.item(m);
							NodeList acntFieldList = acntInfoNode.getElementsByTagName("detl_field");
							
							for (int n = 0; n < acntFieldList.getLength(); n++) {
								Element acntFieldNode = (Element) acntFieldList.item(n);
								NamedNodeMap acntFieldMap = acntFieldNode.getAttributes();
								
								String elementName = acntFieldNode.getAttribute("name");
								
								for (int o = 0; o < acntFieldMap.getLength(); o++) {
									Node attr = acntFieldMap.item(o);
									String name = attr.getNodeName();
									String value = attr.getNodeValue();
									
									// 被定義為雜湊值欄位
									if(DEF_DIGEST.equalsIgnoreCase(name)){
										acntInfoFields.put(value, elementName);
									}
									
								}
								
							}
						}
					}
				}

				paramSize = directList.getLength();
				validParam = new ValidationParam(paramSize, fields, signFields, digestFields, acntInfoFields);
				params.put(directType, validParam);
			}
			
			validationParams.put(pageUrl, params);
		}
    }
   

    /**
     * Inital system directories
     * @param logConfig
     */
    static private void initialSystemDirectories(String logConfig) {
    	System.out.println(logConfig);

    	FileInputStream fis  = null; // v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
    	
    	// 3.Log4J log
    	try {
    		fis = new FileInputStream(logConfig);
			Properties logCfg = new Properties();
			logCfg.load(fis);			
			String logPath = (String) logCfg.get("log4j.appender.DatedFile.Directory");
			logPath = logPath.replaceAll("'", "");
			logPath = logPath.substring(0, (logPath.lastIndexOf(File.separator)>0)? logPath.lastIndexOf(File.separator) : logPath.lastIndexOf("/"));
			makeDirectory(logPath);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Initial log4j directory error! ");
			System.out.println("Exception : " + e.getMessage());
			
		} finally {
			// v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
			if (fis != null) {
				try { fis.close(); } catch (IOException e) { e.printStackTrace(); }
			}
			
		}
    }
    
    /**
     * Mark directory
     * @param dirPath
     */
    static private void makeDirectory(String dirPath) {
    	// v1.02, 修正 Fortify 白箱掃描(Path Manipulation)
    	// v1.03, 修正 Fortify 白箱掃描(Path Manipulation) 
    	String clearPath = FilePathUtil.cleanString(dirPath);
    	
    	try {
			File file = new File(clearPath);
			if (!file.exists()) {
				file.mkdirs();	
			}			
		} catch (Exception e) {
			System.out.println("Make system directory error! ");
			System.out.println("Exception : " + e.getMessage());
		}
    } 
}
