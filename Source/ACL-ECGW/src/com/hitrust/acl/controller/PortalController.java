/*
 * @(#)PortalControl.java
 * @(#)PortalControl.java
 * Copyright (c) 2004 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/03/21, Ada Chen
 *   1) First release
 *	v1.01, 2016/12/02, Eason Hsu
 *	 1) TSBACL-137, ECGW 綁定連結帳號驗證方式調整
 *	v1.02, 2017/09/08, Eason Hsu
 *	 1) TSBACL-165, [Fortify] Unreleased Resource (移除未使用 Method 且被檢測有 Unreleased Resource: Streams 疑慮程式碼)
 *	 2) TSBACL-161, [Fortify] Null Dereference
 *  v1.03, 2017/09/29, Eason Hsu
 *   1) TSBACL-165, [Fortify] Unreleased Resource (Unreleased Resource: Database)
 *   2) TSBACL-161, [Fortify] Null Dereference
 *   V2.00, 2018/01/30 For 因應一銀DBA要求，取消欄位MAX 限制
 *   1) SIGNATURE_LOG:SIG_VALU 不儲存，改填入空白
 */

package com.hitrust.acl.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.common.UUIDGen;
import com.hitrust.acl.common.Validation;
import com.hitrust.acl.common.ValidationParam;
import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.response.AbstractResponseBean;
import com.hitrust.acl.security.isec.Secure;
import com.hitrust.acl.security.isec.VerifyRequest;
import com.hitrust.acl.service.ServiceModel;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.HexBin;
import com.hitrust.acl.util.MAC;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.bean.EcReqBean;
import com.hitrust.bank.common.TbCodeHelper;
import com.hitrust.bank.dao.beans.CustAcntLog;
import com.hitrust.bank.dao.beans.EcCert;
import com.hitrust.bank.dao.beans.EcMsgLog;
import com.hitrust.bank.dao.beans.SignatureLog;
import com.hitrust.bank.dao.home.CustAcntLogHome;
import com.hitrust.bank.dao.home.EcCertHome;
import com.hitrust.bank.dao.home.EcMsgLogHome;
import com.hitrust.bank.dao.home.SignatureLogHome;
import com.hitrust.bank.json.ACLink;
import com.hitrust.bank.response.ACLinkListQueryResBean;
import com.hitrust.bank.response.ACLinkResBeanOne;
import com.hitrust.bank.response.ACLinkResBeanTwo;
import com.hitrust.bank.response.IBankLoginCheckResBean;


import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Application portal control servlet for authorization request right.
 *
 */
public class PortalController extends HttpServlet {

	private static final long serialVersionUID = 5718486558694168511L;
	private static final String EC_USER_AGENT = "Mozilla/5.0";
	private static final String EC_JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
	// log4J category
	static Logger LOG = Logger.getLogger(PortalController.class);
	
    // Application
    private ServletContext application = null;
    protected ServletConfig servletConfig = null;  //Servlet Config
    
    private final String ACL = "ACL";
    

