package com.xsx.ncd.entity;


public class NCD_XTY {
	
	private Integer id;
	
	private Device device;
	
	private Item item;

	private Operator operator;

	private User user;

	private String cardlot;

	private String cardnum;

	private String sampleid;
	
	private java.sql.Timestamp testtime;
	
	private Float ambienttemp;
	
	private Float cardtemp;

	private String series;
	
	private Float testv;

	private Boolean t_isok;
	
	private java.sql.Timestamp uptime;
	
	private java.sql.Timestamp handltime;
	
	private String reportresult;
	
	private String reportdsc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCardlot() {
		return cardlot;
	}

	public void setCardlot(String cardlot) {
		this.cardlot = cardlot;
	}

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public String getSampleid() {
		return sampleid;
	}

	public void setSampleid(String sampleid) {
		this.sampleid = sampleid;
	}

	public java.sql.Timestamp getTesttime() {
		return testtime;
	}

	public void setTesttime(java.sql.Timestamp testtime) {
		this.testtime = testtime;
	}

	public Float getAmbienttemp() {
		return ambienttemp;
	}

	public void setAmbienttemp(Float ambienttemp) {
		this.ambienttemp = ambienttemp;
	}

	public Float getCardtemp() {
		return cardtemp;
	}

	public void setCardtemp(Float cardtemp) {
		this.cardtemp = cardtemp;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Float getTestv() {
		return testv;
	}

	public void setTestv(Float testv) {
		this.testv = testv;
	}

	public Boolean getT_isok() {
		return t_isok;
	}

	public void setT_isok(Boolean t_isok) {
		this.t_isok = t_isok;
	}

	public java.sql.Timestamp getUptime() {
		return uptime;
	}

	public void setUptime(java.sql.Timestamp uptime) {
		this.uptime = uptime;
	}

	public java.sql.Timestamp getHandltime() {
		return handltime;
	}

	public void setHandltime(java.sql.Timestamp handltime) {
		this.handltime = handltime;
	}

	public String getReportresult() {
		return reportresult;
	}

	public void setReportresult(String reportresult) {
		this.reportresult = reportresult;
	}

	public String getReportdsc() {
		return reportdsc;
	}

	public void setReportdsc(String reportdsc) {
		this.reportdsc = reportdsc;
	}
}
