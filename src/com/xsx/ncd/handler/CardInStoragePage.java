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
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.svg.SVGGlyphLoader;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Item;
import com.xsx.ncd.entity.Repertory;
import com.xsx.ncd.entity.User;
import com.xsx.ncd.spring.ActivitySession;
import com.xsx.ncd.spring.UserSession;
import com.xsx.ncd.tool.HttpClientTool;
import com.xsx.ncd.tool.HttpUtils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

@Component
public class CardInStoragePage implements ActivityTemplet {

	private AnchorPane rootPane = null;
	
	@FXML StackPane rootStackPane;
	
	@FXML JFXComboBox<String> GB_CardLotNumBox;
	@FXML JFXComboBox<Item> GB_CardTypeCombox;
	@FXML JFXDatePicker GB_CardMakeTimeDatePicker;
	@FXML JFXDatePicker GB_CardPeriodDateDatePicker;
	@FXML TextField GB_CardVenderTextField;
	@FXML TextField GB_CardInNumTextField;
	@FXML TextField GB_CardInDetailTextField;
	@FXML JFXButton CardInStorageButtom;
	
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
	private ObservableList<Message> myMessagesList = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired UserSession userSession;
	@Autowired HttpClientTool httpClientTool;
	
	@Override
	@PostConstruct
	public void UI_Init() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/CardInStoragePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/CardInStoragePage.fxml");
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
        
        GB_CardInNumTextField.textProperty().addListener((o, oldValue, newValue)->{
       	 if (!newValue.matches("\\d*")) {
            	GB_CardInNumTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        CardInStorageButtom.disableProperty().bind(GB_CardLotNumBox.getSelectionModel().selectedItemProperty().isNull().
        		or(GB_CardTypeCombox.getSelectionModel().selectedItemProperty().isNull().
        		or(GB_CardInNumTextField.lengthProperty().lessThan(1))));
        CardInStorageButtom.setOnAction((e)->{
        	modifyUserInfoDialog.show(rootStackPane);
        });
        
        GB_CardLotNumBox.proper
        
        acceptButton0.disableProperty().bind(userPasswordTextField.lengthProperty().lessThan(6));
        acceptButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();

			if(userPasswordTextField.getText().equals(userSession.getUser().getPassword())){
				tempCard = new Card();
				tempRepertory = new Repertory();

				tempCard.setItem(GB_CardTypeCombox.getSelectionModel().getSelectedItem());
				tempCard.setLotnum(GB_CardLotNumBox.getSelectionModel().getSelectedItem());
				tempCard.setMakedate(java.sql.Date.valueOf(GB_CardMakeTimeDatePicker.getValue()));
				tempCard.setPerioddate(java.sql.Date.valueOf(GB_CardPeriodDateDatePicker.getValue()));
				tempCard.setVender(GB_CardVenderTextField.getText());
				
				tempRepertory.setCard(tempCard);
				tempRepertory.setDepartment(null);
				tempRepertory.setUser(null);
				tempRepertory.setDetailed(GB_CardInDetailTextField.getText());
				tempRepertory.setNum(Integer.valueOf(GB_CardInNumTextField.getText()));
				tempRepertory.setOperator(userSession.getUser());
				tempRepertory.setTime(new Timestamp(System.currentTimeMillis()));

				httpClientTool.myHttpAsynchronousPostJson(ServiceEnum.SaveRepertoryRecord, tempRepertory);
				
			}
			else
				showLogsDialog("´íÎó", "ÃÜÂë´íÎó£¬½ûÖ¹²Ù×÷£¡");
		});
        
        myMessagesList = FXCollections.observableArrayList();
        myMessagesList.addListener(new ListChangeListener<Message>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				// TODO Auto-generated method stub
				while(c.next()){
					if(c.wasAdded()){
						GB_FreshPane.setVisible(false);
						for (Message message : c.getAddedSubList()) {
							switch (message.getWhat()) {
							case SaveRepertoryRecord:
								
								break;
							case ReadAllItems:
								GB_CardTypeCombox.getItems().setAll((List<Item>)message.getObj());
								break;
							default:
								break;
							}
						}
					}
				}
			}
        	
        });

        
        activitySession.getActivityPane().addListener((o, oldValue, newValue)->{
        	if(this.equals(newValue)){
        		
        	}
        	else {

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
		return "ÊÔ¼Á¿¨Èë¿â";
	}

	@Override
	public Pane getActivityRootPane() {
		// TODO Auto-generated method stub
		return rootPane;
	}

	@Override
	public void PostMessageToThisActivity(Message message) {
		// TODO Auto-generated method stub
		Platform.runLater(()->{
			myMessagesList.add(message);
		});
	}

}
