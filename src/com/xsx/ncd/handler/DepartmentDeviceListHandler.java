package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.xsx.ncd.define.DeviceJson;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.SpringFacktory;
import com.xsx.ncd.tool.HttpClientTool;
import com.xsx.ncd.tool.XsxLog;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class DepartmentDeviceListHandler extends AnchorPane {
	
	private AnchorPane rootPane = null;
	
	@FXML Label DepartmentNameLabel;
	@FXML ImageView AddDeviceImageView;
	@FXML FlowPane DeviceListFlowPane;
	
	private Department departmentData = null;
	private DeviceManageHandler fatherActivity = null;
	private QueryThisDepartmentAllDeviceService queryThisDepartmentAllDeviceService = null;
	
	public DepartmentDeviceListHandler(Department department, DeviceManageHandler fatherActivity){
		departmentData = department;
		this.fatherActivity = fatherActivity;
		this.onCreate();
	}

	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/DepartmentDeviceList.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/DepartmentDeviceList.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        DepartmentNameLabel.setText(this.departmentData.getName());
        this.getChildren().add(rootPane);

        AddDeviceImageView.setOnMouseClicked((e)->{
        	fatherActivity.showAddDeviceDialog(departmentData);
        });

        queryThisDepartmentAllDeviceService = new QueryThisDepartmentAllDeviceService();
        queryThisDepartmentAllDeviceService.setPeriod(Duration.minutes(1));
        queryThisDepartmentAllDeviceService.start();
        
        queryThisDepartmentAllDeviceService.lastValueProperty().addListener((o, oldValue, newValue)->{
        	
        	DeviceListFlowPane.getChildren().clear();
        	SpringFacktory.GetBean(XsxLog.class).info("�豸����");

        	if(newValue != null){
        		for (DeviceJson device : newValue) {
        			MyDeviceView deviceView = new MyDeviceView(device);
        			deviceView.setOnMouseClicked((e)->{
        				SpringFacktory.GetBean(ActivitySession.class).startActivity(SpringFacktory.GetBean(DeviceInfoHandler.class), device.getDeviceId());
        			});
        			DeviceListFlowPane.getChildren().add(deviceView);
        		}
        	}
        });
        
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
	
	class QueryThisDepartmentAllDeviceService extends ScheduledService<List<DeviceJson>>{

		@Override
		protected Task<List<DeviceJson>> createTask() {
			// TODO Auto-generated method stub
			return new MyTask();
		}

		class MyTask extends Task<List<DeviceJson>>{

			@Override
			protected List<DeviceJson> call() throws Exception {
				// TODO Auto-generated method stub

				return SpringFacktory.GetBean(HttpClientTool.class).myHttpPost(null, ServiceEnum.QueryAllDeviceByDepartmentInSample, 
						HttpPostType.SynchronousJson, departmentData, null);
			}	
		}
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		for (Node node : DeviceListFlowPane.getChildren()) {
			MyDeviceView deviceView = (MyDeviceView) node;
			deviceView.distroyDevice();
		}
		DeviceListFlowPane.getChildren().clear();
		
		queryThisDepartmentAllDeviceService.cancel();
		queryThisDepartmentAllDeviceService = null;

		departmentData = null;
		fatherActivity = null;
	}

}
