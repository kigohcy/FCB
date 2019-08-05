package com.hitrust.acl.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AclApplicationContextAwarer implements ApplicationContextAware {
	private static ApplicationContext applicationContext; 
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		AclApplicationContextAwarer.applicationContext = applicationContext;

	}
	
	public static ApplicationContext getApplicationContext(){
		return AclApplicationContextAwarer.applicationContext;
	}

}