    /**
     * Servlet service, client request entry point.
     * @param request	HTTP Request
     * @param response	HTTP Response
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 初始化並記錄 Request 處理的開始時間
		long statTime = System.currentTimeMillis();

		// v1.03, 修正 Fortify 白箱掃描(Null Dereference)
    	AbstractResponseBean resBesn = new AbstractResponseBean();
    	
    	String url = request.getServletPath();
		LOG.info("========== [RequestIP]:" +  request.getRemoteAddr() +  ", [Request URL]" + url + " ==========");
		
    	EcReqBean reqBean = new EcReqBean();
    	String servletName = "";// 執行 Servlet Name
    	String service = "";	  // 功能 service
    	String rtnCode = "";	  // 回傳值代碼
    	String rtnMsg = "";		  // 回傳錯誤訊息
    	String resMsg = "";		  // 回傳值(JSON 格式)
    	String serviceClass = ""; // service package
    	String resBeanClass = ""; //
    	String resBeanPackage = "";//
    	String operation = "";	  // 執行功能
    	String resultPage = "";	  // 結果頁
    	String errorPagge = "";	  // v1.01 錯誤頁
    	String msgLogStts = "01";
    	
    	Connection conn = null;
    	OutputStream out = null;
    	Map<String, ValidationParam> validateGroup = null;
    	HashMap<String, String> serviceDefMap = null;
    	HashMap<String, String> commPackageMap = null;
    	
    	try {
    		// *****Step1. check system status*****
    		String sysInitialMsg = APSystem.getSysErr();
    		if (sysInitialMsg != null) {
    			LOG.error("========== System inital Error " + sysInitialMsg + "==========");
    			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    			return;
    		}
    		// 依據 url 取得對應 validate parameter
        	validateGroup = APSystem.getValidationParam(url);
        	
        	// 檢核來源 url 是否定義在 validation.xml
        	if (StringUtil.isBlank(validateGroup)) {
        		LOG.error("========== [Request URL not define in validation.xml!] ==========");
    			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    			return;
    		}
        	
        	// *****Step2. set request encoding*****
            request.setCharacterEncoding(APSystem.getEncoding());
            
        	// =============== 取得請求參數 ===============
        	reqBean.setMsgNo(request.getParameter("MSG_NO"));
        	reqBean.setEcId(request.getParameter("EC_ID"));
        	reqBean.setEcUser(request.getParameter("EC_USER"));
        	reqBean.setCertSn(request.getParameter("CERT_SN"));
        	reqBean.setSignValue(request.getParameter("SIGN_VALUE"));
        	
        	// 取得該次執行的 Servlet Name
        	servletName = servletConfig.getServletName();
        	service = servletConfig.getInitParameter("service"); // 預設取得各 servlet 所定的 service
        	
    		LOG.info("[Request] servletName=(" + servletName+ ")service=(" + service+ ")MSG_NO=(" + reqBean.getMsgNo() 
    				+ ")EC_ID=(" + reqBean.getEcId()+ ")EC_USER =(" + reqBean.getEcUser() + ")CERT_SN =(" + reqBean.getCertSn() + ")");

        	// *****Step3. check reuqest parameter*****
    		// 預設 service 不存在, 則從 request 取得
        	if (StringUtil.isBlank(service)) {
        		LOG.error("service not define in web.xml: " + "[service]" + service);
        		service = request.getParameter("_service");
        		LOG.error("Get service from request: " + "[service]" + service);
			}
        	
        	if (StringUtil.isBlank(service)) {
        		// 系統異常
        		LOG.error("service not defind in reuqest: " + "[service]" + service);
        		response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        		return;
        	}
        	
        	// 依據 service 取得定義在 service.xml 相關資訊
        	commPackageMap = APSystem.getServiceItem("commPackage");
        	serviceDefMap = APSystem.getServiceItem(service.toUpperCase());
    		
        	if (StringUtil.isBlank(serviceDefMap)) {
        		// 系統異常
        		LOG.error("service not define in service.xml: " + "[service]" + service);
        		response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        		return;
    		}
        	
        	// 取得 Connection
        	conn = APSystem.getConnection(APSystem.DB_ACLINK);
        	
        	// 產生 response bean instance            	
        	resBeanPackage = commPackageMap.get("resbean_package");
        	resBeanClass = serviceDefMap.get("resbean");
        	resBesn = this.getResBeanInstance(resBeanPackage, resBeanClass);;
        	
        	// =============== setp3.1  檢核各欄位合理性 ===============
			rtnCode = Validation.validationReqField(url, validateGroup.get("S").getFields(), request);
	    	if (!StringUtil.isBlank(rtnCode)) {
	    		LOG.info("========== [Filed validate fail, rtnCode: " + rtnCode + "] ==========");
	    		if (APSystem.SERVICE_UNAVAILABLE.equals(rtnCode)) {
		    		response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

	    		} else {
					// 回傳值物件轉換成 JSON 字串
	    			resMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
					this.composeRtnMsg(reqBean, rtnCode, resMsg, resBesn);
	    		}
	    		return;
			}
	    	
	    	String checkMsgNo = this.selectEcMsgLog(reqBean, conn);
			if (!StringUtil.isBlank(checkMsgNo)) {
				LOG.info("checkMsgNo is not null");//hsien
				// 回傳值物件轉換成 JSON 字串
				rtnMsg = new TbCodeHelper(checkMsgNo, "01").getTbCodeMsg();
				this.composeRtnMsg(reqBean, checkMsgNo, rtnMsg, resBesn);
				return;
			}
        	
        	// *****Step4. process request from EC*****
	    	// 處理 EC 至 GW 所有請求
        	if (servletName.startsWith(ACL)) { 
        		LOG.info("servletName.startsWith(ACL)");//hsien
        		try {
					// 新增 平台訊息收送記錄
					this.insertEcMsgLog(reqBean, "01", msgLogStts, "", conn);
					LOG.info("cert_verify="+APSystem.getProjectParam("cert_verify"));//hsien
					if ("Y".equals(APSystem.getProjectParam("cert_verify"))) {
						LOG.info("=============== 檢核憑證序號是否存在 ===============");//hsien
						// =============== setp4.1. 檢核憑證序號是否存在  ===============
						rtnCode = this.verifyEcCertIsExist(reqBean, conn);
						if (!StringUtil.isBlank(rtnCode)) {
							LOG.info("rtnCode is not null");//hsien
							// 回傳值物件轉換成 JSON 字串
							rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
							this.composeRtnMsg(reqBean, rtnCode, rtnMsg, resBesn);
							return;
							
						}
					
						// =============== setp4.2. 檢核交易簽章值是否正確  ===============
						LOG.info("=============== 檢核交易簽章值是否正確  ===============");//hsien
						rtnMsg = this.verifySignature(reqBean, request, validateGroup.get("S").getSignFields(), conn);
						if (!StringUtil.isBlank(rtnMsg)) {
							LOG.info("rtnMsg is not null");//hsien
							// 回傳值物件轉換成 JSON 字串
							rtnMsg = new TbCodeHelper("9400", "01").getTbCodeMsg();
							this.composeRtnMsg(reqBean, "9400", rtnMsg, resBesn);
							return;
						}
					}
					
				} catch (DBException e) {
					// 回傳值物件轉換成 JSON 字串
					rtnCode = "9997";
					rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
		    		this.composeRtnMsg(reqBean, rtnCode, rtnMsg, resBesn);
		    		return;
		    		
				} catch (Exception e) {
					// 回傳值物件轉換成 JSON 字串
					rtnCode = "9999";
					rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
		    		this.composeRtnMsg(reqBean, rtnCode, rtnMsg, resBesn);
		    		return;
		    		
				} finally {
					
					// 驗章失敗或憑證不存在需記錄, "會員帳號連結記錄檔"
					if (!StringUtil.isBlank(rtnCode) || !StringUtil.isBlank(rtnMsg)) {
						String custId = request.getParameter("CUST_ID");
						
						// 驗章失敗 回傳代碼 9400
						if (!StringUtil.isBlank(rtnMsg)) {
							rtnCode = "9400";
						}
						
						CustAcntLog acntLog = new CustAcntLog();
						CustAcntLogHome home = new CustAcntLogHome(conn);
						
						acntLog.LOG_NO = UUIDGen.genUUID();
				    	acntLog.CUST_ID = custId;
				    	acntLog.EC_ID	= reqBean.getEcId();
				    	acntLog.EC_USER	= reqBean.getEcUser();
				    	acntLog.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
				    	acntLog.STTS = "99"; // 驗章失敗
				    	acntLog.EXEC_SRC = "A";
				    	acntLog.EC_MSG_NO = reqBean.getMsgNo();
				    	acntLog.ERR_CODE = rtnCode;
				    	
				    	home.insert(acntLog);
					}
				}
		    	
			} else {
				// For 綁定頁面作業
				// 設定 response contentType
        		response.setContentType(APSystem.getProjectParam("html_content_type"));
        		
        		// v1.01
        		HttpSession session = request.getSession();
        		ACLink aclink = (ACLink) session.getAttribute(ACLink.ACLINK);
        		
        		if (!StringUtil.isBlank(aclink)) {
					aclink.clean();
					session.setAttribute(ACLink.ACLINK, aclink);
				}
			}
        	
        	// 依據 service 判斷該作業應導向的Controller
        	serviceClass = serviceDefMap.get("process"); 
        	operation = serviceDefMap.get("operation");
        	resultPage = serviceDefMap.get("result");
        	// v1.01
        	errorPagge = serviceDefMap.get("error");
        	
        	LOG.info("[service]: "+ service + " [resBeanClass]: " + resBeanClass + " [serviceClass]: " + serviceClass + 
        			" [operation]: " + operation + " [result]: " + resultPage);
        	
        	 
        	resBesn = this.invokeController(service, serviceClass, operation, resBesn, request, response);
        	
		} catch (DBException e) {
			resBesn.setRTN_CODE("9997");
			resBesn.setRTN_MSG(new TbCodeHelper(resBesn.getRTN_CODE(), "01").getTbCodeMsg());
			
		} catch (ApplicationException e) {
			resBesn.setRTN_CODE("9999");
			resBesn.setRTN_MSG(new TbCodeHelper(resBesn.getRTN_CODE(), "01").getTbCodeMsg());
			
		} catch (Exception e) {
			LOG.error("[portalController Exception]: ", e);
			resBesn.setRTN_CODE("9999");
			resBesn.setRTN_MSG(new TbCodeHelper(resBesn.getRTN_CODE(), "01").getTbCodeMsg());
			
		} finally {
			
			if (!StringUtil.isBlank(resBesn)) {
				// For EC 平台訊息
        		// 設定 ContentType
        		response.setContentType(APSystem.getProjectParam("json_content_type"));
        		
        		try {
        			
        			// 帳號連結綁定結果處理
        			if (resBesn instanceof ACLinkResBeanTwo) {
        				LOG.info("帳號連結綁定結果處理");
        				HttpSession session = request.getSession(true);
        				
        				String redirectURL = "";
        				
        				ACLink aclink = (ACLink) session.getAttribute("link");
        				validateGroup = APSystem.getValidationParam("/ACLink");
        				resMsg = this.transformResToJson(resBesn, validateGroup);

        				// v1.02, 移除被檢測有 Unreleased Resource: Streams 疑慮程式碼
        				//調整需透由 Transfer Service 將綁定結果回傳給電商平台
        				this.redirectToEcByTrnsSrve(resBesn, aclink.getRsltUrl(), resMsg);
//        				this.redirectToEC(aclink.getRsltUrl(), resMsg);
        				
        				redirectURL = aclink.getSuccUrl();
        				if (!"0000".equals(resBesn.getRTN_CODE())) {
        					redirectURL = aclink.getFailUrl();
        					if(redirectURL.indexOf("?")>0)
        						redirectURL = redirectURL + "&RTN_CODE="+resBesn.getRTN_CODE()+"&RTN_MSG="+URLEncoder.encode(resBesn.getRTN_MSG(), "UTF-8");
        					else
        						redirectURL = redirectURL + "?RTN_CODE="+resBesn.getRTN_CODE()+"&RTN_MSG="+URLEncoder.encode(resBesn.getRTN_MSG(), "UTF-8");
        				}
        				
        				LOG.info("resMsg: " + resMsg);
        				LOG.info("sendRedirect to: " + redirectURL);
        				
        				response.sendRedirect(redirectURL);
        				
        			} else {
        				// 其他訊息處理
        				LOG.info("其他訊息處理");
        				// 回傳值物件轉換成 JSON 字串
        				if(resBesn instanceof IBankLoginCheckResBean) {
        					resBesn.setEC_ID(null);
        					resBesn.setEC_USER(null);
        					resBesn.setMSG_NO(null);
        					resBesn.setRTN_CODE(null);
        					resBesn.setRTN_DIGEST(null);
        					resBesn.setRTN_MSG(null);
        					resMsg = JsonUtil.object2Json(resBesn, false);
        					
        				} else
        					resMsg = this.transformResToJson(resBesn, validateGroup);
        				
        				if (servletName.startsWith("ACL")) {
        					String checkMsgNo = this.selectEcMsgLog(reqBean, conn);
                			if (StringUtil.isBlank(checkMsgNo)) {
                				// 新增平台訊息收送記錄
            					this.insertEcMsgLog(reqBean, "02", msgLogStts, resMsg, conn);
        					}
					}
        				LOG.info("resMsg: " + resMsg);
        				out = response.getOutputStream();
        				out.write(resMsg.getBytes(APSystem.RESPONSE_ENCODING));
        				out.flush();
        				out.close();
        			}
        			
				} catch (Exception e) {
					LOG.error("========== [傳送回應訊息失敗] ==========", e);
					
					if (servletName.startsWith("ACL")) {
						try {
							// 回應訊息傳送失敗需將平台送送記錄(發送訊息)更新為失敗
							EcMsgLogHome home = new EcMsgLogHome(conn);
							home.updateEcMsgLogByKey(reqBean.getEcId(), reqBean.getMsgNo(), "02", "02");
							
						} catch (Exception ex) {
							LOG.error("========== [更新平台訊息收送記錄失敗] ==========", ex);
						}
					}
					
				} finally { // v1.03, 修正 Fortify 白箱掃描(Unreleased Resource: Database)
					if (conn != null) {
						APSystem.returnConnection(conn, APSystem.DB_ACLINK);
					}
				}
        		 
				
			} else {
				// v1.03, 修正 Fortify 白箱掃描(Unreleased Resource: Database)
				try {
					// For 綁定頁面作業
					// v1.01, 調整回上一頁顯示訊息
					String error = "";
					HttpSession session = request.getSession();
					ACLink aclink = (ACLink) session.getAttribute(ACLink.ACLINK);
					
					error = (!StringUtil.isBlank(aclink)) ? aclink.get_error() : "";
					
					if (!StringUtil.isBlank(error)) {
						LOG.info("[forward page]: " + errorPagge); 
						this.DispatchURL(request, response, errorPagge);
						
					} else if(!StringUtil.isBlank(resultPage)) {
						// 需做頁面 forward 處理
						LOG.info("[forward page]: " + resultPage); 
						this.DispatchURL(request, response, resultPage);
						
					} else if (StringUtil.isBlank(resultPage) && servletName.equals("portal")) {
						resMsg = (String) request.getAttribute("_result");
						
						// 將處理結果回傳至前端頁面
						if (!StringUtil.isBlank(resMsg)) {
							out = response.getOutputStream();	
							out.write(resMsg.getBytes(APSystem.RESPONSE_ENCODING));
							out.flush();
							out.close();
							
						}
					}
					
				} finally {
					if (conn != null) {
						APSystem.returnConnection(conn, APSystem.DB_ACLINK);
					}
				}
			}
		}
    	
		// 計算 Request 的處理結束時間, 並記入 Log 中
		long endTime = System.currentTimeMillis();
		LOG.info("[MSG_NO]"+reqBean.getMsgNo()+"[StatTime]"+statTime+"[EndTime]"+endTime+"[TotalTime]"+((double)(endTime - statTime))/1000);
    	
    }

    /**
     * 
     * @param request
     * @param response
     * @param url
     * @throws IOException
     * @throws ServletException
     */
	private void DispatchURL(HttpServletRequest request, HttpServletResponse response,String url) throws IOException, ServletException {
		application.getRequestDispatcher(url).forward(request, response);
	}

