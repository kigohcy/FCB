/*
 * @(#)CommuFactory.java
 * 
 * Copyright (c) 2006 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2006/03/07, Jerry Hu
 *  1) First release
 * v1.01, 2006/03/22, Tim Cao
 *  2) Add communication configuration
 */
package com.hitrust.bank.telegram.communication;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.hitrust.bank.telegram.exception.CommunicationException;
import com.hitrust.communication.config.CommunicationConfig;

public class FBCommunicationFactory {
	
	/**
	 * Get communication object by name of commuication
	 * @param communication_name
	 * @return
	 * @throws CommunicationException
	 */
	public static FBGenericCommunication getInstance(String communication_name) throws CommunicationException {
		// 1.Get class of the specified communication
		//String communicationClass = APSystem.getCommunicationParameterValue(communication_name, "class");
		String communicationClass = CommunicationConfig.getCommunicationParameterValue(communication_name, "class");
		// 2.Check communication configuration
		if (communicationClass == null || "".equals(communicationClass)) {
			// Exception : communication configuration not exist
			throw new CommunicationException("CMU01");
		} 
		Class cClass = null;
		try {
			cClass = Class.forName(communicationClass);
		} catch (ClassNotFoundException e) {
			// Exception : communication class not exist
			throw new CommunicationException(e.getMessage(), "CMU02");
		}
		// 3.Get all parameters of communication 
		//HashMap params = APSystem.getCommunicationParameters(communication_name);
		HashMap params = CommunicationConfig.getCommunicationParameters(communication_name);
		// 4.Instantiate communication
		FBGenericCommunication communication = null;
		try {
			// get the constructor method
			Constructor cConstructor = cClass.getConstructor(new Class[]{HashMap.class});
			// new an instance
			communication = (FBGenericCommunication) cConstructor.newInstance(new Object[]{params});
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
