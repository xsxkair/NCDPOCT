package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.swing.RootPaneContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.ActivitySession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

@Component
public class RepertoryPage extends Activity {
	
	@FXML JFXButton CardInStorageButton;
	@FXML JFXButton CardOutStorageButton;
	
	@Autowired ActivitySession activitySession;
	@Autowired CardInStoragePage cardInStoragePage;
	@Autowired CardOutStoragePage cardOutStoragePage;
	
	@Override
	@PostConstruct
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/RepertoryPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/RepertoryPage.fxml");
        loader.setController(this);
        try {
        	this.setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        CardInStorageButton.setOnAction((e)->{
        	activitySession.startActivity(cardInStoragePage, null);
        });
        
        CardOutStorageButton.setOnAction((e)->{
        	activitySession.startActivity(cardOutStoragePage, null);
        });
        
        this.setActivityName("ø‚¥Êπ‹¿Ì");
        this.setActivityStatus(ActivityStatusEnum.Create);
        
        AnchorPane.setTopAnchor(this.getRootPane(), 0.0);
        AnchorPane.setBottomAnchor(this.getRootPane(), 0.0);
        AnchorPane.setLeftAnchor(this.getRootPane(), 0.0);
        AnchorPane.setRightAnchor(this.getRootPane(), 0.0);
        
        loader = null;
        in = null;
	}

	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

}