    /**
     * 產生回應訊息物件
     * @param resBeanPackage
     * @param resBeanClass
     * @return AbstractResponseBean
     */
    private AbstractResponseBean getResBeanInstance(String resBeanPackage, String resBeanClass) {
    	AbstractResponseBean resBean = null;
    	
    	String beanClass = resBeanPackage + "." + resBeanClass;
    	
    	LOG.info("[response bean class]: " + beanClass);
    	
    	try {
			resBean = (AbstractResponseBean) Class.forName(beanClass).newInstance();
		} catch (InstantiationException e) {
			LOG.error("Can not get instance of the specified response bean! ");
			LOG.error("IllegalAccessException : " + e.getMessage());
		} catch (IllegalAccessException e) {
			LOG.error("Can not get instance of the specified response bean! ");
			LOG.error("InstantiationException : " + e.getMessage());
		} catch (ClassNotFoundException e) {
			LOG.error("Can not get instance of the specified response bean! ");
			LOG.error("ClassNotFoundException : " + e.getMessage());
		}
    	
    	return resBean;
    }
    
    /**
     * Invoke Controller for service
     * @param service
     * @param serviceClass
     * @param operation
     * @param resBean
     * @param req
     * @param res
     * @return
     * @throws ApplicationException
     * @throws DBException
     */
    private AbstractResponseBean invokeController(String service, String serviceClass, String operation, AbstractResponseBean resBean, HttpServletRequest req, HttpServletResponse res) throws ApplicationException, DBException {
    	
    	ServiceModel serviceModel = this.getServiceModel(service, serviceClass, operation, resBean, req, res);
    	
    	return serviceModel.process();
    }
    
