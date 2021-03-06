package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.ZoneId;
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
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.HttpPostType;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

@Component
public class CardInStoragePage extends Activity {

	private AnchorPane rootPane = null;
	
	@FXML StackPane rootStackPane;
	
	@FXML TextField GB_CardLotNumTextfield;
	private ContextMenu lotnumListContextMenu = null;
	@FXML ComboBox<Item> GB_CardTypeCombox;
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
	private Card tempCard2 = null;
	private Repertory tempRepertory = null;
	private ChangeListener<String> lotnumChangeListener = null;
	private ObjectProperty<String> selectLotnumProperty = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	
	@Autowired ActivitySession activitySession;
	@Autowired UserSession userSession;
	@Autowired HttpClientTool httpClientTool;
	
	@Override
	@PostConstruct
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/CardInStoragePage.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/CardInStoragePage.fxml");
        loader.setController(this);
        try {
        	this.setRootPane(loader.load(in));
        	in.close();
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
        
        CardInStorageButtom.disableProperty().bind(GB_CardLotNumTextfield.lengthProperty().lessThan(1).
        		or(GB_CardTypeCombox.getSelectionModel().selectedItemProperty().isNull().
        		or(GB_CardInNumTextField.lengthProperty().lessThan(1))));
        CardInStorageButtom.setOnAction((e)->{
        	userPasswordTextField.clear();
        	modifyUserInfoDialog.show(rootStackPane);
        	userPasswordTextField.requestFocus();
        });
        
        GB_CardLotNumTextfield.focusedProperty().addListener((o, oldValue, newValue)->{
        	if(!newValue){
        		if(GB_CardLotNumTextfield.getLength() > 0){
        			startHttpWork(ServiceEnum.QueryCardByLotNum, HttpPostType.AsynchronousJson, GB_CardLotNumTextfield.getText(), null, GB_FreshPane);
        		}
        	}
        });

 /*       selectLotnumProperty = new SimpleObjectProperty<>();
        selectLotnumProperty.addListener((o, oldValue, newValue)->{
        	if(newValue == null)
        		GB_CardLotNumTextfield.textProperty().addListener(lotnumChangeListener);
        	else{
        		System.out.println(newValue);
        		GB_CardLotNumTextfield.textProperty().removeListener(lotnumChangeListener);
        		tempCard2.setLotnum(newValue);
        		httpClientTool.myHttpAsynchronousPostJson(ServiceEnum.QueryCardByLotNum, tempCard2);
        	}
        });
        
        lotnumChangeListener = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				tempCard2.setLotnum(newValue);
	        	httpClientTool.myHttpAsynchronousPostJson(ServiceEnum.QueryCardLotNumLikeThis, tempCard2);
			}
		};
		GB_CardLotNumTextfield.textProperty().addListener(lotnumChangeListener);
*/
        
        acceptButton0.disableProperty().bind(userPasswordTextField.lengthProperty().lessThan(6));
        acceptButton0.setOnMouseClicked((e)->{
			modifyUserInfoDialog.close();

			if(userPasswordTextField.getText().equals(userSession.getUser().getPassword())){
				tempCard = new Card();
				tempRepertory = new Repertory();

				tempCard.setItem(GB_CardTypeCombox.getSelectionModel().getSelectedItem());
				tempCard.setLotnum(GB_CardLotNumTextfield.getText());
				tempCard.setMakedate(java.sql.Timestamp.valueOf(GB_CardMakeTimeDatePicker.getValue().atStartOfDay()));
				tempCard.setPerioddate(java.sql.Timestamp.valueOf(GB_CardPeriodDateDatePicker.getValue().atStartOfDay()));
				tempCard.setVender(GB_CardVenderTextField.getText());
				
				tempRepertory.setCard(tempCard);
				tempRepertory.setDepartment(null);
				tempRepertory.setUser(null);
				tempRepertory.setDetailed(GB_CardInDetailTextField.getText());
				tempRepertory.setNum(Integer.valueOf(GB_CardInNumTextField.getText()));
				tempRepertory.setOperator(userSession.getUser());
				tempRepertory.setTime(new Timestamp(System.currentTimeMillis()));

				startHttpWork(ServiceEnum.SaveRepertoryRecord, HttpPostType.AsynchronousJson, tempRepertory, null, GB_FreshPane);
			}
			else
				showLogsDialog("����", "������󣬽�ֹ������");
		});
        
        myMessageListChangeListener = c ->{
        	while(c.next()){
				if(c.wasAdded()){
					GB_FreshPane.setVisible(false);
					for (Message message : c.getAddedSubList()) {
						switch (message.getWhat()) {
						case SaveRepertoryRecord:
							Repertory repertory = message.getObj();
							if(repertory == null)
								showLogsDialog("����", "���ʧ�ܣ�");
							else
								clearPage();
							break;
						case QueryCardLotNumLikeThis:
							//refreshCardLotnumList((List<String>)message.getObj());
							//lotnumListContextMenu.setFocused(true);
							break;
						case ReadAllItems:
							List<Item> items = message.getObj();
							GB_CardTypeCombox.getItems().setAll(items);
							break;
						case QueryCardByLotNum:
							tempCard = message.getObj();
							if(tempCard != null){									
								GB_CardTypeCombox.getSelectionModel().select(tempCard.getItem());
								
								if(tempCard.getMakedate() != null)
									GB_CardMakeTimeDatePicker.setValue(tempCard.getMakedate().toLocalDateTime().toLocalDate());
								
								if(tempCard.getPerioddate() != null)
									GB_CardPeriodDateDatePicker.setValue(tempCard.getPerioddate().toLocalDateTime().toLocalDate());
								
								if(tempCard.getVender() != null)
									GB_CardVenderTextField.setText(tempCard.getVender());
							}
							else{
								GB_CardTypeCombox.getSelectionModel().select(null);
								GB_CardMakeTimeDatePicker.setValue(null);
								GB_CardPeriodDateDatePicker.setValue(null);
								GB_CardVenderTextField.setText(null);
							}
							break;
						default:
							break;
						}
					}
				}
			}
        };
        
        this.setActivityName("�Լ������");
        this.setActivityStatus(ActivityStatusEnum.Create);
        
        AnchorPane.setTopAnchor(this.getRootPane(), 0.0);
        AnchorPane.setBottomAnchor(this.getRootPane(), 0.0);
        AnchorPane.setLeftAnchor(this.getRootPane(), 0.0);
        AnchorPane.setRightAnchor(this.getRootPane(), 0.0);
        
        loader = null;
        in = null;
	}
	
