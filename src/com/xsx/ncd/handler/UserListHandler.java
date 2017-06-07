
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
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.MyUserActionEnum;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
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
public class UserListHandler extends Activity {

	@FXML StackPane rootStackPane;
	
	@FXML JFXListView<ListViewCell> GB_UserListView;
	
	@FXML TextField GB_UserAccountTextField;
	@FXML PasswordField GB_UserPassWordPassWordField;
	@FXML TextField GB_UserNameTextField;
	@FXML TextField GB_UserAgeTextField;
	@FXML TextField GB_UserSexTextField;
	@FXML TextField GB_UserPhoneTextField;
	@FXML TextField GB_UserJobTextField;
	@FXML JFXComboBox<Department> GB_UserDepartmentCombox;
	@FXML TextField GB_UserDescTextField;
	@FXML JFXToggleButton GB_UserManageToggle;
	@FXML JFXToggleButton GB_DeviceManageToggle;
	@FXML JFXToggleButton GB_CardManageToggle;
	@FXML JFXToggleButton GB_ReportManageToggle;
	
	@FXML ImageView GB_AddUserImageView;
	@FXML ImageView GB_CancelAddUserImageView;
	@FXML ImageView GB_CancelEditUserImageView;
	@FXML ImageView GB_EditUserImageView;
	private Image deleteUserIco = null;
	@FXML StackPane GB_ModifyIcoStackPane;
	@FXML Button GB_SaveUserInfoButton;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;
	
	@FXML VBox GB_FreshPane;

	private MyUserActionEnum GB_ActionType = MyUserActionEnum.NONE;
	private User itsMe = null;
	private User tempUser = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;

	@Autowired HttpClientTool httpClientTool;
	@Autowired private UserSession userSession;
	
	@PostConstruct
	@Override
	public void onCreate(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/UserListPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/UserListPage.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        deleteUserIco = new Image(this.getClass().getResourceAsStream("/RES/deleteUserIco.png"));
        rootStackPane.getChildren().remove(LogDialog);
        
        GB_EditUserImageView.disableProperty().bind(GB_UserListView.getSelectionModel().selectedItemProperty().isNull());
        GB_EditUserImageView.setOnMouseClicked((e)->{
        	setUserInfoInStatus(MyUserActionEnum.EDIT);
        	startHttpWork(ServiceEnum.ReadAllDepartment, HttpPostType.AsynchronousJson, null, null, null);
            GB_ActionType = MyUserActionEnum.EDIT;
        });
        
        GB_CancelEditUserImageView.setOnMouseClicked((e)->{
        	GB_ActionType = MyUserActionEnum.NONE;
        	startHttpWork(ServiceEnum.ReadAllOtherUser, HttpPostType.AsynchronousJson, itsMe, null, null);
        });
        
        GB_AddUserImageView.setOnMouseClicked((e)->{
        	setUserInfoInStatus(MyUserActionEnum.ADD);
        	clearUserInfo();
        	GB_ActionType = MyUserActionEnum.ADD;
        	startHttpWork(ServiceEnum.ReadAllDepartment, HttpPostType.AsynchronousJson, null, null, null);
        });
        GB_CancelAddUserImageView.setOnMouseClicked((e)->{
        	GB_ActionType = MyUserActionEnum.NONE;
        	startHttpWork(ServiceEnum.ReadAllOtherUser, HttpPostType.AsynchronousJson, itsMe, null, null);
        });
        
		GB_FreshPane.setVisible(false);
        
        //取消修改密码   
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        //添加或修改
        GB_SaveUserInfoButton.disableProperty().bind(GB_UserAccountTextField.lengthProperty().lessThan(1).
        		or(GB_UserPassWordPassWordField.lengthProperty().lessThan(6)).
        		or(GB_UserNameTextField.lengthProperty().lessThan(1)).or(GB_UserListView.getSelectionModel().selectedItemProperty().isNull()));
        GB_SaveUserInfoButton.setOnAction((e)->{
        	if(GB_ActionType.equals(MyUserActionEnum.ADD)){
    			tempUser = new User();
    				
    			tempUser.setAccount(GB_UserAccountTextField.getText());
    			tempUser.setPassword(GB_UserPassWordPassWordField.getText());
    			tempUser.setName(GB_UserNameTextField.getText());
    			tempUser.setAge(GB_UserAgeTextField.getText());
    			tempUser.setSex(GB_UserSexTextField.getText());
    			tempUser.setPhone(GB_UserPhoneTextField.getText());
    			tempUser.setJob(GB_UserJobTextField.getText());
    			tempUser.setDes(GB_UserDescTextField.getText());
    			tempUser.setDepartment(GB_UserDepartmentCombox.getSelectionModel().getSelectedItem());
    			tempUser.setManageuser(GB_UserManageToggle.isSelected());
    			tempUser.setManagereport(GB_ReportManageToggle.isSelected());
    			tempUser.setManagedevice(GB_DeviceManageToggle.isSelected());
    			tempUser.setManagecard(GB_CardManageToggle.isSelected());

    			startHttpWork(ServiceEnum.CheckUserIsExist, HttpPostType.AsynchronousJson, tempUser, null, null);
    		}
    		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
    			tempUser = (User) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();
    			
    			tempUser.setAccount(GB_UserAccountTextField.getText());
    			tempUser.setPassword(GB_UserPassWordPassWordField.getText());
    			tempUser.setName(GB_UserNameTextField.getText());
    			tempUser.setAge(GB_UserAgeTextField.getText());
    			tempUser.setSex(GB_UserSexTextField.getText());
    			tempUser.setPhone(GB_UserPhoneTextField.getText());
    			tempUser.setJob(GB_UserJobTextField.getText());
    			tempUser.setDes(GB_UserDescTextField.getText());
    			tempUser.setDepartment(GB_UserDepartmentCombox.getSelectionModel().getSelectedItem());
    			tempUser.setManageuser(GB_UserManageToggle.isSelected());
    			tempUser.setManagereport(GB_ReportManageToggle.isSelected());
    			tempUser.setManagedevice(GB_DeviceManageToggle.isSelected());
    			tempUser.setManagecard(GB_CardManageToggle.isSelected());
    			
    			startHttpWork(ServiceEnum.CheckUserIsExist, HttpPostType.AsynchronousJson, tempUser, null, null);
    		}
        });

