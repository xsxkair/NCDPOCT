package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.NCD_YGFXY;
import com.xsx.ncd.spring.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

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
	
	private Map<String, String> reportInfo = null;
	private ListChangeListener<Message> myMessageListChangeListener = null;
	private NCD_YGFXY reportData = null;
	private Series<Number, Number> reportSeries = new Series<>();
	
	@Autowired UserSession userSession;
	
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
        
        GB_TestLineChart.getData().add(reportSeries);
        
        GB_EditReportButton.setOnAction((e)->{
        	if(GB_EditReportButton.getText().equals("编辑报告")){
        		GB_ReportOKToggleButton.setDisable(false);
	        	GB_ReportErrorToggleButton.setDisable(false);
	        	GB_ReportDescTextField.setEditable(true);
        		GB_EditReportButton.setText("取消编辑");
        		GB_ReportControlHBox.getChildren().setAll(GB_EditReportButton, GB_CommitReportButton);
        	}
        	else{
        		GB_ReportOKToggleButton.setDisable(true);
	        	GB_ReportErrorToggleButton.setDisable(true);
	        	GB_ReportDescTextField.setEditable(false);
        		GB_EditReportButton.setText("编辑报告");
        		GB_ReportControlHBox.getChildren().setAll(GB_EditReportButton);
        	}
        });
        
        GB_CommitReportButton.setOnAction((e)->{
        	reportData.setUser(userSession.getUser());
        	reportData.setHandltime(new Timestamp(System.currentTimeMillis()));
        	reportData.setReportresult(((JFXRadioButton)S_ReportToogleGroup.getSelectedToggle()).getText());
        	reportData.setReportdsc(GB_ReportDescTextField.getText());
        	startHttpWork(ServiceEnum.SaveNcdYGFXYReport, HttpPostType.AsynchronousJson, reportData, null, null);
        });
        
        myMessageListChangeListener = c ->{
        	while(c.next()){
				if(c.wasAdded()){

					for (Message message : c.getAddedSubList()) {
						switch (message.getWhat()) {
							case QueryNcdYGFXYReportById:
								showReportInfo(message.getObj());
								break;
								
							default:
								break;
						}
					}
				}
			}
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

	@SuppressWarnings("unchecked")
	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		reportInfo = (Map<String, String>) object;
		
		setMyMessagesList(FXCollections.observableArrayList());
		getMyMessagesList().addListener(myMessageListChangeListener);
		
		startHttpWork(ServiceEnum.QueryNcdYGFXYReportById, HttpPostType.AsynchronousForm, null, reportInfo, null);
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
		getMyMessagesList().removeListener(myMessageListChangeListener);
		setMyMessagesList(null);
		
		reportInfo = null;
	}

	private void showReportInfo(NCD_YGFXY reportJson) {
		StringBuffer stringBuffer = new StringBuffer();
		
		reportData = reportJson;
		
		GB_TestTimeLabel.setText((reportData.getTesttime() == null)?"无":reportData.getTesttime().toString());
		GB_EnvironmentTemperatureLabel.setText((reportData.getAmbienttemp() == null)?"无":reportData.getAmbienttemp().toString());
		GB_CardTemperatureLabel.setText((reportData.getCardtemp() == null)?"无":reportData.getCardtemp().toString());
		
		stringBuffer.setLength(0);
		stringBuffer.append(reportData.getDevice().getAddr());
		stringBuffer.append("(");
		stringBuffer.append(reportData.getDevice().getDid());
		stringBuffer.append(")");
		GB_DeviceLabel.setText(stringBuffer.toString());
		
		stringBuffer.setLength(0);
		stringBuffer.append(reportData.getItem().getName());
		stringBuffer.append("(");
		stringBuffer.append(reportData.getCardlot());
		stringBuffer.append("-");
		stringBuffer.append(reportData.getCardnum());
		stringBuffer.append(")");
		GB_CardLabel.setText(stringBuffer.toString());
		
		GB_SampleIDLabel.setText(reportData.getSampleid());
		GB_TesterLabel.setText(reportData.getOperator().getName());
		
		if(reportData.getT_isok()){
			stringBuffer.setLength(0);
			stringBuffer.append(reportData.getTestv());
			stringBuffer.append(' ');
			if(reportData.getItem() != null)
				stringBuffer.append(reportData.getItem().getUnit());
			GB_TestResultLabel.setText(stringBuffer.toString());
			GB_TestResultLabel.setStyle("-fx-text-fill: #1a3f83");
		}
		else{
			GB_TestResultLabel.setText("Error");
			GB_TestResultLabel.setStyle("-fx-text-fill: red");
		}
		
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Integer.class);
		try {
			List<Integer> series = mapper.readValue(reportData.getSeries(), javaType);
			
			Integer t = reportData.getTline();
	        Integer b = reportData.getBline();
	        Integer c = reportData.getCline();
			int length = series.size();
			for (int i=0; i<length; i++) {
				Data<Number, Number> data = new Data<Number, Number>(i, series.get(i));
	        	StackPane stackPane = new StackPane();
	        	
	        	stackPane.setPrefSize(3, 3);
	        	
	        	if((t != null) && (i == t.intValue())){
	        		stackPane.setStyle("-fx-background-color:red");
	        		stackPane.setPrefSize(10, 10);
	        	}
	        	else if((b != null) && (i == b.intValue())){
	        		stackPane.setStyle("-fx-background-color:green");
	        		stackPane.setPrefSize(10, 10);
	        	}
	        	else if((c != null) && (i == c.intValue())){
	        		stackPane.setStyle("-fx-background-color:blue");
	        		stackPane.setPrefSize(10, 10);
	        	}
	        	
	        	data.setNode(stackPane);
	        	reportSeries.getData().addAll(data);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String r_result = reportData.getReportresult();
        if("合格".equals(r_result)){
        	S_ReportToogleGroup.selectToggle(GB_ReportOKToggleButton);
        	GB_ReportOKToggleButton.setDisable(true);
        	GB_ReportErrorToggleButton.setDisable(true);
        	GB_ReportDescTextField.setEditable(false);
        	GB_EditReportButton.setText("编辑报告");
        	GB_ReportControlHBox.getChildren().setAll(GB_EditReportButton);
        }
        else if("不合格".equals(r_result)){
        	S_ReportToogleGroup.selectToggle(GB_ReportErrorToggleButton);
        	GB_ReportOKToggleButton.setDisable(true);
        	GB_ReportErrorToggleButton.setDisable(true);
        	GB_ReportDescTextField.setEditable(false);
        	GB_EditReportButton.setText("编辑报告");
        	GB_ReportControlHBox.getChildren().setAll(GB_EditReportButton);
        }
        else {
			S_ReportToogleGroup.selectToggle(null);
			GB_ReportOKToggleButton.setDisable(false);
        	GB_ReportErrorToggleButton.setDisable(false);
        	GB_ReportDescTextField.setEditable(true);
        	GB_EditReportButton.setText("编辑报告");
        	GB_ReportControlHBox.getChildren().setAll(GB_CommitReportButton);
		}
	}
}
