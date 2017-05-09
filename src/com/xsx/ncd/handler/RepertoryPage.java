package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.swing.RootPaneContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.spring.ActivitySession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

@Component
public class RepertoryPage implements ActivityTemplet {

	private AnchorPane rootPane = null;
	
	@FXML JFXButton CardInStorageButton;
	@FXML JFXButton CardOutStorageButton;
	
	@Autowired private ActivitySession activitySession;
	@Autowired CardInStoragePage cardInStoragePage;
	@Autowired CardOutStoragePage cardOutStoragePage;
	
	@Override
	@PostConstruct
	public void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/RepertoryPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/RepertoryPage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        CardInStorageButton.setOnAction((e)->{
        	cardInStoragePage.startActivity(null);
        });
        
        CardOutStorageButton.setOnAction((e)->{
        	cardOutStoragePage.startActivity(null);
        });
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}

	@Override
	public void startActivity(Object object) {
		// TODO Auto-generated method stub
		activitySession.setRootActivity(this);
		activitySession.setFatherActivity(null);
		activitySession.setChildActivity(null);
		activitySession.setActivityPane(this);
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
		return " ‘º¡ø®π‹¿Ì";
	}

	@Override
	public Pane getActivityRootPane() {
		// TODO Auto-generated method stub
		return rootPane;
	}

	@Override
	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		
	}

}
