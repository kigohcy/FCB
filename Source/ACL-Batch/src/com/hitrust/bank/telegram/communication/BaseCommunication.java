/*
 * @(#)JcicCommunication.java
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2017/10/11, Bing Lien
 *  1) First release
 */
package com.hitrust.bank.telegram.communication;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitrust.bank.telegram.exception.CommunicationException;
import com.hitrust.communication.GenericCommunication;

public abstract class BaseCommunication extends GenericCommunication implements MultiPartCommunication{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(BaseCommunication.class);
	// Communication environment
	@SuppressWarnings("rawtypes")
	protected HashMap params;
	// Send parts number
	protected int sendPartsNum;
	// Send or Recv parts
	@SuppressWarnings("rawtypes")
	protected List sendMsgPart;
	protected List<byte[]> recvMsgPart;
	
	/**
	 * Constructor
	 * @param params
	 * @throws CommunicationException
	 */
	public BaseCommunication(@SuppressWarnings("rawtypes") HashMap params) throws CommunicationException {
		super(params);
		this.params = params;
		this.init();
	}
	
	/**
	 * Set send parts number
	 * @param partNum
	 */
	public void setSendPartsNum(int partNum) {
		this.sendPartsNum = partNum;
	}
	
	protected abstract void init() throws CommunicationException;
	

	public abstract void send() throws CommunicationException;
	
	public void receive() throws CommunicationException {
		

	}

	public int getReceivePartsNum() throws CommunicationException {
		return recvMsgPart.size();
	}
	
	public byte[] receivePart(int partNum) throws CommunicationException {
		byte[] partData = (byte[]) this.recvMsgPart.get(partNum); 
		return partData!=null&&partData.length==0? null : partData;
	}
	
	public abstract void close() throws CommunicationException;
	
	/* (non-Javadoc)
	 * @see com.hitrust.communication.GenericCommunication#send(byte[], byte[])
	 */
	@Override
	public void send(byte[] arg0, byte[] arg1)
			throws com.hitrust.communication.exception.CommunicationException {
		send();
	}

	
}
