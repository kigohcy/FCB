/**
 * @(#)ConfigServlet.java
 *
 * Copyright(c)2017 HiTRUST Incorporated. All rights reserved.
 * 
 * Description : 設定DB SysParm Table Servlet
 *
 * Modify History:
 *  v1.00, 2017/11/24, Yann
 *   1) First release
 *  
 */
package com.firstbank.web;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.firstbank.batch.AclDcRpt03Job;
import com.firstbank.batch.AlertJob;
import com.firstbank.batch.CheckCertJob;
import com.firstbank.batch.DWFtpJob;
import com.firstbank.batch.JKOExchangeJob;
import com.firstbank.batch.TrnsSyncJob;
import com.hitrust.acl.service.SysParmService;
import com.hitrust.acl.util.AclApplicationContextAwarer;
import com.hitrust.acl.util.SpringHelper;
import com.hitrust.util.DateUtil;

public class ConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 5246902262858539808L;

	private static Logger LOG = Logger.getLogger(ConfigServlet.class);
	
	public ApplicationContext ac;
	
	// Application
	protected ServletContext servletContext = null;
	protected ServletConfig  servletConfig  = null;
	
	    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfigServlet() {
        super();
    }
    
    //
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.servletContext = config.getServletContext();
        this.servletConfig  = config;
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.info("ConfigServlet processRequest start.");
        
        String rtnMsg = "執行完成";
        //取得前端傳入值
        String alert_frequency        = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_frequency"));
        String alert_mail_receiver    = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_mail_receiver"));
        String alert_period           = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_period"));
        String alert_sms_end          = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_sms_end"));
        String alert_sms_frequency    = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_sms_frequency"));
        String alert_sms_period       = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_sms_period"));
        String alert_sms_start        = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_sms_start"));
        String alert_sms_telno        = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_sms_telno"));
        String alert_sms_status       = StringEscapeUtils.escapeHtml((String)request.getParameter("alert_sms_status"));
        
        LOG.debug("alert_frequency	  =["+alert_frequency	 +"]"); 			
        LOG.debug("alert_mail_receiver=["+alert_mail_receiver+"]");
        LOG.debug("alert_period       =["+alert_period       +"]");
        LOG.debug("alert_sms_end      =["+alert_sms_end      +"]");
        LOG.debug("alert_sms_frequency=["+alert_sms_frequency+"]");
        LOG.debug("alert_sms_period   =["+alert_sms_period   +"]");
        LOG.debug("alert_sms_start    =["+alert_sms_start    +"]");
        LOG.debug("alert_sms_telno    =["+alert_sms_telno    +"]");
        LOG.debug("alert_sms_status   =["+alert_sms_status   +"]");
        
    	try{
    		 if (ac == null){
    				ac = AclApplicationContextAwarer.getApplicationContext();//首先嘗試取得framework自動載入的ac
    				if (ac == null){
    			ac = SpringHelper.getApplicationContext();//取不到freamwork的ac，則載入預設的SrpingHelper
    				}
    			}
    		    
    		    SysParmService sysParmService =  (SysParmService) ac.getBean("sysParmService");
    		    
    		    sysParmService.updateSysParmByParmCode("ALERT_FREQUENCY",alert_frequency);
    		    sysParmService.updateSysParmByParmCode("ALERT_MAIL_RECEIVER",alert_mail_receiver);
    		    sysParmService.updateSysParmByParmCode("ALERT_PERIOD",alert_period);
    		    sysParmService.updateSysParmByParmCode("ALERT_SMS_END",alert_sms_end);
    		    sysParmService.updateSysParmByParmCode("ALERT_SMS_FREQUENCY",alert_sms_frequency);
    		    sysParmService.updateSysParmByParmCode("ALERT_SMS_PERIOD",alert_sms_period);
    		    sysParmService.updateSysParmByParmCode("ALERT_SMS_START",alert_sms_start);
    		    sysParmService.updateSysParmByParmCode("ALERT_SMS_STATUS",alert_sms_status);
    		    sysParmService.updateSysParmByParmCode("ALERT_SMS_TELNO",alert_sms_telno);
            	
        	
        }catch(Exception e){
        	LOG.error("Exception:"+e.toString(), e);
        	rtnMsg = "批次執行失敗";
        }
        
        //回應訊息
    	String url  =  request.getParameter("jspUrl");
    	request.setAttribute("_rtnMsg", rtnMsg);
        servletContext.getRequestDispatcher(url).forward(request, response);
        
        //ServletOutputStream sos = response.getOutputStream();
        //sos.write(rtnMsg.getBytes("UTF-8"));
        //sos.flush();
        
        LOG.info("ConfigServlet processRequest end.");
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("Use GET method: " + request.getParameter("batchToken") + "[" + request.getRemoteAddr()+"]");
    	String rtnMsg = "Method not allowed for your request!";
        ServletOutputStream sos = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        sos.write(rtnMsg.getBytes("UTF-8"));
        sos.flush();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
}
