package com.xsx.ncd.handler;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.jfoenix.controls.JFXSpinner;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.define.UserFilePath;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.spring.SpringFacktory;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DepartmentDeviceListHandler extends AnchorPane implements HttpTemplet{
	
	private AnchorPane rootPane = null;
	
	@FXML Label DepartmentNameLabel;
	@FXML ImageView AddDeviceImageView;
	@FXML FlowPane DeviceListFlowPane;
	
	private Department departmentData = null;
	private DeviceManageHandler fatherActivity = null;
	private QueryThisDepartmentAllDeviceService queryThisDepartmentAllDeviceService = null;
	
	private ObservableList<Message> myMessagesList = null;
	
	public DepartmentDeviceListHandler(Department department, DeviceManageHandler fatherActivity){
		departmentData = department;
		this.fatherActivity = fatherActivity;
		this.UI_Init();
	}
	
	private void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/DepartmentDeviceList.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/DepartmentDeviceList.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        DepartmentNameLabel.setText(this.departmentData.getName());
        this.getChildren().add(rootPane);

        AddDeviceImageView.setOnMouseClicked((e)->{
        	fatherActivity.showAddDeviceDialog(departmentData);
        });
        
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
									showAllDevice(message.getObj(List.class));
									break;
									
								default:
									break;
							}
						}
					}
				}
			}
        });
        
        queryThisDepartmentAllDeviceService = new QueryThisDepartmentAllDeviceService();
        queryThisDepartmentAllDeviceService.setPeriod(Duration.minutes(5));
        queryThisDepartmentAllDeviceService.start();
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}

	public Pane getActivityRootPane() {
		// TODO Auto-generated method stub
		return rootPane;
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
	
	private void showAllDevice(List<Device> devices){
		DeviceListFlowPane.getChildren().clear();
		for (Device device : devices) {
			MyDeviceView deviceView = new MyDeviceView(device);
			DeviceListFlowPane.getChildren().add(deviceView);
		}
	}
	
	class QueryThisDepartmentAllDeviceService extends ScheduledService<Void>{

		@Override
		protected Task<Void> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}

		class MyTask extends Task<Void>{

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				startHttpWork(ServiceEnum.QueryThisDepartmentAllDeviceList, departmentData);
				return null;
			}	
		}
	}
}
