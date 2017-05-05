package com.xsx.ncd.handler;

import com.xsx.ncd.define.Message;

import javafx.scene.layout.Pane;

public interface ActivityTemplet {
	
	public void UI_Init();
	
	public Pane getActivityRootPane();
	
	public void startActivity(Object object);
	
	public void resumeActivity();
	
	public void distroyActivity();

	public String getActivityName();
	
	public void PostMessageToThisActivity(Message message);
}