        GB_UserListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		newValue.setDeleteIcoVisible(true);
        		showSelectUserInfo();
        	}
        	if(oldValue != null)
        		oldValue.setDeleteIcoVisible(false);
        });
        
        myMessageListChangeListener = c -> {
        	while(c.next()){
				if(c.wasAdded()){
					for (Message message : c.getAddedSubList()) {
						switch (message.getWhat()) {
						
							case DeleteUser:
								boolean result = message.getObj();
								if(result)
									startHttpWork(ServiceEnum.ReadAllOtherUser, HttpPostType.AsynchronousJson, itsMe, null, null);
				    			else
				    				showLogsDialog("错误", "删除失败！");
								
								break;
								
							case SaveUser:
								tempUser = message.getObj();
				        		
				        		if(GB_ActionType.equals(MyUserActionEnum.ADD)){
				        			if(tempUser == null){
				        				showLogsDialog("错误", "添加失败！");
				        			}
				        			else{
				        				startHttpWork(ServiceEnum.ReadAllOtherUser, HttpPostType.AsynchronousJson, itsMe, null, null);
				        			}
				        		}
				        		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
				        			if(tempUser == null){
				        				showLogsDialog("错误", "修改失败！");
				        			}
				        			else{
				        				startHttpWork(ServiceEnum.ReadAllOtherUser, HttpPostType.AsynchronousJson, itsMe, null, null);
				        			}
				        		}
								break;
								
							case ReadAllOtherUser:
								upUserList(message.getObj());
								setUserInfoInStatus(MyUserActionEnum.NONE);
								break;
								
							case CheckUserIsExist:
								boolean result1 = message.getObj();
								if(GB_ActionType.equals(MyUserActionEnum.ADD)){
				        			if(result1){
				        				showLogsDialog("错误", "用户已存在，请检查！");
				            		}
				        			else{
				        				startHttpWork(ServiceEnum.SaveUser, HttpPostType.AsynchronousJson, tempUser, null, null);
				        			}
				        		}
				        		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
				        			if(result1){
				        				startHttpWork(ServiceEnum.SaveUser, HttpPostType.AsynchronousJson, tempUser, null, null);
				            		}
				        			else{
				        				showLogsDialog("错误", "用户不存在，请检查！");
				        			}
				        		}
								break;
							case ReadAllDepartment:
								List<Department> departments = message.getObj();
								GB_UserDepartmentCombox.getItems().setAll(departments);
								break;
								
							default:
								break;
						}
					}
					GB_FreshPane.setVisible(false);
				}
			}
        };
        
        this.setActivityName("审核人管理");
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
		itsMe = userSession.getUser();
		
		setMyMessagesList(FXCollections.observableArrayList());
		getMyMessagesList().addListener(myMessageListChangeListener);
		
		startHttpWork(ServiceEnum.ReadAllOtherUser, HttpPostType.AsynchronousJson, itsMe, null, null);
	}
	
	private void upUserList(List<User> userList) {
		GB_UserListView.getItems().clear();
		for (User user : userList) {
			ListViewCell tempListViewCell = new ListViewCell(user, deleteUserIco);
			GB_UserListView.getItems().add(tempListViewCell);
		}
		
		GB_UserListView.getSelectionModel().selectFirst();
	}
	
	private void setUserInfoInStatus(MyUserActionEnum status){
		GB_UserAccountTextField.setEditable(status.equals(MyUserActionEnum.ADD));
		GB_UserPassWordPassWordField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserNameTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserAgeTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserSexTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserPhoneTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserJobTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		GB_UserDescTextField.setEditable(!status.equals(MyUserActionEnum.NONE));
		
		GB_UserDepartmentCombox.setDisable(status.equals(MyUserActionEnum.NONE));
		GB_UserManageToggle.setDisable(status.equals(MyUserActionEnum.NONE));
		GB_ReportManageToggle.setDisable(status.equals(MyUserActionEnum.NONE));
		GB_DeviceManageToggle.setDisable(status.equals(MyUserActionEnum.NONE));
		GB_CardManageToggle.setDisable(status.equals(MyUserActionEnum.NONE));
		
		GB_SaveUserInfoButton.setVisible(!status.equals(MyUserActionEnum.NONE));
		
		GB_EditUserImageView.setVisible(status.equals(MyUserActionEnum.NONE));
		GB_CancelEditUserImageView.setVisible(status.equals(MyUserActionEnum.EDIT));
		GB_AddUserImageView.setVisible(status.equals(MyUserActionEnum.NONE));
		GB_CancelAddUserImageView.setVisible(status.equals(MyUserActionEnum.ADD));
	}

	
	private void clearUserInfo() {
		GB_UserAccountTextField.clear();;
		GB_UserPassWordPassWordField.clear();
		GB_UserNameTextField.clear();
		GB_UserAgeTextField.clear();
		GB_UserSexTextField.clear();
		GB_UserPhoneTextField.clear();
		GB_UserJobTextField.clear();
		GB_UserDescTextField.clear();
		GB_UserDepartmentCombox.getSelectionModel().select(null);
		GB_UserManageToggle.setSelected(false);
		GB_ReportManageToggle.setSelected(false);
		GB_DeviceManageToggle.setSelected(false);
		GB_CardManageToggle.setSelected(false);
	}
	
	private void showSelectUserInfo() {
		User user = (User) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();

		GB_UserAccountTextField.setText(user.getAccount());
		GB_UserPassWordPassWordField.setText(user.getPassword());
		GB_UserNameTextField.setText(user.getName());
		GB_UserAgeTextField.setText(user.getAge());
		GB_UserSexTextField.setText(user.getSex());
		GB_UserPhoneTextField.setText(user.getPhone());
		GB_UserJobTextField.setText(user.getJob());
		GB_UserDescTextField.setText(user.getDes());
		GB_UserDepartmentCombox.getSelectionModel().select(user.getDepartment());
		GB_UserManageToggle.setSelected(user.getManageuser());
		GB_ReportManageToggle.setSelected(user.getManagereport());
		GB_DeviceManageToggle.setSelected(user.getManagedevice());
		GB_CardManageToggle.setSelected(user.getManagecard());
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}
	
	class ListViewCell extends AnchorPane{
		
		ImageView imageView = null;
		
		public ListViewCell(User user, Image image){
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

	    		tempUser = (User) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();

    			startHttpWork(ServiceEnum.DeleteUser, HttpPostType.AsynchronousJson, tempUser, null, null);
			});
			AnchorPane.setTopAnchor(imageView, 0.0);
	        AnchorPane.setBottomAnchor(imageView, 0.0);
	        AnchorPane.setRightAnchor(imageView, 0.0);
			
			this.setUserData(user);
			this.getChildren().addAll(userName, imageView);
			//this.setMouseTransparent(true);
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
		getMyMessagesList().removeListener(myMessageListChangeListener);
		setMyMessagesList(null);
		
		itsMe = null;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}
}
