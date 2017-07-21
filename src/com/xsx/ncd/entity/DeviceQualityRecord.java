package com.xsx.ncd.entity;


public class DeviceQualityRecord {

	private Integer id;
	
	private Float normalv;										//标准值
	
	private String measurev;									//测量值json
	
	private java.sql.Timestamp testtime;
	
	private Item item;
	
	private Device device;
	
	private Operator operator;
	
	private Boolean result;
	
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

	public String getMeasurev() {
		return measurev;
	}

	public void setMeasurev(String measurev) {
		this.measurev = measurev;
	}

	public java.sql.Timestamp getTesttime() {
		return testtime;
	}

	public void setTesttime(java.sql.Timestamp testtime) {
		this.testtime = testtime;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
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
}
