package com.xsx.ncd.handler;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
public class LoginHandler {

	private Stage s_Stage;
	private AnchorPane root = null;
	private Scene s_Scene;
	
	@FXML StackPane GB_RootStackPane;
	
	@FXML JFXDialog GB_LogDialog;
	@FXML Label logHeadLabel;
	@FXML Label logContentLabel;
	@FXML JFXButton acceptButton;
	
	@FXML TextField UserNameText;
	@FXML PasswordField UserPasswordText;
	@FXML JFXButton LoginButton;
	
	@FXML ImageView	GB_CloseButton;
	
	private Image closeImage1 = null;
	private Image closeImage2 = null;
	
	private double initX;
    private double initY;
	
    private User loginUser = null;
	private User tempuser = null;
	
	private LoginService loginService = null;
	
	@Autowired HttpClientTool httpClientTool;;
	@Autowired private UserSession userSession;
	@Autowired private UIFrameworkHandler uiFrameworkHandler;
	
	@PostConstruct
	public void UI_Init() {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/LoginFXML.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/LoginFXML.fxml");
        loader.setController(this);
        
        try {
        	root = loader.load(in);
        	in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        GB_RootStackPane.getChildren().remove(GB_LogDialog);
        
        s_Scene = new Scene(root, Color.TRANSPARENT);
		
        root.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

                initX = me.getScreenX() - s_Stage.getX();

                initY = me.getScreenY() - s_Stage.getY();
            }
        });

    	root.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {

            	s_Stage.setX(me.getScreenX() - initX);

            	s_Stage.setY(me.getScreenY() - initY);
            }
        });
    	
    	loginService = new LoginService();
    	loginService.runningProperty().addListener((o, oldValue, newValue)->{
    		if(newValue)
    			root.setCursor(Cursor.WAIT);
    		else{
    			root.setCursor(Cursor.DEFAULT);
    			
    			tempuser = loginService.getValue();
    			if(tempuser == null)
    				showLogs("����", "��¼ʧ�ܣ����飡");
    			else{
    				userSession.setUser(tempuser);
    				s_Stage.hide();
    				uiFrameworkHandler.startMainUI();
    			}
    		}
    	});
    	
    	LoginButton.disableProperty().bind(UserNameText.lengthProperty().lessThan(1).
    			or(UserPasswordText.lengthProperty().lessThan(6)).or(loginService.runningProperty()));
    	
    	loginUser = new User();
    	LoginButton.setOnAction((e)->{
    		loginUser.setAccount(UserNameText.getText());
			loginUser.setPassword(UserPasswordText.getText());
			loginService.restart();
    	});
        
        closeImage1 = new Image(this.getClass().getResourceAsStream("/RES/close1.png"));
        closeImage2 = new Image(this.getClass().getResourceAsStream("/RES/close2.png"));
        
        GB_CloseButton.setOnMouseEntered((e)->{
        	GB_CloseButton.setImage(closeImage2);
        });
        
        GB_CloseButton.setOnMouseExited((e)->{
        	GB_CloseButton.setImage(closeImage1);
        });
        
        GB_CloseButton.setOnMouseClicked((e)->{
        	System.exit(0);
        });
        
        acceptButton.setOnAction((e)->{
        	GB_LogDialog.close();
        });
        
        loader = null;
        in = null;
	}
	
	public void startLoginActivity() {
		if(s_Stage == null){
			s_Stage = new Stage();
			s_Stage.initStyle(StageStyle.TRANSPARENT);
			s_Stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
			s_Stage.setResizable(false);
			s_Stage.setScene(s_Scene);
		}

		UserPasswordText.setText(null);

		s_Stage.show();
	}
	
	private void showLogs(String logHead, String logContent) {
		logHeadLabel.setText(logHead);
		logContentLabel.setText(logContent);
		GB_LogDialog.show(GB_RootStackPane);
	}
	
	class LoginService extends Service<User>{

		@Override
		protected Task<User> createTask() {
			// TODO Auto-generated method stub
			return new LoginTask();
		}
		
		class LoginTask extends Task<User>{

			@Override
			protected User call() throws Exception {
				// TODO Auto-generated method stub
				return httpClientTool.myHttpPost(null, ServiceEnum.Login, HttpPostType.SynchronousJson, loginUser, null);
			}
		}
	}
}