    /**
     * 
     * @param service
     * @param serviceClass
     * @param operation
     * @param resBean
     * @param request
     * @param response
     * @return
     */
	private ServiceModel getServiceModel(String service, String serviceClass, String operation, AbstractResponseBean resBean, HttpServletRequest request, HttpServletResponse response) {
		ServiceModel serviceModel = null;
		Constructor constructor  = null;
		
		try {
			// 1.Get the constructor of the business model class
			constructor = Class.forName(serviceClass).getConstructor(new Class[]{String.class, String.class, AbstractResponseBean.class, HttpServletRequest.class, HttpServletResponse.class});
			// 2.Invoke the constructor and new an instance
			serviceModel = (ServiceModel) constructor.newInstance(new Object[]{service, operation, resBean, request, response});
		} catch (SecurityException e) {
			LOG.error("Can not get instance of the specified business model! "); // v1.02
			LOG.error("SecurityException : " + e.getMessage());
			// Exception : Can not get instance of the specified business model.(Ingore this error)
		} catch (NoSuchMethodException e) {
			LOG.error("Can not get instance of the specified business model! "); // v1.02
			LOG.error("NoSuchMethodException : " + e.getMessage());
			// Exception : Can not get instance of the specified business model.(Ingore this error)
		} catch (ClassNotFoundException e) {
			LOG.error("Can not get instance of the specified business model! "); // v1.02
			LOG.error("ClassNotFoundException : " + e.getMessage());
			// Exception : Can not get instance of the specified business model.(Ingore this error)
		} catch (IllegalArgumentException e) {
			LOG.error("Can not get instance of the specified business model! "); // v1.02
			LOG.error("IllegalArgumentException : " + e.getMessage());
			// Exception : Can not get instance of the specified business model.(Ingore this error)
		} catch (InstantiationException e) {
			LOG.error("Can not get instance of the specified business model! "); // v1.02
			LOG.error("InstantiationException : " + e.getMessage());
			// Exception : Can not get instance of the specified business model.(Ingore this error)
		} catch (IllegalAccessException e) {
			LOG.error("Can not get instance of the specified business model! "); // v1.02
			LOG.error("IllegalAccessException : " + e.getMessage());
			// Exception : Can not get instance of the specified business model.(Ingore this error)
		} catch (InvocationTargetException e) {
			LOG.error("Can not get instance of the specified business model! "); // v1.02
			LOG.error("InvocationTargetException : " + e.getMessage());
			// Exception : Can not get instance of the specified business model.(Ingore this error)
		}
		
		return serviceModel;
	}
    
