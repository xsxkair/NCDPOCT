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
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

@Component
public class MyInfoHandler implements ActivityTemplet{

	private AnchorPane rootpane;
	private final String activityName = "我的信息";
	
	@FXML StackPane rootStackPane;
	
	@FXML Button GB_ModifyUserPassWordButton;
	@FXML Button GB_SaveUserInfoButton;
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
	@FXML StackPane GB_ModifyIcoStackPane;
	@FXML ImageView editUserInfoImageView;
	private SVGGlyph cancelModifySvg;
	
	@FXML JFXButton GB_UserManageButton;
	@FXML JFXButton GB_OperatorManageButton;
	
	@FXML JFXDialog modifyUserInfoDialog;
	@FXML PasswordField userPasswordTextField;
	@FXML JFXButton acceptButton0;
	@FXML JFXButton cancelButton0;
	
	@FXML JFXDialog modifyUserPasswordDialog;
	@FXML PasswordField userNewPasswordTextField0;
	@FXML PasswordField userNewPasswordTextField1;
	@FXML PasswordField userOldPasswordTextField;
	@FXML JFXButton acceptButton1;
	@FXML JFXButton cancelButton1;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;
	
	@FXML VBox GB_FreshPane;
	
	private User itsMe = null;
	private User tempUser = null;
	private List<Department> tempDepartmentList = null;
	
	private ChangeListener<Boolean> httpUtilsServiceChangeListener = null;
	@Autowired private UserSession userSession;
	@Autowired private ActivitySession activitySession;
	@Autowired private HttpUtils httpUtils;
	@Autowired private UserListHandler userListHandler;
	@Autowired OperatorListHandler operatorListHandler;
	
