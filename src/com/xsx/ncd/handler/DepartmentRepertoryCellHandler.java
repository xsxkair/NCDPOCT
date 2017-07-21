package com.xsx.ncd.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.jfoenix.controls.JFXListView;

import javafx.scene.control.Label;

public class DepartmentRepertoryCellHandler {

	private Label departmentNameLabel;
	private JFXListView<ItemRrepertoryCellHandler> itemRepertoryCellListView;

	public DepartmentRepertoryCellHandler(String name, Map<String, Long> dataMaps) {
		super();
		
		onCreate();
		
		departmentNameLabel.setText(name);
		
		this.updateRepertory(dataMaps);
	}
	
	private void onCreate(){
		
		departmentNameLabel = new Label();
		
		itemRepertoryCellListView = new JFXListView<>();
	}
	
	public void setPageValue(String name, Map<String, Long> dataMaps){
		departmentNameLabel.setText(name);
		
		this.updateRepertory(dataMaps);
	}
	
	private void updateRepertory(Map<String, Long> dataMaps){
		if(dataMaps == null)
			return;
		
		itemRepertoryCellListView.getItems().clear();
		
		Set<String> keys = dataMaps.keySet();
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String type = iterator.next();
			long num = dataMaps.get(type);
			
			ItemRrepertoryCellHandler itemRrepertoryCellHandler = new ItemRrepertoryCellHandler(type, num);
			
			itemRepertoryCellListView.getItems().add(itemRrepertoryCellHandler);
		}
	}

	public Label getDepartmentNameLabel() {
		return departmentNameLabel;
	}

	public JFXListView<ItemRrepertoryCellHandler> getItemRepertoryCellListView() {
		return itemRepertoryCellListView;
	}
}
