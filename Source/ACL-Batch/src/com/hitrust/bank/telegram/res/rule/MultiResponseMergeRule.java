/*
 * @(#)JcicMultiResponseMergeRule.java
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2017/10/13, Bing Lien
 *  1) First release
 */
package com.hitrust.bank.telegram.res.rule;

import com.hitrust.bank.telegram.res.ResponseInfo;

public interface MultiResponseMergeRule {
	public ResponseInfo process(ResponseInfo mergeInfo);
}
