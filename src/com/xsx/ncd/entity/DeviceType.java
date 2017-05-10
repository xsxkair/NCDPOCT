package com.xsx.ncd.entity;

import java.util.HashSet;
import java.util.Set;


public class DeviceType{


	private Integer id;

	private String code;
	
	private String name;
	
	private String onicon;
	
	private String officon;
	
	private String erroricon;
	
	private Set<Item> items = new HashSet<>();
	
	private String vender;
	
	private String venderphone;
	
	private String venderaddr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOnicon() {
		return onicon;
	}

	public void setOnicon(String onicon) {
		this.onicon = onicon;
	}

	public String getOfficon() {
		return officon;
	}

	public void setOfficon(String officon) {
		this.officon = officon;
	}

	public String getErroricon() {
		return erroricon;
	}

	public void setErroricon(String erroricon) {
		this.erroricon = erroricon;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public String getVender() {
		return vender;
	}

	public void setVender(String vender) {
		this.vender = vender;
	}

	public String getVenderphone() {
		return venderphone;
	}

	public void setVenderphone(String venderphone) {
		this.venderphone = venderphone;
	}

	public String getVenderaddr() {
		return venderaddr;
	}

	public void setVenderaddr(String venderaddr) {
		this.venderaddr = venderaddr;
	}
	
	
}
