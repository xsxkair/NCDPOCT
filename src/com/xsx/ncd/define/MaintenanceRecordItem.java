package com.xsx.ncd.define;

import java.sql.Timestamp;

public class MaintenanceRecordItem {
	private Integer id;

	private java.sql.Timestamp testtime;
	
	private String deviceId;

	private String userName;
	
	private Boolean result;
	
	private String resultstr;
	
	private String dsc;

	public MaintenanceRecordItem() {

	}

	public MaintenanceRecordItem(Integer id, Timestamp testtime, String deviceId, String userName, Boolean result, String dsc) {
		super();
		this.id = id;
		this.testtime = testtime;
		this.deviceId = deviceId;
		this.userName = userName;
		this.result = result;
		this.dsc = dsc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.sql.Timestamp getTesttime() {
		return testtime;
	}

	public void setTesttime(java.sql.Timestamp testtime) {
		this.testtime = testtime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
