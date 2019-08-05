/*
 * @(#)ResponseRule.java
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2017/10/13, Bing Lien
 *  1) First release
 */
package com.hitrust.bank.telegram.res.rule;

/**
 * @author bing
 *
 */
public interface ResponseRule {
	 void process(String transactionCode);
}
