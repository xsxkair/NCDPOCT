package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.DeviceReportItem;
import com.xsx.ncd.define.DeviceTypeJson;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.RecordJson;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

@Component
public class ReportQueryHandler extends Activity {

	@FXML JFXComboBox<DeviceTypeJson> deviceTypeCombobox;
	@FXML JFXTextField GB_TestDeviceFilterTextField;
	@FXML JFXTextField GB_TestItemFilterTextfield;
	@FXML JFXDatePicker GB_TestTimeFilterDateChoose;
	@FXML JFXTextField GB_TesterFilterTextfield;
	@FXML JFXTextField GB_TestSampleFilterTextField;
	
	@FXML TableView<DeviceReportItem> GB_TableView;
	@FXML TableColumn<DeviceReportItem, Integer> TableColumn1;
	@FXML TableColumn<DeviceReportItem, String> TableColumn2;
	@FXML TableColumn<DeviceReportItem, Timestamp> TableColumn3;
	@FXML TableColumn<DeviceReportItem, Float> TableColumn4;
	@FXML TableColumn<DeviceReportItem, String> TableColumn5;
	@FXML TableColumn<DeviceReportItem, String> TableColumn6;
	@FXML TableColumn<DeviceReportItem, String> TableColumn7;
	@FXML TableColumn<DeviceReportItem, String> TableColumn8;
	
	@FXML Pagination GB_Pagination;
	@FXML VBox GB_FreshPane;
	
	private Map<String, Object> activityParm = null;
	private Map<String, String> formParm = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	private QueryReportService queryReportService = null;
	private ChangeListener<RecordJson<DeviceReportItem>> queryDeviceReportServiceListener = null;
	private Map<String, String> reportInfo = new HashMap<>();
	
	@Autowired HttpClientTool httpClientTool;
	@Autowired NCDYGFXYReportHandler nCDYGFXYReportHandler;
	@Autowired ActivitySession activitySession;
	
