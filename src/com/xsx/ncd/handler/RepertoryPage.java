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
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.ActivitySession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

@Component
public class RepertoryPage implements ActivityTemplet, HttpTemplet {

	private AnchorPane rootPane = null;
	
	@FXML JFXButton CardInStorageButton;
	@FXML JFXButton CardOutStorageButton;
	
	@Autowired private ActivitySession activitySession;
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
        	rootPane = loader.load(in);
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        CardInStorageButton.setOnAction((e)->{
        	cardInStoragePage.onStart(null);
        });
        
        CardOutStorageButton.setOnAction((e)->{
        	cardOutStoragePage.onStart(null);
        });
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}

	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		activitySession.setActivityPane(this);
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
	public String getActivityName() {
		// TODO Auto-generated method stub
		return "试剂卡管理";
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
	
	@Override
	public void startHttpWork(ServiceEnum serviceEnum, Object parm) {
//		GB_FreshPane.setVisible(true);
		
//		if(!httpClientTool.myHttpAsynchronousPostJson(this, serviceEnum, parm)){
//			GB_FreshPane.setVisible(false);
//			showLogsDialog("错误", "数据转换失败，请重试！");
//		}	
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

}
