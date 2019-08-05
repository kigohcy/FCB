/*
 * @(#)JMSProcess.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2007/04/17, Tim Cao
 *   1)first release
 */
package com.hitrust.bank.telegram.communication;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueReceiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.hitrust.bank.telegram.exception.UtilException;
import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;

public class JMSProcess {

	// Log4j
	private static Logger LOG = Logger.getLogger(JMSProcess.class);

	// JMS
	private MQQueueConnectionFactory queueConnectionFactory;
	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private QueueSender queueSender;
	private MQQueueReceiver queueReceiver;
	private Queue sendQueue;
	private Queue recvQueue;

	// MQ Configuration
	private String mqIp;
	private int    mqPort;
	private String mqChannel;
	private int    mqCcsid;
	private String mqUser;
	private String mqPass;
	private String mqQueueManager;
	private String mqSendQName;
	private String mqRecvQName;
	
	private int recvRetryCount;
	private int recvRetryInterval;

	/**
	 * Constructor
	 * @param map
	 */
	public JMSProcess(HashMap map) {
		this.init(map);
	}

	/**
	 * Initial JMS environment
	 * @param parameters
	 * @throws UtilException
	 */
	private void init(HashMap parameters) throws UtilException{
		LOG.info("*********** Initial JMS Environment begin ***********");
		this.mqIp       = (String)parameters.get("MQ_Host");
		this.mqChannel  = (String)parameters.get("MQ_Channel");
		this.mqPort     = Integer.parseInt((String)parameters.get("MQ_Port"));
		this.mqUser     = (String)parameters.get("MQ_UserId");
		this.mqPass     = (String)parameters.get("MQ_Passwd");
		this.mqCcsid    = Integer.parseInt((String)parameters.get("MQ_Ccsid"));
		this.mqRecvQName= (String)parameters.get("MQ_Receive");
		this.mqSendQName= (String)parameters.get("MQ_Send");
		this.recvRetryCount    = Integer.parseInt((String)parameters.get("Recv_Retry_Count"));
		this.recvRetryInterval = Integer.parseInt((String)parameters.get("Recv_Retry_Interval"));
		try {
			// Get connection
			MQEnvironment.properties.put("transport", "MQSeries");
			this.queueConnectionFactory = new MQQueueConnectionFactory();
			this.queueConnectionFactory.setHostName(this.mqIp);
			this.queueConnectionFactory.setChannel(this.mqChannel);
			this.queueConnectionFactory.setPort(this.mqPort);
			this.queueConnectionFactory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
			this.queueConnectionFactory.setCCSID(this.mqCcsid);
			this.queueConnection = this.queueConnectionFactory.createQueueConnection();
			this.queueConnection.start();
			// Create JMS
			this.queueSession = this.queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			this.sendQueue    = this.queueSession.createQueue(this.mqSendQName);
			this.recvQueue    = this.queueSession.createQueue(this.mqRecvQName);
			this.queueSender  = this.queueSession.createSender(this.sendQueue);
			// Pre config
			((MQQueue)this.sendQueue).setTargetClient(JMSC.MQJMS_CLIENT_NONJMS_MQ);
			LOG.info("*********** Initial JMS Environment end ***********");
			LOG.info("HOST=["+this.mqIp+"],Channel=["+this.mqChannel+"],SendQueue=["+this.mqSendQName+"],RecvQueue=["+this.recvQueue+"]");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new UtilException("Initial JMS Environment error!", e);
		}
	}

	/**
	 * Send message
	 * @param message
	 * @param correlationID
	 */
	public void sendMessage(byte[] message, String correlationID) {
		try {
			LOG.info("*********** Send message Begin ***********["+correlationID+"]");
			// 1.Create JMS message
			TextMessage sendMessage = this.queueSession.createTextMessage();
			sendMessage.setJMSReplyTo(this.recvQueue);
			sendMessage.setJMSCorrelationID(correlationID);
			//2012-07-30_JerryChien_因message bytes本身是UTF-8所以先轉UTF-8
			String s = new String(message, "UTF-8");
			//sendMessage.setText(new String(message));
			sendMessage.setText(s);
			//LOG.debug("Sending Message:"+new String(message));
			LOG.debug("Sending Message:"+s);
			// 2.Send message to queue
			this.queueSender.send(sendMessage);
			LOG.info("*********** Send message End ***********");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new UtilException("Send message error! ", e);
		}
	}

	/**
	 * Receive message
	 * @param correlationID
	 * @return
	 */
	public byte[] recvMessage(String correlationID) {
		
		try {
			LOG.info("*********** Recv message Begin ***********["+correlationID+"]");
			// 1.Query condition
			String queryCondition = "JMSCorrelationID='ID:"+toHexString(correlationID.getBytes())+"'";
			//TODO - arf mark retryCount/retryInterval setting 
			// 2.Receive message
			//int retryCount    = Integer.parseInt((String)parameters.get("MQ_CONN_RETRY_COUNT"));
			//int retryInterval = Integer.parseInt((String)parameters.get("MQ_CONN_WAIT_TIME"));
			this.queueReceiver  =(MQQueueReceiver) this.queueSession.createReceiver(this.recvQueue, queryCondition);

			Message recvMessage = null;
			String replyStr = "";
			for (int i=0; i<recvRetryCount; i++) {
				recvMessage = this.queueReceiver.receive(recvRetryInterval);

				if (recvMessage != null) {
					if (recvMessage instanceof JMSTextMessage) {
						if(LOG.isDebugEnabled()) LOG.debug("JMSTextMessage" + recvMessage);
						JMSTextMessage tMsg = (JMSTextMessage) recvMessage;
						replyStr = tMsg.getText();
					} else if (recvMessage instanceof JMSBytesMessage) {
						if(LOG.isDebugEnabled()) LOG.debug("JMSBytesMessage" + recvMessage);
						JMSBytesMessage bMsg = (JMSBytesMessage) recvMessage;
						byte buf[] = new byte[1024 * 1024];
						int length = 0;
						try {
							length = bMsg.readBytes(buf);
							byte[] bArrTemp = new byte[length];
							System.arraycopy(buf, 0, bArrTemp, 0, length);
							replyStr = new String(buf, 0, length, "UTF-8");
						} catch(JMSException e) {
							throw new UtilException("errors.mqClient.readFail", e);
						}
					}
					break;
				} else {
					LOG.error("recMsg is null，EAI沒有回覆訊息");
					LOG.warn("Message not avaiable, Retry count " + (i+1));
				}
			}

			// 3.Return message content
			/**
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			byte data=-1;
			while ((data=recvMessage.readByte()) != -1) {
				byteArray.write(data);
			}**/
			LOG.info("*********** Recv message End ***********");
			LOG.debug(replyStr);
			return  replyStr.getBytes("UTF-8");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new UtilException("Recv message error! ", e);
		}
	}

	/**
	 * Close MQ Manager
	 * @throws UtilException
	 */
	public void close() throws UtilException {
		try {
			LOG.debug("*********** Finalize JMS - begin ***********");
			if (this.queueConnection!=null) {
				if (this.queueSender!=null) this.queueSender.close();
				if (this.queueReceiver!=null) this.queueReceiver.close();
				if (this.queueSession!=null) this.queueSession.close();
				if (this.queueConnection!=null) this.queueConnection.close();
			}
			LOG.debug("*********** Finalize JMS - End ***********");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new UtilException(e.getMessage());
		}
	}

	private static String toHex(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF".charAt(b & 0xf));
	}

	private static String toHexString(byte[] bArr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bArr.length; i++) {
			sb.append(toHex(bArr[i]));
		}
		return sb.toString();
	}
}
