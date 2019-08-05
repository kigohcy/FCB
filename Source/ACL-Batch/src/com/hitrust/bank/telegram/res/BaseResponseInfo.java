package com.hitrust.bank.telegram.res;


public abstract class BaseResponseInfo implements ResponseInfo {
	
	private String responseId;

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}
	

}
