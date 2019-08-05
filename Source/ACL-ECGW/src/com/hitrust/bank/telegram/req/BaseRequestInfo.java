package com.hitrust.bank.telegram.req;


public  class BaseRequestInfo implements RequestInfo {

	private String requestId;

	/* (non-Javadoc)
	 * @see com.hitrust.fcb.telegram.req.RequestInfo#getRequestId()
	 */
	@Override
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	

}
