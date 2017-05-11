package com.xsx.ncd.handler;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.stream.FileImageInputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.jfoenix.controls.JFXSpinner;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.define.UserFilePath;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DepartmentDeviceListHandler extends AnchorPane implements HttpTemplet{
	
	private AnchorPane rootPane = null;
	
	@FXML Label DepartmentNameLabel;
	@FXML ImageView AddDeviceImageView;
	@FXML FlowPane DeviceListFlowPane;
	
	private Department departmentData = null;
	private QueryThisDepartmentAllDeviceService queryThisDepartmentAllDeviceService = null;
	
	@Autowired HttpClientTool httpClientTool;
	@Autowired UserFilePath userFilePath;
	
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
        
        DepartmentNameLabel.setText(this.departmentData.getName());
        this.getChildren().add(rootPane);
        
        queryThisDepartmentAllDeviceService = new QueryThisDepartmentAllDeviceService();
        queryThisDepartmentAllDeviceService.setPeriod(Duration.minutes(5));
        
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

	@Override
	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startHttpWork(ServiceEnum serviceEnum, Object parm) {
		// TODO Auto-generated method stub
		
	}
	
	class QueryThisDepartmentAllDeviceService extends ScheduledService<Void>{

		@Override
		protected Task<Void> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}

		class MyTask extends Task<Void>{

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}	
		}
	}
	
	class MyDeviceView extends VBox{
		
		private Device device = null;
		private ImageView imageView = null;
		private Image image = null;

		public MyDeviceView(Device device) {
			super();
			this.device = device;
			this.Init();
		}
		
		private void Init() {
			imageView = new ImageView();
			JFXSpinner jfxSpinner = new JFXSpinner();
			StackPane stackPane = new StackPane(imageView, jfxSpinner);
			
			Label deviceName = new Label(device.getName()+"("+device.getModel()+")");
			Label deviceAddr = new Label(device.getAddr());
			VBox vbBox = new VBox(deviceName, deviceAddr);
			
			this.getChildren().addAll(stackPane, vbBox);
			
			jfxSpinner.visibleProperty().bind(imageView.imageProperty().isNull());
		}
		
		private Image getDeviceImage(){
			String imageFilePath =  userFilePath.getDeviceIcoDirPath() + "\\" + device.getIcopath();
			File imageFile = new File(imageFilePath);

			if(imageFile.exists()){
				try {
					image = new Image(new FileInputStream(imageFile));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					image = null;
				}
			}
			
			if(image == null){
				
			}
			
			return image;
		}
	}
}
