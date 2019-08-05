/**
 * @(#) UtilException.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/25, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.exception;

import com.hitrust.framework.exception.BaseException;

/**
 * 業務異常定義類。
 * @author jmiuhan
 * @see com.hitrust.framework.exception.BaseException
 *
 */
public class UtilException extends BaseException {
	
	private static final long serialVersionUID = 1L;

	/**
     * Constructor
     * @param String errorCode 錯誤代碼(設定檔中定義其message)
     */
    public UtilException(String errorCode) {
    	super(null, errorCode, null, null);
    }
	
    /**
     * Constructor
     * @param String errorCode 錯誤代碼(設定檔中定義其message)
     * @param Object[] parameters 參數列表(錯誤代碼對應之message需要的參數列表)
     */
    public UtilException(String errorCode, Object[] parameters) {
    	super(null, errorCode, parameters, null);
    }
	
    /**
     * Constructor
     * @param String errorCode 錯誤代碼(設定檔中定義其message)
     * @param Exception e 錯誤發生原因(原始Exception)
     */
    public UtilException(String errorCode, Exception e) {
    	super(null, errorCode, null, e);
    }
	
    /**
     * Constructor
     * @param String errorCode 錯誤代碼(設定檔中定義其message)
     * @param Object[] parameters 參數列表(錯誤代碼對應之message需要的參數列表)
     * @param Exception e 錯誤發生原因(原始Exception)
     */
    public UtilException(String errorCode, Object[] parameters, Exception e) {
    	super(null, errorCode, parameters, e);
    }
    
    /**
     * Constructor
     * @param String errorMsg 錯誤信息(For debug)
     * @param String errorCode 錯誤代碼(設定檔中定義其message)
     */
    public UtilException(String errorMsg, String errorCode) {
    	super(errorMsg, errorCode, null, null);
    } 
	
    /**
     * Constructor
     * @param String errorMsg 錯誤信息(For debug)
     * @param String errorCode 錯誤代碼(設定檔中定義其message)
     * @param Object[] parameters 參數列表(錯誤代碼對應之message需要的參數列表)
     */
    public UtilException(String errorMsg, String errorCode, Object[] parameters) {
    	super(errorMsg, errorCode, parameters, null);
    }
	
    /**
     * Constructor
     * @param String errorMsg 錯誤信息(For debug)
     * @param String errorCode 錯誤代碼(設定檔中定義其message)
     * @param Object[] parameters 參數列表(錯誤代碼對應之message需要的參數列表)
     * @param Exception e 錯誤發生原因(原始Exception)
     */
    public UtilException(String errorMsg, String errorCode, Object[] parameters, Exception e) {
    	super(errorMsg, errorCode, parameters, e);
    }
}
