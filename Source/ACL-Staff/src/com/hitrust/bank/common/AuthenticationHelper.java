/**
 * @(#)AuthenticationHelper.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 呼叫網銀Web Service共用
 * 
 * Modify History:
 *  v1.00, 2016/04/07, Yann
 *   1) First release
 *  v1.01, 2016/11/08, Eason Hsu
 *   1) TSBACL-124, 當網銀身份驗證失敗時, 出現 ArrayIndexOutOfBoundsException 導致解密失敗
 *  
 */
package com.hitrust.bank.common;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.AppEnv;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.util.AES;
import com.hitrust.acl.util.HexBin;
import com.hitrust.bank.auth.AuthenticationRequest;
import com.hitrust.bank.auth.AuthenticationResponse;
import com.hitrust.bank.auth.AuthenticationSoapProxy;
import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.model.SysParm;
import com.hitrust.framework.APSystem;

public class AuthenticationHelper {
	// Log4j
    private Logger LOG = Logger.getLogger(AuthenticationHelper.class);
    
    public static String webServiceURL = "";
    private String channelId = "ACLINK"; //平台編號
    
    
   /**
    * 身分驗證判斷是否可登入網銀
    * @param custId 身分證字號
    * @param userCode 使用者代號
    * @param passwd 密碼
    * @return WSResBean
    * @throws Exception
    */
    public WSResBean authenticationLogin(String custId, String userCode, String passwd) throws Exception {
    	String txID = "AuthenticationLogin";
    	String inputParams = "";
    	String outputParams= "";
    	WSResBean wsResBean = null;
    	String key = "";
    	String iv = "";
    	
    	try{
    		//是否連接網銀Web service, 1:啟用  0:測試
    		String authWsflag  = AppEnv.getString("AUTH_WS_FLAG");
    		if("0".equals(authWsflag)){
    			String authWsTestLogin = AppEnv.getString("AUTH_WS_TEST_LOGIN");
    			wsResBean = (WSResBean) JsonUtil.json2Object(authWsTestLogin, WSResBean.class);
    			
    		}else{
    			//web service URL
    			webServiceURL = AppEnv.getString("AUTH_WS_URL");
    			//
    			SysParmDAO sysParmDAO = (SysParmDAO)APSystem.getApplicationContext().getBean("sysParmDAO");
    			//網銀Web Service AES KEY(32 bytes)
    			SysParm authWsAesKey = (SysParm)sysParmDAO.queryById(SysParm.class, "AUTH_WS_AES_KEY");
    			//網銀Web Service AES IV(16 bytes)
    			SysParm authWsAesIv = (SysParm)sysParmDAO.queryById(SysParm.class, "AUTH_WS_AES_IV");
    			
    			if(authWsAesKey==null || authWsAesIv==null){
    				//
    			}else{
    				key = authWsAesKey.getParmValue();
        			iv  = authWsAesIv.getParmValue();
    			}
    			
    			WSReqBean wsReqBean = new WSReqBean();
    			wsReqBean.setID(custId);
            	wsReqBean.setPWD(passwd);
            	wsReqBean.setUSERCODE(userCode);
            	
            	String input = JsonUtil.object2Json(wsReqBean, false);
            	inputParams = this.aesEncrypt(input, key, iv);
            	
            	AuthenticationSoapProxy wsSoap = new AuthenticationSoapProxy();
            	AuthenticationRequest authReq = new AuthenticationRequest();
    			authReq.setChannelId(channelId);
    			authReq.setTxID(txID);
    			authReq.setInputParams(inputParams);
    			
    			//authenticationLogin
    			AuthenticationResponse authRes = wsSoap.authenticationLogin(authReq);
    			String outputChnlId = authRes.getChannelId();
    			outputParams = authRes.getOutputParams();
    			// v1.01, 增加網銀回覆結果-解密前 log
    			LOG.info("[網銀回覆結果-解密前]: " + outputParams);
    			
    			//outputParams以AES256解密後,為JSON字串:RESULT,RANK,CODE,TYPE,ERRORMSG
    			String out = this.aesDecrypt(outputParams, key, iv);
    			// v1.01, 增加網銀回覆結果-解密後 log
    			LOG.info("[網銀回覆結果-解密後]: " + out);
    			wsResBean = (WSResBean) JsonUtil.json2Object(out, WSResBean.class);
    			
    		}
			
		}catch(Exception e){
			LOG.error("Exception"+e.toString(), e);
			throw e;
		}
    	
        return wsResBean;
    }
    
