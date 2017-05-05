
package com.xsx.ncd.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.omg.PortableServer.IMPLICIT_ACTIVATION_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.MyUserActionEnum;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpUtils;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

@Component
public class UserListHandler implements ActivityTemplet{

	private AnchorPane rootpane;
	
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
	
	@FXML JFXDialog modifyUserInfoDialog;
	@FXML PasswordField userPasswordTextField;
	@FXML JFXButton acceptButton0;
	@FXML JFXButton cancelButton0;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;
	
	@FXML VBox GB_FreshPane;

	private List<User> myUserList;
	private MyUserActionEnum GB_ActionType = MyUserActionEnum.NONE;
	private User itsMe = null;
	private ListViewCell selectListViewCell = null;
	private User tempUser = null;
	
	private User deleteUser = null;
	private String editUserAccount = null;
	private String addUserAccount = null;
	
	private ChangeListener<Boolean> httpUtilsServiceChangeListener = null;

	@Autowired private UserSession userSession;
	@Autowired private ActivitySession activitySession;
	@Autowired private HttpUtils httpUtils;
	
	@PostConstruct
	@Override
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/UserListPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/UserListPage.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        deleteUserIco = new Image(this.getClass().getResourceAsStream("/RES/deleteUserIco.png"));
        rootStackPane.getChildren().remove(modifyUserInfoDialog);
        rootStackPane.getChildren().remove(LogDialog);
        
        GB_EditUserImageView.setOnMouseClicked((e)->{
        	if(GB_ActionType.equals(MyUserActionEnum.ADD))
        		setInAddStatus(false);
        	
        	if(GB_UserListView.getSelectionModel().getSelectedItem() != null){
        		setEnableEdit(true);
            	httpUtils.startHttpService(ServiceEnum.ReadAllDepartment, null);
            	GB_ActionType = MyUserActionEnum.EDIT;
        	}
        	else{
        		showLogsDialog("¥ÌŒÛ", "—°‘ÒŒ™ø’£°");
        	}
        });
        
        GB_CancelEditUserImageView.setOnMouseClicked((e)->{
        	setEnableEdit(false);
        	GB_ActionType = MyUserActionEnum.NONE;
        });
        
        GB_AddUserImageView.setOnMouseClicked((e)->{
        	if(GB_ActionType.equals(MyUserActionEnum.EDIT))
        		setEnableEdit(false);
        	
        	GB_ActionType = MyUserActionEnum.ADD;
        	setInAddStatus(true);
        	httpUtils.startHttpService(ServiceEnum.ReadAllDepartment, null);
        });
        GB_CancelAddUserImageView.setOnMouseClicked((e)->{
        	GB_ActionType = MyUserActionEnum.NONE;
        	setInAddStatus(false);
        });
        
