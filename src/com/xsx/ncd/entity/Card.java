package com.xsx.ncd.entity;

import java.io.Serializable;
import java.sql.Date;

public class Card implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3043140969197987478L;

	private Integer id;
	
	private String lotnum;					//试剂卡批号

	private Item item;						//试剂卡测试项目信息
	
	private java.sql.Timestamp makedate;			//生产日期
	
	private java.sql.Timestamp perioddate;		//过期日期
	
	private String vender;					//厂家

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLotnum() {
		return lotnum;
	}

	public void setLotnum(String lotnum) {
		this.lotnum = lotnum;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public java.sql.Timestamp getMakedate() {
		return makedate;
	}

	public void setMakedate(java.sql.Timestamp makedate) {
		this.makedate = makedate;
	}

	public java.sql.Timestamp getPerioddate() {
		return perioddate;
	}

	public void setPerioddate(java.sql.Timestamp perioddate) {
		this.perioddate = perioddate;
	}

	public String getVender() {
		return vender;
	}

	public void setVender(String vender) {
		this.vender = vender;
	}
	
	@Override
	public String toString() {
		return lotnum;
	}
}
