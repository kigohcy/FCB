/**
 * @(#) ACLinkService.java
 *
 * Directions: 帳號綁定連結
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *	v1.00, 2016/04/07, Eason Hsu
 *	 1) First release
 *	v1.01, 2016/10/28, Eason Hsu
 *	 1) TSBACL-122, 平台閘道、會員平台，同意條款 PDF 版本號依據 DB 參數(PRVS_VRSN)取得
 *	v1.02, 2016/11/04, Eason Hsu
 *	 1) TSBACL-123, 平台閘道 連結綁定完成後回覆給電商訊息中實體帳號未遮蔽
 *	v1.03, 2016/11/18, Eason Hsu
 *	 1) TSBACL-130, 平台閘道, 執行 OTP 密碼確認後, 確認頁未顯示已選擇綁定帳號
 *  v1.04, 2016/11/18, Eason Hsu
 *   1) TSBACL-131, 同一組 OTP 驗證碼不可重覆驗證 
 *  v1.05, 2016/11/21, Ada Chen
 *   1) TSBACL-132, 綁定之存款帳戶排除清單增加「1604」
 *  v1.06, 2016/11/29, Ada Chen
 *   1) TSBACL-134, 1045 電文回應錯誤時, 錯誤代碼拆解錯誤
 *  v1.07, 2016/12/01, Eason Hsu
 *   1) TSBACL-136, 連結綁定帳號 OTP 驗證流程調整
 *  v1.08, 2016/12/02, Eason Hsu
 *   1) TSBACL-137, ECGW 綁定連結帳號驗證方式調整
 *  v1.09, 2016/12/02, Ada Chen
 *   1) TSBACL-138, 調整錯誤代碼拆解順序
 *  v1.10, 2016/12/23, Eason Hsu
 *   1) TSBACL-144, 網銀 & 晶片卡認證失敗, 錯誤訊息 Mapping 調整
 */

package com.hitrust.bank.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.common.TransactionControl;
import com.hitrust.acl.common.UUIDGen;
import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.mail.MailUtil;
import com.hitrust.acl.response.AbstractResponseBean;
import com.hitrust.acl.service.AbstractServiceModel;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.MathUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.bean.Accounts;
import com.hitrust.bank.bean.BindingAcnt;
import com.hitrust.bank.common.AuthenticationHelper;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.TbCodeHelper;
import com.hitrust.bank.common.WSResBean;
import com.hitrust.bank.dao.beans.BaseLimt;
import com.hitrust.bank.dao.beans.CustAcnt;
import com.hitrust.bank.dao.beans.CustAcntLink;
import com.hitrust.bank.dao.beans.CustAcntLog;
import com.hitrust.bank.dao.beans.CustData;
import com.hitrust.bank.dao.beans.CustPltf;
import com.hitrust.bank.dao.beans.EcData;
import com.hitrust.bank.dao.beans.SessionTempTable;
import com.hitrust.bank.dao.beans.SysParm;
import com.hitrust.bank.dao.beans.TbCode;
import com.hitrust.bank.dao.home.BaseLimtHome;
import com.hitrust.bank.dao.home.CustAcntHome;
import com.hitrust.bank.dao.home.CustAcntLinkHome;
import com.hitrust.bank.dao.home.CustDataHome;
import com.hitrust.bank.dao.home.CustPltfHome;
import com.hitrust.bank.dao.home.EcDataHome;
import com.hitrust.bank.dao.home.MsgSourHome;
import com.hitrust.bank.dao.home.SessionTempTableHome;
import com.hitrust.bank.dao.home.SysParmHome;
import com.hitrust.bank.json.ACLink;
import com.hitrust.bank.json.LoginResBean;
import com.hitrust.bank.response.ACLinkResBeanOne;
import com.hitrust.bank.response.ACLinkResBeanTwo;
import com.hitrust.bank.response.IBankLoginCheckResBean;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.MemberMsgResponseInfo;
import com.hitrust.bank.telegram.res.NotificationHostMsgResponseInfo;

import ni.otp.fb.Proc00F8;

