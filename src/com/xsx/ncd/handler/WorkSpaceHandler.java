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
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.DeviceItem;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.tool.HttpClientTool;
import com.xsx.ncd.tool.XsxLog;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class WorkSpaceHandler extends Activity{
	
	@FXML JFXListView<WorkSpaceDeviceListCellItem> DeviceListView;
	@FXML VBox GB_FreshPane;
	@FXML JFXTextField DeviceDepartmentTextField;
	@FXML JFXTextField DeviceIdTextField;
	
	private QueryDeviceService queryDeviceService = null;
	private ChangeListener<List<DeviceItem>> queryDeviceServiceListener = null;
	private ObjectMapper mapper = null;
	
	@Autowired HttpClientTool httpClientTool;
	@Autowired ActivitySession activitySession;
	@Autowired DeviceReportListHandler deviceReportListHandler;
	@Autowired XsxLog xsxLog;
	
	@PostConstruct
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/WorkSpace.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/WorkSpace.fxml");
        loader.setController(this);
        try {
        	this.setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        DeviceDepartmentTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	if(queryDeviceService != null)
        		queryDeviceService.restart();
        });
        
        DeviceIdTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	if(queryDeviceService != null)
        		queryDeviceService.restart();
        });
        
        queryDeviceServiceListener = (o, oldValue, newValue)->{
        	DeviceListView.getItems().clear();
        	
        	if(newValue != null){
        		for (DeviceItem deviceWorkSpaceItem : newValue) {
        			WorkSpaceDeviceListCellItem item = new WorkSpaceDeviceListCellItem(deviceWorkSpaceItem);
        			DeviceListView.getItems().add(item);
        			item.setOnMouseClicked((e)->{
        	        	if(e.getClickCount() == 2){
        	        		activitySession.startActivity(deviceReportListHandler, deviceWorkSpaceItem);
        	        	}
        	        });
        		}
        	}
        };
        
        this.setActivityName("工作台");
        this.setActivityStatus(ActivityStatusEnum.Create);
        xsxLog.info("工作台创建");
        
        AnchorPane.setTopAnchor(this.getRootPane(), 0.0);
        AnchorPane.setBottomAnchor(this.getRootPane(), 0.0);
        AnchorPane.setLeftAnchor(this.getRootPane(), 0.0);
        AnchorPane.setRightAnchor(this.getRootPane(), 0.0);
        
        loader = null;
        in = null;
	}

	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		DeviceDepartmentTextField.setText(null);
		DeviceIdTextField.setText(null);
		
		mapper = new ObjectMapper();
		
		queryDeviceService = new QueryDeviceService();
		queryDeviceService.valueProperty().addListener(queryDeviceServiceListener);
		GB_FreshPane.visibleProperty().bind(queryDeviceService.runningProperty());
		
		queryDeviceService.restart();
		
		xsxLog.info("工作台启动");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		xsxLog.info("工作台暂停");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		queryDeviceService.restart();
		xsxLog.info("工作台恢复");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mapper = null;
		
		queryDeviceService.valueProperty().removeListener(queryDeviceServiceListener);
		GB_FreshPane.visibleProperty().unbind();
		queryDeviceService = null;
		
		for (WorkSpaceDeviceListCellItem item : DeviceListView.getItems()) {
			item.distroyDevice();
		}
		
		DeviceListView.getItems().clear();
		
		xsxLog.info("工作台销毁");
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
