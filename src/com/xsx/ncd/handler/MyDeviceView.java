package com.xsx.ncd.handler;

import java.io.IOException;

import com.jfoenix.controls.JFXSpinner;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.spring.SpringFacktory;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyDeviceView extends VBox implements HttpTemplet{
	
	private Device device = null;
	private ImageView imageView = null;
	
	private ObservableList<Message> myMessagesList = null;

	public MyDeviceView(Device device) {
		super();
		this.device = device;
		this.Init();
	}
	
	private void Init() {
		imageView = new ImageView();
		imageView.setFitWidth(100);
		imageView.setFitHeight(100);
		JFXSpinner jfxSpinner = new JFXSpinner();
		StackPane stackPane = new StackPane(imageView, jfxSpinner);
		
		Label deviceName = new Label(device.getDeviceType().getName()+"("+device.getDeviceType().getModel()+")");
		Label deviceAddr = new Label(device.getAddr());
		VBox vbBox = new VBox(deviceName, deviceAddr);
		
		this.getChildren().addAll(stackPane, vbBox);
		
		jfxSpinner.visibleProperty().bind(imageView.imageProperty().isNull());
		imageView.imageProperty().addListener((o, oldValue, newValue)->{
			if(newValue != null){
				double bi = (newValue.getHeight() / newValue.getWidth());
				imageView.setFitHeight(bi*100);
			}
		});
		
		new Runnable() {	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), 
						device.getDeviceType().getIcon());
				Request request = new Request.Builder()
				      .url("http://116.62.108.201:8080/NCDPOCT_Server/DownloadDeviceIco")
				      .post(body)
				      .build();

				try {
					Response response = SpringFacktory.GetBean(HttpClientTool.class).getClient().newCall(request).execute();
					
					if(response.isSuccessful()) {
						imageView.setImage(new Image(response.body().byteStream()));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					imageView.setImage(null);
				}
			}
		}.run();
		
		myMessagesList = FXCollections.observableArrayList();
        myMessagesList.addListener(new ListChangeListener<Message>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				// TODO Auto-generated method stub
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
			}
        });
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

	@Override
	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		Platform.runLater(()->{
			myMessagesList.add(message);
		});
	}

	@Override
	public void startHttpWork(ServiceEnum serviceEnum, Object parm) {
		// TODO Auto-generated method stub
		if(!SpringFacktory.GetBean(HttpClientTool.class).myHttpAsynchronousPostJson(this, serviceEnum, parm)){
			//GB_FreshPane.setVisible(false);
		}
	}
}
