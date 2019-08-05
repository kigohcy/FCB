/*
 * @(#)ServiceModel.java
 * 
 * Copyright (c) 2006 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * 	v1.00, 2006/04/13, Tim Cao
 * 	 1) First release
 */
package com.hitrust.acl.service;

import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.response.AbstractResponseBean;

/**
 * The interface of all business model.
 */
public interface ServiceModel {
	
	public AbstractResponseBean process() throws ApplicationException, DBException;
}
