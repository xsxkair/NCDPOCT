package com.xsx.ncd.define;

public class DeviceJson {

	private Integer id;
	private String name;
	private String deviceId;
	private String model;
	private String ico;
	private String departmentName;
	private Long lastTime;
	private String deviceTypeCode;
	private String deviceAddr;
	
	public DeviceJson() {

	}
	
	public DeviceJson(Integer id, String name, String deviceId, String model, String ico, String departmentName,
			Long lastTime, String deviceTypeCode, String deviceAddr) {

		this.id = id;
		this.name = name;
		this.deviceId = deviceId;
		this.model = model;
		this.ico = ico;
		this.departmentName = departmentName;
		this.lastTime = lastTime;
		this.deviceTypeCode = deviceTypeCode;
		this.deviceAddr = deviceAddr;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getIco() {
		return ico;
	}
	public void setIco(String ico) {
		this.ico = ico;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Long getLastTime() {
		return lastTime;
	}
	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}

	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}

	public String getDeviceAddr() {
		return deviceAddr;
	}

	public void setDeviceAddr(String deviceAddr) {
		this.deviceAddr = deviceAddr;
	}
	
	
}
