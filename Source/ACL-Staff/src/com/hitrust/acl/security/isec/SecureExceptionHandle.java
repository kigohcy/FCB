/*
 * @(#)SecureExceptionHandle
 *
 * Copyright (c) 2013 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2013/10/28, Ada Chen
 *   1) First release
*/
 
package com.hitrust.acl.security.isec;

public class SecureExceptionHandle {
	
	private String errorCode;		// 錯誤代碼
	private String errorMessage;	// 錯誤訊息
           
    /**
     * Constructor
     */
    public SecureExceptionHandle(){
    	//
    }
    
    /**
     * Setter method for errorCode
     * @return
     */
    public void setErrorCode(String errorCode) {
    	this.errorCode = errorCode;
    }
    
    /**
     * Getter method for errorCode
     * @return
     */
    public String getErrorCode() {
        return this.errorCode;
    }
    
    /**
     * Setter method for errorMessage
     * @return
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Getter method for errorMessage
     * @return
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
   /**
     * 取得i-Security Exception 對應回自定之 errorCode
     * @param exception - i-Security Exception
     * @return errorCode - 訊息代碼
     */
    //TODO 比對 iSecurity API文件
    synchronized public static int mappingErrCode(String exception){
	   int errCode=0;
	   if (exception.equals("ISecurityException")) {
			errCode = 1002;
		} else if (exception.equals("ISecurityParameterException")) {
			errCode = 1003;
		} else if (exception.equals("ISecurityMemoryException")) {
			errCode = 1004;
		} else if (exception.equals("ISecurityOperationException")) {
			errCode = 1005;
		} else if (exception.equals("ISecurityCertNotFoundException")) {
			errCode = 1006;
		} else if (exception.equals("ISecurityFormatException")) {
			errCode = 1007;
		} else if (exception.equals("ISecurityIssuerNotFoundException")) {
			errCode = 1008;
		} else if (exception.equals("ISecurityIssuerVerifyException")) {
			errCode = 1009;
		} else if (exception.equals("ISecurityExpiredException")) {
			errCode = 1010;
		} else if (exception.equals("ISecurityNotRegisterException")) {
			errCode = 1011;
		} else if (exception.equals("ISecuritySuspendException")) {
			errCode = 1012;
		} else if (exception.equals("ISecurityRevokedException")) {
			errCode = 1013;
		} else if (exception.equals("ISecurityNoPublicKeyException")) {
			errCode = 1014;
		} else if (exception.equals("ISecurityNoPrivateKeyException")) {
			errCode = 1015;
		} else if (exception.equals("ISecurityKeyPairNotMatchException")) {
			errCode = 1016;
		} else if (exception.equals("ISecurityUploadFormatException")) {
			errCode = 1017;
		} else if (exception.equals("ISecurityDownloadFormatException")) {
			errCode = 1018;
		} else if (exception.equals("ISecurityInputFormatException")) {
			errCode = 1019;
		} else if (exception.equals("ISecurityTemplateFormatException")) {
			errCode = 1020;
		} else if (exception.equals("IsecurityDataNotFoundException")) {
			errCode = 1021;
		} else if (exception.equals("ISecurityFileNotFoundException")) {
			errCode = 1022;
		} else if (exception.equals("ISecurityPasswordException")) {
			errCode = 1023;
		} else if (exception.equals("ISecurityURLException")) {
			errCode = 1024;
		} else if (exception.equals("ISecurityCryptoException")) {
			errCode = 1025;
		} else if (exception.equals("ISecurityKeyTypeException")) {
			errCode = 1026;
		} else if (exception.equals("ISecurityAlgorithmException")) {
			errCode = 1027;
		} else if (exception.equals("ISecurityKeyLengthException")) {
			errCode = 1028;
		} else if (exception.equals("ISecurityKeyNotFoundException")) {
			errCode = 1029;
		} else if (exception.equals("ISecurityOthersException")) {
			errCode = 1030;
		} else if (exception.equals("ISecurityKeyExistException")) {
			errCode = 1031;
		} else if (exception.equals("ISecurityReferenceValidException")) {
			errCode = 1032;
		} else if (exception.equals("ISecuritySignatureValidException")) {
			errCode = 1033;
		} else if (exception.equals("ISecurityVerifyException")) {
			errCode = 1034;
		} else if (exception.equals("ISecurityImplementException")) {
			errCode = 1035;
		} else if (exception.equals("ISecurityNoPrivilegeException")) {
			errCode = 1036;
		} else if (exception.equals("ISecuritySocketException")) {
			errCode = 1037;
		} else {
			errCode = 1099;
		}
	   
	   return errCode;
   }
}
