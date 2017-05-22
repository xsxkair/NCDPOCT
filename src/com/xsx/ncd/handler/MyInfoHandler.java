package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

@Component
public class MyInfoHandler extends Activity {
	
	@FXML StackPane rootStackPane;
	
	@FXML Button GB_ModifyUserPassWordButton;
	@FXML Button GB_SaveUserInfoButton;
	@FXML TextField GB_UserNameTextField;
	@FXML TextField GB_UserAgeTextField;
	@FXML TextField GB_UserSexTextField;
	@FXML TextField GB_UserPhoneTextField;
	@FXML TextField GB_UserJobTextField;
	@FXML TextField GB_UserDepartmentTextField;
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
	private ChangeListener<User> myUserListener = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	
	@Autowired private UserSession userSession;
	@Autowired private ActivitySession activitySession;
	@Autowired HttpClientTool httpClientTool;
	@Autowired private UserListHandler userListHandler;
	@Autowired OperatorListHandler operatorListHandler;
	
	@PostConstruct
	@Override
	public void onCreate(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/MyInfoPagefxml.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/MyInfoPagefxml.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
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
        	setEditable(true);
        });
        
        cancelModifySvg.setOnMouseClicked(e->{
        	setEditable(false);
        });
        
        GB_SaveUserInfoButton.setOnAction((e)->{
        	userPasswordTextField.clear();
    		modifyUserInfoDialog.show(rootStackPane);
        });

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
				itsMe.setDes(GB_UserDescTextField.getText());

				startHttpWork(ServiceEnum.SaveUser, HttpPostType.AsynchronousJson, itsMe, null, GB_FreshPane);
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
				startHttpWork(ServiceEnum.SaveUser, HttpPostType.AsynchronousJson, itsMe, null, GB_FreshPane);
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
        	activitySession.startActivity(userListHandler, null);
        });
        
        myMessageListChangeListener = new ListChangeListener<Message>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				// TODO Auto-generated method stub
				while(c.next()){
					if(c.wasAdded()){
						for (Message message : c.getAddedSubList()) {
							switch (message.getWhat()) {
							
								case SaveUser:
									userSession.setUser(message.getObj());
									break;
									
								default:
									break;
							}
						}
						
						GB_FreshPane.setVisible(false);
					}
				}
			}
        	
        };
        
        GB_OperatorManageButton.setOnAction((e)->{
        	activitySession.startActivity(operatorListHandler, null);
        });
        
        myUserListener = (o, oldValue, newValue)->{
        	itsMe = newValue;
        	setEditable(false);
        	upUserInfo();
        };
        
        this.setActivityName("我的信息");
        this.setActivityStatus(ActivityStatusEnum.Create);
        
        AnchorPane.setTopAnchor(getRootPane(), 0.0);
        AnchorPane.setBottomAnchor(getRootPane(), 0.0);
        AnchorPane.setLeftAnchor(getRootPane(), 0.0);
        AnchorPane.setRightAnchor(getRootPane(), 0.0);
        
        loader = null;
        in = null;
	}
	
	private void setEditable(boolean editable) {
		
		GB_SaveUserInfoButton.setVisible(editable);
		GB_UserNameTextField.setEditable(editable);
		GB_UserAgeTextField.setEditable(editable);
		GB_UserSexTextField.setEditable(editable);
		GB_UserPhoneTextField.setEditable(editable);
		GB_UserJobTextField.setEditable(editable);
		GB_UserDescTextField.setEditable(editable);
		
		if(editable){
			editUserInfoImageView.setVisible(false);
			cancelModifySvg.setVisible(true);
		}
		else{
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
		GB_UserDepartmentTextField.setText(itsMe.getDepartment().getName());
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
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		setMyMessagesList(FXCollections.observableArrayList());
		getMyMessagesList().addListener(myMessageListChangeListener);
		
		userSession.getUserProperty().addListener(myUserListener);
		itsMe = userSession.getUser();
		setEditable(false);
		upUserInfo();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		userSession.getUserProperty().removeListener(myUserListener);
		getMyMessagesList().removeListener(myMessageListChangeListener);
		setMyMessagesList(null);
		itsMe = null;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

}
