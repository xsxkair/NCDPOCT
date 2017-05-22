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
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.AdjustRecordItem;
import com.xsx.ncd.define.ErrorRecordItem;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.RecordJson;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.handler.QualityRecordHandler.QueryErrorRecordService;
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
public class AdjustRecordHandler extends Activity {
	
	@FXML JFXDatePicker AdjustDateChoose;
	@FXML JFXTextField AdjustOperatorTextfield;
	@FXML JFXTextField AdjustDeviceTextField;
	@FXML JFXTextField AdjustResultTextField;
	
	@FXML TableView<AdjustRecordItem> AdjustTableView;
	@FXML TableColumn<AdjustRecordItem, Timestamp> AdjustTimeTableColumn;
	@FXML TableColumn<AdjustRecordItem, Float> AdjustNormalValueTableColumn;
	@FXML TableColumn<AdjustRecordItem, Float> AdjustMeasureValueTableColumn;
	@FXML TableColumn<AdjustRecordItem, String> AdjustResultTableColumn;
	@FXML TableColumn<AdjustRecordItem, String> AdjustDeviceTableColumn;
	@FXML TableColumn<AdjustRecordItem, String> AdjustOperatorTableColumn;
	@FXML TableColumn<AdjustRecordItem, String> AdjustDescTableColumn;
	
	@FXML Pagination GB_Pagination;
	@FXML VBox GB_FreshPane;
	
	private ListChangeListener<Message> myMessageListChangeListener = null;
	private ObjectMapper mapper = null;
	private QueryErrorRecordService queryErrorRecordService = null;
	private ChangeListener<RecordJson<AdjustRecordItem>> queryErrorRecordServiceListener = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	
	@PostConstruct
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/AdjustRecord.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/AdjustRecord.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        AdjustTimeTableColumn.setCellValueFactory(new PropertyValueFactory<AdjustRecordItem, Timestamp>("testTime"));;
    	AdjustNormalValueTableColumn.setCellValueFactory(new PropertyValueFactory<AdjustRecordItem, Float>("theoreticalValue"));;
    	AdjustMeasureValueTableColumn.setCellValueFactory(new PropertyValueFactory<AdjustRecordItem, Float>("measuredValue"));;
    	AdjustResultTableColumn.setCellValueFactory(new PropertyValueFactory<AdjustRecordItem, String>("result"));;
    	AdjustDeviceTableColumn.setCellValueFactory(new PropertyValueFactory<AdjustRecordItem, String>("deviceId"));;
    	AdjustOperatorTableColumn.setCellValueFactory(new PropertyValueFactory<AdjustRecordItem, String>("userName"));;
    	AdjustDescTableColumn.setCellValueFactory(new PropertyValueFactory<AdjustRecordItem, String>("desc"));;
        
    	AdjustDateChoose.valueProperty().addListener((o, oldValue, newValue)->{
    		queryErrorRecordService.restart();
        });
        
    	AdjustOperatorTextfield.lengthProperty().addListener((o, oldValue, newValue)->{
    		queryErrorRecordService.restart();
        });
        
    	AdjustDeviceTextField.lengthProperty().addListener((o, oldValue, newValue)->{
    		queryErrorRecordService.restart();
        });
        
    	AdjustResultTextField.lengthProperty().addListener((o, oldValue, newValue)->{
    		queryErrorRecordService.restart();
        });
        
        GB_Pagination.currentPageIndexProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
        queryErrorRecordServiceListener = (o, oldValue, newValue)->{
        	if(newValue != null){
        		GB_Pagination.setPageCount(newValue.getTotalPageNum());
            	
        		AdjustTableView.getItems().setAll(newValue.getRecords());
        	}
        	else{
        		GB_Pagination.setPageCount(-1);
            	
        		AdjustTableView.getItems().clear();
        	}
        		
        };
        
        myMessageListChangeListener = c -> {
        	GB_FreshPane.setVisible(false);
        };

        this.setActivityName("Ð£×¼¼ÇÂ¼");
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
		
		mapper = new ObjectMapper();
		
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
		
		mapper = null;
		
		getMyMessagesList().removeListener(myMessageListChangeListener);
		setMyMessagesList(null);
	}
	
	class QueryErrorRecordService extends Service<RecordJson<AdjustRecordItem>>{

		@Override
		protected Task<RecordJson<AdjustRecordItem>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<RecordJson<AdjustRecordItem>>{

			@Override
			protected RecordJson<AdjustRecordItem> call() {
				// TODO Auto-generated method stub				
				FormBody.Builder requestFormBodyBuilder = new FormBody.Builder();

				if(AdjustDateChoose.getValue() != null)
					requestFormBodyBuilder.add("date", AdjustDateChoose.getValue().toString());
				
				if(AdjustDeviceTextField.getLength() > 0)
					requestFormBodyBuilder.add("deviceId", AdjustDeviceTextField.getText());
				
				if(AdjustOperatorTextfield.getLength() > 0)
					requestFormBodyBuilder.add("operatorName", AdjustOperatorTextfield.getText());
				
				if(AdjustResultTextField.getLength() > 0)
					requestFormBodyBuilder.add("result", AdjustResultTextField.getText());
				
				requestFormBodyBuilder.add("startIndex", String.valueOf((50*GB_Pagination.getCurrentPageIndex())));
				requestFormBodyBuilder.add("size", String.valueOf(50));
				
				RequestBody requestBody = requestFormBodyBuilder.build();
				
				Request request = new Request.Builder()
				      .url("http://116.62.108.201:8080/NCDPOCT_Server/QueryDeviceAdjustRecord")
				      .post(requestBody)
				      .build();
				
				Response response;
				try {
					response = httpClientTool.getClient().newCall(request).execute();
					
					if(response.isSuccessful()) {
						JavaType javaType = mapper.getTypeFactory().constructParametricType(RecordJson.class, AdjustRecordItem.class); 
						RecordJson<AdjustRecordItem> errorRecordJson = mapper.readValue(response.body().string(), javaType);

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

