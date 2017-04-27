package com.xsx.ncd.handler;

public interface ActivityTemplet {
	
	public void UI_Init();
	
	public void startActivity(Object object);
	
	public void resumeActivity();
	
	public void distroyActivity();

	public String getActivityName();
}