    /**
     * 檢核憑證序號是否存在
     * @param reqBean 前端參數
     * @param conn
     * @return 回傳錯誤代碼 or 空白字串
     * @throws DBException
     */
    private String verifyEcCertIsExist(EcReqBean reqBean, Connection conn) throws DBException {
    	String rtnCode = "";
    	
    	EcCert cert = null;
    	EcCertHome home = new EcCertHome(conn);
    	
    	// 依據 平台代碼(EC_ID) & 憑證序號(CERT_SN) 取得憑證資訊
		cert = home.fetchEcCertByCertSn(reqBean.getEcId(), reqBean.getCertSn());
		
		// 檢核 憑憑序號 是否存在
		if (StringUtil.isBlank(cert)) {
			rtnCode = "9401";
		}
    	
    	return rtnCode;
    }
    
    /**
     * 檢核交易簽章值是否正確
     * @param msgNo	  平台訊息序號
     * @param ecId	  平台代碼
     * @param ecUser  平台會員代號
     * @param certSn  憑證序號
     * @param req	  HttpServletRequest
     * @param signMap 簽章順序
     * @param conn
     * @return 回傳錯誤訊息 or 空白字串
     * @throws DBException
     * @throws UnsupportedEncodingException 
     */
    private String verifySignature(EcReqBean reqBean, HttpServletRequest req, Map<String, String> signMap, Connection conn) throws DBException, UnsupportedEncodingException {
    	
    	LOG.info("====== Verify Signature Begin =====");
    	String rtnMsg = "";
    	
    	int rtnCode = 0;
    	String plainVal = "";  // 簽章本文
    	String signValHex = "";
    	
    	byte[] cipherText = HexBin.hex2Bin(reqBean.getSignValue().getBytes());
    	signValHex = new String(cipherText, APSystem.RESPONSE_ENCODING);
    	
    	EcCert cert = null;
    	EcCertHome home = new EcCertHome(conn);
    	
    	// 依據 平台代碼(EC_ID) & 憑證序號(CERT_SN) 取得憑證資訊
		cert = home.fetchEcCertByCertSn(reqBean.getEcId(), reqBean.getCertSn());
    	
    	// 依據簽章順序, 兜組簽章本文
    	for (Map.Entry<String, String> entry : signMap.entrySet()) {
			plainVal = plainVal.concat(req.getParameter(entry.getValue()));
		}
    	
    	LOG.info("[certCN]" + cert.CERT_CN + " [plainVal]" + plainVal);
    	LOG.info("[signValue]" + reqBean.getSignValue());
    	LOG.info("[signValHex]" + signValHex);
    	
    	// verify signature
    	VerifyRequest verifyRequest = new VerifyRequest(signValHex, cert.CERT_CN, plainVal);
    	Secure secure = new Secure();
    	
    	rtnCode = secure.pkcs7Verify(verifyRequest);
		LOG.error("Verify Result: " + String.valueOf(rtnCode));
		LOG.error("ErrorMessage: " + secure.getErrorMessage());
    	
    	if (rtnCode != 0) {
    		LOG.error("Verify ErrMsg: " + secure.getErrorMessage());
    		TbCodeHelper helper = new TbCodeHelper(String.valueOf(rtnCode), "05");
    		rtnMsg = helper.getTbCodeMsg();
    		
			// 新增 訊息簽章記錄
			this.insertSignatureLog(reqBean, "02", conn);

		} else {
			// 新增 訊息簽章記錄
			this.insertSignatureLog(reqBean, "01", conn);

		}
    	
    	LOG.info("====== Verify Signature End =====");
    	
    	return rtnMsg;
    }
    
