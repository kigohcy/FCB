/*
 * @(#)JcicMessage.java
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2017/10/11, Bing Lien
 *  1) First release
 */
package com.hitrust.bank.telegram.message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.hitrust.bank.telegram.communication.BaseCommunication;
import com.hitrust.bank.telegram.communication.BaseCommunicationFactory;
import com.hitrust.bank.telegram.communication.MultiPartCommunication;
import com.hitrust.bank.telegram.exception.CommunicationException;
import com.hitrust.bank.telegram.exception.MessageException;
import com.hitrust.bank.telegram.exception.UtilException;
import com.hitrust.bank.telegram.req.RequestInfo;
import com.hitrust.bank.telegram.req.rule.RequestRule;
import com.hitrust.bank.telegram.res.ResponseInfo;
import com.hitrust.bank.telegram.res.rule.MultiResponseMergeRule;
import com.hitrust.bank.telegram.res.rule.ResponseRule;
import com.hitrust.bank.telegram.util.ConstantUtil;
import com.hitrust.communication.GenericCommunication;
import com.hitrust.framework.exception.BaseException;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.util.DOM;
import com.hitrust.telegram.config.MessageConfig;
//import com.hitrust.lib.APSystem;
import com.hitrust.telegram.formator.TextFormator;
import com.hitrust.telegram.interceptor.HostRequestInterceptor;
import com.hitrust.telegram.interceptor.HostResponseInterceptor;
import com.hitrust.telegram.util.FormatorUtil;
import com.hitrust.telegram.util.XMLBeanUtil;
import com.hitrust.util.ClassUtil;
import com.hitrust.util.DateUtil;
import com.hitrust.util.StringUtil;

/**
 * The class of all the message transaction.
 */
public class BaseMessage {

	// Log4J
	private static final Logger LOG=Logger.getLogger(BaseMessage.class);

	// Input info
	protected RequestInfo requestInfo;

	// Output info
	protected ResponseInfo responseInfo;

	// Message Code (message_transaction.xml)
	protected String messageCode;

	// Transaction Code
	protected String transactionCode;

	// Communication Code
	protected String communicationCode;

	// Message parameters
	@SuppressWarnings("rawtypes")
	protected HashMap messageParams;

	// Send XML Document
	protected Document sendDoc = null;

	// Receive XML Document
	protected Document recvDoc = null;

	// Send telegram
	protected byte[] sendTelegram;

	// Recv telegram
	protected byte[] recvTelegram;
	
	// Send telegram flag(1 send, 0 not send, defaut send)
	protected String sendMsgFlag;
	
	// Recv telegram flag(1 recv, 0 not recv, default recv)
	protected String recvMsgFlag;
	
	// Send telegram type(1 XML, 0 TXT)
	protected String sendMsgType;
	
	// Recv telegram type(1 XML, 0 TXT)
	protected String recvMsgType;

	// Send telegram xsl
	protected String sendXsl;

	// Recv telegram xsl
	protected String recvXsl;
	
	// Recv telegram def file
	protected String recvDefFile; // Only fixed length

	// Send telegram header xsl
	//protected String sendHeaderXsl;

	// Recv telegram header xsl
	//protected String recvHeaderXsl;
	
	// Recv telegram header def file
	protected String recvHeaderDefFile; // Only fixed length

	// Send telegram encoding
	protected String sTelegramEncoding;

	// Recv telegram encoding
	protected String rTelegramEncoding;

	// Send business rule
	protected String sBusinessRule;

	// Recv business rule
	protected String rBusinessRule;

	// Send interceptor
	protected String sInterceptor;

	// Recv interceptor
	protected String rInterceptor;
	
	// Recv merge rule
	protected String rMergeRule;
	
	// Log msg document flag
	protected String logMsgDocumentFlag;
	
	// Log send msg document dir
	protected String logSendMsgDocumentDir;
	
	// Log recv msg document dir
	protected String logRecvMsgDocumentDir;
	
	// Log host msg flag
	protected String logHostMsgFlag;
	
	// Log send host msg dir
	protected String logSendHostMsgDir;
	
	// Log recv host msg dir
	protected String logRecvHostMsgDir;
	
	// Send compele flag
	protected boolean sendCompleteFlag;
	
