package com.xsx.ncd.define;

import java.sql.Timestamp;

public class AdjustRecordItem {
	
	private Integer id;
	private Float theoreticalValue;
	private Float measuredValue;
	private Timestamp testTime;
	private String userName;
	private String deviceId;
	private String result;
	private String desc;
	
	
	public AdjustRecordItem() {

	}
	
	public AdjustRecordItem(Integer id, Float theoreticalValue, Float measuredValue, Timestamp testTime,
			String userName, String deviceId, String result, String desc) {
		super();
		this.id = id;
		this.theoreticalValue = theoreticalValue;
		this.measuredValue = measuredValue;
		this.testTime = testTime;
		this.userName = userName;
		this.deviceId = deviceId;
		this.result = result;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Float getTheoreticalValue() {
		return theoreticalValue;
	}
	public void setTheoreticalValue(Float theoreticalValue) {
		this.theoreticalValue = theoreticalValue;
	}
	public Float getMeasuredValue() {
		return measuredValue;
	}
	public void setMeasuredValue(Float measuredValue) {
		this.measuredValue = measuredValue;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
