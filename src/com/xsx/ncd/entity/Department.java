package com.xsx.ncd.entity;

import java.io.Serializable;

public class Department implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2297757163195796572L;

	private Integer id;
	
	private String name;
	
	private String addr;
	
	private String des;

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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
}
