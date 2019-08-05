/**
 * @(#)LookAheadObjectInputStream.java
 * Copyright(c)2016 HiTRUST Incorporated.All rights reserved.
 * Description : 
 * History:
 * v1.00, 2016/10/26, Ada Chen
 *  1) TSBACL-119, 修正白箱掃瞄問題
 */
package com.hitrust.acl.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

/**
 * @author Ada
 *
 */
public class LookAheadObjectInputStream extends ObjectInputStream {

    /**
     * Logging utiltity
     */
    private static Category LOG = Logger.getLogger(LookAheadObjectInputStream.class);

	
    public LookAheadObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    /**
     * Only deserialize instances of our expected AclCommand class
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    	
    	if(checkResolveClass(desc)){
            throw new InvalidClassException("Unauthorized deserialization attempt",desc.getName());
        }
        return super.resolveClass(desc);
    }
    
    /**
     * 檢查 Class 是否在白名單內
     * @param classDesc
     * @return
     */
    private boolean checkResolveClass(ObjectStreamClass classDesc){
    	
    	boolean rslt = false;
    	String[] classKey = {"com.hitrust", "java"};
    	
    	if(classDesc.getName().indexOf(classKey[0])<0 
    			|| classDesc.getName().indexOf(classKey[1])<0){
    		//
    		LOG.warn("Unauthorized deserialization attempt: " + classDesc.getName());
    	}
    	
    	return rslt;
    }
}
