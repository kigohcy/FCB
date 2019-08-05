/**
 * 
 */
package com.hitrust.bank.telegram.communication;

import com.hitrust.bank.telegram.exception.CommunicationException;

/**
 * @author bing
 *
 */
public interface MultiPartCommunication {
	/**
	 * Set send parts number
	 * @param partNum
	 */
	void setSendPartsNum(int partNum) ;
	void sendPart(int partNum, byte[] data, byte[] verifyId) throws CommunicationException ;
	int getReceivePartsNum() throws CommunicationException ;
	byte[] receivePart(int partNum) throws CommunicationException;
	void close() throws CommunicationException;
	void send(String correlationID) throws CommunicationException ;
	void receive(String correlationID) throws CommunicationException;
}
