package com.xsx.ncd.handler;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

@Component
public class UIFrameworkHandler implements Observer{

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
	
	@FXML HBox topMenuHBox;
	@FXML JFXButton activity1Button;
	@FXML JFXButton activity2Button;
	@FXML JFXButton activity3Button;
	@FXML JFXButton activity4Button;
	@FXML JFXButton activity5Button;
	
	@FXML StackPane NotHandledReportStackPane;
	@FXML Label NotHandledReportNumLabel;
	@FXML HBox GB_MyInfoHBox;
	@FXML Text GB_UserNameText;
	@FXML HBox GB_UserOutHBox;
	
	@FXML AnchorPane GB_RootPane;

	private QueryNotHandledReportNumService queryNotHandledReportNumService = null;
	private ChangeListener<Long> queryNotHandledReportNumServiceListener = null;
	int i=0;
	
	@Autowired UserSession userSession;
	@Autowired LoginHandler loginHandler;
	@Autowired ActivitySession activitySession;
	@Autowired MyInfoHandler myInfoHandler;
	@Autowired RepertoryPage repertoryPage;
	@Autowired DeviceManageHandler deviceManageHandler;
	@Autowired ErrorRecordHandler errorRecordHandler;
	@Autowired AdjustRecordHandler adjustRecordHandler;
	@Autowired QualityRecordHandler qualityRecordHandler;
	@Autowired MaintenanceRecordHandler maintenanceRecordHandler;
	@Autowired WorkSpaceHandler workSpaceHandler;
	@Autowired HttpClientTool httpClientTool;

	@PostConstruct
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/UIFramework.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/UIFramework.fxml");
        loader.setController(this);
        
        try {
        	root = loader.load(in);
        	in.close();
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
        
        activity2Button.visibleProperty().bind(activity2Button.textProperty().length().greaterThan(0));
        activity3Button.visibleProperty().bind(activity3Button.textProperty().length().greaterThan(0));
        activity4Button.visibleProperty().bind(activity4Button.textProperty().length().greaterThan(0));
        activity5Button.visibleProperty().bind(activity5Button.textProperty().length().greaterThan(0));
        for (Node node : topMenuHBox.getChildren()) {
			JFXButton jfxButton = (JFXButton) node;
			jfxButton.setOnAction((e)->{
				activitySession.backToThisActivity((Activity) jfxButton.getUserData());
			});
		}
        
        NotHandledReportStackPane.setOnMouseClicked((e)->{
        	leftMenuListView.getSelectionModel().select(0);
        });
        NotHandledReportNumLabel.visibleProperty().bind(NotHandledReportNumLabel.textProperty().length().greaterThan(0));
        queryNotHandledReportNumServiceListener = (o, oldValue, newValue) -> {
        	if(newValue != null)
        		NotHandledReportNumLabel.setText(String.valueOf(newValue));
        	else
        		NotHandledReportNumLabel.setText("");
        };
        
        GB_MyInfoHBox.setOnMouseClicked((e)->{
        	leftMenuListView.getSelectionModel().select(5);
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
        	stopActivity();
        });
        
       activitySession.addObserver(this);
       leftMenuListView.getSelectionModel().selectedIndexProperty().addListener((o, oldValue, newValue)->{
    	   if(newValue.equals(0))
       			activitySession.clearAndSetOriginActivityAs(workSpaceHandler, null);
    	   else if(newValue.equals(1))
       			activitySession.clearAndSetOriginActivityAs(deviceManageHandler, null);
       		else if(newValue.equals(2))
       			activitySession.clearAndSetOriginActivityAs(repertoryPage, null);
       		else if(newValue.equals(3))
       			activitySession.clearAndSetOriginActivityAs(maintenanceRecordHandler, null);
       		else if(newValue.equals(4))
       			activitySession.clearAndSetOriginActivityAs(adjustRecordHandler, null);
       		else if(newValue.equals(5))
       			activitySession.clearAndSetOriginActivityAs(myInfoHandler, null);
       		else if(newValue.equals(6))
       			activitySession.clearAndSetOriginActivityAs(qualityRecordHandler, null);
       		else if(newValue.equals(7))
       			activitySession.clearAndSetOriginActivityAs(errorRecordHandler, null);
        });

        loader = null;
        in = null;
	}
	
	public void startMainUI() {
		if(s_Stage == null){
			s_Stage = new Stage();
			s_Stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
		    s_Stage.setScene(s_Scene);
		    s_Stage.setMinWidth(1280);
		    s_Stage.setMinHeight(800);
		    
		    s_Stage.setOnCloseRequest((e)->{
		    	System.exit(0);
		    });
		}
		
		/*JFXDecorator decorator = new JFXDecorator(s_Stage, root);
		decorator.setCustomMaximize(true);
		decorator.setOnCloseButtonAction(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});*/

		queryNotHandledReportNumService = new QueryNotHandledReportNumService();
		queryNotHandledReportNumService.lastValueProperty().addListener(queryNotHandledReportNumServiceListener);
		queryNotHandledReportNumService.setPeriod(Duration.minutes(1));
		queryNotHandledReportNumService.start();
		
		leftMenuListView.getSelectionModel().select(0);
		
		s_Stage.show();
	}
	
	private void stopActivity(){
		queryNotHandledReportNumService.cancel();
		queryNotHandledReportNumService.lastValueProperty().removeListener(queryNotHandledReportNumServiceListener);
		queryNotHandledReportNumService = null;
		
		userSession.setUser(null);
		s_Stage.close();
		loginHandler.startLoginActivity();
	}
	
	private void leftMenuExpande(Boolean status){

		if(status){
			expandeTimeLine.play();
		}
		else{
			unexpandeTimeLine.play();
		}
	}

	class QueryNotHandledReportNumService extends ScheduledService<Long>{

		@Override
		protected Task<Long> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}
		
		class MyTask extends Task<Long>{

			@Override
			protected Long call() {
				// TODO Auto-generated method stub				
				
				return httpClientTool.myHttpPost(null, ServiceEnum.QueryAllNotHandledReportNum, 
						HttpPostType.SynchronousJson, null, null);
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		GB_RootPane.getChildren().clear();
    	
		GB_RootPane.getChildren().add(activitySession.getActivityStack().peek().getRootPane());

		Activity activity = activitySession.getActivityStack().getFirst();
		activity1Button.setText(activity.getActivityName());
		activity1Button.setUserData(activity);
		
		Iterator<Activity> iterable = activitySession.getActivityStack().descendingIterator();
		i=0;
		while(iterable.hasNext()){
			Activity tempActivity = iterable.next();
			topMenuHBox.getChildren().get(i).setUserData(tempActivity);
			((JFXButton) topMenuHBox.getChildren().get(i)).setText(tempActivity.getActivityName());
			i++;
		}
		
		for(;i<topMenuHBox.getChildren().size(); i++){
			topMenuHBox.getChildren().get(i).setUserData(null);
			((JFXButton) topMenuHBox.getChildren().get(i)).setText(null);
		}
	}
}
