/**
 * @(#)RerunServlet.java
 *
 * Copyright(c)2017 HiTRUST Incorporated. All rights reserved.
 * 
 * Description : 手動執行排程Servlet
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
import org.apache.log4j.Logger;

import com.firstbank.batch.AclDcRpt03Job;
import com.firstbank.batch.AlertJob;
import com.firstbank.batch.CheckCertJob;
import com.firstbank.batch.DWFtpJob;
import com.firstbank.batch.JKOExchangeJob;
import com.firstbank.batch.TrnsSyncJob;
import com.hitrust.util.DateUtil;

public class RerunServlet extends HttpServlet {
	private static final long serialVersionUID = 5246902262858539808L;

	private static Logger LOG = Logger.getLogger(RerunServlet.class);
	
	// Application
	protected ServletContext servletContext = null;
	protected ServletConfig  servletConfig  = null;
	    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RerunServlet() {
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
        LOG.info("RerunServlet processRequest start.");
        
        String rtnMsg = "執行完成";
        //取得前端傳入值
        String batchId = request.getParameter("batchId");
        String dataDate = request.getParameter("dataDate"); //v1.01
        LOG.info("rerun batchId:"+batchId+ "/ dataDate="+dataDate);
        
    	try{
    		if("JKOExchangeJob".equals(batchId)){
        		if(Integer.parseInt(dataDate) >= Integer.parseInt(DateUtil.getToday())){
        			rtnMsg = "資料日期不可大於等於系統日!";
        		}else{
        			new JKOExchangeJob().execute(new String[]{dataDate});
        		}
            }else if("AclDcRpt03Job".equals(batchId)){
    			new AclDcRpt03Job().execute(new String[]{"NONE"});
            }else if("CheckCertJob".equals(batchId)){
    			new CheckCertJob().execute(new String[]{"NONE"});
            }else if("TrnsSyncJob".equals(batchId)){
    			new TrnsSyncJob().execute(new String[]{"NONE"});
            }else if("DWFtpJob".equals(batchId)){
    			new DWFtpJob().execute(new String[]{"NONE"});	
            }else if("AlertJob".equals(batchId)){
    			new AlertJob().execute(new String[]{"NONE"});	
            }else{
            	LOG.error("error batchId:"+batchId);
            	rtnMsg = "傳入參數錯誤!";
            }
        	
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
        
        LOG.info("RerunServlet processRequest end.");
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