    /**
     * 密碼/使用者代碼 輸入錯誤次數查詢
     * @param custId 身分證字號
     * @return WSResBean
     * @throws Exception
     */
    public WSResBean queryErrorNo(String custId) throws Exception {
    	String txID = "QueryErrorNo";
    	String inputParams = "";
    	String outputParams= "";
    	WSResBean wsResBean = null;
    	String jimYao = "";
    	String iv = "";
    	
    	try{
    		//是否連接網銀Web service, 1:啟用  0:測試
    		String authWsflag  = AppEnv.getString("AUTH_WS_FLAG");
    		if("0".equals(authWsflag)){
    			String authWsTestErrNo = AppEnv.getString("AUTH_WS_TEST_ERRNO");
    			wsResBean = (WSResBean) JsonUtil.json2Object(authWsTestErrNo, WSResBean.class);
    			
    		}else{
    			//web service URL
    			webServiceURL = AppEnv.getString("AUTH_WS_URL");
    			//
    			SysParmDAO sysParmDAO = (SysParmDAO)APSystem.getApplicationContext().getBean("sysParmDAO");
    			//網銀Web Service AES KEY(32 bytes)
    			SysParm authWsAesKey = (SysParm)sysParmDAO.queryById(SysParm.class, "AUTH_WS_AES_KEY");
    			//網銀Web Service AES IV(16 bytes)
    			SysParm authWsAesIv = (SysParm)sysParmDAO.queryById(SysParm.class, "AUTH_WS_AES_IV");
    			
    			if(authWsAesKey==null || authWsAesIv==null){
    				//
    			}else{
    				jimYao = authWsAesKey.getParmValue();
        			iv  = authWsAesIv.getParmValue();
    			}
    			
    			WSReqBean wsReqBean = new WSReqBean();
    			wsReqBean.setID(custId);
    			
    			String input = JsonUtil.object2Json(wsReqBean, false);
            	inputParams = this.aesEncrypt(input, jimYao, iv);
            	
            	AuthenticationSoapProxy wsSoap = new AuthenticationSoapProxy();
            	AuthenticationRequest authReq = new AuthenticationRequest();
    			authReq.setChannelId(channelId);
    			authReq.setTxID(txID);
    			authReq.setInputParams(inputParams);
    			
    			//QueryErrorNo
    			AuthenticationResponse authRes = wsSoap.queryErrorNo(authReq);
    			String outputChnlId = authRes.getChannelId();
    			outputParams = authRes.getOutputParams();
    			
    			//outputParams以AES256解密後,為JSON字串:RESULT,RANK,CODE,TYPE,ERRORMSG
    			String out = this.aesDecrypt(outputParams, jimYao, iv);
    			wsResBean = (WSResBean) JsonUtil.json2Object(out, WSResBean.class);
    			
    		}
			
		}catch(Exception e){
			LOG.error("Exception"+e.toString(), e);
			throw e;
		}
    	
    	return wsResBean;
    }
    
    /**
     * 密碼解鎖
     * @param custId 身分證字號
     * @return WSResBean
     * @throws Exception
     */
    public WSResBean unlock(String custId) throws Exception {
    	String txID = "Unlock";
    	String inputParams = "";
    	String outputParams= "";
    	WSResBean wsResBean = null;
    	String jimYao = "";
    	String iv = "";
    	
    	try{
    		//是否連接網銀Web service, 1:啟用  0:測試
    		String authWsflag  = AppEnv.getString("AUTH_WS_FLAG");
    		if("0".equals(authWsflag)){
    			String testUnlock = AppEnv.getString("TEST_UNLOCK");
    			wsResBean = (WSResBean) JsonUtil.json2Object(testUnlock, WSResBean.class);
    			
    		}else{
    			//web service URL
    			webServiceURL = AppEnv.getString("AUTH_WS_URL");
    			//
    			SysParmDAO sysParmDAO = (SysParmDAO)APSystem.getApplicationContext().getBean("sysParmDAO");
    			//網銀Web Service AES KEY(32 bytes)
    			SysParm authWsAesKey = (SysParm)sysParmDAO.queryById(SysParm.class, "AUTH_WS_AES_KEY");
    			//網銀Web Service AES IV(16 bytes)
    			SysParm authWsAesIv = (SysParm)sysParmDAO.queryById(SysParm.class, "AUTH_WS_AES_IV");
    			
    			if(authWsAesKey==null || authWsAesIv==null){
    				//
    			}else{
    				jimYao = authWsAesKey.getParmValue();
        			iv  = authWsAesIv.getParmValue();
    			}
    			
    			WSReqBean wsReqBean = new WSReqBean();
    			wsReqBean.setID(custId);
    			
    			String input = JsonUtil.object2Json(wsReqBean, false);
            	inputParams = this.aesEncrypt(input, jimYao, iv);
            	
            	AuthenticationSoapProxy wsSoap = new AuthenticationSoapProxy();
            	AuthenticationRequest authReq = new AuthenticationRequest();
    			authReq.setChannelId(channelId);
    			authReq.setTxID(txID);
    			authReq.setInputParams(inputParams);
    			
    			//Unlock
    			AuthenticationResponse authRes = wsSoap.unlock(authReq);
    			String outputChnlId = authRes.getChannelId();
    			outputParams = authRes.getOutputParams();
    			
    			//outputParams以AES256解密後,為JSON字串:RESULT,RANK,CODE,TYPE,ERRORMSG
    			String out = this.aesDecrypt(outputParams, jimYao, iv);
    			wsResBean = (WSResBean) JsonUtil.json2Object(out, WSResBean.class);
    			
    		}
			
		}catch(Exception e){
			LOG.error("Exception"+e.toString(), e);
			throw e;
		}
    	
    	return wsResBean;
    }
    
    /**
     * AES 256 加密
     * @param input
     * @param key
     * @param iv
     * @return String
     * @throws Exception
     */
    public String aesEncrypt(String input, String key, String iv) throws Exception{
		String cipherStr = null;
		byte[] cipherText = AES.encrypt(input.getBytes(), key.getBytes(), iv.getBytes(), "CBC", true);
		cipherStr = new String(HexBin.bin2Hex(cipherText));
		return cipherStr;
	}
	
    /**
     * AES 256 解密
     * @param input
     * @param key
     * @param iv
     * @return String
     * @throws Exception
     */
	public String aesDecrypt(String input, String key, String iv) throws Exception{
		
		byte[] cipherText = HexBin.hex2Bin(input.getBytes());
		byte[] plainText = AES.decrypt(cipherText, key.getBytes(), iv.getBytes(), "CBC", true);
		return new String(plainText, "utf-8");
	}
}