import com.hitrust.bank.telegram.res.AccountListRecord;
import com.hitrust.bank.telegram.res.AccountListResponseInfo;

public class IBankLoginChecService extends AbstractServiceModel {
    
	// Log4j
    private static Logger LOG = Logger.getLogger(IBankLoginChecService.class);
//    private static HashMap ibankLoginMap  = new HashMap();
    
    public IBankLoginChecService(String service, String operation, AbstractResponseBean resBean, HttpServletRequest request, HttpServletResponse response) {
        super(service, operation, resBean, request, response);
    }
    
    public IBankLoginCheckResBean process() throws ApplicationException, DBException {
        
    	LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行連結個網狀態查詢  ["+operation+"]");
    	Connection conn = null;
//    	Connection msgConn = null;
    	conn = APSystem.getConnection(APSystem.DB_ACLINK);
    	
    	
    	
    	IBankLoginCheckResBean resBesn = new IBankLoginCheckResBean();
        
        try {
        	
        	DatabaseMetaData mtdt = conn.getMetaData();
    		LOG.info("DB 使用者: [" + mtdt.getUserName()+"]");
    		LOG.info("用戶IP:["+request.getRemoteAddr()+"]");
    		LOG.info("用戶電腦名稱:["+request.getRemoteHost()+"]");
        	
        	this.ibankLoginCheck(request, resBesn, conn);  
        } catch (Exception e) {
        	LOG.error("[aclink Exception]: ", e);
            throw new ApplicationException(e, "SYS_ERR");
            
        } finally {
            if (conn != null) {
                APSystem.returnConnection(conn, APSystem.DB_ACLINK);
            }
//            
//            if (msgConn != null) {
//            	APSystem.returnConnection(msgConn, APSystem.DB_SMS);
//			}
            
            LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行連結個網狀態查詢  ["+operation+"]");
        }
        
        return resBesn;
    }
    
    public IBankLoginCheckResBean ibankLoginCheck(HttpServletRequest req, IBankLoginCheckResBean resBesn, Connection conn) {
    	try{
    		
    		SysParm sysParm = new SysParm();
    		SysParmHome sysParmHome = new SysParmHome(conn);
    		
    		LOG.info("ibankLoginCheck");
    		String txReqId = req.getParameter("txReqId");
    		String loginResult = req.getParameter("LoginResult");
    		String custId = "";
    		LoginResBean loginResBean = (LoginResBean) JsonUtil.json2Object(req.getParameter("custData").toString(), LoginResBean.class);
    		
    		custId = loginResBean.getCustId();
    		if(custId.length()>=10) {
    			custId = custId.substring(0, 10);
    		}
    		if(txReqId.length()>=30) {
    			txReqId = txReqId.substring(0, 30);
    		}
    		
    		sysParm.PARM_CODE = txReqId;
    		sysParm.PARM_NAME = custId;
    		sysParm.PARM_VALUE = loginResult;
    		sysParmHome.insert(sysParm);
    		
    		LOG.info("custId = " + sysParm.PARM_CODE + "txReqId = " + sysParm.PARM_NAME + "Result = " + sysParm.PARM_VALUE);
//    		ibankLoginMap.put(txReqId, loginResult);
//    		ibankLoginMap.put(txReqId + "_custId", loginResBean.getCustId());
//    		LOG.info("txReqId = "+txReqId+", LoginResult = " + ibankLoginMap.get(txReqId));
//    		LOG.info("custId = " + sysParm.PARM_NAME);
    		
    		if("0000".equals(loginResult)) {
    			LOG.info("ibankLogin OK");
	    		resBesn.setAck("OK");
	    		resBesn.setAutoRedirectWaitSec("0");
    		}
    		return resBesn;
    	} catch (Exception e){
            e.printStackTrace();
        }
    	return null;
    }
    
//    public static String ibankLoginMap(String txReqId) {
//    	String retMsg = (String) ibankLoginMap.get(txReqId+"_custId");
//    	ibankLoginMap.remove(txReqId);
//    	ibankLoginMap.remove(txReqId+"_custId");
//		return retMsg;
//    }
}
