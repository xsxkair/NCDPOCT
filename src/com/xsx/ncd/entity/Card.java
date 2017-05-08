package com.xsx.ncd.entity;

import java.io.Serializable;
import java.sql.Date;

public class Card implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3043140969197987478L;

	private Integer id;
	
	private String lotnum;					//�Լ�������

	private Item item;						//�Լ���������Ŀ��Ϣ
	
	private Date makedate;			//��������
	
	private Date perioddate;		//��������
	
	private String vender;					//����

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

	public Date getMakedate() {
		return makedate;
	}

	public void setMakedate(Date makedate) {
		this.makedate = makedate;
	}

	public Date getPerioddate() {
		return perioddate;
	}

	public void setPerioddate(Date perioddate) {
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
