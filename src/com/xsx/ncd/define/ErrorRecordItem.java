package com.xsx.ncd.define;

import java.sql.Timestamp;

public class ErrorRecordItem {
	
	private Integer id;
	private Timestamp testTime;
	private String userName;
	private String deviceId;
	private Integer errorCode;
	private String desc;
	
	public ErrorRecordItem(){
		
	}
	
	public ErrorRecordItem(Integer id, Timestamp testTime, String userName, String deviceId, int errorCode, String desc) {
		this.id = id;
		this.testTime = testTime;
		this.userName = userName;
		this.deviceId = deviceId;
		this.errorCode = errorCode;
		this.desc = desc;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
