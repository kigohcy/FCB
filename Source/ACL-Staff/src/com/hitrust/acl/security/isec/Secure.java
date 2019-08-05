/*
 * @(#)Secure
 *
 * Copyright (c) 2013 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2013/10/28, Ada Chen
 *   1) First release
 *  v1.01, 2016/08/18, Ada Chen
 *   1) 更換 A/C Link 專用之驗證API
 */
 
package com.hitrust.acl.security.isec;

import org.apache.log4j.Logger;

import com.hitrust.isecurity2_0.ISecurityException;
import com.hitrust.isecurity2_0.SignerInfoEx;
import com.hitrust.isecurity2_0.client.CertInfo;
import com.hitrust.isecurity2_0.client.PKCS7SignatureProc;
import com.hitrust.isecurity2_0.util.Base64;

public class Secure {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(Secure.class);

	// 驗章參數
	// 1 - verify() 驗證簽章、憑證以及檢查CRL
	// 0 - verifyNoCertVerify() 只驗證簽章值是否正確
	public static final int VERIFY_CERT = 1;
	public static final int NOVERIFY_CERT = 0;
	
	private int rtnCode;			// 驗章結果
	private String errorCode;		// 錯誤代碼
	private String errorMessage;	// 錯誤訊息
	private String issuer;			// 憑證發行者
	private String serial;			// 憑證序號
	private String subject;			// 憑證主旨
	private String cert;			// 憑證內容
           
    /**
     * Constructor
     */
    public Secure(){
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
     * Setter method for issuer
     * @return
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    
    /**
     * Getter method for issuer
     * @return
     */
    public String getIssuer() {
        return this.issuer;
    }
    
    /**
     * Setter method for serial
     * @return
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }
    
    /**
     * Getter method for serial
     * @return
     */
    public String getSerial() {
        return this.serial;
    }
    
    /**
     * Setter method for subject
     * @return
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    /**
     * Getter method for subject
     * @return
     */
    public String getSubject() {
        return this.subject;
    }
    
    /**
     * Setter method for cert
     * @return
     */
    public void setCert(String cert) {
        this.cert = cert;
    }
    
    /**
     * Getter method for cert
     * @return
     */
    public String getCert() {
        return this.cert;
    }
    
    /**
     * 
     * @return
     */
    private void clearError() {
    	this.rtnCode = 0;
    	this.errorCode = null;
    	this.errorMessage = null;
    	this.issuer = null;
    	this.serial = null;
    	this.subject = null;
    	this.cert = null;
    }
    
    /**
     * 進行驗章
     * @param verifyRequest - 前端簽章相關資訊
     * @return rtnCode - 驗章結果
     */
    public int pkcs7Verify(VerifyRequest verifyRequest) {
    	
		// clear last execute result.
		clearError();
		
		try {
			boolean bRtn = verifyRequest.getReturn();
			String sCommonName = verifyRequest.getCommonName();
			String sPlainText = verifyRequest.getPlainText();
			int verifyFlag = 0;
			int plainFlag = 0;
			
			// Check Parameter
			// Verify Cert Flag
			//verifyFlag = verifyRequest.getVerifyFlag();
			// v1.01, 固定需驗 Cert Chain
			verifyFlag = Secure.VERIFY_CERT; 
			if(verifyFlag!=Secure.VERIFY_CERT && verifyFlag!=Secure.NOVERIFY_CERT){
				this.errorMessage = "ISecurityParameterException: Invalid Flags!";
				rtnCode = SecureExceptionHandle.mappingErrCode("ISecurityParameterException"); 
				return rtnCode;
				
			}
			
			// New PKCS7 signature object for verify
			PKCS7SignatureProc pkcs7Proc = new PKCS7SignatureProc();
			
			// v1.01, 更換為 Account Link 專用 API 
			// Parsing signed envelop and Verify signature
			try {
				if(verifyRequest.getVerifyFlag()==Secure.VERIFY_CERT){ //verify signature & certificate
					this.rtnCode = pkcs7Proc.verifyEx(verifyRequest.getSignature(), "", "accountlink");
				}
				
				if(this.rtnCode != 0){
					//Verify Fail
					setErrorCode(Integer.toString(this.rtnCode));
				} else {
					// Verify pass
					setErrorCode("0000");
					this.rtnCode = 0;
				}
			}catch(ISecurityException ex){
				this.errorMessage = ex.toString();	     
				String exception = String.valueOf(ex.getClass());
				this.rtnCode = SecureExceptionHandle.mappingErrCode(exception.substring(exception.lastIndexOf(".")+1,exception.length())); //v1.03
				return rtnCode;
				
			}
			
			// Get plain text from signed document
			String plainText = pkcs7Proc.getDataContentString();

			LOG.info("PlainText["+plainText+"]");
			
			// Verify plain text
			if(sPlainText!=null && !"".equals(sPlainText)) {
				if(!sPlainText.equals(plainText)) {
					this.errorMessage = "SecureException: plain text not matched!";
					this.rtnCode = 2001;
					return rtnCode;
				}
			}
			
			// Get signers
			SignerInfoEx[] signers = pkcs7Proc.getSignersEx();
			
			// Verify each document signature
			for(int i=0; i < signers.length; i++){
				SignerInfoEx signer = signers[i];
				
				LOG.info("SignerInfo:****");
				LOG.info("1. DigestMethod:[" + signer.getDigestMethod() +"]");
				LOG.info("2. SignatureMethod:[" + signer.getSignatureMethod() +"]");
				
				String signerCert = signer.getSignerCert();
				if(signerCert != null){
					CertInfo cert = new CertInfo(Base64.decode(signerCert));
					//Debug
					LOG.info("CertInfo:*****************************");
					LOG.info("1. Issuer:[" + cert.getIssuerName().getX500NameString() +"]");
					LOG.info("2. SerialNo:[" + cert.getSerialNumber() +"]");
					LOG.info("3. Subject:[" + cert.getSubjectName().getX500NameString()+"]");
					LOG.info("4. Common Name:[" + cert.getSubjectName().commonName + "]");
						
					//Return information (include: Issuer, Serial, Subject, Certificate)
					if(bRtn){  
						setCert(signerCert);
						setIssuer(cert.getIssuerName().getX500NameString());
						setSerial(cert.getSerialNumber());				   
						setSubject(cert.getSubjectName().getX500NameString());
						
					}
					
					// Verify Common Name
					if(sCommonName!=null && !"".equals(sCommonName)) {
						if( !sCommonName.equals(cert.getSubjectName().commonName) ) {
							this.errorMessage = "SecureException: Common Name not matched!";
							this.rtnCode = 2002; 
							return rtnCode;
						}
					}
					
				}
				
			} // end of for 
			
		}catch(ISecurityException ex){
			this.errorMessage = ex.toString();	     
			String exception = String.valueOf(ex.getClass());
			this.rtnCode = SecureExceptionHandle.mappingErrCode(exception.substring(exception.lastIndexOf(".")+1,exception.length())); //v1.03
			
		}catch(Exception ex) {			
			this.errorMessage = ex.toString();
			setErrorCode("1001");
			this.rtnCode = 1001;
			
		}
					
		return this.rtnCode;
    }
}


