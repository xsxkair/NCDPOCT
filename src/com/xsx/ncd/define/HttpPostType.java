package com.xsx.ncd.define;

public enum HttpPostType {
	
	AsynchronousJson(true, true),		//异步json
	AsynchronousForm(true, false),		//异步表单
	SynchronousJson(false, true),		//同步json
	SynchronousForm(false, false);		//同步表单
	
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
