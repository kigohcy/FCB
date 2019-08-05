/*
 * @(#)JMSImp.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 * v1.00, 2007/04/18, Tim Cao
 *  1) First release
 */

package com.hitrust.bank.telegram.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitrust.bank.telegram.communication.JMSProcess;
import com.hitrust.bank.telegram.exception.CommunicationException;
import com.hitrust.bank.telegram.exception.UtilException;


public class JMSImp extends	FBGenericCommunication{

	private static Logger LOG=Logger.getLogger(JMSImp.class);
	
	private JMSProcess process;
	
	// Send parts number
	protected int sendPartsNum;
	// Send or Recv parts
	protected List sendMsgPart;
	protected List recvMsgPart;

	/**
	 * @param params
	 * @throws CommunicationException
	 */
	public JMSImp(HashMap params) throws CommunicationException {
		super(params);
	}

	protected void init() {
		//try {
			this.process = new JMSProcess(super.params);
//		} catch (UtilException e) {
//			throw new CommunicationException(e.getMessage(), "CMU04");
//		}
	}
	
	public void sendPart(int partNum, byte[] data, byte[] correlationID) throws CommunicationException {
		if (this.sendMsgPart == null) {
			this.sendMsgPart = new ArrayList();
		}
		this.sendMsgPart.add(data);
	}

	public void send(byte[] correlationID) throws CommunicationException {
		send(new String(correlationID));
	}
	
	public void send(String correlationID) throws CommunicationException {
		LOG.debug("Arf debug - before send - correlationID=["+correlationID+"]");
		try {
			for (int i=0; i<this.sendMsgPart.size(); i++) {
				this.process.sendMessage((byte[]) this.sendMsgPart.get(i), correlationID);	
			}
		} catch (UtilException e) {
			e.printStackTrace();
			throw new CommunicationException(e.getMessage(), "CMU05");
		}
		LOG.debug("Arf debug - after send");
	}
	
	public  byte[] receivePart(byte[] correlationID) {
		receivePart(correlationID);
		return receivePart(0);
	}
	
//	public byte[] receive(byte[] correlationID){
//		return null;
//	}
	public void receive(byte[] correlationID) throws CommunicationException{
		receive(new String(correlationID));
	}
	
	public void receive(String correlationID) throws CommunicationException{
		try {
			this.recvMsgPart = new ArrayList();
			
			this.recvMsgPart.add(this.process.recvMessage(correlationID));
		} catch (UtilException e) {
			throw new CommunicationException(e.getMessage(), "CMU06");
		}
	}

	public int getReceivePartsNum() throws CommunicationException {
		if (recvMsgPart != null)
			return this.recvMsgPart.size();
		else
			return 0;
	}

	public byte[] receivePart(int partNum) throws CommunicationException {
		LOG.debug("Arf debug - receivePart - partNum=["+partNum+"]");
		byte[] partData = (byte[]) this.recvMsgPart.get(partNum); 
		return partData!=null&&partData.length==0? null : partData;
	}
	
	public void close() {
		//try {
			this.process.close();
//		} catch (UtilException e) {
//			throw new CommunicationException(e.getMessage(), "CMU04");
//		}
	}


}
