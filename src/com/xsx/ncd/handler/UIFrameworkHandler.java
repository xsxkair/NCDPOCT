package com.xsx.ncd.handler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

@Component
public class UIFrameworkHandler {

	private Stage s_Stage = null;
	private AnchorPane root = null;
	private Scene s_Scene = null;
	
	private Timeline expandeTimeLine = null;
	private KeyFrame expandeStartKeyFrame = null;
	private KeyFrame expandeMiddleKeyFrame = null;
	private KeyFrame expandeEndKeyFrame = null;
	
	private Timeline unexpandeTimeLine = null;
	private KeyFrame unexpandeStartKeyFrame = null;
	private KeyFrame unexpandeMiddleKeyFrame = null;
	private KeyFrame unexpandeEndKeyFrame = null;
	
	@FXML AnchorPane leftMenuAnchorPane;
	@FXML ImageView leftMenuExpandeImageView;
	@FXML ImageView leftMenuUnExpandeImageView;
	@FXML JFXListView<HBox> leftMenuListView;
	@FXML Label leftMenuItem1;
	@FXML Label leftMenuItem2;
	@FXML Label leftMenuItem3;
	@FXML Label leftMenuItem4;
	@FXML Label leftMenuItem5;
	@FXML Label leftMenuItem6;
	@FXML Label leftMenuItem7;
	@FXML Label leftMenuItem8;
	@FXML Label leftMenuItem9;
	
	@FXML HBox leftMenuLogoHbox;
	@FXML ImageView leftMenuLogoImageView;
	
	@FXML JFXButton rootActivityButton;
	@FXML ImageView topMenuSeparatorImageView1;
	@FXML JFXButton fatherActivityButton;
	@FXML ImageView topMenuSeparatorImageView2;
	@FXML JFXButton childActivityButton;
	
	@FXML HBox GB_MyInfoHBox;
	@FXML Text GB_UserNameText;
	@FXML HBox GB_UserOutHBox;
	
	@FXML AnchorPane GB_RootPane;

	
	@Autowired UserSession userSession;
	@Autowired LoginHandler loginHandler;
	@Autowired ActivitySession activitySession;
	@Autowired MyInfoHandler myInfoHandler;
	@Autowired RepertoryPage repertoryPage;
	//@Autowired AddDeviceTypeHandler addDeviceTypeHandler;
	@Autowired DeviceManageHandler deviceManageHandler;

	@PostConstruct
	public void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/UIFramework.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/UIFramework.fxml");
        loader.setController(this);
        
