package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsx.ncd.define.DeviceItem;
import com.xsx.ncd.define.DeviceReportItem;
import com.xsx.ncd.define.ErrorRecordItem;
import com.xsx.ncd.define.RecordJson;
import com.xsx.ncd.handler.WorkSpaceDeviceListCellItem.QueryDeviceService;
import com.xsx.ncd.handler.WorkSpaceDeviceListCellItem.QueryDeviceService.MyTask;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.SpringFacktory;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class DeviceReportListHandler implements ActivityTemplet{

	private AnchorPane rootPane = null;
	
	@FXML ImageView deviceImageView;
	@FXML Label deviceReportNumLabel;
	@FXML Label deviceIdLabel;
	@FXML Label deviceNameLabel;
	@FXML Label deviceAddrLabel;
	@FXML Label deviceStatusLabel;
	
	@FXML TableView<DeviceReportItem> DeviceReportTable;
	@FXML TableColumn<DeviceReportItem, Timestamp> testTimeColumn;
	@FXML TableColumn<DeviceReportItem, Timestamp> upTimeColumn;
	@FXML TableColumn<DeviceReportItem, String> sampleIdColumn;
	@FXML TableColumn<DeviceReportItem, String> operatorNameColumn;
	
	private ObjectProperty<DeviceItem> deviceItemProperty = null;
	
	private QueryDeviceService queryDeviceService = null;
	private ChangeListener<RecordJson<DeviceReportItem>> queryDeviceServiceListener = null;
	private ObjectMapper mapper = null;
	
	
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	
	@Override
	@PostConstruct
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/DeviceReportList.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/DeviceReportList.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        testTimeColumn.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, Timestamp>("testTime"));
        upTimeColumn.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, Timestamp>("upTime"));
        sampleIdColumn.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("sampleId"));
        operatorNameColumn.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("operatorName"));
        
        deviceImageView.imageProperty().addListener((o, oldValue, newValue)->{
			if(newValue != null){
				deviceImageView.setFitHeight(newValue.getHeight());
				deviceImageView.setFitWidth(newValue.getWidth());
			}
		});
        
        deviceReportNumLabel.visibleProperty().bind(deviceReportNumLabel.textProperty().length().greaterThan(0));
    	
        deviceItemProperty = new SimpleObjectProperty<>();
        deviceItemProperty.addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		StringBuffer url = new StringBuffer(HttpClientTool.ServiceDeviceIcoUrl);
        		int lastchart = newValue.getIco().lastIndexOf('/');
        		int endIndex = newValue.getIco().length();
        		url.append( newValue.getIco().substring(lastchart+1, endIndex));
        		deviceImageView.setImage(new Image(url.toString()));
        		
        		deviceIdLabel.setText(newValue.getDeviceId());
        		deviceNameLabel.setText(newValue.getName());
        		deviceAddrLabel.setText(newValue.getDeviceAddr());
        		
        	}
        });
        
        queryDeviceServiceListener = (o, oldValue, newValue)->{
        	if(newValue != null){
        		
        		DeviceReportTable.getItems().setAll(newValue.getRecords());
            	
            	if(newValue.getParm1() != null){
            		if(System.currentTimeMillis() - newValue.getParm1() > 120000)
            			deviceStatusLabel.setText("离线");
            		else
            			deviceStatusLabel.setText("在线");
            	}
            	else
            		deviceStatusLabel.setText("离线");
        	}
        };

        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
			if(this.equals(newValue)){
				mapper = new ObjectMapper();
				
				queryDeviceService = new QueryDeviceService();
				queryDeviceService.setPeriod(Duration.minutes(1));
				queryDeviceService.lastValueProperty().addListener(queryDeviceServiceListener);
				queryDeviceService.restart();
			}
			else if(this.equals(oldValue)){
				mapper = null;
				
				queryDeviceService.lastValueProperty().removeListener(queryDeviceServiceListener);
				queryDeviceService = null;
				
				deviceItemProperty.set(null);
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
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		deviceItemProperty.set((DeviceItem) object);
		activitySession.setActivityPane(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return "设备报告";
	}
	
	class QueryDeviceService extends ScheduledService<RecordJson<DeviceReportItem>>{

		@Override
		protected Task<RecordJson<DeviceReportItem>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<RecordJson<DeviceReportItem>>{

			@Override
			protected RecordJson<DeviceReportItem> call() {
				// TODO Auto-generated method stub				
				FormBody.Builder requestFormBodyBuilder = new FormBody.Builder();

				requestFormBodyBuilder.add("id", String.valueOf(deviceItemProperty.get().getId()));
				
				RequestBody requestBody = requestFormBodyBuilder.build();
				
				Request request = new Request.Builder()
				      .url("http://116.62.108.201:8080/NCDPOCT_Server/QueryDeviceReportNotHandled")
				      .post(requestBody)
				      .build();
				
				Response response;
				try {
					response = SpringFacktory.GetBean(HttpClientTool.class).getClient().newCall(request).execute();
					
					if(response.isSuccessful()) {

						JavaType javaType = mapper.getTypeFactory().constructParametricType(RecordJson.class, DeviceReportItem.class); 
						RecordJson<DeviceReportItem> errorRecordJson = mapper.readValue(response.body().string(), javaType);

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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

}
