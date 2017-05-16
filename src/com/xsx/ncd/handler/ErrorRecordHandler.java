package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.DeviceErrorRecord;
import com.xsx.ncd.entity.Operator;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

@Component
public class ErrorRecordHandler implements ActivityTemplet, HttpTemplet {

	private AnchorPane rootPane = null;
	
	@FXML JFXDatePicker ErrorDateChoose;
	@FXML JFXTextField ErrorOperatorTextfield;
	@FXML JFXTextField ErrorDeviceTextField;
	@FXML JFXTextField ErrorCodeTextField;
	
	@FXML TableView<DeviceErrorRecord> ErrorTableView;
	@FXML TableColumn<DeviceErrorRecord, Timestamp> ErrorTimeTableColumn;
	@FXML TableColumn<DeviceErrorRecord, Integer> ErrorCodeTableColumn;
	@FXML TableColumn<DeviceErrorRecord, Device> ErrorDeviceTableColumn;
	@FXML TableColumn<DeviceErrorRecord, Operator> ErrorOperatorTableColumn;
	@FXML TableColumn<DeviceErrorRecord, String> ErrorDescTableColumn;
	
	@FXML Pagination GB_Pagination;
	@FXML VBox GB_FreshPane;
	
	private ObservableList<Message> myMessagesList = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	
	@PostConstruct
	@Override
	public void UI_Init() {
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
        
        myMessageListChangeListener = (javafx.collections.ListChangeListener.Change<? extends Message> c)->{
        	
        };
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(this.equals(newValue)){
        		myMessagesList = FXCollections.observableArrayList();
        		myMessagesList.addListener(myMessageListChangeListener);
        	}
        	else if(this.equals(oldValue)){
        		myMessagesList.removeListener(myMessageListChangeListener);
        		myMessagesList = null;
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
		return null;
	}

	@Override
	public void startActivity(Object object) {
		// TODO Auto-generated method stub

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
		return "“Ï≥£π‹¿Ì";
	}

}
