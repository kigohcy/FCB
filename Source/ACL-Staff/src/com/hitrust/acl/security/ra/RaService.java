/**
 * @(#)RaService.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/03/18, Ada Chen
 *   1) First release
 */

package com.hitrust.acl.security.ra;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitrust.bank.controller.EcCertController;
import com.hitrust.ra.taica2.HTRAFacade;

public class RaService {
	// Log4
	private static Logger LOG = Logger.getLogger(EcCertController.class);
	
    public static final String X509CertFormat  = "X509";
    public static final String PKCS7CertFormat = "PCKS7";
    
    public static final boolean VERIFY_USER = false;
    
    
    private int applyId;
    
    private String caApplyId;
    
    private String applyStatus;
    
    private String errorMessage;
    
    private String cert;
    
    private RaConfig raConfig;
    
    private List<RaCertInfo> raCertInfos;
    
    private RaCertInfo raCertInfo;
    
    private String key1;
    
    
    /**
     * Constructor
     * @throws RAException
     */
    public RaService(RaConfig raConfig){
            this.raConfig = raConfig;
    }
    
    /**
     * Getter method for errorMessage
     * @return
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    /**
     * Getter method for applyId
     * @return
     */
    public int getApplyId() {
        return this.applyId;
    }
    
    /**
     * Getter method for applyStatus
     * @return
     */
    public String getApplyStatus() {
        return this.applyStatus;
    }
    
    /**
     * Getter method for caApplyId
     * @return
     */
    public String getCaApplyId() {
        return this.caApplyId;
    }
    
    /**
     * Getter method for cert
     * @return
     */
    public String getCert() {
        return this.cert;
    }
    
    /**
     * Getter method for key1
     * @return
     */
    public String getKey1() {
        return this.key1;
    }
    
    /**
     * Getter method for raCertInfos
     * @return
     */
    public List<RaCertInfo> getRaCertInfos() {
        return this.raCertInfos;
    }
    
