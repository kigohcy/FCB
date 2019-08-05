/**
 * @(#)RaConfig.java
 *
 * Copyright (c) 2013 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/03/18, Ada Chen
 *   1) First release
 */
package com.hitrust.acl.security.ra;

public class RaConfig {
 
	private String raUrl;
	 
	private boolean queue;
	
	private String raAccount;
	
	private String raPhrase;
	 
	private String ipAddress;
	
	private boolean isEncrypt = false;
	
	/**
	 * Constructor
	 * @param raUrl
	 * @param queue
	 * @param raoIpAddress
	 */
	public RaConfig(String raUrl, String raAccount, String raPhrase, String ipAddress) {
		this.raUrl = raUrl;
		this.queue = false;
		this.raAccount = raAccount;
		this.raPhrase = raPhrase;
		this.ipAddress = ipAddress;
	}
	
	/**
	 * Constructor
	 * @param raUrl
	 * @param queue
	 * @param raAccount
	 * @param raPhrase
	 * @param ipAddress
	 * @param isEncrypt
	 */
	public RaConfig(String raUrl, boolean queue, String raAccount, String raPhrase, String ipAddress) {
		this.raUrl = raUrl;
		this.queue = queue;
		this.raAccount = raAccount;
		this.raPhrase = raPhrase;
		this.ipAddress = ipAddress;
	}



	public String getRaUrl() {
		return this.raUrl;
	}
	 
	public boolean isQueue() {
		return this.queue;
	}

	public String getRaAccount() {
		return raAccount;
	}

	public void setRaAccount(String raAccount) {
		this.raAccount = raAccount;
	}

	public String getRaPhrase() {
		return raPhrase;
	}

	public void setRaPhrase(String raPhrase) {
		this.raPhrase = raPhrase;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	
	public boolean isEncrypt() {
		return isEncrypt;
	}
}
 
