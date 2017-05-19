
package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.MyUserActionEnum;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
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
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

@Component
public class OperatorListHandler implements ActivityTemplet, HttpTemplet{

	private AnchorPane rootpane;
	
	@FXML StackPane rootStackPane;
	
	@FXML JFXListView<ListViewCell> GB_UserListView;
	
	@FXML TextField GB_UserNameTextField;
	@FXML TextField GB_UserAgeTextField;
	@FXML TextField GB_UserSexTextField;
	@FXML TextField GB_UserPhoneTextField;
	@FXML TextField GB_UserJobTextField;
	@FXML JFXComboBox<Department> GB_UserDepartmentCombox;
	@FXML TextField GB_UserDescTextField;
	@FXML JFXToggleButton GB_OperatorRightToggle;
	
	@FXML ImageView GB_AddUserImageView;
	@FXML ImageView GB_CancelAddUserImageView;
	@FXML ImageView GB_CancelEditUserImageView;
	@FXML ImageView GB_EditUserImageView;
	private Image deleteUserIco = null;
	@FXML StackPane GB_ModifyIcoStackPane;
	@FXML Button GB_SaveUserInfoButton;
	
	@FXML JFXDialog modifyUserInfoDialog;
	@FXML PasswordField userPasswordTextField;
	@FXML JFXButton acceptButton0;
	@FXML JFXButton cancelButton0;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;
	
	@FXML VBox GB_FreshPane;

	private MyUserActionEnum GB_ActionType = MyUserActionEnum.NONE;
	private Operator tempOperator = null;
	private User itsMe = null;
	private ObservableList<Message> myMessagesList = null;

	@Autowired HttpClientTool httpClientTool;
	@Autowired private UserSession userSession;
	@Autowired private ActivitySession activitySession;
	
