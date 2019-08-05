/**
 * @(#)MailUtil.java
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 * 
 * Modify History:
 *  v1.00, 2016/03/21, Ada Chen
 *   1) First release
 *  v1.01, 2016/11/15, Yann
 *   1) TSBACL-127, Mail Log調整
 *  
 */
package com.hitrust.acl.mail;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

//import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.AppEnv;
import com.hitrust.acl.common.ValidationEnv;
import com.hitrust.acl.util.MAC;

public class MailUtil {
	//log4j
	static Logger LOG = Logger.getLogger(MailUtil.class); //v1.01

    /**
     * Render HTML郵件內容
     * @param map 郵件template變數資料, 以HashMap封裝
     * @param templatPath 郵件template路徑
     * @return HTML郵件內容
     * @throws com.hitrust.lib.exception.ApplicationException Render異常時拋出
     */
    public static String renderMailHtmlContent(HashMap map, String templateId) throws Exception {
        VelocityEngine ve = new VelocityEngine();
        
        String templatePath = AppEnv.getString("MAIL_TEMP_PATH");
        LOG.debug("templatePath : " + templatePath);
        Velocity.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, templatePath);
        
        try {
        	ve.init();
        } catch (Exception ex) {
            LOG.error("Failed to initial template engine!" + ex.toString(), ex);
        	ex.printStackTrace();
        	throw ex;
        }

        // load template files
        Template t = getVelocityTemplate(templateId);
        if (t == null) {
            LOG.error("Failed to load mail template file!");
        }
        
        // prepare context & parameters
        VelocityContext context = new VelocityContext();
        context.put("command", map);

        // merge context & template file into String
        StringWriter writer = new StringWriter();
        try {
            t.merge(context, writer);
        } catch (Exception ex) {
            LOG.error("Failed to merge mail template file!" + ex.toString(), ex);
        	throw ex;
        }
        try {
            return new String(writer.toString().getBytes(System.getProperty("file.encoding")));
        } catch (UnsupportedEncodingException ex) {
        	LOG.error("Failed to convert mail content!" + ex.toString(), ex);
            return "";
        }
    }
    
    public static synchronized Template getVelocityTemplate(String templatPath) {
        Template t = null;
        try {
            t = Velocity.getTemplate(templatPath, "utf-8");
        } catch (ResourceNotFoundException ex) {
        	LOG.error("Failed to get mail template!" + ex.toString(), ex);
        } catch (ParseErrorException ex) {
        	LOG.error("Failed to get mail template!" + ex.toString(), ex);
        } catch (Exception ex) {
        	LOG.error("Failed to get mail template!" + ex.toString(), ex);
        }
        return t;
    }
    
    /**
	 * 
	 * @param content
	 * @param contentType
	 * @param subject
	 * @param receivers
	 * @param ccReveicers
	 * @param bccReveicers
	 * @param attachFiles
	 * @throws Exception
	 */
    public void addMail(String content, String subject, String receivers, 
    		String ccReveicers, String bccReveicers, String[] attachFiles) throws Exception {
        // create MailNotc
        MailNotc mailNotc = new MailNotc();
        mailNotc.setMailServer(AppEnv.getString("MAIL_SERVER"));
        mailNotc.setMailServerPort(AppEnv.getString("MAIL_SERVER_PORT"));
        mailNotc.setSender(AppEnv.getString("MAIL_SENDER"));
        mailNotc.setMailUser(AppEnv.getString("MAIL_USER"));
        mailNotc.setAuthentic(AppEnv.getString("AUTHENTIC").equals("1"));
        LOG.debug("MAIL_PSWD:"+AppEnv.getString("MAIL_PSWD"));
        LOG.debug("MAIL_PSWD1:"+MAC.decryptePswd(AppEnv.getString("MAIL_PSWD")));
        mailNotc.setMailPswd(MAC.decryptePswd(AppEnv.getString("MAIL_PSWD")));
        mailNotc.setNickName(AppEnv.getString("NICKNAME"));
        mailNotc.setMailFrom(AppEnv.getString("MAIL_FROM"));
        
        mailNotc.setContentType(MailNotc.HtmlCntnType);
        mailNotc.setContent(content);
        mailNotc.setSubject(subject);
        
        LOG.debug("MailServer:"+mailNotc.getMailServer()
        +"/MailServerPort:"+mailNotc.getMailServerPort()
        +"/Sender:"+AppEnv.getString("MAIL_SENDER")
        +"/MailUser:"+mailNotc.getMailUser()
        +"/Authentic:"+AppEnv.getString("AUTHENTIC")
        +"/NickName:"+AppEnv.getString("NICKNAME")
        +"/MailFrom:"+AppEnv.getString("MAIL_FROM")); //v1.01
        
        try {
            mailNotc.addReceivers(filterReceive(new String[] {receivers}));
            // 副本
            if (ccReveicers != null && !"".equals(ccReveicers)) {
                mailNotc.addCCReceivers(filterReceive(new String[] {ccReveicers}));
            }
            // 密件副本
            if (bccReveicers != null && !"".equals(bccReveicers)) {
                mailNotc.addCCReceivers(filterReceive(new String[] {bccReveicers}));
            }
            // 附件
            if (attachFiles != null) {
                mailNotc.addAttachFiles(attachFiles);
            }
        } catch (Exception ex) {
            LOG.error("Failed to add Mail! " + ex.toString(), ex);
            return;
        }
        
        try{
        	 //mailNotc.sendMail();
        	 mailNotc.start(); //使用Thread方式發送
        }catch(Exception e){
        	throw new Exception("SEND_MAIL_FAILURE");
        }
        
        LOG.info("Send Mail finish!"); //v1.01
    }

    private static String[] filterReceive(String receive[])throws Exception{
		if (receive==null||receive.length==0){
			LOG.error("receive is null ");
			return null;
		}
		ArrayList list = new ArrayList();
		for (int i=0; i<receive.length; i++){
			if (receive[i]!=null&&!"".equals(receive[i].trim())&&!"null".equalsIgnoreCase(receive[i])){
                if(receive[i].indexOf(",") != -1) {
                    String [] temp = receive[i].split(",");
                    for(int j=0;j<temp.length;j++) {
                        list.add(temp[j]);
                    }
                } else if(receive[i].indexOf(";") != -1) {
                    String [] temp = receive[i].split(";");
                    for(int j=0;j<temp.length;j++) {
                        list.add(temp[j]);
                    }
                } else {
                    list.add(receive[i]);
                }
			}
		}
		if (list.size()==0){
			return null;
		}
		return (String[])list.toArray(new String[list.size()]);
	}
}
