package com.xsx.ncd.entity;


public class DeviceAdjustRecord {

	private Integer id;
	
	private Float normalv;
	
	private Float measurev;
	
	private java.sql.Timestamp testtime;
	
	private Device device;
	
	private Operator operator;
	
	private String result;
	
	private String dsc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getNormalv() {
		return normalv;
	}

	public void setNormalv(Float normalv) {
		this.normalv = normalv;
	}

	public Float getMeasurev() {
		return measurev;
	}

	public void setMeasurev(Float measurev) {
		this.measurev = measurev;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}
	
	
}
