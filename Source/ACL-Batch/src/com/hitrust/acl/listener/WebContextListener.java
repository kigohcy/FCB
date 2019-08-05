/**
 * @(#) WebContextListener.java
 *
 * Directions: 
 *
 * Copyright (c) 2017 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *	v1.00, 2017/11/22, Eason Hsu
 *	 1) First release
 */

package com.hitrust.acl.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;

public class WebContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LogManager.shutdown();
	}

}
