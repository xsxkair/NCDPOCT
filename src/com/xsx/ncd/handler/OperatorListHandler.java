
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
public class OperatorListHandler extends Activity{

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
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;
	
	@FXML VBox GB_FreshPane;

	private Operator currentOperator = null;
	private Operator tempOperator2 = null;
	private User itsMe = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;

	@Autowired HttpClientTool httpClientTool;
	@Autowired private UserSession userSession;
	
	@PostConstruct
	@Override
	public void onCreate(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/OperatorListPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/OperatorListPage.fxml");
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
        
        GB_UserNameTextField.editableProperty().bind(GB_CancelAddUserImageView.visibleProperty());
        GB_UserAgeTextField.editableProperty().bind(GB_CancelAddUserImageView.visibleProperty().or(GB_CancelEditUserImageView.visibleProperty()));
        GB_UserSexTextField.editableProperty().bind(GB_UserAgeTextField.editableProperty());
        GB_UserPhoneTextField.editableProperty().bind(GB_UserAgeTextField.editableProperty());
        GB_UserJobTextField.editableProperty().bind(GB_UserAgeTextField.editableProperty());
        GB_UserDescTextField.editableProperty().bind(GB_UserAgeTextField.editableProperty());
        GB_UserDepartmentCombox.disableProperty().bind(GB_CancelAddUserImageView.visibleProperty().not());
        GB_OperatorRightToggle.disableProperty().bind(GB_CancelAddUserImageView.visibleProperty().or(GB_CancelEditUserImageView.visibleProperty()).not());
        GB_SaveUserInfoButton.visibleProperty().bind(GB_CancelAddUserImageView.visibleProperty().or(GB_CancelEditUserImageView.visibleProperty()));

        GB_EditUserImageView.disableProperty().bind(GB_UserListView.getSelectionModel().selectedItemProperty().isNull());
        GB_EditUserImageView.setOnMouseClicked((e)->{
        	GB_EditUserImageView.setVisible(false);
        	GB_CancelEditUserImageView.setVisible(true);
        	GB_AddUserImageView.setVisible(false);
        	GB_CancelAddUserImageView.setVisible(false);
        	currentOperator = (Operator) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();
        	startHttpWork(ServiceEnum.ReadAllDepartment, HttpPostType.AsynchronousJson, null, null, null);
        });
        
        GB_CancelEditUserImageView.setOnMouseClicked((e)->{
        	startHttpWork(ServiceEnum.ReadAllOperator, HttpPostType.AsynchronousJson, null, null, null);
        	currentOperator = null;
        	GB_EditUserImageView.setVisible(true);
        	GB_CancelEditUserImageView.setVisible(false);
        	GB_AddUserImageView.setVisible(true);
        	GB_CancelAddUserImageView.setVisible(false);
        });
        
        GB_AddUserImageView.setOnMouseClicked((e)->{
        	clearUserInfo();
        	GB_EditUserImageView.setVisible(false);
        	GB_CancelEditUserImageView.setVisible(false);
        	GB_AddUserImageView.setVisible(false);
        	GB_CancelAddUserImageView.setVisible(true);
        	currentOperator = new Operator();
        	startHttpWork(ServiceEnum.ReadAllDepartment, HttpPostType.AsynchronousJson, null, null, null);
        });
        GB_CancelAddUserImageView.setOnMouseClicked((e)->{
        	GB_EditUserImageView.setVisible(true);
        	GB_CancelEditUserImageView.setVisible(false);
        	GB_AddUserImageView.setVisible(true);
        	GB_CancelAddUserImageView.setVisible(false);
        	currentOperator = null;
        	startHttpWork(ServiceEnum.ReadAllOperator, HttpPostType.AsynchronousJson, null, null, null);
        });
        
        myMessageListChangeListener = c ->{
        	while(c.next()){
				if(c.wasAdded()){
					GB_FreshPane.setVisible(false);
					for (Message message : c.getAddedSubList()) {
						switch (message.getWhat()) {
						case DeleteOperator:
							boolean result = message.getObj();
							if(result){
								startHttpWork(ServiceEnum.ReadAllOperator, HttpPostType.AsynchronousJson, null, null, null);
		            		}
		        			else{
		        				showLogsDialog("错误", "删除失败！");
		        			}
							break;
						case SaveOperator:
							tempOperator2 = message.getObj();
		            		
		            		if(tempOperator2 == null){
		            			showLogsDialog("错误", "添加失败！");
		            		}
		            		else{
		            			startHttpWork(ServiceEnum.ReadAllOperator, HttpPostType.AsynchronousJson, null, null, null);
		            		}
		            		
							break;
						case ReadAllOperator:
							upUserList(message.getObj());
							break;
						case ReadAllDepartment:
							List<Department> departments = message.getObj();
							GB_UserDepartmentCombox.getItems().setAll(departments);
							break;
						default:
							break;
						}
					}
				}
			}
        };
        
		GB_FreshPane.setVisible(false);
        
        //取消修改密码
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        //添加或修改
        GB_SaveUserInfoButton.disableProperty().bind(GB_UserNameTextField.lengthProperty().lessThan(1).
        		or(GB_UserDepartmentCombox.getSelectionModel().selectedItemProperty().isNull()));
        GB_SaveUserInfoButton.setOnAction((e)->{
        	if(currentOperator == null){
        		showLogsDialog("错误", "对象为空");
        	}
        	else{
        		currentOperator.setName(GB_UserNameTextField.getText());
        		currentOperator.setAge(GB_UserAgeTextField.getText());
        		currentOperator.setSex(GB_UserSexTextField.getText());
        		currentOperator.setPhone(GB_UserPhoneTextField.getText());
        		currentOperator.setJob(GB_UserJobTextField.getText());
        		currentOperator.setDes(GB_UserDescTextField.getText());
        		currentOperator.setDepartment(GB_UserDepartmentCombox.getSelectionModel().getSelectedItem());
        		currentOperator.setChecked(GB_OperatorRightToggle.isSelected());
        	}
        	startHttpWork(ServiceEnum.SaveOperator, HttpPostType.AsynchronousJson, currentOperator, null, null);
        });
        
        GB_UserListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	
        	if(newValue != null){
        		newValue.setDeleteIcoVisible(true);
        		showSelectUserInfo();
        		
        		GB_EditUserImageView.setVisible(true);
            	GB_CancelEditUserImageView.setVisible(false);
            	GB_AddUserImageView.setVisible(true);
            	GB_CancelAddUserImageView.setVisible(false);
        	}
        	
        	if(oldValue != null)
        		oldValue.setDeleteIcoVisible(false);
        });
        
        this.setActivityName("操作人管理");
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
		super.onStart(object);
		getMyMessagesList().addListener(myMessageListChangeListener);
		
		itsMe = userSession.getUser();
		startHttpWork(ServiceEnum.ReadAllOperator, HttpPostType.AsynchronousJson, itsMe, null, null);
	}
	
	private void upUserList(List<Operator> userList) {

		GB_UserListView.getItems().clear();
		for (Operator user : userList) {
			ListViewCell tempListViewCell = new ListViewCell(user, deleteUserIco);
			GB_UserListView.getItems().add(tempListViewCell);
		}

		GB_UserListView.getSelectionModel().selectFirst();
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
				currentOperator = (Operator) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();
    			startHttpWork(ServiceEnum.DeleteOperator, HttpPostType.AsynchronousJson, currentOperator, null, null);
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
		itsMe = null;
		
		getMyMessagesList().removeListener(myMessageListChangeListener);
		
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

}