	// Send telegrams
	protected List<String> sendTelegramFiles;
	
	// Recv telegrams
	protected List<String> recvTelegramFiles;
	
	//protected String correlationId;

	/**
	 * Constructor
	 * @param message_code
	 * @param transaction_code
	 * @param requestInfo
	 * @param responseInfo
	 * @throws MessageException
	 */
	public BaseMessage(String message_code, String transaction_code, RequestInfo requestInfo, ResponseInfo responseInfo) throws MessageException {
		// 1.initial message transaction
		this.transactionCode = transaction_code;
		this.initialMessage(message_code, requestInfo, responseInfo);
		
	}


	/**
	 * Constructor(For test)
	 * @param message_code
	 * @param transaction_code
	 * @param inputDoc
	 * @throws MessageException
	 */
	public BaseMessage(String message_code, String transaction_code, Document inputDoc) throws MessageException {
		// 1.initial message transaction
		this.transactionCode = transaction_code;
		this.initialMessage(message_code, null, null);
		// 2.set send document
		this.sendDoc = inputDoc;
	}
	
	public BaseMessage(String message_code, String transaction_code, String correlationId, Document inputDoc)throws MessageException{
		this.transactionCode = transaction_code;
		initialMessage(message_code, null, null);

		this.sendDoc = inputDoc;
	}


	/**
	 * Initial message transaction
	 * @param message_code
	 * @param requestInfo
	 * @param responseInfo
	 * @throws MessageException
	 */
	private void initialMessage(String message_code, RequestInfo requestInfo, ResponseInfo responseInfo) throws MessageException {
		// 1.Get message transaction config
		this.messageParams = MessageConfig.getMsgTransactionParameters(message_code);
		// 2.Check message transaction exist
		if (this.messageParams == null) {
			// Exception : message transaction not exist
			throw new MessageException("Message transaction config not exist! ", "MSG01");
		}
		// 3.Initial message transaction
		this.requestInfo = requestInfo;
		this.responseInfo= responseInfo;
		this.messageCode     = message_code;
		this.communicationCode   = (String) this.messageParams.get("communication");
		this.sendMsgFlag         = (String) this.messageParams.get("sendMsgFlag");
		this.recvMsgFlag         = (String) this.messageParams.get("recvMsgFlag");
		this.sendMsgType         = (String) this.messageParams.get("sendMsgType");
		this.recvMsgType         = (String) this.messageParams.get("recvMsgType");		
		this.sendXsl             = MessageConfig.getDefinitionRoot() + this.messageParams.get("send_xsl");
		this.recvXsl             = MessageConfig.getDefinitionRoot() + this.messageParams.get("recv_xsl");
//		this.sendHeaderXsl       = MessageConfig.getDefinitionRoot() + this.messageParams.get("send_xsl_header");
//		this.recvHeaderXsl       = MessageConfig.getDefinitionRoot() + this.messageParams.get("recv_xsl_header");
//		this.sendHeaderXsl       = MessageConfig.getDefinitionRoot() + MessageConfig.getMsgTransactionParameterValue("common", "send_xml_xsl_header");
//		this.recvHeaderXsl       = MessageConfig.getDefinitionRoot() + MessageConfig.getMsgTransactionParameterValue("common", "recv_xml_xsl_header");
		this.recvDefFile         = MessageConfig.getDefinitionRoot() + (String) this.messageParams.get("recvDefFile");
		this.recvHeaderDefFile   = MessageConfig.getDefinitionRoot() + MessageConfig.getMsgTransactionParameterValue("common", "RECV_HEADER_DEF_FILE");
		this.sTelegramEncoding   = (String) this.messageParams.get("sEncoding");
		this.rTelegramEncoding   = (String) this.messageParams.get("rEncoding");
		this.sBusinessRule       = (String) this.messageParams.get("sBusinessRule");
		this.rBusinessRule       = (String) this.messageParams.get("rBusinessRule");
		this.sInterceptor        = (String) this.messageParams.get("sInterceptor");
		this.rInterceptor        = (String) this.messageParams.get("rInterceptor");	
		this.rMergeRule          = (String) this.messageParams.get("rMergeRule");
		this.logMsgDocumentFlag    = MessageConfig.getMsgTransactionParameterValue("common", "LOG_MSG_DOCUMENT_FLAG");
		this.logSendMsgDocumentDir = MessageConfig.getMsgTransactionParameterValue("common", "LOG_SEND_MSG_DOCUMENT_DIR");
		this.logRecvMsgDocumentDir = MessageConfig.getMsgTransactionParameterValue("common", "LOG_RECV_MSG_DOCUMENT_DIR");
		this.logHostMsgFlag        = MessageConfig.getMsgTransactionParameterValue("common", "LOG_HOST_MSG_FLAG");
		this.logSendHostMsgDir     = MessageConfig.getMsgTransactionParameterValue("common", "LOG_SEND_HOST_MSG_DIR");
		this.logRecvHostMsgDir     = MessageConfig.getMsgTransactionParameterValue("common", "LOG_RECV_HOST_MSG_DIR");		
		this.sendTelegramFiles     = new ArrayList<String>();
		this.recvTelegramFiles     = new ArrayList<String>();
	}

