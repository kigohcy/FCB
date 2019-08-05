/**
 * @(#)HitrustJxTaglib.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *  v1.00, 2007-6-14, Martin Han
 *   1) First release
 */

package com.hitrust.acl.common;

import java.util.HashMap;
import java.util.Map;

import net.sf.jxls.tag.TagLib;

/**
 * 自定義標簽
 * @author user
 *
 */
public class HitrustJxTaglib implements TagLib {
	 static String[] tagName = new String[]{"if", "outline", "out","forEach"};
	 static String[] tagClassName = new String[]{
		 	"net.sf.jxls.tag.IfTag",
		 	"net.sf.jxls.tag.OutlineTag",
		 	"net.sf.jxls.tag.OutTag",
		 	"net.sf.jxls.tag.ForEachTag"
	 };

	 static Map tags = new HashMap();

	 static{
	     for (int i = 0; i < tagName.length; i++) {
	         String key = tagName[i];
	         String value = tagClassName[i];
	         tags.put( key, value );
	     }
	 }

	 public Map getTags(){
	     return tags;
	 }
}
