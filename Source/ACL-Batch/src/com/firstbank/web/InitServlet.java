package com.firstbank.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * MQListener初始化之前初始化此Serverlet
 * <p/>
 * Package: com.hitrust.fxd.web <br>
 * File Name: InitServlet.java <br>
 * <p/>
 * Purpose: <br>
 * 因應Cluster環境程式部屬後安裝路徑會不一致的問題，於系統啟動時依據系統安裝路徑，重置下列參數:<br>
 * 1. 重置MNBConfig.SysDir參數，供程式取得系統安裝路徑使用 <br>
 * 2. 依據系統安裝路徑，重新設定message_transactoin.xml中的電文定義檔路徑 <br>
 * @author Krystal Lyu
 * @version 1.0  create 2013/10/23
 */
@SuppressWarnings("serial")
public class InitServlet extends HttpServlet {
    private static Logger LOG = Logger.getLogger(InitServlet.class);
    
    /**
     * description:init
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        LOG.info("InitServlet start....");
    }
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InitServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InitServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
           */ 
        } finally { 
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
