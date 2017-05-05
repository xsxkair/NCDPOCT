package com.xsx.ncd.entity;

import java.io.Serializable;

public class Repertory implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2090918329914690883L;

	private Integer id;
	
	private Card card;								//操作的卡信息
	
	private Integer num;							//操作数目
	
	private java.sql.Timestamp time;				//操作时间
	
	private User operator;							//操作人
	
	private User user;								//领用人
	
	private Department department;					//领用科室
	
	private String detailed;						//明细

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public java.sql.Timestamp getTime() {
		return time;
	}

	public void setTime(java.sql.Timestamp time) {
		this.time = time;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDetailed() {
		return detailed;
	}

	public void setDetailed(String detailed) {
		this.detailed = detailed;
	}
	
}