    /**
     * Getter method for raCertInfo
     * @return
     */
    public RaCertInfo getRaCertInfo() {
        return this.raCertInfo;
    }
    
    
    /**
     * 代客憑證申請
     * @param user UserAccount 物件
     * @param certApplyRequest CertApplyRequest 物件
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     * 使用銀行提供的RA帳戶登入RA Server 後, 進行代客憑證申請並且建立使用者帳號
     */
    public int applyCertByAdm(UserAccount user, CertApplyRequest certApplyRequest, String ecId) {
        HTRAFacade htraFacade = new HTRAFacade(this.raConfig.getRaUrl());
        int irtn = -1;
        
        // 1.依銀行提供的RA帳戶呼叫RA-API進行身分驗證
//        int rtn_code = this.loingWithBankRaUser(htraFacade);
//        if (rtn_code != 0) {
//            return rtn_code;
//        }

        // 密碼須先經過SHA-1 Hash
        RaUtil raUtil = new RaUtil();
        String raEncPswd = this.raConfig.getRaPhrase(); //raUtil.toHexString(raUtil.hashBySunJCE(RaUtil.HASH_SHA1, this.raConfig.getRaPhrase()));
        String encodePswd = user.getPhrase(); //raUtil.toHexString(raUtil.hashBySunJCE(RaUtil.HASH_SHA1, user.getPhrase()));
        
        // 判斷 raPhrase and user's phrase 是否需加密
        if (this.raConfig.isEncrypt()) {
        	raEncPswd = raUtil.toHexString(raUtil.hashBySunJCE(RaUtil.HASH_SHA1, raEncPswd));
            encodePswd = raUtil.toHexString(raUtil.hashBySunJCE(RaUtil.HASH_SHA1, encodePswd));
		}
        
        String sAdmAccount = this.raConfig.getRaAccount();
		String sAdmPass = raEncPswd;
		irtn= htraFacade.HTRA_Login(sAdmAccount, sAdmPass, "CertMgrLogin");
		if (irtn != 0) {
			LOG.error("Login error");
			this.errorMessage = htraFacade.HTRA_GetErrorMsg();
			return irtn;
		}

		irtn = htraFacade.HTRA_CreateUser(htraFacade.HTRA_GetKey1(), certApplyRequest.getCn(), certApplyRequest.getCn(), "0F37B93B7A6BCC71004969FF58B3A9537C9485D0", "", user.getIp());
		if (irtn != 0) {
			LOG.error("CreateUser error");
			this.errorMessage = htraFacade.HTRA_GetErrorMsg();
			return irtn;
		}
		
		irtn =htraFacade.HTRA_Connect(certApplyRequest.getCn(), user.getIp());
		if (irtn != 0) {
			LOG.error("Connect error");
			this.errorMessage = htraFacade.HTRA_GetErrorMsg();
			return irtn;
		}
		
        // 2.呼叫RA-API進行憑證申請
        int rtn_code = htraFacade.HTRA2_ApplyCert(htraFacade.HTRA_GetKey1(), certApplyRequest.getCertType(), certApplyRequest.getEmail(), certApplyRequest.getCsr(), "3", user.getIp());
        if (rtn_code != 0) {
        		LOG.error("ApplyCert error");
            this.errorMessage = htraFacade.HTRA_GetErrorMsg();
            return rtn_code;
        }
        // 3.取得憑證申請資訊
        this.applyId     = htraFacade.HTRA_GetApplyID();
        this.applyStatus = htraFacade.HTRA_GetApplyStatus();
        this.caApplyId   = htraFacade.HTRA_GetCAApplyID();
        
        return 0;
    }
    
    
    /**
     * 依憑證編號取得憑證
     * @param certId 憑證編號
     * @param certFormat 憑證格式
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     */
    public int fetchCertById(int certId, String certFormat) {
        HTRAFacade htraFacade = new HTRAFacade(this.raConfig.getRaUrl());
        // 1.呼叫RA-API擷取憑證
        int rtn_code = htraFacade.HTRA_FetchCert(certId, certFormat, this.raConfig.getIpAddress());
        if (rtn_code != 0) {
            this.errorMessage = htraFacade.HTRA_GetErrorMsg();
            return rtn_code;
        }
        this.cert = htraFacade.HTRA_GetCert();
        
        return 0;
    }
    
    /**
     * 依憑證序號取得憑證
     * @param certSerial 憑證序號
     * @param certFormat 憑證格式
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     */
    public int fetchCertBySerial(String certSerial, String certFormat) {
        HTRAFacade htraFacade = new HTRAFacade(this.raConfig.getRaUrl());
        // 1.呼叫RA-API擷取憑證
        int rtn_code = htraFacade.HTRA_FetchCertBySerial(certSerial, certFormat, this.raConfig.getIpAddress());
        if (rtn_code != 0) {
            this.errorMessage = htraFacade.HTRA_GetErrorMsg();
            return rtn_code;
        }
        this.cert = htraFacade.HTRA_GetCert();
        
        return 0;
    }
    
    /**
     * 依 Common Name 取得使用者的全部憑證資訊
     * @param user UserAccount 物件
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     */
    public int queryCertByCn(UserAccount user) {
        HTRAFacade htraFacade = new HTRAFacade(this.raConfig.getRaUrl());
        // 1.依使用者的憑證識別碼(key1)呼叫RA-API查詢憑證
        int rtn_code = htraFacade.HTRA_QueryCertByCN(user.getKey1(), user.getIp());
        if (rtn_code != 0 && rtn_code != 6111) {
            this.errorMessage = htraFacade.HTRA_GetErrorMsg();
            return rtn_code;
        }
        
        // 3.Get all cert
        int certCount  = htraFacade.HTRA_GetCertCount();
        int fieldCount = htraFacade.HTRA_GetFieldCount();
        
        this.raCertInfos = new ArrayList<RaCertInfo>();
        for (int i=0; i<certCount; i++) {
            String[] fields = new String[fieldCount];
            for (int j=0; j<fieldCount; j++) {
                fields[j] = htraFacade.HTRA_GetCertInfo(i, j);
            }
            RaCertInfo certInfo = new RaCertInfo(Integer.parseInt(fields[0]));
            certInfo.setApplyTime(RaUtil.formatDateOfCert(fields[1]));
            certInfo.setApproveTime(RaUtil.formatDateOfCert(fields[2]));
            certInfo.setCertStatus(fields[3]);
            certInfo.setCaApplyId(fields[4]);
            certInfo.setCertSerial(fields[5]);
            certInfo.setCertIssuser(fields[6]);
            certInfo.setCertType(fields[7]);
            certInfo.setCn(fields[8]);
            certInfo.setEmail(fields[9]);
            certInfo.setCertNotBefore(RaUtil.formatDateOfCert(fields[10]));
            certInfo.setCertNotAfter(RaUtil.formatDateOfCert(fields[11]));
            
            this.raCertInfos.add(certInfo);
        }
        
        return 0;
    }
    