	/**
	 * Send the telegram to host
	 * @param communication_code
	 * @throws BaseException
	 * @throws SAXException 
	 * @throws IOException 
	 * @throws UtilException 
	 * @throws com.hitrust.telegram.exception.MessageException 
	 */
	public void postTelegram(String communication_code) throws BusinessException {
		LOG.debug("postTelegram()");
		Object communication = null;
		
		try {
			//LOG.info("During this message transaction, send is "+("0".equals(this.sendMsgFlag)? "skip":"need")+", recv is "+("0".equals(this.recvMsgFlag)? "skip":"need"));
			
			// 1.Locate the communication method
			if (StringUtil.isBlank(communication_code)) {
				communication_code = this.communicationCode;
			}
			LOG.debug("this.communicationCode:"+communication_code);
			communication = BaseCommunicationFactory.getInstance(communication_code);


			if (!"0".equals(this.sendMsgFlag)) {
				// 2.Business to message processing
				RequestInfo[] sendInfoList = null;
				if (this.requestInfo != null) {
					sendInfoList = this.businessToMessage();
				}
				
				// 3.Send telegram to host
				if (this.requestInfo != null) {					
					if (communication instanceof MultiPartCommunication) {
						LOG.debug("Manutilple Telegarm size is:"+sendInfoList.length);
						((MultiPartCommunication)communication).setSendPartsNum(sendInfoList.length);
						for (int i=0; sendInfoList!=null&&i<sendInfoList.length; i++) { // Send telegram one by one
							LOG.debug("Send the part :["+i+"]");
							this.sendDoc = DOM.newDoc();
							this.sendTelegramToHost(i, (MultiPartCommunication)communication, sendInfoList[i]);
						}
						
						LOG.debug("ALL PART IS SEND!");
						((MultiPartCommunication)communication).send(requestInfo.getRequestId());
					}  else if (communication instanceof GenericCommunication) {
						((GenericCommunication)communication).send(sendTelegram, null);
					}
					else
						LOG.error("Unsupported communcatoin: " + communication.getClass().getName());	
					
					if (communication instanceof BaseCommunication) 
						((BaseCommunication)communication).send();
				}
				
				// 4.Send complete
				this.sendCompleteFlag = true;
			}

			
			if (!"0".equals(this.recvMsgFlag)) {
				// 5.Get recv telegram

				if (communication instanceof MultiPartCommunication)
					recvMultiPartTelegram((MultiPartCommunication) communication);

				else if (communication instanceof GenericCommunication)
					recvSingleTelegram((GenericCommunication) communication);
				else
					LOG.error("Unsupported communcatoin: "
							+ communication.getClass().getName());

				// 13.Message to business processing
				this.messageToBusiness();
			}
		} catch(MessageException e){
			LOG.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(),"Message_Exception", null, e);
		} catch(CommunicationException e){
			LOG.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(),ConstantUtil.MQ_COMMUNICATION_MESSAGE, null, e);
		} catch(Exception e){
			LOG.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage(),"");
		}finally {
			// 14.Release communication
			if (communication != null) {
				if (communication instanceof MultiPartCommunication) 
				((MultiPartCommunication)communication).close();
				else if (communication instanceof GenericCommunication) 
					((GenericCommunication)communication).close();
					
				
			}
		}
	}

	private void recvSingleTelegram(GenericCommunication communication)
			throws com.hitrust.telegram.exception.MessageException,
			MessageException, UtilException, IOException, SAXException {

		this.recvTelegram =  communication
				.receive(requestInfo.getRequestId().getBytes());

		if (this.recvTelegram == null) {
			throw new MessageException("telegram.module.error.recv.null",
					(String[]) null);
		}

		try {
			LOG.info("Recv telegram : "
					+ new String(this.recvTelegram, this.rTelegramEncoding));
		} catch (UnsupportedEncodingException e) {

		}

		// 6.Save recv telegram
		this.saveRecvTelegram();

		// 7.Special process for recv telegram
		this.specialProcessForRecvTelegram();

		// 8.Form temp xml for txt type telegram
		if ("0".equals(this.recvMsgType)) {
			this.formTempDocForTxtTypeTelegram();
		}

		// 9.Format recv telegram to recv document
		this.formatTelegramToRecvDoc();

		// 10.Save recv msg document
		// this.saveRecvMsgDocument(correlationID.getBytes());//mark by Arf
		// 2012/04/17

		LOG.info("Recv Doc : " + new String(DOM.getDocBytes(this.recvDoc)));

		// 11.Fill output parameters with recv document
		ResponseInfo responseInfo = (ResponseInfo) XMLBeanUtil
				.XMLToBean(this.recvDoc)[0];

		// 12.Merge multi reponse info
		this.mergeMultiResponseInfo(responseInfo);

	}
	
	private void recvMultiPartTelegram(MultiPartCommunication communication)
			throws com.hitrust.telegram.exception.MessageException,
			MessageException, UtilException, IOException, SAXException {
		
		communication.receive(requestInfo.getRequestId());
		int recvTelegramCount = communication.getReceivePartsNum();
		// LOG.info("Recv telegram count is : " + recvTelegramCount);

		for (int i = 0; i < recvTelegramCount; i++) {
			this.recvTelegram = ((MultiPartCommunication) communication)
					.receivePart(i);

			if (this.recvTelegram == null) {
				throw new MessageException("telegram.module.error.recv.null",
						(String[]) null);
			}

			try {
				LOG.info("Recv telegram : "
						+ new String(this.recvTelegram, this.rTelegramEncoding));
			} catch (UnsupportedEncodingException e) {

			}

			// 6.Save recv telegram
			this.saveRecvTelegram();

			// 7.Special process for recv telegram
			this.specialProcessForRecvTelegram();

			// 8.Form temp xml for txt type telegram
			if ("0".equals(this.recvMsgType)) {
				this.formTempDocForTxtTypeTelegram();
			}

			// 9.Format recv telegram to recv document
			this.formatTelegramToRecvDoc();

			// 10.Save recv msg document
			// this.saveRecvMsgDocument(correlationID.getBytes());//mark by Arf
			// 2012/04/17

			LOG.info("Recv Doc : " + new String(DOM.getDocBytes(this.recvDoc)));

			// 11.Fill output parameters with recv document
			ResponseInfo responseInfo = (ResponseInfo) XMLBeanUtil
					.XMLToBean(this.recvDoc)[0];

			// 12.Merge multi reponse info
			this.mergeMultiResponseInfo(responseInfo);
		}
	}

	/**
	 * Send telegram to host
	 * @param partNum
	 * @param communication
	 * @param info
	 * @param correlationID
	 * @throws MessageException
	 * @throws UtilException
	 * @throws CommunicationException
	 */
	protected void sendTelegramToHost(int partNum, MultiPartCommunication communication, RequestInfo info) throws MessageException, UtilException, CommunicationException {
		// Put input parameters to send document
		if (this.sendDoc.getDocumentElement() == null) { // Telegram test
			XMLBeanUtil.BeanToXML(this.sendDoc, new Object[]{info}, null);
		}
		
		// Format send document to sending telegram
		this.formatSendDocToTelegram();
		// Special process for send telegram
		this.specialProcessForSendTelegram();

		// Save send msg
		this.saveSendTelegram();

		// Send the telegram
		communication.sendPart(partNum, this.sendTelegram, null);
		//communication.send( correlationID.getBytes());
	}

	/**
	 * Business to message(Send)
	 */
	protected RequestInfo[] businessToMessage() {
		//LOG.debug("businessToMessage start...");
		// 1.Check business is valid
		if (StringUtil.isBlank(this.sBusinessRule)) {
			LOG.info("No need to process business rule(Send)! ");
			return new RequestInfo[]{this.requestInfo};
		}
		// 2.Instance business rule object
		RequestRule rule = (RequestRule) ClassUtil.newInstance(this.sBusinessRule, new Class[]{RequestInfo.class}, new Object[]{this.requestInfo});
		// 3.Business to message
		//LOG.debug("businessToMessage end...");
		return rule.process(this.transactionCode);
		
	}

	/**
	 * Message to business(Recv)
	 */
	protected void messageToBusiness() {
		// 1.Check business is valid
		if (StringUtil.isBlank(this.rBusinessRule)) {
			LOG.info("No need to process business rule(Recv)! ");
			return;
		}
		// 2.Instance business rule object
		ResponseRule rule = (ResponseRule) ClassUtil.newInstance(this.rBusinessRule, new Class[]{RequestInfo.class, ResponseInfo.class}, new Object[]{this.requestInfo, this.responseInfo});
		// 3.Message to business
		rule.process(this.transactionCode);
	}

	/**
	 * Special process for send telegram
	 */
	protected void specialProcessForSendTelegram() {
		// 1.Check interceptor is valid
		if (StringUtil.isBlank(this.sInterceptor)) {
			LOG.info("No need to special process(Send)! ");
			return;
		}
		// 2.Instance interceptor object
		HostRequestInterceptor interceptor = (HostRequestInterceptor) ClassUtil.newInstance(this.sInterceptor, new Class[]{String.class, byte[].class}, new Object[]{this.messageCode, this.sendTelegram});
		// 3.Special process
		this.sendTelegram = interceptor.process();
	}

	/**
	 * Special process for recv telegram
	 */
	protected void specialProcessForRecvTelegram() {
		// 1.Check interceptor is valid
		if (StringUtil.isBlank(this.rInterceptor)) {
			LOG.debug("No need to special process(Recv)! ");
			return;
		}
		// 2.Instance interceptor object
		HostResponseInterceptor interceptor = (HostResponseInterceptor) ClassUtil.newInstance(this.rInterceptor, new Class[]{String.class, byte[].class}, new Object[]{this.messageCode, this.recvTelegram});
		// 3.Special process
		this.recvTelegram = interceptor.process();
	}
	
	/**
	 * Merge multi response info
	 * @param mergeInfo
	 */
	protected void mergeMultiResponseInfo(ResponseInfo mergeInfo) {
		if (StringUtil.isBlank(this.rMergeRule)) {
			//LOG.info("No need to multi merge! ");
			this.responseInfo = mergeInfo;
			return;
		}
		MultiResponseMergeRule mergeRule = (MultiResponseMergeRule) ClassUtil.newInstance(this.rMergeRule, new Class[]{String.class, ResponseInfo.class}, new Object[]{this.messageCode, this.responseInfo});
		this.responseInfo = mergeRule.process(mergeInfo); 
	}

	

	protected void formatSendDocToTelegram() throws MessageException, UtilException {
		if ("0".equals(this.sendMsgType)) {
			//LOG.debug("formatSendDocToTXTTelegram 0");
			this.formatSendDocToTXTTelegram();
		} else {
			//LOG.debug("formatSendDocToXMLTelegram !0");
			this.formatSendDocToXMLTelegram();
		}
	}
	
	private void formatSendDocToXMLTelegram() {
		//LOG.debug("formatSendDocToXMLTelegram start...");
		// 1.Get Header
		try{
		//byte[] headerBytes = FormatorUtil.formatXMLWithXsl(DOM.getDocBytes(this.sendDoc), this.sendHeaderXsl, this.sTelegramEncoding, null);
		
		// 2.Get Body
		byte[] bodyBytes = FormatorUtil.formatXMLWithXsl(DOM.getDocBytes(this.sendDoc), this.sendXsl, this.sTelegramEncoding, null);
		
		// 3.Compose to Send telegram
		//this.sendTelegram = DOM.getDocBytes(FormatorUtil.simpleMergeXMLFile(DOM.loadDoc(headerBytes), DOM.loadDoc(bodyBytes)), this.sTelegramEncoding);
		// JCIC 與第一電文結構不同，無法用原架構分成 header, body
		this.sendTelegram = bodyBytes;

		// 4.Special process
		//LOG.debug("formatSendDocToXMLTelegram STEP 2...["+new String(this.sendTelegram)+"]");
		//try {
			String temp = new String(this.sendTelegram, this.sTelegramEncoding);
			temp = temp.substring(0, temp.indexOf("?>"))+"?>"+"<!DOCTYPE " + this.transactionCode +">" + temp.substring(temp.indexOf("?>")+2);
			this.sendTelegram = temp.getBytes(this.sTelegramEncoding);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new UtilException("Special process error! ", e);
		}	
		//LOG.debug("formatSendDocToXMLTelegram end...");
	}
	
	private void formatSendDocToTXTTelegram() {
		this.sendTelegram = FormatorUtil.formatXMLWithXsl(DOM.getDocBytes(this.sendDoc, this.sTelegramEncoding), this.sendXsl, this.sTelegramEncoding, null);
	}
	
	protected void formTempDocForTxtTypeTelegram() throws com.hitrust.telegram.exception.MessageException {
		TextFormator formator = new TextFormator(this.recvDefFile, this.recvHeaderDefFile, this.rTelegramEncoding);
		this.recvTelegram = formator.format(this.recvTelegram);
	}

	protected void formatTelegramToRecvDoc() throws UtilException, MessageException, IOException, SAXException {
		
		byte[] b = FormatorUtil.formatXMLWithXsl(this.recvTelegram, this.recvXsl, this.rTelegramEncoding, null);
		this.recvDoc = DOM.loadDoc(b);
	}
	
	/**
	 * Save send msg document
	 * @param correlationId
	 */
	protected void saveSendMsgDocument(byte[] correlationId) {
		//LOG.debug("saveSendMsgDocument start...");
		if ("1".equals(this.logMsgDocumentFlag)) {
			String filePath = this.logSendMsgDocumentDir;
			//modify by Arf 2012/04/17
			//String fileName = this.messageCode + "_" + new String(correlationId) + "_send"+ (AppEnv.isTelegramTesingEnvironment()? ("_"+DateUtil.getCurrentTimeMills()):"");
			String fileName = this.messageCode + "_" + new String(correlationId) + "_" + (this.sendTelegramFiles.size()+1) + "_send";
			LOG.info("MQ Send Msg File Name=["+filePath + DateUtil.getToday() + File.separator+fileName+".xml]");
			this.saveFile(filePath, fileName, DOM.getDocBytes(this.sendDoc, this.sTelegramEncoding));
		}
		//LOG.debug("saveSendMsgDocument end...");
	}
		
	/**
	 * Save send telegram
	 * @param correlationId
	 */
	protected void saveSendTelegram() {
		//LOG.debug("saveSendTelegram start...");
		if ("1".equals(this.logHostMsgFlag)) {
			String filePath = this.logSendHostMsgDir;
			//modify by Arf 2012/04/17
			//String fileName = this.messageCode + "_" + new String(correlationId) + "_" + (this.sendTelegramFiles.size()+1) + "_send" + (AppEnv.isTelegramTesingEnvironment()? ("_"+DateUtil.getCurrentTimeMills()):"");
			String fileName = this.messageCode + "_" + requestInfo.getRequestId() + "_send";
			LOG.info("Send Msg File Name=["+filePath + DateUtil.getToday() + File.separator+fileName+".xml]");
			this.sendTelegramFiles.add(this.saveFile(filePath, fileName,this.sendTelegram));
		}
		//LOG.debug("saveSendTelegram end...");
	}
	
	/**
	 * Save recv telegram
	 * @param correlationId
	 */
	protected void saveRecvTelegram() {
		if ("1".equals(this.logHostMsgFlag)) {
			String filePath = this.logRecvHostMsgDir;
			String fileName = this.messageCode + "_" + requestInfo.getRequestId() + "_recv";
			LOG.info("Recv Msg File Name=["+filePath + DateUtil.getToday() + File.separator+fileName+".xml]");
			this.recvTelegramFiles.add(this.saveFile(filePath, fileName,this.recvTelegram));
		}
	}
	
	/**
	 * Save file
	 * @param fileDir
	 * @param fileName
	 * @param content
	 */
	protected String saveFile(String fileDir, String fileName, byte[] content) {
		FileOutputStream fos = null;
		
		String filePath = null;
		try {
			// Set file extention name
			fileName = fileName + ".xml";
			// Check today dir exist
			fileDir = fileDir + DateUtil.getToday() + File.separator;
			File dir= new File(fileDir);
			if (!dir.exists() || !dir.isDirectory()) {
				dir.mkdir();
			} 			
			// Save file
			filePath = fileDir+fileName;
			fos = new FileOutputStream(filePath);
			fos.write(content);
		} catch (Exception e) {
			LOG.error("Save file["+fileDir+fileName+"] with error["+e.getMessage()+"]! ", e);			
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOG.warn("Closing FileOutputStream with error["+e.getMessage()+"]! ");
				}
			}
		}
		
		return filePath;
	}

	// Getter & Setter
	public String getCommunicationCode() {
		return communicationCode;
	}

	public void setCommunicationCode(String communicationCode) {
		this.communicationCode = communicationCode;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	@SuppressWarnings("rawtypes")
	public HashMap getMessageParams() {
		return messageParams;
	}

	public void setMessageParams(@SuppressWarnings("rawtypes") HashMap messageParams) {
		this.messageParams = messageParams;
	}

	public Document getRecvDoc() {
		return recvDoc;
	}

	public void setRecvDoc(Document recvDoc) {
		this.recvDoc = recvDoc;
	}

	public byte[] getRecvTelegram() {
		return recvTelegram;
	}

	public void setRecvTelegram(byte[] recvTelegram) {
		this.recvTelegram = recvTelegram;
	}

	public String getRecvXsl() {
		return recvXsl;
	}

	public void setRecvXsl(String recvXsl) {
		this.recvXsl = recvXsl;
	}

	public String getRTelegramEncoding() {
		return rTelegramEncoding;
	}

	public void setRTelegramEncoding(String telegramEncoding) {
		rTelegramEncoding = telegramEncoding;
	}

	public Document getSendDoc() {
		return sendDoc;
	}

	public void setSendDoc(Document sendDoc) {
		this.sendDoc = sendDoc;
	}

	public byte[] getSendTelegram() {
		return sendTelegram;
	}

	public void setSendTelegram(byte[] sendTelegram) {
		this.sendTelegram = sendTelegram;
	}

	public String getSendXsl() {
		return sendXsl;
	}

	public void setSendXsl(String sendXsl) {
		this.sendXsl = sendXsl;
	}

	public String getSTelegramEncoding() {
		return sTelegramEncoding;
	}

	public void setSTelegramEncoding(String telegramEncoding) {
		sTelegramEncoding = telegramEncoding;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

//	public String getRecvHeaderXsl() {
//		return recvHeaderXsl;
//	}
//
//	public void setRecvHeaderXsl(String recvHeaderXsl) {
//		this.recvHeaderXsl = recvHeaderXsl;
//	}
//
//	public String getSendHeaderXsl() {
//		return sendHeaderXsl;
//	}
//
//	public void setSendHeaderXsl(String sendHeaderXsl) {
//		this.sendHeaderXsl = sendHeaderXsl;
//	}

	public String getRBusinessRule() {
		return rBusinessRule;
	}

	public void setRBusinessRule(String businessRule) {
		rBusinessRule = businessRule;
	}

	public String getSBusinessRule() {
		return sBusinessRule;
	}

	public void setSBusinessRule(String businessRule) {
		sBusinessRule = businessRule;
	}

	public String getRInterceptor() {
		return rInterceptor;
	}

	public void setRInterceptor(String interceptor) {
		rInterceptor = interceptor;
	}

	public String getSInterceptor() {
		return sInterceptor;
	}

	public void setSInterceptor(String interceptor) {
		sInterceptor = interceptor;
	}

	@SuppressWarnings("rawtypes")
	public List getRecvTelegramFiles() {
		return recvTelegramFiles;
	}

	public boolean isSendCompleteFlag() {
		return sendCompleteFlag;
	}

	@SuppressWarnings("rawtypes")
	public List getSendTelegramFiles() {
		return sendTelegramFiles;
	}
}
