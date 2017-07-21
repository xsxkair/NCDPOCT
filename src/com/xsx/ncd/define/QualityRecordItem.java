package com.xsx.ncd.define;

import java.sql.Timestamp;


public class QualityRecordItem {

	private Integer id;
	
	private Float theoreticalValue;					//理论值
	
	private String measuredValue;					//测量值
	
	private java.sql.Timestamp testtime;

	private String itemName;
	
	private String deviceId;
	
	private String operatorName;
	
	private Boolean result;
	
	private String resultstr;
	
	private String dsc;

	public QualityRecordItem() {

	}

	public QualityRecordItem(Integer id, Float theoreticalValue, String measuredValue, Timestamp testtime, String itemName,
			String deviceId, String operatorName, Boolean result, String dsc) {
		super();
		this.id = id;
		this.theoreticalValue = theoreticalValue;
		this.measuredValue = measuredValue;
		this.testtime = testtime;
		this.itemName = itemName;
		this.deviceId = deviceId;
		this.operatorName = operatorName;
		this.result = result;
		this.dsc = dsc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getTheoreticalValue() {
		return theoreticalValue;
	}

	public void setTheoreticalValue(Float theoreticalValue) {
		this.theoreticalValue = theoreticalValue;
	}

	public String getMeasuredValue() {
		return measuredValue;
	}

	public void setMeasuredValue(String measuredValue) {
		this.measuredValue = measuredValue;
	}

	public java.sql.Timestamp getTesttime() {
		return testtime;
	}

	public void setTesttime(java.sql.Timestamp testtime) {
		this.testtime = testtime;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}

	public String getResultstr() {
		if(this.result)
			return StringDefine.ReportPass;
		else
			return StringDefine.ReportError;
	}
}
