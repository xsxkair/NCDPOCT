package com.xsx.ncd.entity;

import java.io.Serializable;


public class DeviceMaintenanceRecord implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 2009773139949817302L;

	private Integer id;

	private java.sql.Timestamp testtime;

	private Device device;
	
	private Operator operator;
	
	private String dsc;

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

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}
	
	
}