    /**
     * 兜組 回傳值物件
     * @param reqBean	前端參數
     * @param rtnCode	回傳代碼
     * @param rtnMsg	回傳訊息
     * @param validateGroup 
     * @param out 
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     */
    private void composeRtnMsg(EcReqBean reqBean, String rtnCode, String rtnMsg, AbstractResponseBean resBean) throws UnsupportedEncodingException, IOException {
    	
    	resBean.setMSG_NO(reqBean.getMsgNo());
		resBean.setRTN_CODE(rtnCode);
		resBean.setRTN_MSG(rtnMsg);
		resBean.setEC_ID(reqBean.getEcId());
		resBean.setEC_USER(reqBean.getEcUser());
		
    }
    
    /**
     * 新增 訊息簽章記錄
     * @param reqBean 前端參數
     * @param sigStts 驗章結果 01: 成功, 02: 失敗
     * @param conn
     * @throws DBException
     */
    private void insertSignatureLog(EcReqBean reqBean, String sigStts, Connection conn) throws DBException {
    	SignatureLog log = new SignatureLog();
    	SignatureLogHome home = new SignatureLogHome(conn);
    	
    	try {
			log.EC_ID = reqBean.getEcId();
			log.EC_MSG_NO = reqBean.getMsgNo();
			log.SIG_STTS = sigStts;
			//V2.00, 2018/01/30 For 因應一銀DBA要求，取消欄位MAX 限制 Begin
			//SIGNATURE_LOG:SIG_VALU 不儲存，改填入空白			
			//log.SIG_VALU = reqBean.getSignValue();			
			log.SIG_VALU = "";
			//V2.00, 2018/01/30 For 因應一銀DBA要求，取消欄位MAX 限制 End
			
			home.insert(log);
			
		} catch (DBException e) {
			LOG.error("========== [訊息簽章記錄 新增失敗] ==========", e);
			throw new DBException(e, "DB_INS");
		}
    }
    
    /**
     * 查詢 平台訊息收送記錄
     * @param reqBean 前端參數
     * @param msgType 訊息類別 01: 接收訊息, 02: 發送訊息
     * @param stts	  訊息狀態 01: 成功, 02: 失敗
     * @param msgCntn 訊息內容
     * @param conn
     * @throws UtilException
     * @throws DBException
     */
    private String selectEcMsgLog(EcReqBean reqBean, Connection conn) throws UtilException, DBException {
    	
    	try {
			String returnMSg = "";
    		EcMsgLog msgLog = new EcMsgLog();
    		EcMsgLogHome home = new EcMsgLogHome(conn);
    		msgLog = home.fetchEcMsgLogByKey(reqBean.getEcId(), reqBean.getMsgNo(), "");
    		if(!StringUtil.isBlank(msgLog)) {
    			returnMSg="9201";
    			return returnMSg;
    		}
    		return returnMSg;
		} catch (DBException e) {
			LOG.error("========== [平台訊息收送記錄 查詢失敗] ==========", e);
			throw new DBException(e, "DB_INS");
		}
    	
    }
    
    /**
     * 新增 平台訊息收送記錄
     * @param reqBean 前端參數
     * @param msgType 訊息類別 01: 接收訊息, 02: 發送訊息
     * @param stts	  訊息狀態 01: 成功, 02: 失敗
     * @param msgCntn 訊息內容
     * @param conn
     * @throws UtilException
     * @throws DBException
     */
    private void insertEcMsgLog(EcReqBean reqBean, String msgType, String stts, String msgCntn, Connection conn) throws UtilException, DBException {
    	
    	try {
			
    		EcMsgLog msgLog = new EcMsgLog();
    		EcMsgLogHome home = new EcMsgLogHome(conn);
    		
    		msgLog.EC_ID = reqBean.getEcId();
    		msgLog.EC_MSG_NO = reqBean.getMsgNo();
    		msgLog.MSG_TYPE = msgType;
    		msgLog.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		msgLog.STTS = stts;
    		msgLog.MSG_CNTN = msgCntn;
    		
    		home.insert(msgLog);
    		
		} catch (DBException e) {
			LOG.error("========== [平台訊息收送記錄 新增失敗] ==========", e);
			throw new DBException(e, "DB_INS");
		}
    	
    }
    
    /**
     * 取得平台請求參數值
     * @param req
     * @param validateFieldMap
     * @return
     */
	private Map<String, String> getReqFieldParams(HttpServletRequest req, Map<String, Map<String, String>> validateFieldMap) {
		Map<String, String> reqFieldParams = new HashMap<String, String>();

		for (Map.Entry<String, Map<String, String>> entry : validateFieldMap.entrySet()) {
			String paramName = entry.getKey();
			reqFieldParams.put(paramName, req.getParameter(paramName));
		}

		return reqFieldParams;
	}
    
