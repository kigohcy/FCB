/*
 * @(#)JcicCommunicationFactory.java
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2017/10/11, Bing Lien
 *  1) First release
 */
package com.hitrust.bank.telegram.communication;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.hitrust.bank.telegram.exception.CommunicationException;
import com.hitrust.communication.config.CommunicationConfig;

/**
 * @author bing
 */
public class BaseCommunicationFactory {
	
	/**
	 * Get communication object by name of commuication
	 * @param communication_name
	 * @return
	 * @throws CommunicationException
	 */
	public static Object getInstance(String communication_name) throws CommunicationException {
		// 1.Get class of the specified communication
		//String communicationClass = APSystem.getCommunicationParameterValue(communication_name, "class");
		String communicationClass = CommunicationConfig.getCommunicationParameterValue(communication_name, "class");
		// 2.Check communication configuration
		if (communicationClass == null || "".equals(communicationClass)) {
			// Exception : communication configuration not exist
			throw new CommunicationException("CMU01");
		} 
		
		@SuppressWarnings("rawtypes")
		Class cClass = null;
		
		try {
			cClass = Class.forName(communicationClass);
		} catch (ClassNotFoundException e) {
			// Exception : communication class not exist
			throw new CommunicationException(e.getMessage(), "CMU02");
		}
		
		// 3.Get all parameters of communication 
		//HashMap params = APSystem.getCommunicationParameters(communication_name);
		@SuppressWarnings("rawtypes")
		HashMap params = CommunicationConfig.getCommunicationParameters(communication_name);
		// 4.Instantiate communication
		Object communication = null;
		try {
			// get the constructor method
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Constructor cConstructor = cClass.getConstructor(new Class[]{HashMap.class});
			// new an instance
			communication =  cConstructor.newInstance(new Object[]{params});
		} catch (SecurityException e) {
			// Exception : Can not instantiate communication
			throw new CommunicationException(e.getMessage(), "CMU02");
		} catch (NoSuchMethodException e) {
			// Exception : Can not instantiate communication
			throw new CommunicationException(e.getMessage(), "CMU02");			
		} catch (IllegalArgumentException e) {
			// Exception : Can not instantiate communication
			throw new CommunicationException(e.getMessage(), "CMU02");
		} catch (InvocationTargetException e) {
			// Exception : Can not instantiate communication
			if (e.getTargetException() != null) {
				if (e.getTargetException() instanceof CommunicationException) {
					throw (CommunicationException) e.getTargetException();	
				} else {
					throw new CommunicationException(e.getTargetException().getMessage(), "CMU02");
				}				
			} else {
				throw new CommunicationException("CMU02");	
			}
		} catch (InstantiationException e) {
			// Exception : Can not instantiate communication
			throw new CommunicationException(e.getMessage(), "CMU02");
		} catch (IllegalAccessException e) {
			// Exception : Can not instantiate communication
			throw new CommunicationException(e.getMessage(), "CMU02");
		}
		return communication;
	}
}
