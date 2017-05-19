package com.xsx.ncd.handler;

import javafx.scene.layout.Pane;

public interface ActivityTemplet {
	
	public void onCreate();

	public void onStart(Object object);
	
	public void onResume();
	
	public void onDestroy();
	
	public void onPause();

	public String getActivityName();
	
	public Pane getActivityRootPane();
}
