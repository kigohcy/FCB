/*
 * @(#)SpringHelper.java
 *
 * Copyright (c) 2008 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 * v1.00, 2008年4月16日, Kevin Wang
 *   1) First release
 *
 */

package com.hitrust.acl.util;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Spring 輔助物件，用於協助Job程式存取Spring Container相關資源
 *
 * @author AJax Lin
 */
public class SpringHelper {
    
    private static ApplicationContext ac;
    
    /** Creates a new instance of SpringHelper */
    public SpringHelper() {
    }
    
    /**
     * 取得Spring ApplicationContext
     * @return ApplicationContext
     */
    public static synchronized ApplicationContext getApplicationContext() {
        if (ac==null) {
        	ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext-hibernate.xml"
		        												, "applicationContext-resource.xml"
		        												, "applicationContext-dao.xml"
		        												, "applicationContext-service.xml"});
        }
        return ac;
    }
    
    //開SessionFactory
    private static void openSession() {
        SessionFactory sessionFactory = (SessionFactory) ac.getBean("sessionFactory");
        Session hibSession = SessionFactoryUtils.getSession(sessionFactory, true);
        hibSession.setFlushMode(FlushMode.NEVER);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(hibSession));
    }
    
    //關閉SessionFactory
    public static void closeSession() {
        SessionFactory sessionFactory = (SessionFactory) ac.getBean("sessionFactory");
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
        SessionFactoryUtils.closeSessionIfNecessary(sessionHolder.getSession(), sessionFactory);
    }
    
}