	@PostConstruct
	@Override
	public void onCreate(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/OperatorListPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/OperatorListPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        deleteUserIco = new Image(this.getClass().getResourceAsStream("/RES/deleteUserIco.png"));
        rootStackPane.getChildren().remove(modifyUserInfoDialog);
        rootStackPane.getChildren().remove(LogDialog);
        
        GB_EditUserImageView.disableProperty().bind(GB_UserListView.getSelectionModel().selectedItemProperty().isNull());
        GB_EditUserImageView.setOnMouseClicked((e)->{
        	setUserInfoInStatus(MyUserActionEnum.EDIT);
        	startHttpWork(ServiceEnum.ReadAllDepartment, null);
            GB_ActionType = MyUserActionEnum.EDIT;
        });
        
        GB_CancelEditUserImageView.setOnMouseClicked((e)->{
        	GB_ActionType = MyUserActionEnum.NONE;
        	startHttpWork(ServiceEnum.ReadAllOperator, null);
        });
        
        GB_AddUserImageView.setOnMouseClicked((e)->{
        	setUserInfoInStatus(MyUserActionEnum.ADD);
        	clearUserInfo();
        	GB_ActionType = MyUserActionEnum.ADD;
        	startHttpWork(ServiceEnum.ReadAllDepartment, null);
        });
        GB_CancelAddUserImageView.setOnMouseClicked((e)->{
        	GB_ActionType = MyUserActionEnum.NONE;
        	startHttpWork(ServiceEnum.ReadAllOperator, null);
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
							case DeleteOperator:
								if(message.getObj(Boolean.class)){
									startHttpWork(ServiceEnum.ReadAllOperator, null);
			            		}
			        			else{
			        				showLogsDialog("错误", "删除失败！");
			        			}
								break;
							case SaveOperator:
								tempOperator = message.getObj(Operator.class);
			            		
			            		if(GB_ActionType.equals(MyUserActionEnum.ADD)){
			            			if(tempOperator == null){
			            				showLogsDialog("错误", "添加失败！");
			            			}
			            			else{
			            				startHttpWork(ServiceEnum.ReadAllOperator, null);
			            			}
			            			
			            		}
			            		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
			            			if(tempOperator == null){
			            				showLogsDialog("错误", "修改失败！");
			            			}
			            			else{
			            				startHttpWork(ServiceEnum.ReadAllOperator, null);
			            			}
			            		}
								break;
							case ReadAllOperator:
								upUserList(message.getObj(List.class));
								setUserInfoInStatus(MyUserActionEnum.NONE);
								break;
							case CheckOperatorIsExist:
								if(GB_ActionType.equals(MyUserActionEnum.ADD)){

			            			if(message.getObj(Boolean.class)){
			            				showLogsDialog("错误", "用户已存在，请检查！");
			                		}
			            			else{
			            				startHttpWork(ServiceEnum.SaveOperator, tempOperator);
			            			}
			            		}
			            		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
			            			if(message.getObj(Boolean.class)){
			            				startHttpWork(ServiceEnum.SaveOperator, tempOperator);
			                		}
			            			else{
			            				showLogsDialog("错误", "用户不存在，请检查！");
			            			}
			            		}
								break;
							case ReadAllDepartment:
								GB_UserDepartmentCombox.getItems().setAll(message.getObj(List.class));
								break;
							default:
								break;
							}
						}
					}
				}
			}
        });
        
		GB_FreshPane.setVisible(false);

        
        //权限确认
        acceptButton0.disableProperty().bind(userPasswordTextField.lengthProperty().lessThan(6));
        acceptButton0.setOnAction((e)->{
        	modifyUserInfoDialog.close();
        	if(userPasswordTextField.getText().equals(itsMe.getPassword())){
        		if(GB_ActionType.equals(MyUserActionEnum.DELETE)){
        			tempOperator = (Operator) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();

        			startHttpWork(ServiceEnum.DeleteOperator, tempOperator);
        		}
        		else if(GB_ActionType.equals(MyUserActionEnum.ADD)){
        			tempOperator = new Operator();
        				
        			tempOperator.setName(GB_UserNameTextField.getText());
        			tempOperator.setAge(GB_UserAgeTextField.getText());
        			tempOperator.setSex(GB_UserSexTextField.getText());
        			tempOperator.setPhone(GB_UserPhoneTextField.getText());
        			tempOperator.setJob(GB_UserJobTextField.getText());
        			tempOperator.setDes(GB_UserDescTextField.getText());
        			tempOperator.setDepartment(GB_UserDepartmentCombox.getSelectionModel().getSelectedItem());
        			tempOperator.setChecked(GB_OperatorRightToggle.isSelected());

        			startHttpWork(ServiceEnum.CheckOperatorIsExist, tempOperator);
        		}
        		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
        			tempOperator = (Operator) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();
    				
        			tempOperator.setName(GB_UserNameTextField.getText());
        			tempOperator.setAge(GB_UserAgeTextField.getText());
        			tempOperator.setSex(GB_UserSexTextField.getText());
        			tempOperator.setPhone(GB_UserPhoneTextField.getText());
        			tempOperator.setJob(GB_UserJobTextField.getText());
        			tempOperator.setDes(GB_UserDescTextField.getText());
        			tempOperator.setDepartment(GB_UserDepartmentCombox.getSelectionModel().getSelectedItem());
        			tempOperator.setChecked(GB_OperatorRightToggle.isSelected());

        			startHttpWork(ServiceEnum.CheckOperatorIsExist, tempOperator);
        		}
        	}
        	else
        		showLogsDialog("错误", "密码错误，禁止操作！");
        });
        cancelButton0.setOnAction((e)->{
        	modifyUserInfoDialog.close();
        });
        
        //取消修改密码
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        //添加或修改
        GB_SaveUserInfoButton.disableProperty().bind(GB_UserNameTextField.lengthProperty().lessThan(1).
        		or(GB_UserDepartmentCombox.getSelectionModel().selectedItemProperty().isNull()));
        GB_SaveUserInfoButton.setOnAction((e)->{
			userPasswordTextField.clear();
    		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
    		modifyUserInfoDialog.show(rootStackPane);
        });
        
        GB_UserListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	
        	if(newValue != null){
        		newValue.setDeleteIcoVisible(true);
        		showSelectUserInfo();
        	}
        	
        	if(oldValue != null)
        		oldValue.setDeleteIcoVisible(false);
        });
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(this.equals(newValue)){
        		itsMe = userSession.getUser();
        		startHttpWork(ServiceEnum.ReadAllOperator, itsMe);
        	}
        	else {
        		itsMe = null;
        	}
        });
        
        AnchorPane.setTopAnchor(rootpane, 0.0);
        AnchorPane.setBottomAnchor(rootpane, 0.0);
        AnchorPane.setLeftAnchor(rootpane, 0.0);
        AnchorPane.setRightAnchor(rootpane, 0.0);
        
        loader = null;
        in = null;
	}
	
	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		activitySession.setActivityPane(this);
	}
	
	private void upUserList(List<Operator> userList) {

		GB_UserListView.getItems().clear();
		for (Operator user : userList) {
			ListViewCell tempListViewCell = new ListViewCell(user, deleteUserIco);
			GB_UserListView.getItems().add(tempListViewCell);
		}

		GB_UserListView.getSelectionModel().selectFirst();
	}
	
	private void setUserInfoInStatus(MyUserActionEnum status){
		GB_UserNameTextField.setEditable(status.equals(MyUserActionEnum.ADD));
		GB_UserAgeTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserSexTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserPhoneTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserJobTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserDescTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		
		GB_UserDepartmentCombox.setDisable(!status.equals(MyUserActionEnum.ADD));
		GB_OperatorRightToggle.setDisable(status.equals(MyUserActionEnum.NONE));
		
		GB_SaveUserInfoButton.setVisible(!status.equals(MyUserActionEnum.NONE));
		
		GB_EditUserImageView.setVisible(status.equals(MyUserActionEnum.NONE));
		GB_CancelEditUserImageView.setVisible(status.equals(MyUserActionEnum.EDIT));
		GB_AddUserImageView.setVisible(status.equals(MyUserActionEnum.NONE));
		GB_CancelAddUserImageView.setVisible(status.equals(MyUserActionEnum.ADD));

	}
	
	private void clearUserInfo() {
		GB_UserNameTextField.clear();
		GB_UserAgeTextField.clear();
		GB_UserSexTextField.clear();
		GB_UserPhoneTextField.clear();
		GB_UserJobTextField.clear();
		GB_UserDescTextField.clear();
		GB_UserDepartmentCombox.getSelectionModel().select(null);
		GB_OperatorRightToggle.setSelected(false);
	}
	
	private void showSelectUserInfo() {
		Operator user = (Operator) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();

		GB_UserNameTextField.setText(user.getName());
		GB_UserAgeTextField.setText(user.getAge());
		GB_UserSexTextField.setText(user.getSex());
		GB_UserPhoneTextField.setText(user.getPhone());
		GB_UserJobTextField.setText(user.getJob());
		GB_UserDescTextField.setText(user.getDes());
		GB_UserDepartmentCombox.getSelectionModel().select(user.getDepartment());
		GB_OperatorRightToggle.setSelected(user.getChecked());
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}
	
	class ListViewCell extends AnchorPane{
		
		ImageView imageView = null;
		
		public ListViewCell(Operator user, Image image){
			Label userName = new Label(user.getName());
			AnchorPane.setTopAnchor(userName, 0.0);
	        AnchorPane.setBottomAnchor(userName, 0.0);
	        AnchorPane.setLeftAnchor(userName, 0.0);
			
			imageView = new ImageView(image);
			imageView.setVisible(false);
			imageView.setFitWidth(25);
			imageView.setFitHeight(25);
			imageView.setCursor(Cursor.HAND);
			imageView.setOnMouseClicked((e)->{
				GB_ActionType = MyUserActionEnum.DELETE;
				userPasswordTextField.clear();
	    		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
	    		modifyUserInfoDialog.show(rootStackPane);
			});
			AnchorPane.setTopAnchor(imageView, 0.0);
	        AnchorPane.setBottomAnchor(imageView, 0.0);
	        AnchorPane.setRightAnchor(imageView, 0.0);
			
			this.setUserData(user);
			this.getChildren().addAll(userName, imageView);
		}
		
		public void setDeleteIcoVisible(boolean status){
			imageView.setVisible(status);
		}
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
		return "操作人管理";
	}

	@Override
	public Pane getActivityRootPane() {
		// TODO Auto-generated method stub
		return rootpane;
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
		GB_FreshPane.setVisible(true);
		
		if(!httpClientTool.myHttpAsynchronousPostJson(this, serviceEnum, parm)){
			GB_FreshPane.setVisible(false);
			showLogsDialog("错误", "数据转换失败，请重试！");
		}	
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

}
