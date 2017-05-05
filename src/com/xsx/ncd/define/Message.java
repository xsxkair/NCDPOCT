package com.xsx.ncd.define;

public class Message {
	
	private ServiceEnum what;
	
	private Object obj;

	public Message(ServiceEnum what, Object obj) {
		this.what = what;
		this.obj = obj;
	}

	public ServiceEnum getWhat() {
		return what;
	}

	public void setWhat(ServiceEnum what) {
		this.what = what;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
