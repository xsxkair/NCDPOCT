package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.define.DeviceItem;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class WorkSpaceHandler implements ActivityTemplet{
	
	private AnchorPane rootPane = null;
	
	@FXML JFXListView<WorkSpaceDeviceListCellItem> DeviceListView;
	@FXML VBox GB_FreshPane;
	@FXML JFXTextField DeviceDepartmentTextField;
	@FXML JFXTextField DeviceIdTextField;
	
	private QueryDeviceService queryDeviceService = null;
	private ChangeListener<List<DeviceItem>> queryDeviceServiceListener = null;
	private ObjectMapper mapper = null;
	
	@Autowired HttpClientTool httpClientTool;
	@Autowired ActivitySession activitySession;
	
	@PostConstruct
	@Override
	public void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/WorkSpace.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/WorkSpace.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        DeviceDepartmentTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	queryDeviceService.restart();
        });
        
        DeviceIdTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	queryDeviceService.restart();
        });
        
        queryDeviceServiceListener = (o, oldValue, newValue)->{
        	DeviceListView.getItems().clear();
        	
        	if(newValue != null){
        		for (DeviceItem deviceWorkSpaceItem : newValue) {
        			DeviceListView.getItems().add(new WorkSpaceDeviceListCellItem(deviceWorkSpaceItem));
        		}
        	}
        		
        };
        
		activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
			if(this.equals(newValue)){
				mapper = new ObjectMapper();
				
				queryDeviceService = new QueryDeviceService();
				queryDeviceService.valueProperty().addListener(queryDeviceServiceListener);
				GB_FreshPane.visibleProperty().bind(queryDeviceService.runningProperty());
				
				queryDeviceService.restart();
			}
			else if(this.equals(oldValue)){
				mapper = null;
				
				queryDeviceService.valueProperty().removeListener(queryDeviceServiceListener);
				GB_FreshPane.visibleProperty().unbind();
				queryDeviceService = null;
			}
		});
		
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}
	
	@Override
	public Pane getActivityRootPane() {
		// TODO Auto-generated method stub
		return rootPane;
	}

	@Override
	public void startActivity(Object object) {
		// TODO Auto-generated method stub
		activitySession.setRootActivity(this);
		activitySession.setFatherActivity(null);
		activitySession.setChildActivity(null);
		activitySession.setActivityPane(this);
	}

	@Override
	public void resumeActivity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distroyActivity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return "¹¤×÷Ì¨";
	}

	class QueryDeviceService extends Service<List<DeviceItem>>{

		@Override
		protected Task<List<DeviceItem>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<List<DeviceItem>>{

			@Override
			protected List<DeviceItem> call() {
				// TODO Auto-generated method stub				
				FormBody.Builder requestFormBodyBuilder = new FormBody.Builder();

				if(DeviceDepartmentTextField.getLength() > 0)
					requestFormBodyBuilder.add("departmentName", DeviceDepartmentTextField.getText());
				
				if(DeviceIdTextField.getLength() > 0)
					requestFormBodyBuilder.add("deviceId", DeviceIdTextField.getText());
				
				RequestBody requestBody = requestFormBodyBuilder.build();
				
				Request request = new Request.Builder()
				      .url("http://116.62.108.201:8080/NCDPOCT_Server/QueryAllDeviceInSample")
				      .post(requestBody)
				      .build();
				
				Response response;
				try {
					response = httpClientTool.getClient().newCall(request).execute();
					
					if(response.isSuccessful()) {
						JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, DeviceItem.class); 
						List<DeviceItem> errorRecordJson = mapper.readValue(response.body().string(), javaType);

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
