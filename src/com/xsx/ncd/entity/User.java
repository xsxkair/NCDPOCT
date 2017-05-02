package com.xsx.ncd.entity;

import java.io.Serializable;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 123450L;

	private Integer id;
	
	private String account;
	
	private String password;
	
	private String name;
	
	private String age;
	
	private String sex;
	
	private String phone;
	
	private String job;
	
	private Department department;
	
	private String des;
	
	private Boolean managedevice;
	
	private Boolean managereport;
	
	private Boolean manageuser;
	
	private Boolean managecard;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public Boolean getManagedevice() {
		return managedevice;
	}

	public void setManagedevice(Boolean adddevice) {
		this.managedevice = adddevice;
	}

	public Boolean getManagereport() {
		return managereport;
	}

	public void setManagereport(Boolean managereport) {
		this.managereport = managereport;
	}

	public Boolean getManageuser() {
		return manageuser;
	}

	public void setManageuser(Boolean manageuser) {
		this.manageuser = manageuser;
	}

	public Boolean getManagecard() {
		return managecard;
	}

	public void setManagecard(Boolean managecard) {
		this.managecard = managecard;
	}
}
