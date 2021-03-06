package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xsx.ncd.define.DeviceJson;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.SpringFacktory;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class WorkSpaceDeviceListCellItem extends AnchorPane{

	private DeviceJson deviceWorkSpaceItem = null;
	
	private GridPane RootPane = null;
	@FXML Label departmentNameLabel = null;
	@FXML ImageView deviceImageView = null;
	@FXML Label deviceReportNumLabel = null;
	@FXML Label deviceNameLabel = null;
	@FXML Label deviceId = null;
	@FXML Label deviceStatus = null;
	
	private QueryDeviceService queryDeviceService = null;
	private ChangeListener<List<Long>> queryDeviceServiceListener = null;
	private Map<String, String> formParm = null;

	public WorkSpaceDeviceListCellItem(DeviceJson deviceWorkSpaceItem) {
		super();
		this.deviceWorkSpaceItem = deviceWorkSpaceItem;
		
		onCreate();
	}
	
	private void onCreate(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/WorkSpaceDeviceItemCell.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/WorkSpaceDeviceItemCell.fxml");
        loader.setController(this);
        try {
        	RootPane = loader.load(in);
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		StringBuffer url = new StringBuffer(HttpClientTool.ServiceDeviceIcoUrl);
		
		int lastchart = deviceWorkSpaceItem.getIco().lastIndexOf('/');
		int endIndex = deviceWorkSpaceItem.getIco().length();
		url.append( deviceWorkSpaceItem.getIco().substring(lastchart+1, endIndex));
		
		
		departmentNameLabel.setText(deviceWorkSpaceItem.getDepartmentName());
	     
		deviceImageView.setImage(new Image(url.toString()));
		deviceImageView.imageProperty().addListener((o, oldValue, newValue)->{
			if(newValue != null){
				double bi = (newValue.getHeight() / newValue.getWidth());
				deviceImageView.setFitHeight(bi*100);
			}
		});
		deviceReportNumLabel.visibleProperty().bind(deviceReportNumLabel.textProperty().length().greaterThan(0));
		
		deviceId.setText(deviceWorkSpaceItem.getDeviceId());

		deviceNameLabel.setText(deviceWorkSpaceItem.getName());

		this.getChildren().add(RootPane);
		this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		this.setMouseTransparent(true);
		
		AnchorPane.setTopAnchor(RootPane, 0.0);
        AnchorPane.setBottomAnchor(RootPane, 0.0);
        AnchorPane.setLeftAnchor(RootPane, 0.0);
        AnchorPane.setRightAnchor(RootPane, 0.0);
        
        formParm = new HashMap<>();
        queryDeviceServiceListener = (o, oldValue, newValue)->{
        	if(newValue != null){
        		if((newValue.get(0) != null) && newValue.get(0) != 0)
            		deviceReportNumLabel.setText(String.valueOf(newValue.get(0)));
            	else
            		deviceReportNumLabel.setText("");
            	
            	if(newValue.get(1) != null){
            		if(System.currentTimeMillis() - newValue.get(1) > 120000)
            			deviceStatus.setText("����");
            		else
            			deviceStatus.setText("����");
            	}
            	else
            		deviceStatus.setText("����");
        	}
        };
        
        queryDeviceService = new QueryDeviceService();
        queryDeviceService.lastValueProperty().addListener(queryDeviceServiceListener);
        queryDeviceService.setPeriod(Duration.minutes(1));
        queryDeviceService.start();
	}
	
	public void distroyDevice() {
		queryDeviceService.cancel();
		queryDeviceService.lastValueProperty().removeListener(queryDeviceServiceListener);
		queryDeviceService = null;
		queryDeviceServiceListener = null;
		formParm = null;
	}
	
	class QueryDeviceService extends ScheduledService<List<Long>>{

		@Override
		protected Task<List<Long>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<List<Long>>{

			@Override
			protected List<Long> call() {
				// TODO Auto-generated method stub				
				formParm.clear();
				
				formParm.put("deviceTypeCode", deviceWorkSpaceItem.getDeviceTypeCode());
				formParm.put("deviceId", deviceWorkSpaceItem.getDeviceId());
				
				return SpringFacktory.GetBean(HttpClientTool.class).myHttpPost(null, ServiceEnum.QueryThisDeviceNotHandledReportNumAndLastTime, HttpPostType.SynchronousForm, null, formParm);
			}
		}
	}
}
