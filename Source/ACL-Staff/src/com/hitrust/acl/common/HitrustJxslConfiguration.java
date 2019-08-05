/**
 * @(#)JxslConfiguration.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *  v1.00, 2007-6-14, Martin Han
 *   1) First release
 */

package com.hitrust.acl.common;

import net.sf.jxls.transformer.Configuration;

/**
 * HitrustJxslConfiguration
 * @author Martin Han
 * 
 */
public class HitrustJxslConfiguration extends Configuration{
	 public String getTagPrefix() {
	       return "jxh:";
	    }
}