	@PostConstruct
	@Override
	public void UI_Init(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/MyInfoPagefxml.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/MyInfoPagefxml.fxml");
        loader.setController(this);
        try {
        	rootpane = loader.load(in);
        	SVGGlyphLoader.clear();

			SVGGlyphLoader.loadGlyphsFont(this.getClass().getResourceAsStream("/RES/icomoon.svg"), "icomoon.svg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(modifyUserInfoDialog);
        rootStackPane.getChildren().remove(modifyUserPasswordDialog);
        rootStackPane.getChildren().remove(LogDialog);
        
        cancelModifySvg = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.close, remove, times");
        cancelModifySvg.setPrefSize(27, 27);
        cancelModifySvg.setFill(Color.RED);
        GB_ModifyIcoStackPane.getChildren().add(cancelModifySvg);
        
        editUserInfoImageView.setOnMouseClicked(e->{
        	httpUtils.startHttpService(ServiceEnum.ReadAllDepartment, null);
        	setEditable(true);
        });
        
        cancelModifySvg.setOnMouseClicked(e->{
        	setEditable(false);
        });
        
        GB_SaveUserInfoButton.setOnAction((e)->{
        	userPasswordTextField.clear();
    		modifyUserInfoDialog.setTransitionType(DialogTransition.CENTER);
    		modifyUserInfoDialog.show(rootStackPane);
        });
        
        GB_FreshPane.visibleProperty().bind(httpUtils.runningProperty());
        httpUtilsServiceChangeListener = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(!newValue){
	    			if(httpUtils.getServiceEnum().equals(ServiceEnum.SaveUser)){
	    				tempUser = (User) httpUtils.getValue();
		    			if(tempUser == null){
		    				showLogsDialog("错误", "修改失败！");
		    			}
		    			else{
		    				userSession.setUser(tempUser);
		    				itsMe = tempUser;
		    				setEditable(false);
		    			}
	    			}
	    			else if(httpUtils.getServiceEnum().equals(ServiceEnum.ReadAllDepartment)){
	    				tempDepartmentList = (List<Department>) httpUtils.getValue();
		    			GB_UserDepartmentCombox.getItems().setAll(tempDepartmentList);
	    			}
	    		}
			}
        };

        //确认修改个人信息
        acceptButton0.disableProperty().bind(userPasswordTextField.lengthProperty().lessThan(6));
        acceptButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();

			if(userPasswordTextField.getText().equals(itsMe.getPassword())){
				itsMe.setName(GB_UserNameTextField.getText());
				itsMe.setAge(GB_UserAgeTextField.getText());
				itsMe.setSex(GB_UserSexTextField.getText());
				itsMe.setPhone(GB_UserPhoneTextField.getText());
				itsMe.setJob(GB_UserJobTextField.getText());
				itsMe.setDepartment(GB_UserDepartmentCombox.getSelectionModel().getSelectedItem());
				itsMe.setDes(GB_UserDescTextField.getText());
				itsMe.setManagecard(GB_CardManageToggle.isSelected());
				itsMe.setManageuser(GB_UserManageToggle.isSelected());
				itsMe.setManagereport(GB_ReportManageToggle.isSelected());
				itsMe.setManagedevice(GB_DeviceManageToggle.isSelected());
				httpUtils.startHttpService(ServiceEnum.SaveUser, itsMe);
			}
			else
				showLogsDialog("错误", "密码错误，禁止修改！");
		});
        
        //取消修改个人信息
        cancelButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();
		});
        
        GB_ModifyUserPassWordButton.setOnAction((e)->{
        	userNewPasswordTextField0.clear();
    		userNewPasswordTextField1.clear();
    		userOldPasswordTextField.clear();
    		modifyUserPasswordDialog.setTransitionType(DialogTransition.CENTER);
    		modifyUserPasswordDialog.show(rootStackPane);
        });
        
        //确认修改密码
        acceptButton1.disableProperty().bind(userOldPasswordTextField.lengthProperty().lessThan(6).
        		or(userNewPasswordTextField0.lengthProperty().lessThan(6)).
        		or(userNewPasswordTextField1.lengthProperty().lessThan(6)));
        acceptButton1.setOnMouseClicked((e)->{
        	modifyUserPasswordDialog.close();
			
			if(!userOldPasswordTextField.getText().equals(itsMe.getPassword()))
				showLogsDialog("错误", "密码错误，禁止修改！");
			else if (!userNewPasswordTextField0.getText().equals(userNewPasswordTextField1.getText())) {
				showLogsDialog("错误", "新密码必须一致！");
			}
			else {
				itsMe.setPassword(userNewPasswordTextField0.getText());
				httpUtils.startHttpService(ServiceEnum.SaveUser, itsMe);
			}
		});
        
        //取消修改密码
        cancelButton1.setOnMouseClicked((e)->{
        	modifyUserPasswordDialog.close();
		});
        
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        GB_UserManageButton.setOnAction((e)->{
        	userListHandler.startActivity(null);
        });
        
        GB_OperatorManageButton.setOnAction((e)->{
        	operatorListHandler.startActivity(null);
        });
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(rootpane.equals(newValue)){
        		httpUtils.runningProperty().addListener(httpUtilsServiceChangeListener);
        		itsMe = userSession.getUser();
        		setEditable(false);
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
	
	private void setEditable(boolean editable) {
		itsMe = userSession.getUser();
		
		GB_SaveUserInfoButton.setVisible(editable);
		GB_UserNameTextField.setEditable(editable);
		GB_UserAgeTextField.setEditable(editable);
		GB_UserSexTextField.setEditable(editable);
		GB_UserPhoneTextField.setEditable(editable);
		GB_UserJobTextField.setEditable(editable);
		GB_UserDescTextField.setEditable(editable);
		
		if(itsMe.getManageuser()){
			GB_UserDepartmentCombox.setDisable(!editable);
			GB_UserManageToggle.setDisable(!editable);
			GB_ReportManageToggle.setDisable(!editable);
			GB_DeviceManageToggle.setDisable(!editable);
			GB_CardManageToggle.setDisable(!editable);
		}
		else{
			GB_UserDepartmentCombox.setDisable(true);
			GB_UserManageToggle.setDisable(true);
			GB_ReportManageToggle.setDisable(true);
			GB_DeviceManageToggle.setDisable(true);
			GB_CardManageToggle.setDisable(true);
		}
		
		GB_UserManageButton.setVisible(itsMe.getManageuser());
		GB_OperatorManageButton.setVisible(itsMe.getManageuser());
		
		if(editable){
			editUserInfoImageView.setVisible(false);
			cancelModifySvg.setVisible(true);
		}
		else{
			upUserInfo();
			editUserInfoImageView.setVisible(true);
			cancelModifySvg.setVisible(false);
		}
	}
	
	private void upUserInfo() {
		
		GB_UserNameTextField.setText(itsMe.getName());
		GB_UserAgeTextField.setText(itsMe.getAge());
		GB_UserSexTextField.setText(itsMe.getSex());
		GB_UserPhoneTextField.setText(itsMe.getPhone());
		GB_UserJobTextField.setText(itsMe.getJob());
		GB_UserDepartmentCombox.getSelectionModel().select(itsMe.getDepartment());
		GB_UserDescTextField.setText(itsMe.getDes());
		
		GB_UserManageToggle.setSelected(itsMe.getManageuser());
		GB_ReportManageToggle.setSelected(itsMe.getManagereport());
		GB_DeviceManageToggle.setSelected(itsMe.getManagedevice());
		GB_CardManageToggle.setSelected(itsMe.getManagecard());
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}

	@Override
	public void startActivity(Object object) {
		// TODO Auto-generated method stub
		activitySession.setRootActivity(this);
		activitySession.setActivityPane(rootpane);
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
		return activityName;
	}
}
