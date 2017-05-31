package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.DeviceJson;
import com.xsx.ncd.define.DeviceReportItem;
import com.xsx.ncd.define.DeviceTypeCodeEnum;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.RecordJson;
import com.xsx.ncd.define.ServiceEnum;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.Duration;

@Component
public class DeviceReportListHandler extends Activity{
	
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
	
	private ObjectProperty<DeviceJson> deviceItemProperty = null;
	
	private QueryDeviceService queryDeviceService = null;
	private ChangeListener<RecordJson<DeviceReportItem>> queryDeviceServiceListener = null;
	private Map<String, String> formParm = null;
	
	
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	@Autowired NCDYGFXYReportHandler ncdYGFXYReportHandler;
	
	@Override
	@PostConstruct
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/DeviceReportList.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/DeviceReportList.fxml");
        loader.setController(this);
        try {
        	this.setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        testTimeColumn.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, Timestamp>("testTime"));
        upTimeColumn.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, Timestamp>("upTime"));
        sampleIdColumn.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("sampleId"));
        operatorNameColumn.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("operatorName"));
        
        DeviceReportTable.setRowFactory(new Callback<TableView<DeviceReportItem>, TableRow<DeviceReportItem>>() {
			
			@Override
			public TableRow<DeviceReportItem> call(TableView<DeviceReportItem> param) {
				// TODO Auto-generated method stub
				TableRow<DeviceReportItem> row = new TableRow<>();
	            row.setOnMouseClicked((e)->{
	            	if(e.getClickCount() == 2){
	            		DeviceJson deviceItem = deviceItemProperty.get();
	            		Map<String, String> reportInfo = new HashMap<>();
	            		reportInfo.put("deviceType", deviceItem.getDeviceTypeCode());
	            		reportInfo.put("reportId", String.valueOf(row.getItem().getId()));
	            		if(deviceItem.getDeviceTypeCode().equals(DeviceTypeCodeEnum.NCD_YGFXY.getTypeCode())){
	            			activitySession.startActivity(ncdYGFXYReportHandler, reportInfo);
	            		}
	            	}
	            });

	            return row ;
			}
		});
        
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

        this.setActivityName("设备报告");
        this.setActivityStatus(ActivityStatusEnum.Create);
        
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
		deviceItemProperty.set((DeviceJson) object);
		
		formParm = new HashMap<>();
		formParm.put("id", String.valueOf(deviceItemProperty.get().getId()));
		
		queryDeviceService = new QueryDeviceService();
		queryDeviceService.setPeriod(Duration.minutes(1));
		queryDeviceService.lastValueProperty().addListener(queryDeviceServiceListener);
		queryDeviceService.restart();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		queryDeviceService.cancel();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		queryDeviceService.restart();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		formParm = null;
		
		queryDeviceService.lastValueProperty().removeListener(queryDeviceServiceListener);
		queryDeviceService = null;
		
		deviceItemProperty.set(null);
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

				return SpringFacktory.GetBean(HttpClientTool.class).myHttpPost(null, ServiceEnum.QueryDeviceReportNotHandled, HttpPostType.SynchronousForm,
						null, formParm);
			}
		}
	}
}
