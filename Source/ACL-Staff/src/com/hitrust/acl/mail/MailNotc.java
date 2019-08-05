/*
 * @(#)MailNotc.java
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 * 
 * Modify History:
 *  v1.00, 2016/03/21, Ada Chen
 *   1) First release
 *  v1.01, 2016/11/15, Yann
 *   1) TSBACL-127, Mail Log調整
 *  v1.02, 2016/11/17, Ada Chen
 *   1) TSBACL-127, 調整 Connect 時, 若不需身份認證, 則不傳入 ID / Password
 */
package com.hitrust.acl.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MailNotc extends Thread{
	//log4j
	//protected Log LOG = LogFactory.getLog(getClass().getName());
	static Logger LOG = Logger.getLogger(MailNotc.class); //v1.01
	
	// Constant variable
	public static String HtmlCntnType = "HTML";
	public static String TextCntnType = "TEXT";

	// variable declaration
	private String mailServer;
	private String mailUser;
	private String mailPswd;
	private String mailSender;
	private String mailSource; //郵件來源信箱
	private boolean authentic;
	private String contentType;
	private String content;
	private String subject;
	private static final String CharSet = "utf-8";
	private ArrayList receiverList;
	private ArrayList attachFileList;
	private String nickName;	
	private String mailServerPort; //SMTP server port
	private ArrayList ccReceiverList; //抄送收件人列表
	private ArrayList bccReceiverList; // 密送收件人列表
	
	/**
	 * Constructor
	 */
	public MailNotc() {
		receiverList = new ArrayList();
		attachFileList = new ArrayList();
		this.ccReceiverList  = new ArrayList(); //初始化抄送收件人列表
		this.bccReceiverList = new ArrayList(); //初始化密送收件人列表
	}
	
	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}
	
	public String getMailServer() {
		return this.mailServer;
	}
	
	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}
	
	public String getMailUser() {
		return this.mailUser;
	}
	
	public void setMailPswd(String mailPswd) {
		this.mailPswd = mailPswd;
	}
	
	public void setSender(String mailSender) {
		this.mailSender = mailSender;
	}
	
	/**
	 * @param mailForm The mailForm to set.
	 * @since v1.04
	 */
	public void setMailFrom(String mailForm){
		this.mailSource = mailForm;
	}
	
	// 2004-04-16 add
	public void setNickName(String nickname) {
		this.nickName = nickname;
	}
	
	public void setAuthentic(boolean authentic) {
		 this.authentic = authentic;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return this.contentType;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	/**
	 * Need authenticated or not
	 * @return
	 */
	public boolean isAuthectic() {
		return this.authentic;
	}
	
    /**
     * @return Returns the mailServerPort.
     */
    public String getMailServerPort() {
        return mailServerPort;
    }
    
    /**
     * @param mailServerPort The mailServerPort to set.
     */
    public void setMailServerPort(String sMailServerPort) {
        mailServerPort = sMailServerPort;
    }
    
	/**
	 * Main process of send email
	 * @throws Exception
	 */
	public void sendMail() throws Exception {
		// essential check
		this.checkBeforeSendMail();
		//
		Properties props = System.getProperties();
		props.put("mail.smtp.host", this.mailServer);
		props.put("mail.smtp.port", this.mailServerPort);
		props.put("mail.smtp.from", this.mailSource); //指定郵件來源信箱, 以供發生Mail退件時使用
		if(authentic)//only authentic need username
		    props.put("mail.smtp.user", this.mailUser);
		props.put("mail.smtp.auth", String.valueOf(this.authentic));
		javax.mail.Session session = null;
		// need authentic or not
		if (this.authentic) {
			Authenticator auth = new MailPassAuth(this.mailUser, this.mailPswd);
			//session = javax.mail.Session.getDefaultInstance(props, auth);
			session = javax.mail.Session.getInstance(props, auth);
		} else {
			//session = javax.mail.Session.getDefaultInstance(props, null);
			session = javax.mail.Session.getInstance(props, null);
		}
		// create Message & set relative informations
		MimeMessage message = new MimeMessage(session);
		Multipart multipart = new MimeMultipart();
		if (this.mailSender==null || this.mailSender.equals("")) this.mailSender = this.mailUser;
		if (this.nickName==null || this.nickName.equals("")) this.nickName = this.mailSender;
		//message.setFrom(new InternetAddress(this.mailUser, this.mailSender, this.CharSet));	
		message.setFrom(new InternetAddress(new String(this.mailSender.getBytes(),CharSet), new String(this.nickName),CharSet));
		message.setSubject(new String(this.subject.getBytes()),CharSet);
		// 增加發送日期時間
		message.setSentDate(new Date());
		
		// Associate content with message
		InternetAddress[] toAddrList = new InternetAddress[this.receiverList.size()];
		for (int i=0; i<toAddrList.length; i++) {
			toAddrList[i] = new InternetAddress((String)this.receiverList.get(i));
		}
		message.addRecipients(javax.mail.Message.RecipientType.TO, toAddrList);
		
		// 增加抄送收件人
		if (this.ccReceiverList!=null && this.ccReceiverList.size()>0) {
			InternetAddress[] ccAddrList = new InternetAddress[this.ccReceiverList.size()];			
			for (int i=0; i<this.ccReceiverList.size(); i++) {
				ccAddrList[i] = new InternetAddress((String) this.ccReceiverList.get(i));
			}
			message.addRecipients(javax.mail.Message.RecipientType.CC, ccAddrList);
		}
		// 增加密送收件人
		if (this.bccReceiverList!=null && this.bccReceiverList.size()>0) {
			InternetAddress[] bccAddrList = new InternetAddress[this.bccReceiverList.size()];			
			for (int i=0; i<this.bccReceiverList.size(); i++) {
				bccAddrList[i] = new InternetAddress((String) this.bccReceiverList.get(i));
			}
			message.addRecipients(javax.mail.Message.RecipientType.BCC, bccAddrList);
		}
		
		// Associate content with multipart
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		if (this.contentType.equalsIgnoreCase("HTML")) {
			messageBodyPart.setContent(this.content, "text/html;charset="+CharSet);
			//messageBodyPart.setHeader("content-ID", "image001.jpg"); 
		} else {
			messageBodyPart.setText(this.content, CharSet);
		}
		multipart.addBodyPart(messageBodyPart);
		
		// Associate attach files with multipart
		Iterator iterator = this.attachFileList.iterator();
		while (iterator.hasNext()) {
			messageBodyPart = new MimeBodyPart();
			String fileString = (String)iterator.next();
			File attach = new File(fileString);
			if (!attach.exists() || !attach.isFile()) 
				throw new Exception("Can't sending Email because of attach file dosn't exist.("+fileString+")");
			DataSource fds = new FileDataSource(attach);
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setFileName(attach.getName());
			multipart.addBodyPart(messageBodyPart);
		}
		
		// Associate multi-part with message
		message.setContent(multipart); 
		try {
			Transport transport = session.getTransport("smtp");
			//v1.02, 調整不需身份認證, 改用 Generic connect method
			if(this.authentic){
				transport.connect(this.mailServer, this.mailUser, this.mailPswd);
			}else{
				transport.connect();
			}
			transport.sendMessage(message,message.getAllRecipients());
			transport.close();
			LOG.info("send mail success!!"); //v1.01
			
			// Clear Receivers and attach Files in ArrayList
			this.receiverList.clear();
			this.attachFileList.clear();
			this.ccReceiverList.clear(); //clear cc receivers
			this.bccReceiverList.clear();//clear bcc receivers
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Check before Send Mail
	 * @throws Exception
	 */
	private void checkBeforeSendMail() throws Exception {
		if (this.mailServer==null || this.mailServer.equals("")) throw new Exception(
			"mailServer han't been setted yet when sendMail in MailNotc");
		if (authentic && (this.mailUser==null || this.mailUser.equals(""))) //if need authentic check user
		    throw new Exception("mailUser han't been setted yet when sendMail in MailNotc");
		if (authentic && (this.mailPswd==null || this.mailPswd.equals(""))) //if need authentic check password
		    throw new Exception("Mail password hasn't been setted yet when sendMail in MailNotc");
		if (this.content==null || this.content.equals("")) throw new Exception(
			"content hasn't been setted yet when sendMail in MailNotc");
		if (this.subject==null || this.subject.equals("")) throw new Exception(
			"subject hasn't been setted yet when sendMail in MailNotc");
		// 當收件人，抄送人，密送人存在其一即可發送mail
		if (this.receiverList.size()==0 && this.ccReceiverList.size()==0 && this.bccReceiverList.size()==0) throw new Exception(
			"Receiver hasn't been setted yet when sendMail in MailNotc");
		if (!this.contentType.equalsIgnoreCase("HTML") && !this.contentType.equals("TEXT"))
			throw new Exception("Only support html/text type for MailNotc");
	}
	
	/**
	 * add multipul receivers
	 * @param RecvEmails
	 * @exception Exception
	 */
	public void addReceivers(String[] RecvEmails) throws Exception {
		// essential check
		if (RecvEmails==null || RecvEmails.length==0)
			return;
		// add addReceivers in ArrayList
		if (RecvEmails.length==1){
			RecvEmails = this.parsMailReceiver(RecvEmails[0]);
		}
		for (int i=0; i<RecvEmails.length; i++) {
			if (!this.checkEmailFormat(RecvEmails[i]))
				throw new Exception("Invalid email address format:"+RecvEmails[i]);
			this.receiverList.add(RecvEmails[i]);
		}
	}
	
	/**
	 * add multipul cc receivers
	 * @param RecvEmails
	 * @exception Exception
	 */
	public void addCCReceivers(String[] RecvEmails) throws Exception {
		// essential check
		if (RecvEmails==null || RecvEmails.length==0)
			return;
		// add cc receivers in ArrayList
		if (RecvEmails.length==1){
			RecvEmails = this.parsMailReceiver(RecvEmails[0]);
		}
		for (int i=0; i<RecvEmails.length; i++) {
			if (!this.checkEmailFormat(RecvEmails[i]))
				throw new Exception("Invalid email address format:"+RecvEmails[i]);
			this.ccReceiverList.add(RecvEmails[i]);
		}
	}
	
	/**
	 * add multipul bcc receivers
	 * @param RecvEmails
	 * @exception Exception
	 */
	public void addBCCReceivers(String[] RecvEmails) throws Exception {
		// essential check
		if (RecvEmails==null || RecvEmails.length==0)
			return;
		// add bcc receivers in ArrayList
		if (RecvEmails.length==1){
			RecvEmails = this.parsMailReceiver(RecvEmails[0]);
		}
		for (int i=0; i<RecvEmails.length; i++) {
			if (!this.checkEmailFormat(RecvEmails[i]))
				throw new Exception("Invalid email address format:"+RecvEmails[i]);
			this.bccReceiverList.add(RecvEmails[i]);
		}
	}
	
	 private String[] parsMailReceiver(String receiver) throws Exception {
	        if (receiver == null) {
	            return null;
	        }
	        StringTokenizer st = new StringTokenizer(receiver, ";");
	        String result[] = new String[st.countTokens()];
	        int i = 0;
	        while (st.hasMoreElements()) {
	            result[i++] = st.nextToken();
	        }
	        return result;
	  }
	
	/**
	 * method for check email format
	 * @param EmailAddr
	 * @return boolean
	 */
	public boolean checkEmailFormat(String EmailAddr) {
		if (EmailAddr.indexOf('@')==-1) return false;
		if (EmailAddr.indexOf('.')==-1) return false;
		return true;
	}
	
	/**
	 * add Attach File<br>
	 * use ArrayList to store input attach file
	 * @param AttachFile
	 */
	public void addAttachFile(String AttachFile) {
		this.attachFileList.add(AttachFile);
	}
	
	/**
	 * add multipul Attach Files
	 * use ArrayList to store input attach files
	 * @param AttachFiles
	 */
	public void addAttachFiles(String[] AttachFiles) {
		for (int i=0; i<AttachFiles.length; i++) {
			this.attachFileList.add(AttachFiles[i]);
		}
	}
	
	/**
	 * Inner class for authentication
	 * @author Kevin Wang
	 */
	public class MailPassAuth extends javax.mail.Authenticator {
		private String mailUser;
		private String mailPswd;
		// disable default Constructor
		private MailPassAuth() {};
		// Constructor
		public MailPassAuth(String user, String password) {
			this.mailUser = user;
			this.mailPswd = password;
		}
		public javax.mail.PasswordAuthentication getPasswordAuthentication() {
			javax.mail.PasswordAuthentication PassAuth = new javax.mail.PasswordAuthentication(mailUser ,mailPswd);
			return PassAuth;
		}
	}
	
	public void run(){
		try {
			this.sendMail();
		} catch (Exception e) {
			LOG.error("Failed to send mail: " + e.toString(), e);
		}
	}
}
