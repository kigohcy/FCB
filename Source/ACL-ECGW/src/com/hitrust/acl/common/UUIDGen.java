package com.hitrust.acl.common;
import java.util.UUID;


/**
 * @(#)UUIDGen.java 
 *
 * Copyright (c) 2010 HiTRUST Incorporated.All rights reserved.
 *
 * Description : UUID工具類
 *
 * Modify History:
 *  v1.00, 2013-9-6, Woodman Fan
 *   1) First release
 *  
 */

public class UUIDGen {

    /**
     * 功能：產生32位UUID
     * @return
     */
    public synchronized static String genUUID(){
        UUID uuid =  UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
