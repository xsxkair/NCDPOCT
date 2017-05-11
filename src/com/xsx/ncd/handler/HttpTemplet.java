package com.xsx.ncd.handler;

import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;

public interface HttpTemplet {

	public void PostMessageToThisActivity(Message message);
	
	public void startHttpWork(ServiceEnum serviceEnum, Object parm);
}