	private void showLogsDialog(String head, String logs) {
		LogDialogHead.setText(head);
		LogDialogContent.setText(logs);
		LogDialog.show(rootStackPane);
	}

	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		setMyMessagesList(FXCollections.observableArrayList());
		getMyMessagesList().addListener(myMessageListChangeListener);
		
		tempCard2 = new Card();
		clearPage();
		lotnumListContextMenu = new ContextMenu();
		lotnumListContextMenu.setPrefWidth(200);
		startHttpWork(ServiceEnum.ReadAllItems, HttpPostType.AsynchronousJson, tempRepertory, null, GB_FreshPane);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		tempCard2 = null;
		lotnumListContextMenu = null;
		
		getMyMessagesList().removeListener(myMessageListChangeListener);
		setMyMessagesList(null);
	}

	private void clearPage(){
		GB_CardLotNumTextfield.clear();
		GB_CardTypeCombox.getSelectionModel().select(null);
		GB_CardMakeTimeDatePicker.setValue(null);
		GB_CardPeriodDateDatePicker.setValue(null);
		GB_CardVenderTextField.setText(null);
		GB_CardInNumTextField.clear();
		GB_CardInDetailTextField.clear();
	}
	
	private void refreshCardLotnumList(List<String> lots){
		
		lotnumListContextMenu.getItems().clear();
		
		for (String string : lots) {
			 CustomMenuItem menuItem = new CustomMenuItem(new Label(string), true);
			 menuItem.setOnAction((e)->{
				 selectLotnumProperty.set(string);
				 GB_CardLotNumTextfield.setText(string);
			 });
			 lotnumListContextMenu.getItems().add(menuItem);
		}
		if(lots.size() <= 1)
			lotnumListContextMenu.hide();
		else{
			lotnumListContextMenu.show(GB_CardLotNumTextfield, Side.BOTTOM, 0, 0);
			selectLotnumProperty.set(null);
		}
	}
}
