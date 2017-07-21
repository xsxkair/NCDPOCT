package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xsx.ncd.define.DeviceJson;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.spring.SpringFacktory;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ItemRrepertoryCellHandler extends AnchorPane{

	private GridPane RootPane = null;
	@FXML Label itemNameLabel;
	@FXML Label itemNumLabel;

	public ItemRrepertoryCellHandler(String name, long num) {
		super();
		
		onCreate();
		
		itemNameLabel.setText(name);
		itemNumLabel.setText(String.valueOf(num));
	}
	
	private void onCreate(){
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/ItemRrepertoryCell.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/ItemRrepertoryCell.fxml");
        loader.setController(this);
        try {
        	RootPane = loader.load(in);
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		this.getChildren().add(RootPane);
		this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		this.setMouseTransparent(true);
		
		AnchorPane.setTopAnchor(RootPane, 0.0);
        AnchorPane.setBottomAnchor(RootPane, 0.0);
        AnchorPane.setLeftAnchor(RootPane, 0.0);
        AnchorPane.setRightAnchor(RootPane, 0.0);
	}

	public String getItemNameLabel() {
		return itemNameLabel.getText();
	}
	
	public void setPageValue(String name, long num){
		itemNameLabel.setText(name);
		itemNumLabel.setText(String.valueOf(num));
	}
}
