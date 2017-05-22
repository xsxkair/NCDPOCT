package com.xsx.ncd.handler;

import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.DeviceReportItem;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.RecordJson;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.handler.DeviceReportListHandler.QueryDeviceService.MyTask;
import com.xsx.ncd.spring.SpringFacktory;
import com.xsx.ncd.tool.HttpClientTool;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class NCDYGFXYReportHandler extends Activity {

	@FXML StackPane GB_RootStackPane;

	//测试信息
	@FXML Label GB_ChartPointLabel;
	@FXML LineChart<Number, Number> GB_TestLineChart;
	@FXML private Label GB_TestTimeLabel;
	@FXML private Label GB_EnvironmentTemperatureLabel;
	@FXML private Label GB_CardTemperatureLabel;
	@FXML private Label GB_DeviceLabel;
	@FXML private Label GB_CardLabel;
	@FXML private Label GB_SampleIDLabel;
	@FXML private Label GB_TesterLabel;
	@FXML private Label GB_RealWaittimeLabel;
	@FXML private Label GB_TestResultLabel;

	//报告信息
	@FXML Label GB_ManagerNameLabel;
	@FXML Label GB_ManagTimeLabel;
	@FXML Label GB_ReportStatusLabel;
	@FXML HBox GB_ReportStatusToggleHBox;
	@FXML JFXRadioButton GB_ReportOKToggleButton;
	@FXML JFXRadioButton GB_ReportErrorToggleButton;
	@FXML ToggleGroup S_ReportToogleGroup;
	@FXML TextField GB_ReportDescTextField;
	@FXML HBox GB_ReportControlHBox;
	@FXML JFXButton GB_EditReportButton;
	@FXML JFXButton GB_CommitReportButton;
	
	//信息提示对话框
	@FXML JFXDialog LogDialog;
	@FXML Label LogDialogHeadLabel;
	@FXML Label LogDialogContentLabel;
	@FXML JFXButton acceptButton1;
	
	private String deviceType = null;
	private Integer reportId = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	
	@Override
	@PostConstruct
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/NCDYGFXYReport.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/NCDYGFXYReport.fxml");
        loader.setController(this);
        
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        myMessageListChangeListener = c ->{
        	
        };
        
        this.setActivityName("报告信息");
        this.setActivityStatus(ActivityStatusEnum.Create);
        
        AnchorPane.setTopAnchor(this.getRootPane(), 0.0);
        AnchorPane.setBottomAnchor(this.getRootPane(), 0.0);
        AnchorPane.setLeftAnchor(this.getRootPane(), 0.0);
        AnchorPane.setRightAnchor(this.getRootPane(), 0.0);
        
        loader = null;
        in = null;
	}

	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		deviceType = (String) ((Object[])object)[0];
		reportId = (Integer) ((Object[])object)[1];
		
		setMyMessagesList(FXCollections.observableArrayList());
		getMyMessagesList().addListener(myMessageListChangeListener);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

}
