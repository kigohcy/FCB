/**
 * @(#)ValidationEnv.java
 *
 * Copyright (c) 2014 HiTRUST Incorporated. All rights reserved.
 * 
 * Description : 系統參數初始設定
 * 
 * Modify History:
 *  v1.00, 2014/05/02, Yann
 *   1) First release
 */
package com.hitrust.acl.common;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hitrust.framework.APSystem;
import com.hitrust.framework.util.DOM;

public class ValidationEnv {
    //log4J
    private static Logger LOG = Logger.getLogger(ValidationEnv.class);
    
    // Private properities for service
    private static String sysDir = null;
    private static String sysErr = null;
    private static Document validationDoc; // 存儲validation.xml
    //System validation config
    private static HashMap sysService;
    //System validation config
    private static HashMap fieldNameService;
  
    //系統上下文context
    //private static WebApplicationContext applicationContext;
    
    /**
     * 功能：初始化配置文件。設定系統目錄；加載 validation.xml。
     * 
     */
    static{
    	try {
    		
    		if(APSystem.getApplicationContext()==null)
    			throw new IllegalStateException(
    					"Cannot initialize APSystem's WebApplicationContext - " +
    					"check whether you have ContextLoader* definitions in your web.xml!");
    		LOG.info("ValidationEnv initial start..");
            //取得系統根目錄
    		sysDir=APSystem.getApplicationContext().getServletContext().getRealPath("/");
    		
            LOG.debug("The System Path is :"+sysDir);
			
            //設置配置文件根目錄
            String cfgDir = sysDir+"/WEB-INF/config/";

    		////////////////////String cfgDir = "d:/";
            //加載 validation.xml
            validationDoc = DOM.loadDoc(cfgDir + "validation.xml");
            loadSysService();
            LOG.info("ValidationEnv initial finish..");
            
        }catch(Exception ex) {
            //store the initial error message for display.
            sysErr = ex.toString();
            LOG.error("ValidationEnv initial exception:"+sysErr, ex);
        }
    }
   
    /**
     * 功能:取得系統根目錄
     * @return String 系統根目錄
     */
    public static String getSysDir() {
        return sysDir;
    }

    /**
     * 功能:取得系統初始錯誤信息
     * @return String 初始化錯誤信息, null means OK.
     */
    public static String getSysErr() {
        return sysErr;
    }

    /**
     * 取得validation.xml定義
     * @return HashMap
     */
    public static HashMap getServiceConfig() {
    	return sysService;    	
    }
    
    /*
	 * 功能：加載validation.xml
	 */
   private static void loadSysService() {
	   sysService = new HashMap();
	   fieldNameService = new HashMap();
	   // Load service list
	   NodeList serviceList = validationDoc.getElementsByTagName("page");
	   for (int i=0; serviceList!=null && i<serviceList.getLength(); i++) {
		   Element serviceNode = (Element)serviceList.item(i);
		   //獲取page url
		   String pageUrl = serviceNode.getAttribute("url");
		   //獲取page 對應 屬性
		   HashMap serviceAttrMap = getNodeAttributes(serviceNode);
		   sysService.put(pageUrl, serviceAttrMap);
		   
		   //判斷page 是否配置field模式
		   HashMap stepMap = new HashMap();
		   NodeList stepList = serviceNode.getElementsByTagName("field");
		   for (int j=0; stepList!=null && j<stepList.getLength(); j++) {
			   Element fieldNode = (Element) stepList.item(j);
			   String fieldName = fieldNode.getAttribute("name");
			   
			   //獲取name屬性
			   if(fieldName!=null){
				   fieldNameService.put(pageUrl+"/"+fieldName, getNodeAttributes(fieldNode));
				   fieldNameService.put(pageUrl, new HashMap());
				   //System.out.println("name/stepurl:"+name+"/"+stepUrl);
			   }
			   stepMap.put(fieldName, getNodeAttributes(fieldNode));
		   }
		   if (stepList!=null && stepList.getLength()>0) {
			   serviceAttrMap.put("field", stepMap);
		   }
	   }
   }
   
   /**
    * 功能：取得server.xml中指定url 設定屬性值
    * @param String url
    * @return HashMap
    */
    public static HashMap getUrlAttributes(String url) {
	    if(fieldNameService.containsKey(url)){
		    return (HashMap)fieldNameService.get(url);
	    }else{
		    return null;
	    }
    }
    
    //取得xml結點屬性
    private static HashMap getNodeAttributes(Node node) {
	    HashMap attrMap = new HashMap();
	    
	    NamedNodeMap attrList = node.getAttributes();
	    for (int i=0; attrList!=null && i<attrList.getLength(); i++) {
		    Node attr = attrList.item(i);
		    attrMap.put(attr.getNodeName(), attr.getNodeValue());
	    }
	    
	    return attrMap;
    }
    
}
