package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.svg.SVGGlyphLoader;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.DeviceTypeJson;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.MyUserActionEnum;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.Item;
import com.xsx.ncd.entity.Operator;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

@Component
public class DeviceInfoHandler extends Activity {

	@FXML StackPane rootStackPane;
	
	@FXML ImageView deviceImageView;
	@FXML Label deviceNameLabel;
	@FXML Label deviceIdLabel;
	@FXML Label deviceVenderLabel;
	@FXML Label deviceAddrLabel;
	@FXML Label deviceUserLabel;
	@FXML FlowPane deviceItemFlowPane;
	@FXML FlowPane deviceOperatorFlowPane;
	@FXML ImageView modifyOperatorImageView;
	
	@FXML JFXButton deviceMaintenanceRecordButton;
	@FXML JFXButton deviceUseRecordButton;
	@FXML JFXButton deviceAdjustRecordButton;
	@FXML JFXButton deviceQualityRecordButton;
	@FXML JFXButton deviceErrorRecordButton;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton logDialogCommitButton;
	
	@FXML JFXDialog addOperatorDialog;
	@FXML FlowPane opratorRadioFlowPane;
	@FXML JFXButton addOperatorDialogCancelButton;
	@FXML JFXButton addOperatorDialogCommitButton;
	
	@FXML VBox GB_FreshPane;
	
	private String deviceId = null;
	private Device device = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	private Map<String, String> formParm = null;
	private Map<String, Object> activityParm = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired MaintenanceRecordHandler maintenanceRecordHandler;
	@Autowired ErrorRecordHandler errorRecordHandler;
	@Autowired AdjustRecordHandler adjustRecordHandler;
	@Autowired QualityRecordHandler qualityRecordHandler;
	@Autowired ReportQueryHandler reportQueryHandler;
	
	@PostConstruct
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/DeviceInfo.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/DeviceInfo.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(addOperatorDialog);
        rootStackPane.getChildren().remove(LogDialog);
        
        logDialogCommitButton.setOnAction((e)->{
        	LogDialog.close();
        });
        
        addOperatorDialogCancelButton.setOnAction((e)->{
        	addOperatorDialog.close();
        });
        
        addOperatorDialogCommitButton.setOnAction((e)->{
        	Set<Operator> operators = new HashSet<>();
        	for (Node node : opratorRadioFlowPane.getChildren()) {
				JFXRadioButton tempRadio = (JFXRadioButton) node;
				if(tempRadio.isSelected())
					operators.add((Operator) tempRadio.getUserData());
			}
        	device.setOperators(operators);
        	startHttpWork(ServiceEnum.UpDateDevice, HttpPostType.AsynchronousJson, device, null, GB_FreshPane);
        	addOperatorDialog.close();
        });
        
        modifyOperatorImageView.setOnMouseClicked((e)->{
        	startHttpWork(ServiceEnum.QueryOperatorByDepartment, HttpPostType.AsynchronousJson, device.getDepartment(), null, GB_FreshPane);
        	
        	addOperatorDialog.show(rootStackPane);
        });
        
        deviceUseRecordButton.setOnAction((e)->{
        	activityParm.clear();
        	activityParm.put("DeviceType", new DeviceTypeJson(device.getDeviceType()));
        	activityParm.put("DeviceId", device.getDid());
        	activitySession.startActivity(reportQueryHandler, activityParm);
        });
        
        deviceMaintenanceRecordButton.setOnAction((e)->{
        	activitySession.startActivity(maintenanceRecordHandler, device.getDid());
        });
        
        deviceAdjustRecordButton.setOnAction((e)->{
        	activitySession.startActivity(adjustRecordHandler, device.getDid());
        });
        
        deviceQualityRecordButton.setOnAction((e)->{
        	activitySession.startActivity(qualityRecordHandler, device.getDid());
        });
        
        deviceErrorRecordButton.setOnAction((e)->{
        	activitySession.startActivity(errorRecordHandler, device.getDid());
        });
        
        myMessageListChangeListener = c ->{
        	while(c.next()){
				if(c.wasAdded()){
					for (Message message : c.getAddedSubList()) {
						switch (message.getWhat()) {
						
							case QueryDeviceByDeviceId:
								showDeviceInfo(message.getObj());
								break;
								
							case QueryOperatorByDepartment:
								List<Operator> operators = message.getObj();
								for (Operator operator : operators) {
									JFXRadioButton tempRadio = new JFXRadioButton(operator.getName());
									tempRadio.setUserData(operator);
									opratorRadioFlowPane.getChildren().add(tempRadio);
								}
								break;

							default:
								break;
						}
					}
					GB_FreshPane.setVisible(false);
				}
			}
        };
        
        this.setActivityName("设备信息");
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
		deviceId = (String) object;
		
		activityParm = new HashMap<>();
		
		formParm = new HashMap<>();
		formParm.put("deviceId", deviceId);
		
		setMyMessagesList(FXCollections.observableArrayList());
		getMyMessagesList().addListener(myMessageListChangeListener);
		
		startHttpWork(ServiceEnum.QueryDeviceByDeviceId, HttpPostType.AsynchronousForm, null, formParm, GB_FreshPane);
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
		deviceImageView.setImage(null);
		
		deviceNameLabel.setText(null);
		deviceIdLabel.setText(null);
		deviceVenderLabel.setText(null);
		deviceAddrLabel.setText(null);
		deviceUserLabel.setText(null);
		
		deviceItemFlowPane.getChildren().clear();
		deviceOperatorFlowPane.getChildren().clear();
		opratorRadioFlowPane.getChildren().clear();
		
		getMyMessagesList().addListener(myMessageListChangeListener);
		setMyMessagesList(null);
		
		deviceId = null;
		
		formParm = null;
		
		activityParm = null;
	}

	private void showDeviceInfo(Device device){
		StringBuffer url = new StringBuffer(HttpClientTool.ServiceDeviceIcoUrl);
		
		int lastchart = device.getDeviceType().getIcon().lastIndexOf('/');
		int endIndex = device.getDeviceType().getIcon().length();
		url.append(device.getDeviceType().getIcon().substring(lastchart+1, endIndex));
		
		this.device = device;
		deviceImageView.setImage(new Image(url.toString()));
		
		deviceNameLabel.setText(device.getDeviceType().getName());
		deviceIdLabel.setText(device.getDid());
		deviceVenderLabel.setText(device.getDeviceType().getVender());
		deviceAddrLabel.setText(device.getAddr());
		deviceUserLabel.setText(device.getOperator().getName());
		
		for (Item item : device.getDeviceType().getItems()) {
			Label tempItem = new Label(item.getName());
			deviceItemFlowPane.getChildren().add(tempItem);
		}
		
		for (Operator operator : device.getOperators()) {
			Label tempOperator = new Label(operator.getName());
			deviceOperatorFlowPane.getChildren().add(tempOperator);
		}
	}
	

}