    /**
     * 回傳值物件轉換成 JSON 字串
     * @param resBesn
     * @param validateGroup
     * @return
     */
    private String transformResToJson(AbstractResponseBean resBesn, Map<String, ValidationParam> validateGroup) {
    	String resMsg = "";
    	String digestVal = ""; // 雜湊值
    	
    	Map<String, String> resMsgMap = null;
    	Map<String, String> digestMap = null;
    	Map<String, String> acntInfoMap = null;
    	List<Map<String, String>> acntInfos = new ArrayList<Map<String, String>>();
    	
    	try {
			// 將回應訊息物件轉換成 Map for 兜組雜湊值本文 使用
			resMsgMap = BeanUtils.describe(resBesn);
			
		} catch (Exception e) {
			LOG.error("[回應訊息物件轉換 Map 發生錯誤]: ", e);
		}
		
		// for 連結綁定特殊處理
		if (resBesn instanceof ACLinkResBeanOne) {
			// 連結綁定回應訊息1
			digestMap = validateGroup.get("R1").getDigestFields();
			
		} else if (resBesn instanceof ACLinkResBeanTwo){
			// 連結綁定回應訊息2
			digestMap = validateGroup.get("R2").getDigestFields();

		} else if (resBesn instanceof ACLinkListQueryResBean) {
			// 查詢可使用連結帳戶特殊處理
			digestMap = validateGroup.get("R").getDigestFields();
			acntInfoMap = validateGroup.get("R").getAcntInfoFields();
			acntInfos = ((ACLinkListQueryResBean) resBesn).getACNT_INFO();
			
		} else {
			// 一般回應訊息
			digestMap = validateGroup.get("R").getDigestFields();
			
		}
		
		// 依據 validation.xml direct type = R 雜湊宣告序順 兜組雜湊值本文
		// v1.02, 修正 Fortify 白箱掃描(Null Dereference)
		if (!StringUtil.isBlank(resMsgMap)) {
			for (Map.Entry<String, String> entry : digestMap.entrySet()) {
				digestVal = digestVal.concat(resMsgMap.get(entry.getValue()));
			}
		}
		
		// 依據 validation.xml direct type = R 雜湊宣告序順 兜組雜湊值本文 for (查詢可使用連結帳戶 acnt_info)
		// v1.02, 修正 Fortify 白箱掃描(Null Dereference)
		if (!acntInfos.isEmpty() && !StringUtil.isBlank(acntInfoMap)) {
			for (Map<String, String> acnt : acntInfos) {
				for (Map.Entry<String, String> entry : acntInfoMap.entrySet()) {
					digestVal = digestVal.concat(acnt.get(entry.getValue()));
					
				}
			}
		}
		
		digestVal = MAC.digestSHA256(digestVal);
		
		resBesn.setRTN_DIGEST(digestVal);
		
		// 將回應轉成 JSON
		resMsg = JsonUtil.object2Json(resBesn, true);
		
		LOG.info("[Response message]: " + resMsg);
		
		return resMsg;
    }
    
//    private int redirectToEC(String rsltUrl, String jsonStr) throws Exception {
//
//		int rtnResCode = 0;
//		
//		// v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//		HttpURLConnection conn = null;
//		HttpURLConnection connHttps = null;
//		OutputStreamWriter outWriter = null;
//		BufferedWriter bufWriter = null;
//		URL url = null;
//		// v1.04, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//		OutputStream outStream = null;
//		
//		try {
//			if(rsltUrl.toUpperCase().startsWith("HTTPS")){
//				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//					@Override
//					public X509Certificate[] getAcceptedIssuers() {
//						return null;
//					}
//					@Override
//					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//					}
//					@Override
//					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//					}
//				} };
//		    	
//		    	SSLContext sc = SSLContext.getInstance("SSL");
//				sc.init(null, trustAllCerts, new SecureRandom());
//				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//				HostnameVerifier allHostsValid = new HostnameVerifier() {
//					@Override
//					public boolean verify(String hostname, SSLSession session) {
//						return true;
//					}
//				};
//				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//			}
//			//------------------------
//			// Initial URL
//			//------------------------
//			if(rsltUrl.toUpperCase().startsWith("HTTPS")){
//				LOG.info("--- Initial URL ---");
//				url = new URL(rsltUrl);
//				connHttps  = (HttpsURLConnection) url.openConnection(); // v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//				
//				//------------------------
//				// set Connection parameter
//				//------------------------
//				connHttps.setRequestMethod("POST");
//				connHttps.setRequestProperty("User-Agent", EC_USER_AGENT);
//				connHttps.setRequestProperty("Content-Type", EC_JSON_CONTENT_TYPE);
//				connHttps.setDoOutput(true);
//				
//				//-------------------------
//				// Write message to EC
//				//-------------------------
//				LOG.info("--- Open Stream to EC Begin ---");
//				// v1.04, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//				outStream = connHttps.getOutputStream();
//				// v1.01, 調整回覆訊息增加使用 URLEncoder, v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//				outWriter = new OutputStreamWriter(outStream, "UTF-8");
//				bufWriter = new BufferedWriter(outWriter);
//				bufWriter.write(URLEncoder.encode(jsonStr, "UTF-8"));
//				
//				bufWriter.flush();
//				
//				LOG.info("--- Open Stream to EC End ---");
//	
//				//-------------------------
//				// Get Response Code from connection
//				//-------------------------
//				rtnResCode = connHttps.getResponseCode();
//			}else{
//				LOG.info("--- Initial URL ---");
//				url = new URL(rsltUrl);
//				conn  = (HttpURLConnection) url.openConnection(); // v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//				
//				//------------------------
//				// set Connection parameter
//				//------------------------
//		    	conn.setRequestMethod("POST");
//		    	conn.setRequestProperty("User-Agent", EC_USER_AGENT);
//		    	conn.setRequestProperty("Content-Type", EC_JSON_CONTENT_TYPE);
//				conn.setDoOutput(true);
//				
//				//-------------------------
//				// Write message to EC
//				//-------------------------
//				LOG.info("--- Open Stream to EC Begin ---");
//				// v1.04, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//				outStream = conn.getOutputStream();
//				// v1.01, 調整回覆訊息增加使用 URLEncoder, v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//				outWriter = new OutputStreamWriter(outStream, "UTF-8");
//				bufWriter = new BufferedWriter(outWriter);
//				bufWriter.write(URLEncoder.encode(jsonStr, "UTF-8"));
//				
//				bufWriter.flush();
//				
//				LOG.info("--- Open Stream to EC End ---");
//	
//				//-------------------------
//				// Get Response Code from connection
//				//-------------------------
//				rtnResCode = conn.getResponseCode();
//			}
//			
//			LOG.info("Sending 'POST' request to URL: " + url);
//			LOG.info("Post parameters: " + jsonStr);
//			LOG.info("Response Code: " + rtnResCode);
//			
//		} catch (ConnectException ce) {
//			ce.printStackTrace();
//			LOG.error("ConnectException occurs:" + ce.toString(), ce);
//			throw ce;
//		} finally {
//			// v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//			if (bufWriter != null) {
//				try { bufWriter.close(); } catch (IOException e) { LOG.error("Failed to close OutputStreamWriter", e); }
//			}
//			if (outWriter != null) {
//				try { outWriter.close(); } catch (IOException e) { LOG.error("Failed to close BufferedWriter", e); }
//			}
//			// v1.04, 修正 Fortify 白箱掃描(Unreleased Resource: Streams)
//			if (outStream != null) {
//				try { outStream.close(); } catch (IOException e) { LOG.error("Failed to close OutputStream", e); }
//			}
//		}
//		
//		return rtnResCode;
//	}
    
