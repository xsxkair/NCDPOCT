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

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
public class CardOutStoragePage implements ActivityTemplet, HttpTemplet {

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
	
	private ObjectProperty<Card> outCard = null;					//出库试剂卡信息，如果为null，不允许操作
	private Card tempCard2 = null;									//与服务器通信数据
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
        
        outCard = new SimpleObjectProperty<>(null);
        
 /*       CardOutStorageButton.disableProperty().bind(outCard.isNull().
        		or(GB_CardOutUserCombox.getSelectionModel().selectedItemProperty().isNull().
        		or(GB_CardOutDepartmentCombox.getSelectionModel().selectedItemProperty().isNull().
        		or(GB_CardOutNumTextField.lengthProperty().lessThan(1)))));*/
        CardOutStorageButton.disableProperty().bind(new BooleanBinding() {
			{
				bind(outCard);
				bind(GB_CardOutUserCombox.getSelectionModel().selectedItemProperty());
				bind(GB_CardOutDepartmentCombox.getSelectionModel().selectedItemProperty());
				bind(GB_CardOutNumTextField.lengthProperty());
				bind(GB_CardStorageNumLabel.textProperty());
			}
			
			@Override
			protected boolean computeValue() {
				// TODO Auto-generated method stub
				if(outCard.get() == null)
					return true;
				
				if(GB_CardOutUserCombox.getSelectionModel().getSelectedItem() == null)
					return true;
				
				if(GB_CardOutDepartmentCombox.getSelectionModel().getSelectedItem() == null)
					return true;
				
				Integer allNum = null;
				Integer thisNum = null;
				
				try {
					allNum = Integer.valueOf(GB_CardStorageNumLabel.getText());
					thisNum = Integer.valueOf(GB_CardOutNumTextField.getText());
				} catch (Exception e2) {
					// TODO: handle exception
					return true;
				}
				
				if(thisNum > allNum)
					return true;
				
				return false;
			}
		});
        CardOutStorageButton.setOnAction((e)->{
        	
        	userPasswordTextField.clear();
        	modifyUserInfoDialog.show(rootStackPane);
        });
        
        GB_CardLotTextField.focusedProperty().addListener((o, oldValue, newValue)->{
        	if(!newValue){
        		if(GB_CardLotTextField.getLength() > 0){
        			startHttpWork(ServiceEnum.QueryCardByLotNum, GB_CardLotTextField.getText());
        		}
        	}
        });
        
        acceptButton0.disableProperty().bind(userPasswordTextField.lengthProperty().lessThan(6));
        acceptButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();

			if(userPasswordTextField.getText().equals(userSession.getUser().getPassword())){
				tempRepertory = new Repertory();
				
				tempRepertory.setCard(outCard.get());
				tempRepertory.setDepartment(GB_CardOutDepartmentCombox.getSelectionModel().getSelectedItem());
				tempRepertory.setUser(GB_CardOutUserCombox.getSelectionModel().getSelectedItem());
				tempRepertory.setDetailed(GB_CardOutDetailTextField.getText());
				tempRepertory.setNum(0-Integer.valueOf(GB_CardOutNumTextField.getText()));
				tempRepertory.setOperator(userSession.getUser());
				tempRepertory.setTime(new Timestamp(System.currentTimeMillis()));

				startHttpWork(ServiceEnum.SaveRepertoryRecord, tempRepertory);
				
			}
			else
				showLogsDialog("错误", "密码错误，禁止操作！");
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
								if(message.getObj(Repertory.class) == null)
									showLogsDialog("错误", "入库失败！");
								else
									clearPage();
								break;
							case ReadAllUser:
								GB_CardOutUserCombox.getItems().setAll(message.getObj(List.class));
								break;
							case ReadAllDepartment:
								GB_CardOutDepartmentCombox.getItems().setAll(message.getObj(List.class));
								break;
							case QueryCardByLotNum:
								tempCard2 = message.getObj(Card.class);
								outCard.set(tempCard2);
								if(tempCard2 != null){									
									GB_CardItemLabel.setText(tempCard2.getItem().getName());
									
									if(tempCard2.getMakedate() != null)
										GB_CardMadeDateLabel.setText(tempCard2.getMakedate().toLocalDateTime().toLocalDate().toString());
									
									if(tempCard2.getPerioddate() != null)
										GB_CardPeriodDateLabel.setText(tempCard2.getPerioddate().toLocalDateTime().toLocalDate().toString());
									
									if(tempCard2.getVender() != null)
										GB_CardVenderLabel.setText(tempCard2.getVender());
									
									startHttpWork(ServiceEnum.QueryRepertoryNumByCard, tempCard2);
								}
								else{
									GB_CardItemLabel.setText(null);
									GB_CardMadeDateLabel.setText(null);
									GB_CardPeriodDateLabel.setText(null);
									GB_CardVenderLabel.setText(null);
									GB_CardStorageNumLabel.setText(null);
								}
								break;
							case QueryRepertoryNumByCard:
								GB_CardStorageNumLabel.setText((message.getObj(Long.class)).toString());
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
        		tempCard2 = new Card();
        		clearPage();
        		startHttpWork(ServiceEnum.ReadAllUser, tempRepertory);
        		startHttpWork(ServiceEnum.ReadAllDepartment, tempRepertory);
        	}
        	else {
        		tempCard2 = null;
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
		activitySession.setFatherActivity(this);
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
		return "试剂卡出库";
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

	private void clearPage(){
		GB_CardLotTextField.clear();
		GB_CardItemLabel.setText(null);
		GB_CardMadeDateLabel.setText(null);
		GB_CardPeriodDateLabel.setText(null);
		GB_CardVenderLabel.setText(null);
		GB_CardStorageNumLabel.setText(null);
		GB_CardOutNumTextField.clear();
		GB_CardOutUserCombox.getSelectionModel().select(null);
		GB_CardOutDepartmentCombox.getSelectionModel().select(null);
		GB_CardOutDetailTextField.clear();
	}
	
	@Override
	public void startHttpWork(ServiceEnum serviceEnum, Object parm) {
		GB_FreshPane.setVisible(true);
		
		if(!httpClientTool.myHttpAsynchronousPostJson(this, serviceEnum, parm)){
			GB_FreshPane.setVisible(false);
			showLogsDialog("错误", "数据转换失败，请重试！");
		}	
	}
}
