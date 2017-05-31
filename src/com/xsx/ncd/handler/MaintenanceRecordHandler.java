package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.MaintenanceRecordItem;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.RecordJson;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

@Component
public class MaintenanceRecordHandler extends Activity {
	
	@FXML JFXDatePicker MaintenanceDateChoose;
	@FXML JFXTextField MaintenanceOperatorTextfield;
	@FXML JFXTextField MaintenanceDeviceTextField;
	@FXML JFXTextField MaintenanceResultTextField;
	
	@FXML TableView<MaintenanceRecordItem> MaintenanceTableView;
	@FXML TableColumn<MaintenanceRecordItem, Timestamp> MaintenanceTimeTableColumn;
	@FXML TableColumn<MaintenanceRecordItem, String> MaintenanceDeviceTableColumn;
	@FXML TableColumn<MaintenanceRecordItem, String> MaintenanceOperatorTableColumn;
	@FXML TableColumn<MaintenanceRecordItem, String> MaintenanceResultTableColumn;
	@FXML TableColumn<MaintenanceRecordItem, String> MaintenanceDescTableColumn;
	
	@FXML Pagination GB_Pagination;
	@FXML VBox GB_FreshPane;

	private String deviceId = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	private Map<String, String> formParm = null;
	private QueryErrorRecordService queryErrorRecordService = null;
	private ChangeListener<RecordJson<MaintenanceRecordItem>> queryErrorRecordServiceListener = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	
	@PostConstruct
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/MaintenanceRecord.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/MaintenanceRecord.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        MaintenanceTimeTableColumn.setCellValueFactory(new PropertyValueFactory<MaintenanceRecordItem, Timestamp>("testtime"));
        MaintenanceDeviceTableColumn.setCellValueFactory(new PropertyValueFactory<MaintenanceRecordItem, String>("deviceId"));
        MaintenanceOperatorTableColumn.setCellValueFactory(new PropertyValueFactory<MaintenanceRecordItem, String>("userName"));
        MaintenanceResultTableColumn.setCellValueFactory(new PropertyValueFactory<MaintenanceRecordItem, String>("result"));
        MaintenanceDescTableColumn.setCellValueFactory(new PropertyValueFactory<MaintenanceRecordItem, String>("dsc"));
        
        MaintenanceDateChoose.valueProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
        MaintenanceOperatorTextfield.lengthProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
        MaintenanceDeviceTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
        MaintenanceResultTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
        GB_Pagination.currentPageIndexProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
        queryErrorRecordServiceListener = (o, oldValue, newValue)->{
        	if(newValue != null){
        		GB_Pagination.setPageCount(newValue.getTotalPageNum());
            	
        		MaintenanceTableView.getItems().setAll(newValue.getRecords());
        	}
        	else{
        		GB_Pagination.setPageCount(-1);
            	
        		MaintenanceTableView.getItems().clear();
        	}
        		
        };
        
        myMessageListChangeListener = c -> {
        	GB_FreshPane.setVisible(false);
        };
        
        this.setActivityName("±£Ñø¼ÇÂ¼");
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

		setMyMessagesList(FXCollections.observableArrayList());
		getMyMessagesList().addListener(myMessageListChangeListener);
		
		queryErrorRecordService = new QueryErrorRecordService();
		queryErrorRecordService.valueProperty().addListener(queryErrorRecordServiceListener);
		GB_FreshPane.visibleProperty().bind(queryErrorRecordService.runningProperty());
		
		deviceId = (String) object;
		MaintenanceDeviceTextField.setText(deviceId);
		
		formParm = new HashMap<>();
		
		queryErrorRecordService.restart();
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
		queryErrorRecordService.valueProperty().removeListener(queryErrorRecordServiceListener);
		GB_FreshPane.visibleProperty().unbind();
		queryErrorRecordService = null;
		
		formParm = null;
		
		getMyMessagesList().removeListener(myMessageListChangeListener);
		setMyMessagesList(null);
	}
	
	class QueryErrorRecordService extends Service<RecordJson<MaintenanceRecordItem>>{

		@Override
		protected Task<RecordJson<MaintenanceRecordItem>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<RecordJson<MaintenanceRecordItem>>{

			@Override
			protected RecordJson<MaintenanceRecordItem> call() {
				// TODO Auto-generated method stub				
				formParm.clear();

				if(MaintenanceDateChoose.getValue() != null)
					formParm.put("date", MaintenanceDateChoose.getValue().toString());
				
				if(MaintenanceDeviceTextField.getLength() > 0)
					formParm.put("deviceId", MaintenanceDeviceTextField.getText());
				
				if(MaintenanceOperatorTextfield.getLength() > 0)
					formParm.put("operatorName", MaintenanceOperatorTextfield.getText());
				
				if(MaintenanceResultTextField.getLength() > 0)
					formParm.put("result", MaintenanceResultTextField.getText());
				
				formParm.put("startIndex", String.valueOf((50*GB_Pagination.getCurrentPageIndex())));
				formParm.put("size", String.valueOf(50));
				
				return httpClientTool.myHttpPost(null, ServiceEnum.QueryDeviceMaintenanceRecord, HttpPostType.SynchronousForm, null, formParm);

			}
		}
	}
}

