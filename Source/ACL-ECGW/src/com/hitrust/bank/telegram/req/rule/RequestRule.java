/**
 * 
 */
package com.hitrust.bank.telegram.req.rule;

import com.hitrust.bank.telegram.req.RequestInfo;

/**
 * @author bing
 *
 */
public interface RequestRule {
	RequestInfo[] process(String transactionCode);
}