	// v1.02, 移除被檢測有 Unreleased Resource: Streams 疑慮程式碼
    
    /**
     * 連結 Transfer Service 將綁定結果回傳給電商平台
     * @param resBesn
     * @param rsltUrl
     * @param validateGroup
     * @throws IOException
     */
    private void redirectToEcByTrnsSrve(AbstractResponseBean resBesn, String rsltUrl, String jsonStr) throws IOException {
    	
    	String trnsSrvHost = APSystem.getProjectParam("TRNS_SERVICE_HOST");
    	int trnsSrvPort = Integer.valueOf(APSystem.getProjectParam("TRNS_SERVICE_PORT"));
    	int timeOut = Integer.valueOf(APSystem.getProjectParam("TRNS_SERVICE_TIMEOUT"));
    	
    	// v1.02, 修正 Fortify 白箱掃描(Unreleased Resource: Sockets)
    	Socket clientSocket = null;
    	PrintWriter iSocketWriter = null;
    	BufferedReader iSocketReader = null;
    	
    	try {
    		//------------------------------------
    		// Initial Socket
    		//------------------------------------
    		clientSocket = new Socket(trnsSrvHost, trnsSrvPort);
    		clientSocket.setSoTimeout(timeOut);
    		
    		LOG.info("[TRNS_SERVER_HOST]" + trnsSrvHost + "[TRNS_SERVER_PORT]" + trnsSrvPort + "[timeOut]" + String.valueOf(timeOut));
    		
    		OutputStream outToServerStream = clientSocket.getOutputStream();
    		iSocketWriter = new PrintWriter(outToServerStream);
    		InputStream  inFromServerStream = clientSocket.getInputStream();
    		iSocketReader = new BufferedReader(new InputStreamReader(inFromServerStream));
    		//------------------------------------
    		// 傳送綁定結果給 Transfer Server
    		//------------------------------------
    		StringBuffer outStr = new StringBuffer();
    		outStr.append("[").append(rsltUrl).append("]"); //綁定結果URL
    		outStr.append("[").append(new String(HexBin.bin2Hex(jsonStr.getBytes()))).append("]"); //綁定結果
    		iSocketWriter.println(outStr.toString());
    		iSocketWriter.flush();
    		//------------------------------------
    		// 接收傳送結果
    		//------------------------------------
    		String line = null;
    		String rtnResCode = null;
    		while ((line = iSocketReader.readLine()) != null){
    			rtnResCode += line;
    		}
    		
    		LOG.info("Sending 'POST' request to URL: " + rsltUrl);
    		LOG.info("Post parameters: " + jsonStr);
    		LOG.info("Response Code: " + rtnResCode);
    		
		} catch (IOException e) {
			throw e;
			
		} finally {
			//------------------------------------
    		// Close Connection
    		//------------------------------------
			try {
				if (clientSocket != null) {
					clientSocket.close();
				}
				if (iSocketWriter != null) {
					iSocketWriter.close();
				}
				if (iSocketReader != null) {
					iSocketReader.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error("Exception occurs:", e);
			}
		}
		
    }

    /**
     * Servlet initial, get the variables for following access. & log information
     * @param config the servlet config.
     */
    final public void init(ServletConfig config) throws ServletException {

        // servlet application context
        this.application = config.getServletContext();
		this.servletConfig = config; //v3.04

		// update system dir
		String sysDir = this.application.getRealPath("/");
		sysDir = sysDir + (sysDir.endsWith(File.separator)? "" : File.separator);
        		
		try {
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			APSystem.setSysErr(e.getMessage());
		}        

        LOG.info("init() - initialization");
    }    

    /**
     * Servlet destroy, log the information.
     * @param none
     */
    final public void destroy() {
        LOG.info("destroy() - finialize"); 
    }

}
