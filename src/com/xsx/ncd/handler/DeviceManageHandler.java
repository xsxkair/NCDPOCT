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
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.DeviceType;
import com.xsx.ncd.entity.Item;
import com.xsx.ncd.entity.Operator;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class DeviceManageHandler implements ActivityTemplet, HttpTemplet {
	private AnchorPane rootPane = null;
	
	@FXML StackPane rootStackPane;
	
	@FXML JFXButton addNewDeviceTypeButton;
	
	@FXML FlowPane DepartmentFlowPane;

	//添加设备类型
	@FXML JFXDialog addDeviceTypeDialog;
	@FXML TextField DeviceCodeTextfield;
	@FXML TextField DeviceNameTextfield;
	@FXML TextField DeviceModelTextfield;
	@FXML StackPane DeviceIcoStackPane;
	@FXML ImageView DeviceIcoImageView;
	@FXML Label DeviceIcoLabel;
	@FXML FlowPane DeviceItemsFlowPane;
	@FXML TextField DeviceVenderTextField;
	@FXML TextField DeviceVenderPhoneTextField;
	@FXML TextField DeviceVenderAddrTextField;
	@FXML JFXButton cancelAddDeviceTypeButton;
	@FXML JFXButton commitAddDeviceTypeButton;
	
	//添加设备
	@FXML JFXDialog addDeviceDialog;
	@FXML TextField DeviceIdTextfield;
	@FXML JFXComboBox<DeviceType> DeviceTypeCombox;
	@FXML JFXComboBox<Operator> DeviceOpertorCombox;
	@FXML JFXComboBox<Department> DeviceDepartmentCombox;
	@FXML FlowPane DeviceOpertorsFlowPane;
	@FXML TextField DeviceAddrTextField;
	@FXML JFXButton cancelAddDeviceButton;
	@FXML JFXButton commitAddDeviceButton;

	//验证操作密码
	@FXML JFXDialog checkUserRightDialog;
	@FXML PasswordField userPasswordTextField;
	@FXML JFXButton cancelcheckUserRightButton;
	@FXML JFXButton commitcheckUserRightButton;
		
	//信息提示框
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton LogDialogCloseButton;
	
	private ObservableList<Message> myMessagesList = null;
	
	private DeviceType deviceType = null;
	private Set<Item> deviceItemSet = null;
	private ActionType actionType = ActionType.None;
	
	private Device device = null;
	private Set<Operator> deviceOperators = null;
	
	@Autowired UserSession userSession;
	@Autowired ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	
	@PostConstruct
	@Override
	public void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/DeviceManagePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/DeviceManagePage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(addDeviceTypeDialog);
        rootStackPane.getChildren().remove(addDeviceDialog);
        rootStackPane.getChildren().remove(checkUserRightDialog);
        rootStackPane.getChildren().remove(LogDialog);
        
        //添加设备类型
        addNewDeviceTypeButton.setOnAction((e)->{
        	startHttpWork(ServiceEnum.ReadAllItems, null);
        	clearAddDeviceTypeDialog();
        	actionType = ActionType.AddDeviceType;
        	addDeviceTypeDialog.show(rootStackPane);
        });
        
        cancelAddDeviceTypeButton.setOnAction((e)->{
        	addDeviceTypeDialog.close();
        });
        
        commitAddDeviceTypeButton.disableProperty().bind(DeviceNameTextfield.lengthProperty().lessThan(1).
        		or(DeviceModelTextfield.lengthProperty().lessThan(1)).or(DeviceIcoImageView.imageProperty().isNull()).
        		or(DeviceCodeTextfield.lengthProperty().lessThan(1)));
        commitAddDeviceTypeButton.setOnAction((e)->{
        	addDeviceTypeDialog.close();
        	//组合设备类型数据
        	deviceType.setCode(DeviceCodeTextfield.getText());
        	deviceType.setName(DeviceNameTextfield.getText());
        	deviceType.setModel(DeviceModelTextfield.getText());
        	deviceType.setVender(DeviceVenderTextField.getText());
        	deviceType.setVenderaddr(DeviceVenderAddrTextField.getText());	
        	deviceType.setVenderphone(DeviceVenderPhoneTextField.getText());
        	
        	deviceItemSet.clear();
        	for (Node node : DeviceItemsFlowPane.getChildren()) {
				JFXRadioButton jfxRadioButton = (JFXRadioButton) node;
				if(jfxRadioButton.isSelected())
					deviceItemSet.add((Item) jfxRadioButton.getUserData());
			}
        	deviceType.setItems(deviceItemSet);
        	
        	userPasswordTextField.clear();
        	checkUserRightDialog.show(rootStackPane);
        });
        DeviceIcoLabel.visibleProperty().bind(DeviceIcoImageView.imageProperty().isNull());
        
        DeviceIcoStackPane.setOnMouseClicked((e)->{
        	FileChooser fileChooser = new FileChooser();
   		 	fileChooser.setTitle("选择图标");
   		 
   		 	fileChooser.getExtensionFilters().addAll(
   		         new ExtensionFilter("Image Files", "*.PNG", "*.BMP", "*.GIF", "*.JPEG"));
   		 	File selectedFile = fileChooser.showOpenDialog(null);
   		 
   		 	if(selectedFile != null){ 
   		 		try {
   		 			DeviceIcoImageView.setImage(new Image(new FileInputStream(selectedFile)));
   		 			DeviceIcoImageView.setUserData(selectedFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					showLogsDialog("错误", "文件读取失败！");
					DeviceIcoImageView.setUserData(null);
					DeviceIcoImageView.setImage(null);
				}
   		 	}
        });
        
        //添加设备
        cancelAddDeviceButton.setOnAction((e)->{
        	addDeviceDialog.close();
        });
        
        commitAddDeviceButton.setOnAction((e)->{
        	addDeviceDialog.close();
        	
        	//组合设备信息
        	device.setDid(DeviceIdTextfield.getText());
        	device.setAddr(DeviceAddrTextField.getText());
        	device.setDepartment(DeviceDepartmentCombox.getSelectionModel().getSelectedItem());
        	device.setDeviceType(DeviceTypeCombox.getSelectionModel().getSelectedItem());
        	device.setOperator(DeviceOpertorCombox.getSelectionModel().getSelectedItem());
        	
        	deviceOperators.clear();
        	for (Node node : DeviceOpertorsFlowPane.getChildren()) {
				JFXRadioButton jfxRadioButton = (JFXRadioButton) node;
				if(jfxRadioButton.isSelected())
					deviceOperators.add((Operator) jfxRadioButton.getUserData());
			}
        	device.setOperators(deviceOperators);
        	
        	userPasswordTextField.clear();
        	checkUserRightDialog.show(rootStackPane);
        });
        
        
        //权限验证对话框
        commitcheckUserRightButton.disableProperty().bind(userPasswordTextField.lengthProperty().lessThan(6));
        commitcheckUserRightButton.setOnAction((e)->{
        	checkUserRightDialog.close();
        	if(userSession.getUser().getPassword().equals(userPasswordTextField.getText())){
        		switch (actionType) {
				case AddDeviceType:
					httpClientTool.myHttpPostDeviceType(this, ServiceEnum.SaveDeviceTypeAndIco, deviceType,
							(File) DeviceIcoImageView.getUserData());
					break;
				
				case AddDevice:
					httpClientTool.myHttpAsynchronousPostJson(this, ServiceEnum.AddNewDevice, device);
					break;

				default:
					break;
				}
        	}
        	else
        		showLogsDialog("错误", "密码错误，无此权限！");
        });
        
        cancelcheckUserRightButton.setOnAction((e)->{
        	checkUserRightDialog.close();
        });
        
        //提示对话框
        LogDialogCloseButton.setOnAction((e)->{
        	LogDialog.close();
        });
        
        myMessagesList = FXCollections.observableArrayList();
        myMessagesList.addListener(new ListChangeListener<Message>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				// TODO Auto-generated method stub
				while(c.next()){
					if(c.wasAdded()){

						for (Message message : c.getAddedSubList()) {
							switch (message.getWhat()) {
								case ReadAllItems:
									showAddDeviceTypeAllItem(message.getObj(List.class));
									break;
									
								case QueryOperatorByDepartment:
									showAddDeviceOperator( message.getObj(List.class));
									break;
									
								case SaveDeviceTypeAndIco:
									showLogsDialog("消息", message.getObj(String.class));
									break;
								
								case QueryAllDeviceType:
									DeviceTypeCombox.getItems().setAll(message.getObj(List.class));
									break;
								
								case AddNewDevice:
									showLogsDialog("消息", message.getObj(String.class));
									break;
									
								case ReadAllDepartment:
									showAllDepartment(message.getObj(List.class));
									break;
									
								default:
									break;
							}
						}
					}
				}
			}
        });
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(this.equals(newValue)){
        		deviceItemSet = new HashSet<>();
            	deviceType = new DeviceType();
            	
            	device = new Device();
            	deviceOperators = new HashSet<>();
            	
        		startHttpWork(ServiceEnum.ReadAllDepartment, null);
        	}
        	else{
        		deviceItemSet = null;
            	deviceType = null;
            	
            	device = null;
            	deviceOperators = null;
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
		return "设备管理";
	}

	@Override
	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		Platform.runLater(()->{
			myMessagesList.add(message);
		});
	}
	
	private void showAllDepartment(List<Department> departments) {
		DepartmentFlowPane.getChildren().clear();
		for (Department department : departments) {
			DepartmentDeviceListHandler departmentDeviceListHandler = new DepartmentDeviceListHandler(department, this);
			DepartmentFlowPane.getChildren().add(departmentDeviceListHandler);
		}
	}
	
	@Override
	public void startHttpWork(ServiceEnum serviceEnum, Object parm) {
		//GB_FreshPane.setVisible(true);
		
		if(!httpClientTool.myHttpAsynchronousPostJson(this, serviceEnum, parm)){
			//GB_FreshPane.setVisible(false);
			
		}
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}
	
	public void showAddDeviceDialog(Department department) {
		clearAddDeviceDialog();
		
		DeviceDepartmentCombox.getSelectionModel().select(department);

		//查找这个科室的操作人
		startHttpWork(ServiceEnum.QueryOperatorByDepartment, department);
		//查找所有设备类型
		startHttpWork(ServiceEnum.QueryAllDeviceType, null);
		actionType = actionType.AddDevice;
		addDeviceDialog.show(rootStackPane);
	}
	
	private void showAddDeviceTypeAllItem(List<Item> itemList){
		DeviceItemsFlowPane.getChildren().clear();
		
		for (Item item : itemList) {
			JFXRadioButton jfxRadioButton = new JFXRadioButton(item.getName());
			jfxRadioButton.setUserData(item);
			DeviceItemsFlowPane.getChildren().add(jfxRadioButton);
		}
	}
	
	private void showAddDeviceOperator(List<Operator> operators){
		
		DeviceOpertorCombox.getItems().setAll(operators);
		DeviceOpertorsFlowPane.getChildren().clear();
		
		for (Operator operator : operators) {
			JFXRadioButton jfxRadioButton = new JFXRadioButton(operator.toString());
			jfxRadioButton.setUserData(operator);
			DeviceOpertorsFlowPane.getChildren().add(jfxRadioButton);
		}
	}
	
	private void clearAddDeviceTypeDialog() {
		DeviceCodeTextfield.clear();
		DeviceNameTextfield.clear();
		DeviceModelTextfield.clear();
		DeviceIcoImageView.setImage(null);;
		DeviceItemsFlowPane.getChildren().clear();;
		DeviceVenderTextField.clear();
		DeviceVenderPhoneTextField.clear();
		DeviceVenderAddrTextField.clear();
	}
	
	private void clearAddDeviceDialog() {
		DeviceIdTextfield.clear();
		DeviceTypeCombox.getSelectionModel().select(null);
		DeviceOpertorCombox.getSelectionModel().select(null);
		DeviceDepartmentCombox.getSelectionModel().select(null);
		DeviceOpertorsFlowPane.getChildren().clear();
		DeviceAddrTextField.clear();
	}
	
	enum ActionType{
		None, AddDevice, DeleteDevice, AddDeviceType;
	}
}
