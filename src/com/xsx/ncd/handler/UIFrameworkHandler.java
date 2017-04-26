package com.xsx.ncd.handler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXListView;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

@Component
public class UIFrameworkHandler implements HandlerTemplet{

	private Stage s_Stage = null;
	private AnchorPane root = null;
	private Scene s_Scene = null;
	
	private Timeline timeline = null;
	private KeyFrame startKeyFrame = null;
	private KeyFrame endKeyFrame = null;
	
	@FXML AnchorPane leftMenuAnchorPane;
	@FXML ImageView leftMenuExpandeImageView;
	@FXML ImageView leftMenuUnExpandeImageView;
	@FXML JFXListView<HBox> leftMenuListView;
	
	private List<Label> menuItemLabelList = null;
	private List<KeyValue> menuItemStartKeyValue = null;
	private List<KeyValue> menuItemEndKeyValue = null;
	private HBox tempHbox = null;
	
	@Override
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

 /*       root.setOnMousePressed(new EventHandler<MouseEvent>() {

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
 */   	
        menuItemLabelList = new ArrayList<>();
        menuItemStartKeyValue = new ArrayList<>();
        menuItemEndKeyValue = new ArrayList<>();
        for (int i=0; i<leftMenuListView.getItems().size(); i++) {
        	tempHbox = leftMenuListView.getItems().get(i);
        	menuItemLabelList.add((Label) tempHbox.getChildren().get(1));
        	
        	menuItemStartKeyValue.add(new KeyValue(tempHbox.getChildren().get(1).opacityProperty(), 1));
        	menuItemEndKeyValue.add(new KeyValue(tempHbox.getChildren().get(1).opacityProperty(), 0));
		}
        startKeyFrame = new KeyFrame(Duration.ZERO, menuItemStartKeyValue.get(0), menuItemStartKeyValue.get(1));

        endKeyFrame = new KeyFrame(new Duration(1000), menuItemEndKeyValue.get(0), menuItemEndKeyValue.get(1));
        
        timeline = new Timeline(startKeyFrame, endKeyFrame);
        timeline.setCycleCount(0);
        
        
        leftMenuExpandeImageView.setOnMouseClicked((e)->{
        	leftMenuExpande(true);
        	leftMenuExpandeImageView.setVisible(false);
        	leftMenuUnExpandeImageView.setVisible(true);
        });
        
        leftMenuUnExpandeImageView.setOnMouseClicked((e)->{
        	leftMenuExpande(false);
        	leftMenuUnExpandeImageView.setVisible(false);
        	leftMenuExpandeImageView.setVisible(true);
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
		
		s_Scene = new Scene(root, 1280, 800);
		s_Stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/RES/logo.png")));
	    s_Stage.setScene(s_Scene);
	    s_Stage.setMinWidth(1280);
	    s_Stage.setMinHeight(800);
		s_Stage.show();
	}
	
	@Override
	public void showPane() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showPane(Object object) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void leftMenuExpande(Boolean status){

		if(status){
			timeline.play();
		}
		else{
			
		}
	}
}