		GB_FreshPane.visibleProperty().bind(httpUtils.runningProperty());
		httpUtilsServiceChangeListener = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue)
	        		rootpane.setCursor(Cursor.WAIT);
	        	else{
	        		rootpane.setCursor(Cursor.DEFAULT);
	        		
	        		if(httpUtils.getServiceEnum().equals(ServiceEnum.DeleteUser)){
	        			if((Boolean)httpUtils.getValue()){
	        				httpUtils.startHttpService(ServiceEnum.ReadAllOtherUser, itsMe);
	            		}
	        			else{
	        				showLogsDialog("¥ÌŒÛ", "…æ≥˝ ß∞‹£°");
	        			}
	        		}
	        		else if(httpUtils.getServiceEnum().equals(ServiceEnum.SaveUser)){
	        			tempUser = (User) httpUtils.getValue();
	            		
	            		if(GB_ActionType.equals(MyUserActionEnum.ADD)){
	            			if(tempUser == null){
	            				showLogsDialog("¥ÌŒÛ", "ÃÌº” ß∞‹£°");
	            			}
	            			else{
	            				setInAddStatus(false);
	            				httpUtils.startHttpService(ServiceEnum.ReadAllOtherUser, itsMe);
	            			}
	            			
	            		}
	            		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
	            			if(tempUser == null){
	            				showLogsDialog("¥ÌŒÛ", "–ﬁ∏ƒ ß∞‹£°");
	            			}
	            			else{
	            				setEnableEdit(false);
	            				httpUtils.startHttpService(ServiceEnum.ReadAllOtherUser, itsMe);
	            			}
	            		}
	        		}
	        		else if(httpUtils.getServiceEnum().equals(ServiceEnum.CheckUserIsExist)){
	        			if(GB_ActionType.equals(MyUserActionEnum.ADD)){

	            			if((Boolean)httpUtils.getValue()){
	            				showLogsDialog("¥ÌŒÛ", "”√ªß“—¥Ê‘⁄£¨«ÎºÏ≤È£°");
	                		}
	            			else{
	            				httpUtils.startHttpService(ServiceEnum.SaveUser, tempUser);
	            			}
	            		}
	            		else if(GB_ActionType.equals(MyUserActionEnum.EDIT)){
	            			if((Boolean)httpUtils.getValue()){
	            				httpUtils.startHttpService(ServiceEnum.SaveUser, tempUser);
	                		}
	            			else{
	            				showLogsDialog("¥ÌŒÛ", "”√ªß≤ª¥Ê‘⁄£¨«ÎºÏ≤È£°");
	            			}
	            		}
	        		}
	        		else if(httpUtils.getServiceEnum().equals(ServiceEnum.ReadAllOtherUser)){
	        			upUserList((List<User>) httpUtils.getValue());
	        		}
	        		else if(httpUtils.getServiceEnum().equals(ServiceEnum.ReadAllDepartment)){
		    			GB_UserDepartmentCombox.getItems().setAll((List<Department>) httpUtils.getValue());
	    			}
	        	}
			}
        };
        
        //»®œﬁ»∑»œ
        acceptButton0.disableProperty().bind(userPasswordTextField.lengthProperty().lessThan(6));
        acceptButton0.setOnAction((e)->{
        	modifyUserInfoDialog.close();
        	if(userPasswordTextField.getText().equals(itsMe.getPassword())){
        		if(GB_ActionType.equals(MyUserActionEnum.DELETE)){
        			deleteUser = (User) GB_UserListView.getSelectionModel().getSelectedItem().getUserData();

        			httpUtils.startHttpService(ServiceEnum.DeleteUser, deleteUser);
        		}
        		else if(GB_ActionType.equals(MyUserActionEnum.ADD)){
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

        			httpUtils.startHttpService(ServiceEnum.CheckUserIsExist, tempUser);
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
        			
        			httpUtils.startHttpService(ServiceEnum.CheckUserIsExist, tempUser);
        		}
        	}
        	else
        		showLogsDialog("¥ÌŒÛ", "√‹¬Î¥ÌŒÛ£¨Ω˚÷π≤Ÿ◊˜£°");
        });
        cancelButton0.setOnAction((e)->{
        	modifyUserInfoDialog.close();
        });
        
        //»°œ˚–ﬁ∏ƒ√‹¬Î
        
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        //ÃÌº”ªÚ–ﬁ∏ƒ
        GB_SaveUserInfoButton.disableProperty().bind(GB_UserAccountTextField.lengthProperty().lessThan(1).
        		or(GB_UserPassWordPassWordField.lengthProperty().lessThan(6)).
        		or(GB_UserNameTextField.lengthProperty().lessThan(1)));
        GB_SaveUserInfoButton.setOnAction((e)->{
			userPasswordTextField.clear();
    		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
    		modifyUserInfoDialog.show(rootStackPane);
        });
        
        GB_UserListView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		newValue.setDeleteIcoVisible(true);
        		setEnableEdit(false);
        	}
        	if(oldValue != null)
        		oldValue.setDeleteIcoVisible(false);
        });
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(rootpane.equals(newValue)){
        		itsMe = userSession.getUser();
        		httpUtils.runningProperty().addListener(httpUtilsServiceChangeListener);
        		httpUtils.startHttpService(ServiceEnum.ReadAllOtherUser, itsMe);
        	}
        	else {
        		itsMe = null;
        		httpUtils.runningProperty().removeListener(httpUtilsServiceChangeListener);
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
	public void startActivity(Object object) {
		// TODO Auto-generated method stub
		activitySession.setFatherActivity(this);
		activitySession.setActivityPane(this);
	}
	
	private void upUserList(List<User> userList) {

		GB_UserListView.getItems().clear();
		for (User user : userList) {
			ListViewCell tempListViewCell = new ListViewCell(user, deleteUserIco);
			GB_UserListView.getItems().add(tempListViewCell);
		}

		GB_UserListView.getSelectionModel().selectFirst();
	}
	
	private void setEnableEdit(boolean editable) {
		GB_UserAccountTextField.setEditable(false);
		GB_UserPassWordPassWordField.setEditable(editable);
		GB_UserNameTextField.setEditable(editable);
		GB_UserAgeTextField.setEditable(editable);
		GB_UserSexTextField.setEditable(editable);
		GB_UserPhoneTextField.setEditable(editable);
		GB_UserJobTextField.setEditable(editable);
		GB_UserDescTextField.setEditable(editable);
		
		GB_UserDepartmentCombox.setDisable(!editable);
		GB_UserManageToggle.setDisable(!editable);
		GB_ReportManageToggle.setDisable(!editable);
		GB_DeviceManageToggle.setDisable(!editable);
		GB_CardManageToggle.setDisable(!editable);
		
		GB_SaveUserInfoButton.setVisible(editable);
		
		if(editable){
			GB_EditUserImageView.setVisible(false);
			GB_CancelEditUserImageView.setVisible(true);
		}
		else{
			GB_EditUserImageView.setVisible(true);
			GB_CancelEditUserImageView.setVisible(false);
			showSelectUserInfo();
		}			
	}
	
	private void setInAddStatus(boolean editable) {
		GB_UserAccountTextField.setEditable(editable);
		GB_UserPassWordPassWordField.setEditable(editable);
		GB_UserNameTextField.setEditable(editable);
		GB_UserAgeTextField.setEditable(editable);
		GB_UserSexTextField.setEditable(editable);
		GB_UserPhoneTextField.setEditable(editable);
		GB_UserJobTextField.setEditable(editable);
		GB_UserDescTextField.setEditable(editable);
		
		GB_UserDepartmentCombox.setDisable(!editable);
		GB_UserManageToggle.setDisable(!editable);
		GB_ReportManageToggle.setDisable(!editable);
		GB_DeviceManageToggle.setDisable(!editable);
		GB_CardManageToggle.setDisable(!editable);
		
		GB_SaveUserInfoButton.setVisible(editable);
		
		if(editable){
			GB_AddUserImageView.setVisible(false);
			GB_CancelAddUserImageView.setVisible(true);
			clearUserInfo();
		}
		else{
			GB_AddUserImageView.setVisible(true);
			GB_CancelAddUserImageView.setVisible(false);
			showSelectUserInfo();
		}			
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
				userPasswordTextField.clear();
	    		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
	    		modifyUserInfoDialog.show(rootStackPane);
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
		return "…Û∫À»Àπ‹¿Ì";
	}

	@Override
	public Pane getActivityRootPane() {
		// TODO Auto-generated method stub
		return rootpane;
	}

	@Override
	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		
	}
}
