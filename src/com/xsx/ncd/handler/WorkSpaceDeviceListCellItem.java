package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsx.ncd.define.DeviceJson;
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
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
	private ObjectMapper mapper = null;

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
        
        mapper = new ObjectMapper();
        queryDeviceServiceListener = (o, oldValue, newValue)->{
        	if(newValue != null){
        		if((newValue.get(0) != null) && newValue.get(0) != 0)
            		deviceReportNumLabel.setText(String.valueOf(newValue.get(0)));
            	else
            		deviceReportNumLabel.setText("");
            	
            	if(newValue.get(1) != null){
            		if(System.currentTimeMillis() - newValue.get(1) > 120000)
            			deviceStatus.setText("离线");
            		else
            			deviceStatus.setText("在线");
            	}
            	else
            		deviceStatus.setText("离线");
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
		mapper = null;
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
				FormBody.Builder requestFormBodyBuilder = new FormBody.Builder();

				requestFormBodyBuilder.add("deviceTypeCode", deviceWorkSpaceItem.getDeviceTypeCode());
				requestFormBodyBuilder.add("deviceId", deviceWorkSpaceItem.getDeviceId());
				
				RequestBody requestBody = requestFormBodyBuilder.build();
				
				Request request = new Request.Builder()
				      .url("http://116.62.108.201:8080/NCDPOCT_Server/QueryThisDeviceNotHandledReportNumAndLastTime")
				      .post(requestBody)
				      .build();
				
				Response response;
				try {
					response = SpringFacktory.GetBean(HttpClientTool.class).getClient().newCall(request).execute();
					
					if(response.isSuccessful()) {
						JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Long.class); 
						List<Long> errorRecordJson = mapper.readValue(response.body().string(), javaType);

						response.body().close();
						
						return errorRecordJson;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
		}
	}
}
