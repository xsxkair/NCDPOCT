package com.xsx.ncd.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsx.ncd.define.ActivityStatusEnum;
import com.xsx.ncd.define.QualityRecordItem;
import com.xsx.ncd.define.StringDefine;
import com.xsx.ncd.define.ValueDefine;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

@Component
public class QualityDetailHandler extends Activity {

	@FXML Label valueLabel1;
	@FXML Label valueLabel2;
	@FXML Label valueLabel3;
	@FXML Label valueLabel4;
	@FXML Label valueLabel5;
	@FXML Label valueLabel6;
	@FXML Label valueLabel7;
	@FXML Label valueLabel8;
	@FXML Label valueLabel9;
	@FXML Label valueLabel10;
	
	@FXML Label pianchaLabel1;
	@FXML Label pianchaLabel2;
	@FXML Label pianchaLabel3;
	@FXML Label pianchaLabel4;
	@FXML Label pianchaLabel5;
	@FXML Label pianchaLabel6;
	@FXML Label pianchaLabel7;
	@FXML Label pianchaLabel8;
	@FXML Label pianchaLabel9;
	@FXML Label pianchaLabel10;
	
	@FXML Label maxPianchaLabel;
	@FXML Label minPianchaLabel;
	@FXML Label avgPianchaLabel;
	@FXML Label itemLabel;
	@FXML Label resultLabel;
	@FXML Label resultDescLabel;
	
	@FXML LineChart<Number, Number> qualityChart;
	@FXML NumberAxis Y_NumberAxis;
	@FXML Label testValueLabel;
	@FXML Label normalValueLabel;

	private Series<Number, Number> normalSeries = null;
	private Series<Number, Number> testSeries = null;
	private List<Label> valueLabel = new ArrayList<>();
	private List<Label> pianchaLabel = new ArrayList<>();
	private double firstPointX = 0;
	private StackPane tempNode = null;
	private int minPoint = -1;
	private QualityRecordItem qualityRecordItem = null;
	
	@Override
	@PostConstruct
	public void onCreate() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/com/xsx/ncd/view/QualityDetail.fxml"));
        InputStream in = this.getClass().getResourceAsStream("/com/xsx/ncd/view/QualityDetail.fxml");
        loader.setController(this);
        try {
        	setRootPane(loader.load(in));
        	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        valueLabel.add(valueLabel1);
        valueLabel.add(valueLabel2);
        valueLabel.add(valueLabel3);
        valueLabel.add(valueLabel4);
        valueLabel.add(valueLabel5);
        valueLabel.add(valueLabel6);
        valueLabel.add(valueLabel7);
        valueLabel.add(valueLabel8);
        valueLabel.add(valueLabel9);
        valueLabel.add(valueLabel10);
        
        pianchaLabel.add(pianchaLabel1);
        pianchaLabel.add(pianchaLabel2);
        pianchaLabel.add(pianchaLabel3);
        pianchaLabel.add(pianchaLabel4);
        pianchaLabel.add(pianchaLabel5);
        pianchaLabel.add(pianchaLabel6);
        pianchaLabel.add(pianchaLabel7);
        pianchaLabel.add(pianchaLabel8);
        pianchaLabel.add(pianchaLabel9);
        pianchaLabel.add(pianchaLabel10);
        
        normalSeries = new Series<>();
        normalSeries.setName("标准值");
    	testSeries = new Series<>();
    	testSeries.setName("测量值");
    	qualityChart.getData().add(normalSeries);
    	qualityChart.getData().add(testSeries);
    	
    	qualityChart.setOnMouseEntered((e)->{
    		testValueLabel.setVisible(true);
    		normalValueLabel.setVisible(true);
        });
    	qualityChart.setOnMouseExited((e)->{
        	testValueLabel.setVisible(false);
    		normalValueLabel.setVisible(false);
        });
    	qualityChart.setOnMouseMoved((e)->{
        	int dataSize = 0;
        	double min = 65535;
        	double tempjuli = 0;
        	Label tempLabel = null;
        	firstPointX = qualityChart.getYAxis().getWidth() + qualityChart.getYAxis().getLayoutX() + qualityChart.getChildrenUnmodifiable().get(1).getLayoutX() ;

			dataSize = testSeries.getData().size();

			for(int i=0; i<2; i++){
				min = 65535;
				for (int j=0; j<dataSize; j++ ) {
					tempNode = (StackPane) testSeries.getData().get(j).getNode();
					tempjuli = tempNode.getLayoutX()+firstPointX;

					if(tempjuli >= e.getX())
						tempjuli = tempjuli - e.getX();
					else
						tempjuli = e.getX() - tempjuli;
						 
					if(min > tempjuli){
						min = tempjuli;
						minPoint = j;
					}
				}
					
				if(minPoint >= 0){
					tempNode = (StackPane) qualityChart.getData().get(i).getData().get(minPoint).getNode();
					
					if(i == 0)
						tempLabel = normalValueLabel;
					else
						tempLabel = testValueLabel;
					
					tempLabel.setText(String.valueOf(qualityChart.getData().get(i).getData().get(minPoint).getYValue().intValue()));
					tempLabel.setLayoutX(tempNode.getLayoutX() + firstPointX);
					tempLabel.setLayoutY(tempNode.getLayoutY() + 40);
				}
			}
			
        });
    	
        clearPageValue();

        this.setActivityName("质控详情");
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
		
		qualityRecordItem = (QualityRecordItem) object;
		
		if(qualityRecordItem != null){
			if(qualityRecordItem.getTheoreticalValue() > 0)
				updatePageValue();
		}
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

		clearPageValue();
		
		super.onDestroy();
	}

