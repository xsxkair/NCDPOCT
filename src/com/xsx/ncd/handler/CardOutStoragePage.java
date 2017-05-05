package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.annotation.PostConstruct;
import javax.swing.RootPaneContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Item;
import com.xsx.ncd.entity.Repertory;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;


public class CardOutStoragePage implements ActivityTemplet {

	private AnchorPane rootPane = null;
	
	@FXML StackPane rootStackPane;
	
	@FXML TextField GB_CardLotTextField;
	@FXML Label GB_CardItemLabel;
	@FXML Label GB_CardMadeDateLabel;
	@FXML Label GB_CardPeriodDateLabel;
	@FXML Label GB_CardVenderLabel;
	@FXML Label GB_CardStorageNumLabel;
	@FXML TextField GB_CardOutNumTextField;
	@FXML TextField GB_CardOutDetailTextField;
	@FXML ComboBox<User> GB_CardOutUserCombox;
	@FXML ComboBox<Department> GB_CardOutDepartmentCombox;
	@FXML JFXButton CardOutStorageButton;
	
	@FXML JFXDialog modifyUserInfoDialog;
	@FXML PasswordField userPasswordTextField;
	@FXML JFXButton acceptButton0;
	@FXML JFXButton cancelButton0;
	
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHead;
	@FXML Label LogDialogContent;
	@FXML JFXButton acceptButton2;
	
	@FXML VBox GB_FreshPane;
	
	private Card tempCard = null;
	private Repertory tempRepertory = null;
	private ChangeListener<Boolean> httpUtilsServiceChangeListener = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired UserSession userSession;
	@Autowired HttpUtils httpUtils;
	
	@Override
	@PostConstruct
	public void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/CardOutStoragePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/CardOutStoragePage.fxml");
        loader.setController(this);
        try {
        	rootPane = loader.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        rootStackPane.getChildren().remove(modifyUserInfoDialog);
        rootStackPane.getChildren().remove(LogDialog);
        
        cancelButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();
		});
        
        acceptButton2.setOnMouseClicked((e)->{
        	LogDialog.close();
		});
        
        GB_CardOutNumTextField.textProperty().addListener((o, oldValue, newValue)->{
       	 if (!newValue.matches("\\d*")) {
       		 GB_CardOutNumTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        CardOutStorageButton.disableProperty().bind(GB_CardLotTextField.lengthProperty().lessThan(1).
        		or(GB_CardOutUserCombox.getSelectionModel().selectedItemProperty().isNull().
        		or(GB_CardOutDepartmentCombox.getSelectionModel().selectedItemProperty().isNull().
        		or(GB_CardOutNumTextField.lengthProperty().lessThan(1)))));
        CardOutStorageButton.setOnAction((e)->{
        	modifyUserInfoDialog.show(rootStackPane);
        });
        
        acceptButton0.disableProperty().bind(userPasswordTextField.lengthProperty().lessThan(6));
        acceptButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();

			if(userPasswordTextField.getText().equals(userSession.getUser().getPassword())){
				tempCard = new Card();
				tempRepertory = new Repertory();

				tempCard.setItem(GB_CardTypeCombox.getSelectionModel().getSelectedItem());
				tempCard.setLotnum(GB_CardLotNumTextField.getText());
				tempCard.setMakedate(java.sql.Date.valueOf(GB_CardMakeTimeDatePicker.getValue()));
				tempCard.setPerioddate(java.sql.Date.valueOf(GB_CardPeriodDateDatePicker.getValue()));
				tempCard.setVender(GB_CardVenderTextField.getText());
				
				tempRepertory.setCard(tempCard);
				tempRepertory.setDepartment(null);
				tempRepertory.setUser(null);
				tempRepertory.setDetailed(GB_CardInDetailTextField.getText());
				tempRepertory.setDsc(GB_CardInDescTextField.getText());
				tempRepertory.setNum(Integer.valueOf(GB_CardInNumTextField.getText()));
				tempRepertory.setOperator(userSession.getUser());
				tempRepertory.setTime(new Timestamp(System.currentTimeMillis()));

				httpUtils.startHttpService(ServiceEnum.SaveRepertoryRecord, tempRepertory);
				
			}
			else
				showLogsDialog("´íÎó", "ÃÜÂë´íÎó£¬½ûÖ¹²Ù×÷£¡");
		});
        
        httpUtilsServiceChangeListener = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(!newValue){
	    			if(httpUtils.getServiceEnum().equals(ServiceEnum.SaveRepertoryRecord)){

	    			}
	    			else if(httpUtils.getServiceEnum().equals(ServiceEnum.ReadAllItems)){
	    				if(httpUtils.getValue() instanceof List)
	    					GB_CardTypeCombox.getItems().setAll((List<Item>)httpUtils.getValue());
	    			}
	    		}
			}
        };
        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(rootPane.equals(newValue)){
        		httpUtils.runningProperty().addListener(httpUtilsServiceChangeListener);
        		httpUtils.startHttpService(ServiceEnum.ReadAllItems, null);
        	}
        	else {
        		httpUtils.runningProperty().removeListener(httpUtilsServiceChangeListener);
        	}
        });
        
        AnchorPane.setTopAnchor(rootPane, 0.0);
        AnchorPane.setBottomAnchor(rootPane, 0.0);
        AnchorPane.setLeftAnchor(rootPane, 0.0);
        AnchorPane.setRightAnchor(rootPane, 0.0);
        
        loader = null;
        in = null;
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}

	@Override
	public void startActivity(Object object) {
		// TODO Auto-generated method stub
		activitySession.setRootActivity(this);
		activitySession.setFatherActivity(null);
		activitySession.setChildActivity(null);
		activitySession.setActivityPane(rootPane);
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
		return "ÊÔ¼Á¿¨¹ÜÀí";
	}

}
