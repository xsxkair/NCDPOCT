package com.xsx.ncd.define;

import com.xsx.ncd.entity.DeviceType;

public class DeviceTypeJson {
	private Integer id;
	
	private String code;
	
	private String name;
	
	private String model;
	
	public DeviceTypeJson() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DeviceTypeJson(Integer id, String code, String name, String model) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.model = model;
	}

	public DeviceTypeJson(DeviceType deviceType) {
		this.id = deviceType.getId();
		this.code = deviceType.getCode();
		this.name = deviceType.getName();
		this.model = deviceType.getModel();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return name + "   [" + model + "]";
	}
}
