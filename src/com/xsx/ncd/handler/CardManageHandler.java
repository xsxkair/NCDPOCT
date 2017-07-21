package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.DataJson;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.spring.ActivitySession;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

@Component
public class CardManageHandler extends Activity {

	@FXML GridPane repertoryGridPane;
	@FXML JFXListView<ItemRrepertoryCellHandler> summyRepertoryListView;
	
	@FXML PieChart centralRepertoryPieChart;
	@FXML FlowPane departmentRepertoryFlowPane;
	@FXML FlowPane departmentFlowPane;
	@FXML FlowPane deviceFlowPane;
	@FXML ToggleGroup viewToggleGroup;
	@FXML JFXRadioButton yearViewToggle;
	@FXML JFXRadioButton monthViewToggle;
	@FXML LineChart<String, Number> cardDetailLineChart;
	@FXML JFXButton CardInStorageButton;
	@FXML JFXButton CardOutStorageButton;
	
	private ToggleGroup departmentToggleGroup = null;
	private ToggleGroup deviceToggleGroup = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	private StringBuffer stringBuffer = null;
	private List<DepartmentRepertoryCellHandler> departmentRepertoryCellHandlerList = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired CardInStoragePage cardInStoragePage;
	@Autowired CardOutStoragePage cardOutStoragePage;
	
	@Override
	@PostConstruct
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/CardManagerPage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/CardManagerPage.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        yearViewToggle.setUserData(true);
        monthViewToggle.setUserData(false);
        monthViewToggle.setSelected(true);
        
