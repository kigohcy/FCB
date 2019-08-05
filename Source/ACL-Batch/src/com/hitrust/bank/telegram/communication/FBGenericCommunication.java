/*
 * @(#)GenericCommunication.java
 * 
 * Copyright (c) 2006 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2006/03/22, Tim Cao
 *  1) First release
 */
package com.hitrust.bank.telegram.communication;

import java.util.HashMap;
import java.util.List;

import com.hitrust.bank.telegram.exception.CommunicationException;

public abstract class FBGenericCommunication implements MultiPartCommunication {

	// Communication environment
	protected HashMap params;
	// Send parts number
	protected int sendPartsNum;
	// Send or Recv parts
	protected List sendMsgPart;
	protected List recvMsgPart;
	
	/**
	 * Constructor
	 * @param params
	 * @throws CommunicationException
	 */
	public FBGenericCommunication(HashMap params) throws CommunicationException {
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
	
	public abstract void sendPart(int partNum, byte[] data, byte[] correlationID) throws CommunicationException;
	
	public abstract void send(byte[] correlationID) throws CommunicationException;
	
	public abstract void send(String correlationID) throws CommunicationException;
	
	public abstract void receive(byte[] correlationID) throws CommunicationException;
	
	public abstract void receive(String correlationID) throws CommunicationException;
	
	public abstract int getReceivePartsNum() throws CommunicationException;
	
	public abstract byte[] receivePart(int partNum) throws CommunicationException;
	
	public abstract void close() throws CommunicationException;
	
}
