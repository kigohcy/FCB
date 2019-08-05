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

import org.apache.log4j.Logger;

import com.hitrust.bank.telegram.exception.CommunicationException;
import com.hitrust.bank.telegram.util.StringUtil;
import com.hitrust.communication.GenericCommunication;

public class TestFileCommunication extends GenericCommunication {
	private static final Logger LOG = Logger
			.getLogger(TestFileCommunication.class);

	private String recvFilePath;

	/**
	 * @param params
	 * @throws CommunicationException
	 */
	public TestFileCommunication(@SuppressWarnings("rawtypes") HashMap params) throws CommunicationException {
		super(params);
	}

	protected void init() throws CommunicationException {

		if (params == null)
			return;

		for (Object key : params.keySet()) {
			if (!"class".equals(key)) {
				recvFilePath = (String) params.get(key);
			}
		}

	}

	public void send(byte[] correlationID) throws CommunicationException {
		// Nothing to do
	}

	public void close() throws CommunicationException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitrust.communication.GenericCommunication#send(byte[], byte[])
	 */
	@Override
	public void send(byte[] data, byte[] arg1)
			throws com.hitrust.communication.exception.CommunicationException {
		// sendPart(0, data);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hitrust.communication.GenericCommunication#receive(byte[])
	 */
	@Override
	public byte[] receive(byte[] arg0)
			throws com.hitrust.communication.exception.CommunicationException {

		if (StringUtil.isBlank(recvFilePath))
			return null;

		try {

			ByteArrayOutputStream byteAttr = new ByteArrayOutputStream();
			FileInputStream fis = new FileInputStream(recvFilePath);
			byte data;

			while ((data = (byte) fis.read()) != -1) {
				byteAttr.write(data);
			}

			fis.close();

			return byteAttr.toByteArray();

		} catch (Exception e) {
			LOG.error("Recv telegram error! ");
			LOG.error("Exception : " + e.getMessage());
		}

		return null;
	}

}
