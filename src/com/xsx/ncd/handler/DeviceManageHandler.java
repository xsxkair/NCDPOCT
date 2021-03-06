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
import com.jfoenix.controls.JFXRadioButton;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.define.UserFilePath;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.DeviceType;
import com.xsx.ncd.entity.Item;
import com.xsx.ncd.entity.Operator;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpClientTool;
import com.xsx.ncd.tool.XsxLog;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
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
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class DeviceManageHandler extends Activity {

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
	
	private DeviceType deviceType = null;
	private Set<Item> deviceItemSet = null;
	private ActionType actionType = ActionType.None;
	
	private Device device = null;
	private Set<Operator> deviceOperators = null;
	
	private ObservableSet<String> deviceIcoPathSet = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	
	@Autowired UserSession userSession;
	@Autowired ActivitySession activitySession;
	@Autowired UserFilePath userFilePath;
	@Autowired HttpClientTool httpClientTool;
	@Autowired XsxLog xsxLog;
	
	@PostConstruct
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/DeviceManagePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/DeviceManagePage.fxml");
        loader.setController(this);
        try {
        	this.setRootPane(loader.load(in));
        	in.close();
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
        	startHttpWork(ServiceEnum.ReadAllItems, HttpPostType.AsynchronousJson, null, null, null);
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
					SpringFacktory.GetBean(DeviceManageHandler.class).startHttpWork(ServiceEnum.AddNewDevice, 
							HttpPostType.AsynchronousJson, device, null, null);
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

        myMessageListChangeListener = c ->{
        	while(c.next()){
				if(c.wasAdded()){

					for (Message message : c.getAddedSubList()) {
						switch (message.getWhat()) {
							case ReadAllItems:
								showAddDeviceTypeAllItem(message.getObj());
								break;
								
							case QueryOperatorByDepartment:
								showAddDeviceOperator( message.getObj());
								break;
								
							case SaveDeviceTypeAndIco:
								showLogsDialog("消息", message.getObj());
								break;
							
							case QueryAllDeviceType:
								showAllDeviceType(message.getObj());
								break;
							
							case AddNewDevice:
								showLogsDialog("消息", message.getObj());
								break;
								
							case ReadAllDepartment:
								showAllDepartment(message.getObj());
								break;
							default:
								break;
						}
					}
				}
			}
        };
        
        this.setActivityName("设备管理");
        this.setActivityStatus(ActivityStatusEnum.Create);
        xsxLog.info("设备管理界面创建");
  
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
		
		this.setMyMessagesList(FXCollections.observableArrayList());
		this.getMyMessagesList().addListener(myMessageListChangeListener);
		
		deviceItemSet = new HashSet<>();
    	deviceType = new DeviceType();
    	
    	device = new Device();
    	deviceOperators = new HashSet<>();
    	
    	startHttpWork(ServiceEnum.ReadAllDepartment, HttpPostType.AsynchronousJson, null, null, null);
    	
    	xsxLog.info("设备管理界面启动");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		xsxLog.info("设备管理界面暂停");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		xsxLog.info("设备管理界面恢复");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		deviceItemSet = null;
    	deviceType = null;
    	
    	device = null;
    	deviceOperators = null;
    	
		for (Node node : DepartmentFlowPane.getChildren()) {
			DepartmentDeviceListHandler departmentDeviceListHandler = (DepartmentDeviceListHandler) node;
			departmentDeviceListHandler.onDestroy();
		}
		DepartmentFlowPane.getChildren().clear();
		
		this.getMyMessagesList().removeListener(myMessageListChangeListener);
		this.setMyMessagesList(null);
		
		xsxLog.info("设备管理界面销毁");
	}
	
	private void showAllDepartment(List<Department> departments) {
		DepartmentFlowPane.getChildren().clear();
		for (Department department : departments) {
			DepartmentDeviceListHandler departmentDeviceListHandler = new DepartmentDeviceListHandler(department, this);
			DepartmentFlowPane.getChildren().add(departmentDeviceListHandler);
		}
	}
	private void showAllDeviceType(List<DeviceType> deviceTypes) {
		DeviceTypeCombox.getItems().setAll(deviceTypes);
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
		startHttpWork(ServiceEnum.QueryOperatorByDepartment, HttpPostType.AsynchronousJson, department, null, null);
		//查找所有设备类型
		startHttpWork(ServiceEnum.QueryAllDeviceType, HttpPostType.AsynchronousJson, null, null, null);
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
