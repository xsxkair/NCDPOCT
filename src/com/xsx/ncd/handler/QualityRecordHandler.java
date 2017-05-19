package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.define.ErrorRecordItem;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.RecordJson;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class QualityRecordHandler implements ActivityTemplet, HttpTemplet {

	private AnchorPane rootPane = null;
	
	@FXML JFXDatePicker ErrorDateChoose;
	@FXML JFXTextField ErrorOperatorTextfield;
	@FXML JFXTextField ErrorDeviceTextField;
	@FXML JFXTextField ErrorCodeTextField;
	
	@FXML TableView<ErrorRecordItem> ErrorTableView;
	@FXML TableColumn<ErrorRecordItem, Timestamp> ErrorTimeTableColumn;
	@FXML TableColumn<ErrorRecordItem, Integer> ErrorCodeTableColumn;
	@FXML TableColumn<ErrorRecordItem, String> ErrorDeviceTableColumn;
	@FXML TableColumn<ErrorRecordItem, String> ErrorOperatorTableColumn;
	@FXML TableColumn<ErrorRecordItem, String> ErrorDescTableColumn;
	
	@FXML Pagination GB_Pagination;
	@FXML VBox GB_FreshPane;
	
	private ObservableList<Message> myMessagesList = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	private ObjectMapper mapper = null;
	private QueryErrorRecordService queryErrorRecordService = null;
	private ChangeListener<RecordJson<ErrorRecordItem>> queryErrorRecordServiceListener = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	
	@PostConstruct
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/ErrorRecord.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/ErrorRecord.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ErrorTimeTableColumn.setCellValueFactory(new PropertyValueFactory<ErrorRecordItem, Timestamp>("testTime"));
        ErrorCodeTableColumn.setCellValueFactory(new PropertyValueFactory<ErrorRecordItem, Integer>("errorCode"));
        ErrorDeviceTableColumn.setCellValueFactory(new PropertyValueFactory<ErrorRecordItem, String>("deviceId"));
        ErrorOperatorTableColumn.setCellValueFactory(new PropertyValueFactory<ErrorRecordItem, String>("userName"));
        ErrorDescTableColumn.setCellValueFactory(new PropertyValueFactory<ErrorRecordItem, String>("desc"));
        
        ErrorDateChoose.valueProperty().addListener((o, oldValue, newValue)->{
        	startQueryDeviceErrorRecord();
        });
        
        ErrorOperatorTextfield.lengthProperty().addListener((o, oldValue, newValue)->{
        	startQueryDeviceErrorRecord();
        });
        
        ErrorDeviceTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	startQueryDeviceErrorRecord();
        });
        
        ErrorCodeTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	startQueryDeviceErrorRecord();
        });
        
        GB_Pagination.currentPageIndexProperty().addListener((o, oldValue, newValue)->{
        	startQueryDeviceErrorRecord();
        });
        
        queryErrorRecordServiceListener = (o, oldValue, newValue)->{
        	if(newValue != null){
        		GB_Pagination.setPageCount(newValue.getTotalPageNum());
            	
            	ErrorTableView.getItems().setAll(newValue.getRecords());
        	}
        	else{
        		GB_Pagination.setPageCount(-1);
            	
            	ErrorTableView.getItems().clear();
        	}
        		
        };
        
        myMessageListChangeListener = c -> {
        	GB_FreshPane.setVisible(false);
        };
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(this.equals(newValue)){
        		myMessagesList = FXCollections.observableArrayList();
        		myMessagesList.addListener(myMessageListChangeListener);
        		
        		queryErrorRecordService = new QueryErrorRecordService();
        		queryErrorRecordService.valueProperty().addListener(queryErrorRecordServiceListener);
        		GB_FreshPane.visibleProperty().bind(queryErrorRecordService.runningProperty());
        		
        		mapper = new ObjectMapper();
        		
        		queryErrorRecordService.restart();
        		
        	}
        	else if(this.equals(oldValue)){
        		myMessagesList.removeListener(myMessageListChangeListener);
        		myMessagesList = null;
        		
        		queryErrorRecordService.valueProperty().removeListener(queryErrorRecordServiceListener);
        		GB_FreshPane.visibleProperty().unbind();
        		queryErrorRecordService = null;
        		
        		
        		mapper = null;
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
	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		Platform.runLater(()->{
			myMessagesList.add(message);
		});
	}

	@Override
	public void startHttpWork(ServiceEnum serviceEnum, Object parm) {
		// TODO Auto-generated method stub
		GB_FreshPane.setVisible(true);
		
		if(!httpClientTool.myHttpAsynchronousPostJson(this, serviceEnum, parm)){
			GB_FreshPane.setVisible(false);
		}
	}

	@Override
	public Pane getActivityRootPane() {
		// TODO Auto-generated method stub
		return rootPane;
	}

	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
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
		return "“Ï≥£π‹¿Ì";
	}

	private void startQueryDeviceErrorRecord() {
		queryErrorRecordService.restart();
	}
	
	class QueryErrorRecordService extends Service<RecordJson<ErrorRecordItem>>{

		@Override
		protected Task<RecordJson<ErrorRecordItem>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<RecordJson<ErrorRecordItem>>{

			@Override
			protected RecordJson<ErrorRecordItem> call() {
				// TODO Auto-generated method stub				
				FormBody.Builder requestFormBodyBuilder = new FormBody.Builder();

				if(ErrorDateChoose.getValue() != null)
					requestFormBodyBuilder.add("date", ErrorDateChoose.getValue().toString());
				
				if(ErrorDeviceTextField.getLength() > 0)
					requestFormBodyBuilder.add("deviceId", ErrorDeviceTextField.getText());
				
				if(ErrorOperatorTextfield.getLength() > 0)
					requestFormBodyBuilder.add("operatorName", ErrorOperatorTextfield.getText());
				
				if(ErrorCodeTextField.getLength() > 0)
					requestFormBodyBuilder.add("errorCode", ErrorCodeTextField.getText());
				
				requestFormBodyBuilder.add("startIndex", String.valueOf((50*GB_Pagination.getCurrentPageIndex())));
				requestFormBodyBuilder.add("size", String.valueOf(50));
				
				RequestBody requestBody = requestFormBodyBuilder.build();
				
				Request request = new Request.Builder()
				      .url("http://116.62.108.201:8080/NCDPOCT_Server/QueryDeviceErrorRecord")
				      .post(requestBody)
				      .build();
				
				Response response;
				try {
					response = httpClientTool.getClient().newCall(request).execute();
					
					if(response.isSuccessful()) {
						JavaType javaType = mapper.getTypeFactory().constructParametricType(RecordJson.class, ErrorRecordItem.class); 
						RecordJson<ErrorRecordItem> errorRecordJson = mapper.readValue(response.body().string(), javaType);

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