        departmentToggleGroup = new ToggleGroup();
        departmentToggleGroup.selectedToggleProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		startHttpWork(ServiceEnum.QueryThisDepartmentAllDeviceList, HttpPostType.AsynchronousJson, newValue.getUserData(), null, null);
        	}
        });
        
        deviceToggleGroup = new ToggleGroup();
        deviceToggleGroup.selectedToggleProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		Device device = (Device) newValue.getUserData();
        		Boolean isYearView = (Boolean) viewToggleGroup.getSelectedToggle().getUserData();
        		
        		device.setDepartment(null);
        		device.setOperator(null);
        		device.setOperators(null);
        		device.getDeviceType().setItems(null);
        		DataJson<Device, Boolean, String, String, String> dataJson = new DataJson<>(device, isYearView, null, null);

        		startHttpWork(ServiceEnum.QueryDeviceActivity, HttpPostType.AsynchronousJson, dataJson, null, null);
        	}
        });
        
        viewToggleGroup.selectedToggleProperty().addListener((o, oldValue, newValue)->{
        	if(newValue != null){
        		Device device = (Device) deviceToggleGroup.getSelectedToggle().getUserData();
        		Boolean isYearView = (Boolean) newValue.getUserData();
        		
        		device.setDepartment(null);
        		device.setOperator(null);
        		device.setOperators(null);
        		device.getDeviceType().setItems(null);
        		DataJson<Device, Boolean, String, String, String> dataJson = new DataJson<>(device, isYearView, null, null);

        		startHttpWork(ServiceEnum.QueryDeviceActivity, HttpPostType.AsynchronousJson, dataJson, null, null);
        	}
        });
        
        myMessageListChangeListener = new ListChangeListener<Message>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				// TODO Auto-generated method stub
				while(c.next()){
					if(c.wasAdded()){
						for (Message message : c.getAddedSubList()) {
							switch (message.getWhat()) {
							
								case ReadAllDepartment:
									showDepartmentList(message.getObj());
									break;
								
								case QueryThisDepartmentAllDeviceList:
									showDeviceList(message.getObj());
									break;
								
								case QueryAllCardRepertory:
									showAllCardRepertoryPicChart(message.getObj());
									break;
									
								case QueryDepartmentAllCardRepertory:
									showDepartmentAllCardRepertoryPicChart(message.getObj());
									break;
									
								case QueryDeviceActivity:
									showDeviceActivity(message.getObj());
									break;
								default:
									break;
							}
						}
					}
				}
			}
        };
        
        CardInStorageButton.setOnAction((e)->{
        	activitySession.startActivity(cardInStoragePage, null);
        });
        
        CardOutStorageButton.setOnAction((e)->{
        	activitySession.startActivity(cardOutStoragePage, null);
        });
        
        this.setActivityName("试剂卡管理");
        this.setActivityStatus(ActivityStatusEnum.Create);
        
        AnchorPane.setTopAnchor(getRootPane(), 0.0);
        AnchorPane.setBottomAnchor(getRootPane(), 0.0);
        AnchorPane.setLeftAnchor(getRootPane(), 0.0);
        AnchorPane.setRightAnchor(getRootPane(), 0.0);
        
        loader = null;
        in = null;
	}
	
	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		super.onStart(object);
		getMyMessagesList().addListener(myMessageListChangeListener);

		stringBuffer = new StringBuffer();
		departmentRepertoryCellHandlerList = new ArrayList<>();
		
		//查询总仓库所有项目的库存
		startHttpWork(ServiceEnum.QueryAllCardRepertory, HttpPostType.AsynchronousJson, null, null, null);
		
		//查询各个科室所有项目的库存
		startHttpWork(ServiceEnum.QueryDepartmentAllCardRepertory, HttpPostType.AsynchronousJson, null, null, null);
		
		startHttpWork(ServiceEnum.ReadAllDepartment, HttpPostType.AsynchronousJson, null, null, null);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//查询总仓库所有项目的库存
		startHttpWork(ServiceEnum.QueryAllCardRepertory, HttpPostType.AsynchronousJson, null, null, null);
				
		//查询各个科室所有项目的库存
		startHttpWork(ServiceEnum.QueryDepartmentAllCardRepertory, HttpPostType.AsynchronousJson, null, null, null);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getMyMessagesList().removeListener(myMessageListChangeListener);
		
		stringBuffer = null;
		departmentRepertoryCellHandlerList = null;
		super.onDestroy();
	}
	
	private void showDepartmentList(List<Department> departments){
		departmentFlowPane.getChildren().clear();
		
		departmentToggleGroup.getToggles().clear();

		for (Department department : departments) {
			JFXToggleNode jfxToggleNode = new JFXToggleNode();
			Label label = new Label(department.getName());
			jfxToggleNode.setGraphic(label);
			jfxToggleNode.setUserData(department);
			jfxToggleNode.setToggleGroup(departmentToggleGroup);
			departmentFlowPane.getChildren().add(jfxToggleNode);
		}
		
		departmentToggleGroup.selectToggle((Toggle) departmentFlowPane.getChildren().get(0));
	}
	
	private void showDeviceList(List<Device> devices){
		deviceFlowPane.getChildren().clear();
		
		deviceToggleGroup.getToggles().clear();

		for (Device device : devices) {
			JFXToggleNode jfxToggleNode = new JFXToggleNode();
			Label label = new Label(device.getDid());
			jfxToggleNode.setGraphic(label);
			jfxToggleNode.setUserData(device);
			jfxToggleNode.setToggleGroup(deviceToggleGroup);
			deviceFlowPane.getChildren().add(jfxToggleNode);
		}
		
		deviceToggleGroup.selectToggle((Toggle) deviceFlowPane.getChildren().get(0));
	}
	
	private void showAllCardRepertoryPicChart(Map<String, Long> cardRepertoryInfo){
		int summyListSize = summyRepertoryListView.getItems().size();
		int newsummyListSize = cardRepertoryInfo.size();
		int i=0;

		if(summyListSize > newsummyListSize){
			summyRepertoryListView.getItems().remove(newsummyListSize, summyListSize);
		}
		else{
			for(i=0; i<(newsummyListSize - summyListSize); i++){
				summyRepertoryListView.getItems().add(new ItemRrepertoryCellHandler(null, 0));
			}
		}
		
		Set<String> keys = cardRepertoryInfo.keySet();
		Iterator<String> iterator = keys.iterator();
		i=0;
		while (iterator.hasNext()) {
			String type = iterator.next();
			long num = cardRepertoryInfo.get(type);
			
			summyRepertoryListView.getItems().get(i).setPageValue(type, num);
			
			i++;
		}
	}
	
	private void showDepartmentAllCardRepertoryPicChart(List<DataJson<String, Department, String, String, Long>> departmentCardRepertoryInfo){
		int i=0;
		
		i = 0;
		for (DataJson<String, Department, String, String, Long> dataJson : departmentCardRepertoryInfo) {
			DepartmentRepertoryCellHandler departmentRepertoryCellHandler = new DepartmentRepertoryCellHandler(dataJson.getParm2().getName(), dataJson.getParmMap());
			Label nameLabel = departmentRepertoryCellHandler.getDepartmentNameLabel();
			JFXListView<ItemRrepertoryCellHandler> tempListView = departmentRepertoryCellHandler.getItemRepertoryCellListView();
			departmentRepertoryCellHandler.setPageValue(dataJson.getParm2().getName(), dataJson.getParmMap());
			repertoryGridPane.add(nameLabel, 0, i+2);
			repertoryGridPane.add(tempListView, 1, i+2);
			GridPane.setColumnSpan(tempListView, 2);
			
			i++;
		}
		
		repertoryGridPane.setGridLinesVisible(false);
		repertoryGridPane.setGridLinesVisible(true);
		
	}
	
	private void showDeviceActivity(List<DataJson<String, String, String, String, Long>> datas) {
		List<String> timeStrList = new ArrayList<>();
		
		cardDetailLineChart.getData().clear();
		
		//获取所有时间
		for (DataJson<String, String, String, String, Long> dataJson : datas) {
			Map<String, Long> dataMap = dataJson.getParmMap();
			Set<String> keySet = dataMap.keySet();
			Iterator<String> keyIterator = keySet.iterator();
			while(keyIterator.hasNext()){
				String tempstr = keyIterator.next();
				if(!timeStrList.contains(tempstr))
					timeStrList.add(tempstr);
			}
		}
		//对时间排序
		Collections.sort(timeStrList, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return o1.compareTo(o2);
			}
		});
		
		for (DataJson<String, String, String, String, Long> dataJson : datas) {
			Series<String, Number> series = new Series<>();
			Map<String, Long> dataMap = dataJson.getParmMap();
			
			//项目
			series.setName(dataJson.getParm1());
			
			//遍历时间，没有的时间写0
			for (String string : timeStrList) {
				Long num = dataMap.get(string);

				if(num == null)
					num = 0L;

				javafx.scene.chart.XYChart.Data<String, Number> dataPoint = new javafx.scene.chart.XYChart.Data<String, Number>(string, num);
				series.getData().add(dataPoint);
			}
			
			cardDetailLineChart.getData().add(series);
		}
	}

}
