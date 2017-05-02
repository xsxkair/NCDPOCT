package com.xsx.ncd.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.handler.LoginHandler.LoginService.LoginTask;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpUtils;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;

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
	@FXML TextField GB_UserDepaTextField;
	@FXML TextField GB_UserDescTextField;
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
	
	private User itsMe = null;
	private User modifyUser = null;
	private User tempUser = null;
	private MyService myService = null;

	ContextMenu myContextMenu;
	MenuItem RefreshMenuItem;
	
	@Autowired private UserSession userSession;
	@Autowired private ActivitySession activitySession;
	@Autowired private HttpUtils httpUtils;
	@Autowired private UserListHandler userListHandler;
	
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
        
        myService = new MyService();
        myService.runningProperty().addListener((o, oldValue, newValue)->{

    		if(newValue){
    			rootpane.setCursor(Cursor.WAIT);
    		}
    		else{
    			tempUser = myService.getValue();
    			if(tempUser == null){
    				showLogsDialog("错误", "修改失败！");
    			}
    			else{
    				userSession.setUser(tempUser);
    				itsMe = tempUser;
    				setEditable(false);
    			}
    			
    			rootpane.setCursor(Cursor.DEFAULT);
    		}	
    	});
        
        modifyUser = new User();
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
				//itsMe.setDepartment(GB_UserDepaTextField.getText());
				itsMe.setDes(GB_UserDescTextField.getText());
				
				myService.restart();
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
				myService.restart();
			}
		});
        
        //取消修改密码
        cancelButton1.setOnMouseClicked((e)->{
        	modifyUserPasswordDialog.close();
		});
        
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        RefreshMenuItem = new MenuItem("刷新");
        RefreshMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				upUserInfo();
			}
		});
        myContextMenu = new ContextMenu(RefreshMenuItem);
        
        GB_UserManageButton.setOnAction((e)->{
        	userListHandler.startActivity(null);
        });
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(rootpane.equals(newValue)){
        		itsMe = userSession.getUser();
        		setEditable(false);
        		upUserInfo();
        		
        		GB_UserManageButton.setVisible(itsMe.getManageuser());
        		GB_OperatorManageButton.setVisible(itsMe.getManageuser());
        	}
        	else if(rootpane.equals(oldValue)){
        		itsMe = null;
        	}
        });
        
        rootpane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if(event.getButton().equals(MouseButton.SECONDARY)){
						myContextMenu.show(rootpane, event.getScreenX(), event.getScreenY());
				}
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
		GB_SaveUserInfoButton.setVisible(editable);
		GB_UserNameTextField.setEditable(editable);
		GB_UserAgeTextField.setEditable(editable);
		GB_UserSexTextField.setEditable(editable);
		GB_UserPhoneTextField.setEditable(editable);
		GB_UserJobTextField.setEditable(editable);
		GB_UserDepaTextField.setEditable(editable);
		GB_UserDescTextField.setEditable(editable);
		
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
		GB_UserDepaTextField.setText(itsMe.getDepartment().getName());
		GB_UserDescTextField.setText(itsMe.getDes());
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
	
	class MyService extends Service<User>{

		@Override
		protected Task<User> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<User>{

			@Override
			protected User call() {
				// TODO Auto-generated method stub
				return httpUtils.SaveUser(itsMe);
			}
		}
	}
}
