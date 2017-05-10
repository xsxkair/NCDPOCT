package com.xsx.ncd.handler;


import java.io.IOException;
import java.io.InputStream;

import com.xsx.ncd.entity.Department;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class DepartmentDeviceListHandler extends AnchorPane{
	
	private AnchorPane rootPane = null;
	
	@FXML Label DepartmentNameLabel;
	@FXML ImageView AddDeviceImageView;
	@FXML FlowPane DeviceListFlowPane;
	
	private Department departmentData = null;
	
	public DepartmentDeviceListHandler(Department department){
		departmentData = department;
		this.UI_Init();
	}
	
	private void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/DepartmentDeviceList.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/DepartmentDeviceList.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //DepartmentNameLabel.setText(this.departmentData.getName());
        this.getChildren().add(rootPane);
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}

	public Pane getActivityRootPane() {
		// TODO Auto-generated method stub
		return rootPane;
	}

}
