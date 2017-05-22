package com.xsx.ncd.define;

public enum HttpPostType {
	
	AsynchronousJson(true, true),		//�첽json
	AsynchronousForm(true, false),		//�첽��
	SynchronousJson(false, true),		//ͬ��json
	SynchronousForm(false, false);		//ͬ����
	
	private final Boolean isAsynchronous;
	private final Boolean isJson;
	
	private HttpPostType(Boolean isAsynchronous, Boolean isJson) {
		this.isAsynchronous = isAsynchronous;
		this.isJson = isJson;
	}

	public Boolean getIsAsynchronous() {
		return isAsynchronous;
	}

	public Boolean getIsJson() {
		return isJson;
	}
	
}