	@PostConstruct
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/ReportListPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/ReportListPage.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        TableColumn1.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, Integer>("index"));
    	TableColumn2.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("item"));
    	TableColumn3.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, Timestamp>("testTime"));
    	TableColumn4.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, Float>("result"));
    	TableColumn5.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("operatorName"));
    	TableColumn6.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("deviceId"));
    	TableColumn7.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("sampleId"));
    	TableColumn8.setCellValueFactory(new PropertyValueFactory<DeviceReportItem, String>("reportStatus"));
    	
    	GB_TableView.setRowFactory(new Callback<TableView<DeviceReportItem>, TableRow<DeviceReportItem>>() {
			
			@Override
			public TableRow<DeviceReportItem> call(TableView<DeviceReportItem> param) {
				// TODO Auto-generated method stub
				TableRow<DeviceReportItem> row = new TableRow<>();
	            row.setOnMouseClicked((e)->{
	            	if(e.getClickCount() == 2){
	            		reportInfo.clear();
	            		reportInfo.put("deviceType", deviceTypeCombobox.getValue().getCode());
	            		reportInfo.put("reportId", String.valueOf(row.getItem().getId()));
	            		activitySession.startActivity(nCDYGFXYReportHandler, reportInfo);
	            	}
	            });

	            return row ;
			}
		});
    	
    	deviceTypeCombobox.valueProperty().addListener((e)->{
    		queryReportService.restart();
    	});
    	
    	GB_TestDeviceFilterTextField.lengthProperty().addListener((e)->{
    		queryReportService.restart();
    	});;
    	GB_TestItemFilterTextfield.lengthProperty().addListener((e)->{
    		queryReportService.restart();
    	});;
    	GB_TestTimeFilterDateChoose.valueProperty().addListener((e)->{
    		queryReportService.restart();
    	});;
    	GB_TesterFilterTextfield.lengthProperty().addListener((e)->{
    		queryReportService.restart();
    	});;
    	GB_TestSampleFilterTextField.lengthProperty().addListener((e)->{
    		queryReportService.restart();
    	});;
    	
    	GB_Pagination.currentPageIndexProperty().addListener((o, oldValue, newValue)->{
    		queryReportService.restart();
        });
    	
    	queryDeviceReportServiceListener = (o, oldValue, newValue)->{
         	if(newValue != null){
         		GB_Pagination.setPageCount(newValue.getTotalPageNum());
             	
         		GB_TableView.getItems().setAll(newValue.getRecords());
         	}
         	else{
         		GB_Pagination.setPageCount(-1);
             	
         		GB_TableView.getItems().clear();
         	}
         		
         };
        
        myMessageListChangeListener = c ->{
        	while(c.next()){
				if(c.wasAdded()){
					for (Message message : c.getAddedSubList()) {
						switch (message.getWhat()) {
							case QueryAllDeviceTypeJson:
								List<DeviceTypeJson> deviceTypeJsonLists = message.getObj();
								deviceTypeCombobox.getItems().setAll(deviceTypeJsonLists);
								deviceTypeCombobox.getSelectionModel().selectFirst();
								break;
							default:
								break;
						}
					}
				}
			}
        };
        
        this.setActivityName("报告管理");
        this.setActivityStatus(ActivityStatusEnum.Create);
        
        AnchorPane.setTopAnchor(getRootPane(), 0.0);
        AnchorPane.setBottomAnchor(getRootPane(), 0.0);
        AnchorPane.setLeftAnchor(getRootPane(), 0.0);
        AnchorPane.setRightAnchor(getRootPane(), 0.0);
        
        loader = null;
        in = null;
	}

	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		super.onStart(object);

		activityParm = (Map<String, Object>) object;
		
		formParm = new HashMap<>();
		
		queryReportService = new QueryReportService();
		queryReportService.valueProperty().addListener(queryDeviceReportServiceListener);
		GB_FreshPane.visibleProperty().bind(queryReportService.runningProperty());
		
		getMyMessagesList().addListener(myMessageListChangeListener);
		
		if(activityParm == null)
			startHttpWork(ServiceEnum.QueryAllDeviceTypeJson, HttpPostType.AsynchronousJson, null, null, null);
		else{
			DeviceTypeJson deviceTypeJson = (DeviceTypeJson) activityParm.get("DeviceType");
			String deviceId = (String) activityParm.get("DeviceId");
			
			deviceTypeCombobox.getItems().setAll(deviceTypeJson);
			deviceTypeCombobox.getSelectionModel().selectFirst();
			GB_TestDeviceFilterTextField.setText(deviceId);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		queryReportService.valueProperty().removeListener(queryDeviceReportServiceListener);
		queryReportService = null;
		formParm = null;
		GB_FreshPane.visibleProperty().unbind();
		getMyMessagesList().removeListener(myMessageListChangeListener);
		
		super.onDestroy();
	}

	class QueryReportService extends Service<RecordJson<DeviceReportItem>>{

		@Override
		protected Task<RecordJson<DeviceReportItem>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<RecordJson<DeviceReportItem>>{

			@Override
			protected RecordJson<DeviceReportItem> call() {
				// TODO Auto-generated method stub				

				formParm.clear();

				//如果设备类型不选，则不查询数据
				if(deviceTypeCombobox.getValue() != null)
					formParm.put("deviceTypeId", deviceTypeCombobox.getValue().getId().toString());
				else
					return null;

				if(GB_TestDeviceFilterTextField.getLength() > 0)
					formParm.put("deviceDid", GB_TestDeviceFilterTextField.getText());
				
				if(GB_TestItemFilterTextfield.getLength() > 0)
					formParm.put("itemName", GB_TestItemFilterTextfield.getText());
				
				if(GB_TestTimeFilterDateChoose.getValue() != null)
					formParm.put("testTime", GB_TestTimeFilterDateChoose.getValue().toString());
				
				if(GB_TesterFilterTextfield.getLength() > 0)
					formParm.put("operatorName", GB_TesterFilterTextfield.getText());
				
				if(GB_TestSampleFilterTextField.getLength() > 0)
					formParm.put("sampleId", GB_TestSampleFilterTextField.getText());
				
				formParm.put("startIndex", String.valueOf(GB_Pagination.getCurrentPageIndex()));
				formParm.put("size", String.valueOf(50));
				
				return httpClientTool.myHttpPost(null, ServiceEnum.QueryReportJsonByFilter, HttpPostType.SynchronousForm, null, formParm);
			}
		}
	}
}
