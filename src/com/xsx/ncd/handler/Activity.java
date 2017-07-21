package com.xsx.ncd.handler;

import java.util.Map;

import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.SpringFacktory;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

public abstract class Activity {
	
	private ActivityStatusEnum activityStatus;
	
	private Pane rootPane;
	
	private String activityName;
	
	private ObservableList<Message> myMessagesList = null;

	public abstract void onCreate();

	public void onStart(Object object){
		myMessagesList = FXCollections.observableArrayList();
	}
	
	public abstract void onPause();
	
	public abstract void onResume();
	
	public void onDestroy(){
		myMessagesList = null;
	}
	
	public Pane getRootPane() {
		return rootPane;
	}

	public void setRootPane(Pane rootPane) {
		this.rootPane = rootPane;
	}

	public ActivityStatusEnum getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(ActivityStatusEnum activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
	public ObservableList<Message> getMyMessagesList() {
		return myMessagesList;
	}

	public void setMyMessagesList(ObservableList<Message> myMessagesList) {
		this.myMessagesList = myMessagesList;
	}

	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		Platform.runLater(()->{
			if(myMessagesList != null)
				myMessagesList.add(message);
		});
	}

	public void startHttpWork(ServiceEnum serviceEnum, HttpPostType httpPostType, Object parm, Map<String, String> formParm,
			Pane pane) {
		
		if(pane != null)
			pane.setVisible(true);
		
		SpringFacktory.GetBean(HttpClientTool.class).myHttpPost(this, serviceEnum, httpPostType, parm, formParm);
	}
}