        try {
        	root = loader.load(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        s_Scene = new Scene(root, 1280, 800);
        
        //展开
        expandeStartKeyFrame = new KeyFrame(Duration.ZERO, 
        		new KeyValue(leftMenuItem1.opacityProperty(), 0), 
        		new KeyValue(leftMenuListView.maxWidthProperty(), 50));
        expandeMiddleKeyFrame = new KeyFrame(new Duration(150), 
        		new KeyValue(leftMenuItem1.opacityProperty(), 0), 
        		new KeyValue(leftMenuListView.maxWidthProperty(), 50));
        expandeEndKeyFrame = new KeyFrame(new Duration(200), 
        		new KeyValue(leftMenuItem1.opacityProperty(), 1), 
        		new KeyValue(leftMenuListView.maxWidthProperty(), 200));
        expandeTimeLine = new Timeline(expandeStartKeyFrame, expandeMiddleKeyFrame, expandeEndKeyFrame);
        expandeTimeLine.setCycleCount(0);
        
        //收缩
        unexpandeStartKeyFrame = new KeyFrame(Duration.ZERO, 
        		new KeyValue(leftMenuItem1.opacityProperty(), 1), 
        		new KeyValue(leftMenuListView.maxWidthProperty(), 200));
        unexpandeMiddleKeyFrame = new KeyFrame(new Duration(150),
        		new KeyValue(leftMenuItem1.opacityProperty(), 1), 
        		new KeyValue(leftMenuListView.maxWidthProperty(), 200));
        unexpandeEndKeyFrame = new KeyFrame(new Duration(200), 
        		new KeyValue(leftMenuItem1.opacityProperty(), 0), 
        		new KeyValue(leftMenuListView.maxWidthProperty(), 50));
        
        unexpandeTimeLine = new Timeline(unexpandeStartKeyFrame, unexpandeMiddleKeyFrame, unexpandeEndKeyFrame);
        unexpandeTimeLine.setCycleCount(0);

        leftMenuItem2.opacityProperty().bind(leftMenuItem1.opacityProperty());
        leftMenuItem3.opacityProperty().bind(leftMenuItem1.opacityProperty());
        leftMenuItem4.opacityProperty().bind(leftMenuItem1.opacityProperty());
        leftMenuItem5.opacityProperty().bind(leftMenuItem1.opacityProperty());
        leftMenuItem6.opacityProperty().bind(leftMenuItem1.opacityProperty());
        leftMenuItem7.opacityProperty().bind(leftMenuItem1.opacityProperty());
        leftMenuItem8.opacityProperty().bind(leftMenuItem1.opacityProperty());
        leftMenuItem9.opacityProperty().bind(leftMenuItem1.opacityProperty());
        
        leftMenuLogoImageView.fitWidthProperty().bind(leftMenuItem1.opacityProperty().multiply(81).add(2));
        leftMenuLogoImageView.visibleProperty().bind(leftMenuListView.maxWidthProperty().isEqualTo(200));
        
        leftMenuExpandeImageView.setOnMouseClicked((e)->{
        	leftMenuExpandeImageView.setVisible(false);
        	leftMenuUnExpandeImageView.setVisible(true);
        	leftMenuExpande(true);
        });
        
        leftMenuUnExpandeImageView.setOnMouseClicked((e)->{
        	leftMenuUnExpandeImageView.setVisible(false);
        	leftMenuExpandeImageView.setVisible(true);
        	leftMenuExpande(false);
        });
        
        //更新用户名
        userSession.getUserProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null)
        		GB_UserNameText.setText((newValue.getName() == null)?"^_^":newValue.getName());
        	else
        		GB_UserNameText.setText(null);
        });
        
        //注销
        GB_UserOutHBox.setOnMouseClicked((e)->{
        	userSession.setUser(null);
        	activitySession.setActivityPane(null);
    		s_Stage.close();
    		loginHandler.startLoginActivity();
        });
        
        rootActivityButton.visibleProperty().bind(activitySession.getRootActivity().isNotNull());
        rootActivityButton.visibleProperty().addListener((o, oldValue, newValue)->{
        	if(newValue){
        		rootActivityButton.setText(activitySession.getRootActivity().get().getActivityName());
        	}
        });
        rootActivityButton.setOnAction((e)->{
        	activitySession.getRootActivity().get().startActivity(null);
        });
        
        topMenuSeparatorImageView1.visibleProperty().bind(fatherActivityButton.visibleProperty());
        fatherActivityButton.visibleProperty().bind(activitySession.getFatherActivity().isNotNull());
        fatherActivityButton.visibleProperty().addListener((o, oldValue, newValue)->{
        	if(newValue){
        		fatherActivityButton.setText(activitySession.getFatherActivity().get().getActivityName());
        	}
        });
        fatherActivityButton.setOnAction((e)->{
        	activitySession.getFatherActivity().get().startActivity(null);
        });
        
        topMenuSeparatorImageView2.visibleProperty().bind(childActivityButton.visibleProperty());
        childActivityButton.visibleProperty().bind(activitySession.getChildActivity().isNotNull());
        childActivityButton.visibleProperty().addListener((o, oldValue, newValue)->{
        	if(newValue){
        		childActivityButton.setText(activitySession.getChildActivity().get().getActivityName());
        	}
        });
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	GB_RootPane.getChildren().clear();
        	if(newValue != null)
        		GB_RootPane.getChildren().add(newValue.getActivityRootPane());
        });
        leftMenuListView.getSelectionModel().selectedIndexProperty().addListener((o, oldValue, newValue)->{
        	if(newValue.equals(5))
        		myInfoHandler.startActivity(null);
        	else if(newValue.equals(2))
        		repertoryPage.startActivity(null);
        	else if(newValue.equals(1))
        		deviceManageHandler.startActivity(null);
        });

        loader = null;
        in = null;
	}
	
	public void startMainUI() {
		s_Stage = new Stage();

		/*JFXDecorator decorator = new JFXDecorator(s_Stage, root);
		decorator.setCustomMaximize(true);
		decorator.setOnCloseButtonAction(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});*/

		s_Stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
	    s_Stage.setScene(s_Scene);
	    s_Stage.setMinWidth(1280);
	    s_Stage.setMinHeight(800);
		s_Stage.show();
	}
	
	private void leftMenuExpande(Boolean status){

		if(status){
			expandeTimeLine.play();
		}
		else{
			unexpandeTimeLine.play();
		}
	}

}
