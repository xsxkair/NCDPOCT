package com.xsx.ncd.define;

import java.sql.Timestamp;

public class DeviceReportItem {

	private Integer id;
	private Integer index;
	private Timestamp testTime;
	private String sampleId;
	private String operatorName;
	private String item;
	private String deviceId;
	private Float result;
	private String reportStatus;
	
	public DeviceReportItem() {

	}
	
	public DeviceReportItem(Integer id, Integer index, Timestamp testTime, String sampleId, String operatorName, String item,
			String deviceId, Float result, Boolean reportStatus) {
		super();
		this.id = id;
		this.index = index;
		this.testTime = testTime;
		this.sampleId = sampleId;
		this.operatorName = operatorName;
		this.item = item;
		this.deviceId = deviceId;
		this.result = result;
		
		if(reportStatus == null)
			this.reportStatus = null;
		else if(reportStatus)
			this.reportStatus = StringDefine.ReportPass;
		else 
			this.reportStatus = StringDefine.ReportError;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Timestamp getTestTime() {
		return testTime;
	}
	public void setTestTime(Timestamp testTime) {
		this.testTime = testTime;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Float getResult() {
		return result;
	}

	public void setResult(Float result) {
		this.result = result;
	}

	public String getReportStatus() {
		return this.reportStatus;
	}

	public void setReportStatus(Boolean reportStatus) {
		if(reportStatus == null)
			this.reportStatus = null;
		else if(reportStatus)
			this.reportStatus = StringDefine.ReportPass;
		else 
			this.reportStatus = StringDefine.ReportError;
	}
}
