package com.xsx.ncd.entity;

import java.util.HashSet;
import java.util.Set;


public class Device{


	private Integer id;
	
	private String did;
	
	private DeviceType devicetype;
	
	private String status;
	
	private Long lasttime;
	
	private Operator operator;
	
	private Set<Operator> operators = new HashSet<>();
	
	private Department department;
	
	private String addr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public DeviceType getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(DeviceType devicetype) {
		this.devicetype = devicetype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLasttime() {
		return lasttime;
	}

	public void setLasttime(Long lasttime) {
		this.lasttime = lasttime;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Set<Operator> getOperators() {
		return operators;
	}

	public void setOperators(Set<Operator> operators) {
		this.operators = operators;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
	
}
