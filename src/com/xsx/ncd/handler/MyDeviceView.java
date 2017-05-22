package com.xsx.ncd.handler;

import com.jfoenix.controls.JFXSpinner;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Device;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MyDeviceView extends VBox {
	
	private JFXSpinner jfxSpinner = null;
	private Label deviceName = null;
	private Label deviceAddr = null;
	private VBox vbBox = null;
	private StackPane stackPane = null;
	
	private Device device = null;
	private ImageView imageView = null;
	
	private ObservableList<Message> myMessagesList = null;
	private ListChangeListener<Message> myMessageListeren = null;

	public MyDeviceView(Device device) {
		super();
		this.device = device;
		this.Init();
	}
	
	private void Init() {
		StringBuffer url = new StringBuffer("http://116.62.108.201:8080/ico/");
		
		int lastchart = device.getDeviceType().getIcon().lastIndexOf('/');
		int endIndex = device.getDeviceType().getIcon().length();
		url.append(device.getDeviceType().getIcon().substring(lastchart+1, endIndex));

		imageView = new ImageView(url.toString());
		imageView.setFitWidth(100);
		jfxSpinner = new JFXSpinner();
		stackPane = new StackPane(imageView, jfxSpinner);
		
		deviceName = new Label(device.getDeviceType().getName()+"("+device.getDeviceType().getModel()+")");
		deviceAddr = new Label(device.getAddr());
		vbBox = new VBox(deviceName, deviceAddr);
		
		this.getChildren().addAll(stackPane, vbBox);
		
		jfxSpinner.visibleProperty().bind(imageView.imageProperty().isNull());
		imageView.imageProperty().addListener((o, oldValue, newValue)->{
			if(newValue != null){
				double bi = (newValue.getHeight() / newValue.getWidth());
				imageView.setFitHeight(bi*100);
			}
		});
		
		myMessageListeren = (javafx.collections.ListChangeListener.Change<? extends Message> c)->{
			while(c.next()){
				if(c.wasAdded()){

					for (Message message : c.getAddedSubList()) {
						switch (message.getWhat()) {
							case QueryThisDepartmentAllDeviceList:
								
								break;
								
							default:
								break;
						}
					}
				}
			}
		};
		myMessagesList = FXCollections.observableArrayList();
        myMessagesList.addListener(myMessageListeren);
        
        url = null;
	}
	
	class QueryDeviceStatusService extends ScheduledService<Void>{

		@Override
		protected Task<Void> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}

		class MyTask extends Task<Void>{

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				//startHttpWork(ServiceEnum.QueryThisDepartmentAllDeviceList, departmentData);
				return null;
			}	
		}
	}

	public void distroyDevice(){
		jfxSpinner = null;
		deviceName = null;
		deviceAddr = null;
		vbBox = null;
		stackPane = null;
		
		myMessagesList.removeListener(myMessageListeren);
		myMessageListeren = null;
		myMessagesList = null;
		
		device = null;
	}
}
