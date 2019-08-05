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
 *  v1.02, 2017/09/05, Eason Hsu
 *   1) TSBACL-159, [Fortify] Key Management
 */

package com.hitrust.bank.common;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.util.AES;
import com.hitrust.acl.util.HexBin;
import com.hitrust.bank.auth.AuthenticationRequest;
import com.hitrust.bank.auth.AuthenticationResponse;
import com.hitrust.bank.auth.AuthenticationSoapProxy;
import com.hitrust.bank.dao.beans.SysParm;
import com.hitrust.bank.dao.home.SysParmHome;

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
    	Connection conn = null;
    	String aesKey = "";	// v1.02, 修正 Fortify 白箱掃描(Key Management: Empty Encryption Key)
    	String iv = "";
    	
    	try{
    		//是否連接網銀Web service, 1:啟用  0:測試
    		String authWsflag  = APSystem.getProjectParam("AUTH_WS_FLAG");
    		if("0".equals(authWsflag)){
    			String authWsTestLogin = APSystem.getProjectParam("AUTH_WS_TEST_LOGIN");
    			wsResBean = (WSResBean) JsonUtil.json2Object(authWsTestLogin, WSResBean.class);
    			
    		}else{
    			//web service URL
    			webServiceURL = APSystem.getProjectParam("AUTH_WS_URL");
    			//
    			conn = APSystem.getConnection(APSystem.DB_ACLINK);
    			SysParmHome home = new SysParmHome(conn);
    			//網銀Web Service AES KEY(32 bytes)
    			SysParm authWsAesKey = home.fetchSysParmByKey("AUTH_WS_AES_KEY");
    			//網銀Web Service AES IV(16 bytes)
    			SysParm authWsAesIv  = home.fetchSysParmByKey("AUTH_WS_AES_IV");
    			if(authWsAesKey==null || authWsAesIv==null){
    				//
    			}else{
    				aesKey = authWsAesKey.PARM_VALUE; // v1.02, 修正 Fortify 白箱掃描(Key Management: Empty Encryption Key)
        			iv  = authWsAesIv.PARM_VALUE;
    			}
    			
    			WSReqBean wsReqBean = new WSReqBean();
    			wsReqBean.setID(custId);
            	wsReqBean.setPWD(passwd);
            	wsReqBean.setUSERCODE(userCode);
            	
            	String input = JsonUtil.object2Json(wsReqBean, false);
            	inputParams = this.aesEncrypt(input, aesKey, iv); // v1.02, 修正 Fortify 白箱掃描(Key Management: Empty Encryption Key)
            	
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
//    			LOG.info("[網銀回覆結果-解密前]: " + outputParams);
    			
    			//outputParams以AES256解密後,為JSON字串:RESULT,RANK,CODE,TYPE,ERRORMSG
    			String out = this.aesDecrypt(outputParams, aesKey, iv); // v1.02, 修正 Fortify 白箱掃描(Key Management: Empty Encryption Key)
    			// v1.01, 增加網銀回覆結果-解密後 log
//    			LOG.info("[網銀回覆結果-解密後]: " + out);
    			wsResBean = (WSResBean) JsonUtil.json2Object(out, WSResBean.class);
    			
    		}
			
		}catch(Exception e){
			LOG.error("Exception"+e.toString(), e);
			throw e;
		}finally {
            if (conn != null) {
                APSystem.returnConnection(conn, APSystem.DB_ACLINK);
            }
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
    	String txId = "QueryErrorNo";
    	String inputParams = "";
    	String outputParams= "";
    	WSResBean wsResBean = null;
    	
    	try{
    		//是否連接網銀Web service, 1:啟用  0:測試
    		String authWsflag  = APSystem.getProjectParam("AUTH_WS_FLAG");
    		if("0".equals(authWsflag)){
    			String authWsTestErrNo = APSystem.getProjectParam("AUTH_WS_TEST_ERRNO");
    			wsResBean = (WSResBean) JsonUtil.json2Object(authWsTestErrNo, WSResBean.class);
    			
    		}else{
    			AuthenticationSoapProxy wsSoap = new AuthenticationSoapProxy();
    			
    			wsResBean = new WSResBean();
    			
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
    	String txId = "Unlock";
    	String inputParams = "";
    	String outputParams= "";
    	WSResBean wsResBean = null;
    	
    	try{
    		//是否連接網銀Web service, 1:啟用  0:測試
    		String authWsflag  = APSystem.getProjectParam("AUTH_WS_FLAG");
    		if("0".equals(authWsflag)){
    			String testUnlock = APSystem.getProjectParam("AUTH_WS_TEST_UNLOCK");
    			wsResBean = (WSResBean) JsonUtil.json2Object(testUnlock, WSResBean.class);
    			
    		}else{
    			AuthenticationSoapProxy wsSoap = new AuthenticationSoapProxy();
    			
    			wsResBean = new WSResBean();
    			
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
