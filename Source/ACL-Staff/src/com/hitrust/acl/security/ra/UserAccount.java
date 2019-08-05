/*
 * @(#)UserAccount.java
 *
 * Copyright (c) 2013 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2013/10/28, Ada Chen
 *   1) First release
 */
package com.hitrust.acl.security.ra;

public class UserAccount {
 
	private String account;
	private String key1;
	private String phrase;
	private String email;
	private String ip;
	 
	
	/**
	 * @param account 使用者帳號
	 * @param key1 Common Name(憑證識別碼)
	 * @param email
	 * @param ip
	 */
	public UserAccount(String account, String key1, String email, String ip) {
		this.account = account;
		this.key1 = key1;
		this.phrase  = account;
		this.email = email;
		this.ip = ip;
	}
	 
	public String getAccount() {
		return this.account;
	}
	 
	public String getPhrase() {
		return this.phrase;
	}
	 
	public String getEmail() {
		return this.email;
	}
	 
	public String getKey1() {
		return this.key1;
	}

	public String getIp() {
		return ip;
	}


}
 
