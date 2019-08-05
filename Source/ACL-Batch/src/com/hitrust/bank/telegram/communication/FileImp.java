/*
 * @(#)FileImp.java
 * 
 * Copyright (c) 2006 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2006/03/07, Tim Cao
 *  1) First release
 */
package com.hitrust.bank.telegram.communication;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.hitrust.bank.telegram.exception.CommunicationException;
import com.hitrust.bank.telegram.util.StringUtil;
import com.hitrust.util.DOM;

public class FileImp extends FBGenericCommunication {
	private static Category LOG=Category.getInstance(FileImp.class);
	
	private String txId = null;
	private String budgetType = null;
	
	/**
	 * @param params
	 * @throws CommunicationException
	 */
	public FileImp(HashMap params) throws CommunicationException {
		super(params);
	}

	protected void init() throws CommunicationException {
		
	}

	public void sendPart(int partNum, byte[] data, byte[] correlationID) throws CommunicationException {
		try {
			this.txId = this.getElementValueByName(data, "TxID");
			this.budgetType = this.getElementValueByName(data, "BudgetType");
			this.budgetType = this.budgetType!=null&&this.budgetType.length()>=1? this.budgetType.substring(0,1) : this.budgetType;
		} catch (Exception e) {
			LOG.error("Send telegram error! ");
			LOG.error("Exception : " + e.getMessage());
		}	
	}
	
	private String getElementValueByName(byte[] data, String name) {
		Document doc = DOM.loadDoc(data);
		NodeList nodeList = doc.getElementsByTagName(name);
		if (nodeList!=null&&nodeList.getLength()>0) {
			return nodeList.item(0).getFirstChild()!=null? nodeList.item(0).getFirstChild().getNodeValue() : null;				
		} else {
			LOG.warn(name + " is null! ");
		}
		return null;
	}

	public void send(byte[] correlationID) throws CommunicationException {
		
	}
	

	public int getReceivePartsNum() throws CommunicationException {
		return 1;
	}

	public byte[] receivePart(int partNum) throws CommunicationException {
		try {
			if (!StringUtil.isBlank(this.txId)) {
				String recvFilePath = (String) this.params.get(this.txId+(StringUtil.isBlank(this.budgetType)? "":("_"+this.budgetType)));
				if (!StringUtil.isBlank(recvFilePath)) {
					ByteArrayOutputStream byteAttr = new ByteArrayOutputStream();
					FileInputStream fis = new FileInputStream(recvFilePath);
					byte data;
					while ((data=(byte) fis.read()) != -1) {
						byteAttr.write(data);
					}
					fis.close();
					
					return byteAttr.toByteArray();
				}
			}
		} catch (Exception e) {
			LOG.error("Recv telegram error! ");
			LOG.error("Exception : " + e.getMessage());
		}
		return null;
	}
	
	public void close() throws CommunicationException {
		
	}


	@Override
	public void send(String correlationID) throws CommunicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receive(String correlationID) throws CommunicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receive(byte[] correlationID) throws CommunicationException {
		// TODO Auto-generated method stub
		
	}
}