	private void updatePageValue(){
		ObjectMapper mapper = null;
		JavaType javaType = null;
		List<Float> dataList = null;
		int testCnt = 0;
		int i=0;
		float maxPicha = -65535;
		float minPicha = 65535;
		float sumPicha = 0;
		float tempValue1 = 0.0f;
		int tempValue2 = 0;
		float tempValue3 = 0;
		
		mapper = new ObjectMapper();
		javaType = mapper.getTypeFactory().constructParametricType(List.class, Float.class);
		
		try {
			dataList = mapper.readValue(qualityRecordItem.getMeasuredValue(), javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		testCnt = dataList.size();
		normalSeries.getData().clear();
		testSeries.getData().clear();
		for (i = 0; i < ValueDefine.QualityMaxTestCount; i++) {
			if(i < testCnt){
				tempValue1 = dataList.get(i);
				
				normalSeries.getData().add(new Data<Number, Number>(i+1, qualityRecordItem.getTheoreticalValue()));
				testSeries.getData().add(new Data<Number, Number>(i+1, tempValue1));
				
				valueLabel.get(i).setText(String.format("%.3f", tempValue1));

				tempValue1 -= qualityRecordItem.getTheoreticalValue();
				tempValue1 /= qualityRecordItem.getTheoreticalValue();
				pianchaLabel.get(i).setText(String.format("%.2f%%", tempValue1*100));
				
				if(maxPicha < tempValue1)
					maxPicha = tempValue1;
				
				if(minPicha > tempValue1)
					minPicha = tempValue1;
				
				sumPicha += tempValue1;
			}
		}

		maxPianchaLabel.setText(String.format("%.2f%%", maxPicha*100));
		minPianchaLabel.setText(String.format("%.2f%%", minPicha*100));
		
		sumPicha /= testCnt;
		avgPianchaLabel.setText(String.format("%.2f%%", sumPicha*100));
		
		itemLabel.setText(qualityRecordItem.getItemName());
		resultLabel.setText(qualityRecordItem.getResultstr());		
    	resultDescLabel.setText(qualityRecordItem.getDsc());
    	
    	tempValue1 = maxPicha*qualityRecordItem.getTheoreticalValue();
    	tempValue1 = Math.abs(tempValue1);
    	
    	tempValue3 = minPicha*qualityRecordItem.getTheoreticalValue();
    	tempValue3 = Math.abs(tempValue3);
    	
    	if(tempValue1 > tempValue3)
    		tempValue3 = tempValue1;
    	
    	tempValue3 /= 4;
		tempValue3 *= 5;
		
		tempValue1 = qualityRecordItem.getTheoreticalValue().intValue() - tempValue3;
		tempValue2 = new Float(tempValue1).intValue();
		Y_NumberAxis.setLowerBound(tempValue2);
		
		tempValue1 = qualityRecordItem.getTheoreticalValue().intValue() + tempValue3;
		tempValue2 = new Float(tempValue1).intValue();
		Y_NumberAxis.setUpperBound(tempValue2);
		
		tempValue2 = new Float(tempValue3).intValue();
		Y_NumberAxis.setTickUnit(tempValue2 / 5);
	}
	
	private void clearPageValue() {
	    
        for (Label label : valueLabel) {
        	label.setText(null);
		}
        
        for (Label label : pianchaLabel) {
        	label.setText(null);
		}
        
        maxPianchaLabel.setText(null);
    	minPianchaLabel.setText(null);
    	avgPianchaLabel.setText(null);
    	itemLabel.setText(null);
    	resultLabel.setText(null);
    	resultDescLabel.setText(null);
    	
		normalSeries.getData().clear();
		testSeries.getData().clear();
	}
}
