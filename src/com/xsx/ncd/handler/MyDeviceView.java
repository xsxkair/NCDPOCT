package com.xsx.ncd.handler;

import com.jfoenix.controls.JFXSpinner;
import com.xsx.ncd.define.DeviceJson;
import com.xsx.ncd.entity.Device;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MyDeviceView extends VBox {
	
	private JFXSpinner jfxSpinner = null;
	private Label deviceName = null;
	private Label deviceAddr = null;
	private VBox vbBox = null;
	private StackPane stackPane = null;
	
	private DeviceJson deviceJson = null;
	private ImageView imageView = null;

	public MyDeviceView(DeviceJson deviceJson) {
		super();
		this.deviceJson = deviceJson;
		this.Init();
	}
	
	private void Init() {
		StringBuffer url = new StringBuffer("http://116.62.108.201:8080/ico/");
		
		int lastchart = deviceJson.getIco().lastIndexOf('/');
		int endIndex = deviceJson.getIco().length();
		url.append(deviceJson.getIco().substring(lastchart+1, endIndex));

		imageView = new ImageView(url.toString());
		imageView.setFitWidth(100);
		jfxSpinner = new JFXSpinner();
		stackPane = new StackPane(imageView, jfxSpinner);
		
		deviceName = new Label(deviceJson.getName()+"("+deviceJson.getModel()+")");
		deviceAddr = new Label(deviceJson.getDeviceAddr());
		vbBox = new VBox(deviceName, deviceAddr);
		
		Long devicetime = deviceJson.getLastTime();
		long currenttime = System.currentTimeMillis();

		if((devicetime == null) || ((currenttime > devicetime) && (currenttime - devicetime > 120000))){
			this.setStyle("-fx-background-color:  grey");
		}
		else {
			this.setStyle("-fx-background-color:  lightblue");
		}
		
		this.getChildren().addAll(stackPane, vbBox);
		
		jfxSpinner.visibleProperty().bind(imageView.imageProperty().isNull());
		imageView.imageProperty().addListener((o, oldValue, newValue)->{
			if(newValue != null){
				double bi = (newValue.getHeight() / newValue.getWidth());
				imageView.setFitHeight(bi*100);
			}
		});

        url = null;
	}
	

	public void distroyDevice(){
		jfxSpinner = null;
		deviceName = null;
		deviceAddr = null;
		vbBox = null;
		stackPane = null;
		
		deviceJson = null;
	}
}