    /**
     * 取得指定憑證編號之憑證資訊
     * @param user UserAccount 物件
     * @param certId 憑證編號
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     */
    public int queryCertById(UserAccount user, int certId) {
        HTRAFacade htraFacade = new HTRAFacade(this.raConfig.getRaUrl());
        // 1.依使用者的憑證識別碼(key1)呼叫RA-API查詢憑證並依certId過濾查詢出的憑證資料
        int rtn_code = htraFacade.HTRA_QueryCertByCN(user.getKey1(), user.getIp());
        if (rtn_code != 0) {
            this.errorMessage = htraFacade.HTRA_GetErrorMsg();
            return rtn_code;
        }
        // 3.Get cert by certId
        int certCount  = htraFacade.HTRA_GetCertCount();
        int fieldCount = htraFacade.HTRA_GetFieldCount();
        for (int i=0; i<certCount; i++) {
            String[] fields = new String[fieldCount];
            for (int j=0; j<fieldCount; j++) {
                fields[j] = htraFacade.HTRA_GetCertInfo(i, j);
            }
            if (certId == Integer.parseInt(fields[0])) {
                this.raCertInfo = new RaCertInfo(Integer.parseInt(fields[0]));
                this.raCertInfo.setApplyTime(RaUtil.formatDateOfCert(fields[1]));
                this.raCertInfo.setApproveTime(RaUtil.formatDateOfCert(fields[2]));
                this.raCertInfo.setCertStatus(fields[3]);
                this.raCertInfo.setCaApplyId(fields[4]);
                this.raCertInfo.setCertSerial(fields[5]);
                this.raCertInfo.setCertIssuser(fields[6]);
                this.raCertInfo.setCertType(fields[7]);
                this.raCertInfo.setCn(fields[8]);
                this.raCertInfo.setEmail(fields[9]);
                this.raCertInfo.setCertNotBefore(RaUtil.formatDateOfCert(fields[10]));
                this.raCertInfo.setCertNotAfter(RaUtil.formatDateOfCert(fields[11]));
                break;
            }
        }
        
        return 0;
    }
    
