package com.xsx.ncd.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.DeviceType;
import com.xsx.ncd.entity.Item;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class AddDeviceTypeHandler implements ActivityTemplet, HttpTemplet {

	private AnchorPane rootPane = null;
	
	@FXML StackPane rootStackPane;
	
	@FXML TextField DeviceNameTextfield;
	@FXML StackPane DeviceOnIcoStackPane;
	@FXML ImageView DeviceOnIcoImageView;
	@FXML Label DeviceOnIcoLabel;
	@FXML StackPane DeviceOffIcoStackPane;
	@FXML ImageView DeviceOffIcoImageView;
	@FXML Label DeviceOffIcoLabel;
	@FXML StackPane DeviceErrorIcoStackPane;
	@FXML ImageView DeviceErrorIcoImageView;
	@FXML Label DeviceErrorIcoLabel;
	@FXML FlowPane DeviceItemFlowPane;
	@FXML TextField DeviceVenderTextField;
	@FXML TextField DevicePhoneTextField;
	@FXML TextField DeviceAddrTextField;
	
	@FXML JFXButton AddDeviceTypeButton;
	
	@FXML VBox GB_FreshPane;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;
	
	private DeviceType deviceType = null;
	private Set<Item> deviceItemSet = null;
	private ObservableList<Message> myMessagesList = null;
	
	@Autowired UserSession userSession;
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	
	@PostConstruct
	@Override
	public void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/AddDeviceTypePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/AddDeviceTypePage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(LogDialog);
        
        DeviceOnIcoLabel.visibleProperty().bind(DeviceOnIcoImageView.imageProperty().isNull());
        DeviceOffIcoLabel.visibleProperty().bind(DeviceOffIcoImageView.imageProperty().isNull());
        DeviceErrorIcoLabel.visibleProperty().bind(DeviceErrorIcoImageView.imageProperty().isNull());
        
        DeviceOnIcoStackPane.setOnMouseClicked((e)->{
        	FileChooser fileChooser = new FileChooser();
   		 	fileChooser.setTitle("Ñ¡ÔñÍ¼±ê");
   		 
   		 	fileChooser.getExtensionFilters().addAll(
   		         new ExtensionFilter("Image Files", "*.PNG", "*.BMP", "*.GIF", "*.JPEG"));
   		 	File selectedFile = fileChooser.showOpenDialog(null);
   		 
   		 	if(selectedFile != null){ 
   		 		try {
					DeviceOnIcoImageView.setImage(new Image(new FileInputStream(selectedFile)));
					DeviceOnIcoImageView.setUserData(selectedFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					showLogsDialog("´íÎó", "¶ÁÈ¡Ê§°Ü£¡");
					DeviceOnIcoImageView.setUserData(null);
					DeviceOnIcoImageView.setImage(null);
				}
   		 	}
        });
        
        DeviceOffIcoStackPane.setOnMouseClicked((e)->{
        	FileChooser fileChooser = new FileChooser();
   		 	fileChooser.setTitle("Ñ¡ÔñÍ¼±ê");
   		 
   		 	fileChooser.getExtensionFilters().addAll(
   		         new ExtensionFilter("Image Files", "*.PNG", "*.BMP", "*.GIF", "*.JPEG"));
   		 	File selectedFile = fileChooser.showOpenDialog(null);
   		 
   		 	if(selectedFile != null){ 
   		 		try {
					DeviceOffIcoImageView.setImage(new Image(new FileInputStream(selectedFile)));
					DeviceOffIcoImageView.setUserData(selectedFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					showLogsDialog("´íÎó", "¶ÁÈ¡Ê§°Ü£¡");
					DeviceOffIcoImageView.setUserData(null);
					DeviceOffIcoImageView.setImage(null);
				}
   		 	}
        });
        
        DeviceErrorIcoStackPane.setOnMouseClicked((e)->{
        	FileChooser fileChooser = new FileChooser();
   		 	fileChooser.setTitle("Ñ¡ÔñÍ¼±ê");
   		 
   		 	fileChooser.getExtensionFilters().addAll(
   		         new ExtensionFilter("Image Files", "*.PNG", "*.BMP", "*.GIF", "*.JPEG"));
   		 	File selectedFile = fileChooser.showOpenDialog(null);
   		 
   		 	if(selectedFile != null){ 
   		 		try {
					DeviceErrorIcoImageView.setImage(new Image(new FileInputStream(selectedFile)));
					DeviceErrorIcoImageView.setUserData(selectedFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					showLogsDialog("´íÎó", "¶ÁÈ¡Ê§°Ü£¡");
					DeviceErrorIcoImageView.setUserData(null);
					DeviceErrorIcoImageView.setImage(null);
				}
   		 	}
        });
        myMessagesList = FXCollections.observableArrayList();
        myMessagesList.addListener(new ListChangeListener<Message>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				// TODO Auto-generated method stub
				while(c.next()){
					if(c.wasAdded()){
						GB_FreshPane.setVisible(false);
						for (Message message : c.getAddedSubList()) {
							switch (message.getWhat()) {
							case ReadAllItems:
								showAllItem( message.getObj(List.class));
								break;
							case SaveDeviceTypeAndIco:
								Boolean result =  message.getObj(Boolean.class);
								if(result == null)
									showLogsDialog("´íÎó", "ÇëÖØÊÔ£¡");
								else if(result == false)
									showLogsDialog("´íÎó", "Ìí¼ÓÊ§°Ü£¡");
								break;
							default:
								break;
							}
						}
					}
				}
			}
        	
        });
        
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        AddDeviceTypeButton.setOnAction((e)->{
        	deviceType.setName(DeviceNameTextfield.getText());
        	deviceType.setVender(DeviceVenderTextField.getText());
        	deviceType.setVenderphone(DevicePhoneTextField.getText());
        	deviceType.setVenderaddr(DeviceAddrTextField.getText());
        	
        	deviceItemSet.clear();
        	for (Node node : DeviceItemFlowPane.getChildren()) {
				JFXRadioButton jfxRadioButton = (JFXRadioButton) node;
				if(jfxRadioButton.isSelected())
					deviceItemSet.add((Item) jfxRadioButton.getUserData());
			}
        	deviceType.setItems(deviceItemSet);
        	//httpClientTool.myHttpPostDeviceType(ServiceEnum.SaveDeviceTypeAndIco, deviceType, (File) (DeviceOnIcoImageView.getUserData()), 
        	//		(File)DeviceOffIcoImageView.getUserData(), (File)DeviceErrorIcoImageView.getUserData());
        });
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(this.equals(newValue)){
        		deviceType = new DeviceType();
        		deviceItemSet = new HashSet<>();
        		startHttpWork(ServiceEnum.ReadAllItems, null);
        	}
        	else{
        		deviceType = null;
        		deviceItemSet = null;
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
	public void startActivity(Object object) {
		// TODO Auto-generated method stub
		activitySession.setRootActivity(this);
		activitySession.setFatherActivity(null);
		activitySession.setChildActivity(null);
		activitySession.setActivityPane(this);
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
		return null;
	}

	@Override
	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		Platform.runLater(()->{
			myMessagesList.add(message);
		});
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}
	
	private void showAllItem(List<Item> itemList){
		DeviceItemFlowPane.getChildren().clear();
		
		for (Item item : itemList) {
			JFXRadioButton jfxRadioButton = new JFXRadioButton(item.getName());
			jfxRadioButton.setUserData(item);
			jfxRadioButton.selectedProperty().addListener((o, oldValue, newValue)->{
				if(newValue)
					deviceItemSet.add((Item) jfxRadioButton.getUserData());
			});
			DeviceItemFlowPane.getChildren().add(jfxRadioButton);
		}
	}
	
	@Override
	public void startHttpWork(ServiceEnum serviceEnum, Object parm) {
		GB_FreshPane.setVisible(true);
		
		if(!httpClientTool.myHttpAsynchronousPostJson(this, serviceEnum, parm)){
			GB_FreshPane.setVisible(false);
			showLogsDialog("´íÎó", "Êý¾Ý×ª»»Ê§°Ü£¬ÇëÖØÊÔ£¡");
		}	
	}
}
