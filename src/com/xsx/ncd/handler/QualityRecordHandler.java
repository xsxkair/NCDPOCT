package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.QualityRecordItem;
import com.xsx.ncd.define.RecordJson;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.define.ValueDefine;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

@Component
public class QualityRecordHandler extends Activity {
	
	@FXML JFXDatePicker QualityDateChoose;
	@FXML JFXTextField QualityOperatorTextfield;
	@FXML JFXTextField QualityDeviceTextField;
	@FXML JFXTextField QualityItemTextField;
	
	@FXML TableView<QualityRecordItem> QualityTableView;
	@FXML TableColumn<QualityRecordItem, Timestamp> QualityTimeTableColumn;
	@FXML TableColumn<QualityRecordItem, String> QualityItemTableColumn;
	@FXML TableColumn<QualityRecordItem, Float> QualityNormalValueTableColumn;
	@FXML TableColumn<QualityRecordItem, String> QualityResultTableColumn;
	@FXML TableColumn<QualityRecordItem, String> QualityDeviceTableColumn;
	@FXML TableColumn<QualityRecordItem, String> QualityOperatorTableColumn;
	
	@FXML Pagination GB_Pagination;
	@FXML VBox GB_FreshPane;
	
	private String deviceId = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	private Map<String, String> formParm = null;
	private QueryErrorRecordService queryErrorRecordService = null;
	private ChangeListener<RecordJson<QualityRecordItem>> queryErrorRecordServiceListener = null;
	private static final PseudoClass myErrorPseudoClass = PseudoClass.getPseudoClass("error");
	
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	@Autowired QualityDetailHandler qualityDetailHandler;
	
	@PostConstruct
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/QualityRecord.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/QualityRecord.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        QualityTimeTableColumn.setCellValueFactory(new PropertyValueFactory<QualityRecordItem, Timestamp>("testtime"));
        QualityItemTableColumn.setCellValueFactory(new PropertyValueFactory<QualityRecordItem, String>("itemName"));
        QualityNormalValueTableColumn.setCellValueFactory(new PropertyValueFactory<QualityRecordItem, Float>("theoreticalValue"));
        QualityResultTableColumn.setCellValueFactory(new PropertyValueFactory<QualityRecordItem, String>("resultstr"));
        QualityDeviceTableColumn.setCellValueFactory(new PropertyValueFactory<QualityRecordItem, String>("deviceId"));
        QualityOperatorTableColumn.setCellValueFactory(new PropertyValueFactory<QualityRecordItem, String>("operatorName"));

        QualityTableView.setRowFactory(tv -> {
			TableRow<QualityRecordItem> row = new TableRow<QualityRecordItem>();

			row.setOnMouseClicked((e)->{
				if(e.getClickCount() == 2){
					activitySession.startActivity(qualityDetailHandler, row.getItem());
				}
			});

			row.itemProperty().addListener((o, oldValue, newValue) -> {
				
				if(newValue == null) {
					row.pseudoClassStateChanged(myErrorPseudoClass, false);
				}
				else if(newValue.getResult()){
					row.pseudoClassStateChanged(myErrorPseudoClass, false);
				}
				else{
					row.pseudoClassStateChanged(myErrorPseudoClass, true);
				}
            });
            
            return row;
		});
        
    	QualityDateChoose.valueProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
    	QualityOperatorTextfield.lengthProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
    	QualityDeviceTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
    	QualityItemTextField.lengthProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
        GB_Pagination.currentPageIndexProperty().addListener((o, oldValue, newValue)->{
        	queryErrorRecordService.restart();
        });
        
        queryErrorRecordServiceListener = (o, oldValue, newValue)->{
        	if(newValue != null){
        		GB_Pagination.setPageCount(newValue.getTotalPageNum());
            	
        		QualityTableView.getItems().setAll(newValue.getRecords());
        	}
        	else{
        		GB_Pagination.setPageCount(-1);
            	
        		QualityTableView.getItems().clear();
        	}
        		
        };
        
        myMessageListChangeListener = c -> {
        	GB_FreshPane.setVisible(false);
        };
        
        this.setActivityName("ÖÊ¿Ø¼ÇÂ¼");
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
		QualityDeviceTextField.setText(deviceId);
		
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

	class QueryErrorRecordService extends Service<RecordJson<QualityRecordItem>>{

		@Override
		protected Task<RecordJson<QualityRecordItem>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<RecordJson<QualityRecordItem>>{

			@Override
			protected RecordJson<QualityRecordItem> call() {
				// TODO Auto-generated method stub				
				formParm.clear();
				
				if(QualityDateChoose.getValue() != null)
					formParm.put("date", QualityDateChoose.getValue().toString());
				
				if(QualityDeviceTextField.getLength() > 0)
					formParm.put("deviceId", QualityDeviceTextField.getText());
				
				if(QualityItemTextField.getLength() > 0)
					formParm.put("itemName", QualityItemTextField.getText());
				
				if(QualityOperatorTextfield.getLength() > 0)
					formParm.put("operatorName", QualityOperatorTextfield.getText());
				
				formParm.put("startIndex", String.valueOf(GB_Pagination.getCurrentPageIndex()));
				formParm.put("size", String.valueOf(50));
				
				return httpClientTool.myHttpPost(null, ServiceEnum.QueryDeviceQualityRecord, HttpPostType.SynchronousForm, null, formParm);

			}
		}
	}
}