    /**
     * 取得指定憑證序號之憑證資訊
     * @param user UserAccount 物件
     * @param certSerial 憑證序號
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     */
    public int queryCertBySerial(UserAccount user, String certSerial) {
        HTRAFacade htraFacade = new HTRAFacade(this.raConfig.getRaUrl());
        // 2.依使用者的憑證識別碼(key1)呼叫RA-API查詢憑證並依certSerial過濾查詢出的憑證資料
        int rtn_code = htraFacade.HTRA_QueryCertByCN(user.getKey1(), user.getIp());
        if (rtn_code != 0) {
            this.errorMessage = htraFacade.HTRA_GetErrorMsg();
            return rtn_code;
        }
        // 3.Get cert by certSerial
        int certCount  = htraFacade.HTRA_GetCertCount();
        int fieldCount = htraFacade.HTRA_GetFieldCount();
        
        for (int i=0; i<certCount; i++) {
            String[] fields = new String[fieldCount];
            for (int j=0; j<fieldCount; j++) {
                fields[j] = htraFacade.HTRA_GetCertInfo(i, j);
            }
            if (fields[5].equals(certSerial)) {
                this.raCertInfo = new RaCertInfo(Integer.parseInt(fields[0]));
                this.raCertInfo.setApplyTime(RaUtil.formatDateOfCert(fields[1]));
                this.raCertInfo.setApproveTime(RaUtil.formatDateOfCert(fields[2]));
                this.raCertInfo.setCertStatus(fields[3]);
                this.raCertInfo.setCaApplyId(fields[4]);
                this.raCertInfo.setCertSerial(fields[5]);
                this.raCertInfo.setCertIssuser(fields[6]);
                this.raCertInfo.setCertType(fields[7]);
                this.raCertInfo.setCn(fields[8]);
                this.raCertInfo.setEmail(fields[9]);
                this.raCertInfo.setCertNotBefore(RaUtil.formatDateOfCert(fields[10]));
                this.raCertInfo.setCertNotAfter(RaUtil.formatDateOfCert(fields[11]));
                break;
            }
        }
        
        return 0;
    }
    
    /**
     * 憑證同步
     * @param key1 Common Name(憑證識別碼)
     * @param certId 憑證序號
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     * 調整RA憑證與CA憑證狀態同步
     */
    public int syncCert(String key1, int certId) {
        HTRAFacade htraFacade = new HTRAFacade(this.raConfig.getRaUrl());
        // 1.依銀行提供的RA帳戶呼叫RA-API進行身分驗證
        int rtn_code = this.loingWithBankRaUser(htraFacade);
        if (rtn_code != 0) {
            return rtn_code;
        }
        // 2.呼叫RA-API同步憑證資料
        rtn_code = htraFacade.HTRA_SyncCert(key1, certId, this.raConfig.getIpAddress());
        
        return this.checkError(htraFacade, rtn_code);
    }
    
    /**
     * 依銀行提供的RA帳戶呼叫RA-API進行身分驗證
     * @param htraFacade
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     */
    private int loingWithBankRaUser(HTRAFacade htraFacade) {
        // 密碼須先經過SHA-1 Hash
    	RaUtil raUtil = new RaUtil();
        String encodePswd = this.raConfig.getRaPhrase();
        
        // 判斷 raPhrase 是否加密
        if (this.raConfig.isEncrypt()) {
        	encodePswd = raUtil.toHexString(raUtil.hashBySunJCE(RaUtil.HASH_SHA1, encodePswd));
		}
        // 呼叫RA-API確認身分
        int rtn_code = htraFacade.HTRA_Login(this.raConfig.getRaAccount(), encodePswd, this.raConfig.getIpAddress());
        this.errorMessage = (rtn_code!=0)? htraFacade.HTRA_GetErrorMsg() : null;
        return rtn_code;
    }
    
    /**
     * 檢查是否有錯誤發生
     * @param htraFacade
     * @param rtn_code
     * @return int: 0 成功, others 失敗, 可使用RaService.getErrorMessage()取得錯誤訊息
     */
    private int checkError(HTRAFacade htraFacade, int rtn_code) {
        if (rtn_code != 0) {
            this.errorMessage = htraFacade.HTRA_GetErrorMsg();
            return rtn_code;
        }
        return 0;
    }
    
    /**
     * 監控 RA 及 CA 的狀態
     * @return
     */
    public int monitor() {
        HTRAFacade htraFacade = new HTRAFacade(this.raConfig.getRaUrl());
        // 1.依銀行提供的RA帳戶呼叫RA-API進行身分驗證
        int rtn_code = this.loingWithBankRaUser(htraFacade);
        if (rtn_code != 0) {
            return rtn_code;
        }
        // 2.呼叫RA-API確認 RA 及 CA 狀態
        rtn_code = htraFacade.HTRA_AdmMoniter();
        
        return this.checkError(htraFacade, rtn_code);
    }
    
}

