/*
 * @(#)JcicTelegramBo.java
 * 
 * Copyright (c) 2017 HiTRUST Incorporated. 
 * All rights reserved.
 * 
 * Modify History: 
 * v1.00, 2017/10/11, Bing Lien
 *  1) First release
 */
package com.hitrust.bank.telegram;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.hitrust.bank.telegram.exception.MessageException;
import com.hitrust.bank.telegram.exception.UtilException;
import com.hitrust.bank.telegram.message.BaseMessage;
import com.hitrust.bank.telegram.req.RequestInfo;
import com.hitrust.bank.telegram.res.ResponseInfo;
import com.hitrust.framework.exception.ApplicationException;
import com.hitrust.framework.exception.BusinessException;

public class BaseTelegramBo {

	// Log4J
	/** The LOG. */
	private static final Logger LOG = Logger.getLogger(BaseTelegramBo.class);

	/**
	 * Constructor.
	 */
	public BaseTelegramBo() {

	}

	/**
	 * Send telegram
	 * 
	 * @param msg_code
	 * @param txn_code
	 * @param ResponseInfo
	 * @return
	 * @throws ApplicationException
	 */
	private ResponseInfo sendTelegram(String msg_code, String txn_code,
			RequestInfo requestInfo) throws ApplicationException,
			BusinessException {
		// 1.Only TW need to send telegram
		// if (!APSystem.TW_DISTRICT.equals(APSystem.getSystemDistrict())) {
		// LOG.info("District["+APSystem.getSystemDistrict()+"] do not need to send telegram! ");
		// return null;
		// }
		// 2.New message
		BaseMessage message;
		try {
			LOG.debug("sendTelegram msg_code:" + msg_code);
			LOG.debug("sendTelegram txn_code:" + txn_code);

			message = new BaseMessage(msg_code, txn_code, requestInfo, null);
			LOG.debug("message=" + message);
			message.postTelegram(null);
			ResponseInfo jcicResponseInfo = message.getResponseInfo();

			if (LOG.isDebugEnabled())
				LOG.debug(ReflectionToStringBuilder.toString(jcicResponseInfo));

			// jcicResponseInfo.s
			return jcicResponseInfo;
		} catch (ApplicationException e) {
			LOG.error("ApplicationException:[" + e.toString() + "]");
			throw e;
		} catch (MessageException e) {
			e.printStackTrace();
			// throw new ApplicationException(e.getErrorMsg(), e.getErrorCode(),
			// e.getParameters(), e.getErrorCause());
			LOG.error("MessageException:[" + e.toString() + "]");
			throw new ApplicationException(e.getMessage());
			// } catch (CommunicationException e) {
			// throw new ApplicationException(e.getErrorMsg(), e.getErrorCode(),
			// e.getParameters(), e.getErrorCause());
		} catch (UtilException e) {
			LOG.error("UtilException:[" + e.toString() + "]");
			if (e.getErrorCause() instanceof ApplicationException) {
				throw (ApplicationException) e.getErrorCause();
			} else {
				throw e;
			}
		} catch (BusinessException e) {
			LOG.error("BusinessException:[" + e.toString() + "]", e);
			throw new BusinessException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			LOG.error("Exception:[" + e + "]", e);
			throw new ApplicationException("telegram.module.error.noCode", e);
		}
	}
}