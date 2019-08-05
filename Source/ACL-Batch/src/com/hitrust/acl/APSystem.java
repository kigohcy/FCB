/**
 * @(#) APSystem.java
 *
 * Directions: BtchLogDAO
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/03/31
 *    1) First release
 *
 */
package com.hitrust.acl;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class APSystem {
	// log4J category
	static Logger LOG = Logger.getLogger(APSystem.class);
	
	private static String sysDir = null;
	 // Initial values for application's properities
    
    static {
    	try {
        	try {
        		String classpath = Thread.currentThread().getContextClassLoader().getResource("log4j.xml").getPath();
            	
            	sysDir = classpath.substring(1, classpath.indexOf("WEB-INF/classes/"));
            	LOG.debug("sysDir = " + sysDir);
    		} catch (Throwable t) {
    			sysDir =  "/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/ACLKAP1PCell01/ACL-ECGW_war.ear/ACL-ECGW.war/";
    			LOG.debug("error sysDir = " + sysDir);
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
            // store the initial error message for display.
            LOG.debug("APSystem initial exception:" + ex);
    	}
    }
	public static String getSysDir() {
		return sysDir;
	}
	public static void setSysDir(String sysDir) {
		APSystem.sysDir = sysDir;
	}
    
}
