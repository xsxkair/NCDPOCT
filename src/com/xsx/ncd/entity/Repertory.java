package com.xsx.ncd.entity;

import java.io.Serializable;

public class Repertory implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2090918329914690883L;

	private Integer id;
	
	private Card card;								//�����Ŀ���Ϣ
	
	private Integer num;							//������Ŀ
	
	private java.sql.Timestamp time;				//����ʱ��
	
	private User operator;							//������
	
	private User user;								//������
	
	private Department department;					//���ÿ���
	
	private String detailed;						//��ϸ

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
