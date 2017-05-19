package com.xsx.ncd.define;

import java.sql.Timestamp;

public class DeviceReportItem {

	private Integer id;
	private Timestamp testTime;
	private Timestamp upTime;
	private String sampleId;
	private String operatorName;
	
	public DeviceReportItem() {

	}

	public DeviceReportItem(Integer id, Timestamp testTime, Timestamp upTime, String sampleId, String operatorName) {
		super();
		this.id = id;
		this.testTime = testTime;
		this.upTime = upTime;
		this.sampleId = sampleId;
		this.operatorName = operatorName;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Timestamp getTestTime() {
		return testTime;
	}
	public void setTestTime(Timestamp testTime) {
		this.testTime = testTime;
	}
	public Timestamp getUpTime() {
		return upTime;
	}
	public void setUpTime(Timestamp upTime) {
		this.upTime = upTime;
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
}
