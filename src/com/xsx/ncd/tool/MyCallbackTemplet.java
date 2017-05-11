package com.xsx.ncd.tool;

import com.xsx.ncd.define.ServiceEnum;

import okhttp3.Callback;

public interface MyCallbackTemplet extends Callback{
	
	public void setMyServiceEnum(ServiceEnum serviceEnum);
}
