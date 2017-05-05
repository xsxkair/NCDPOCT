package com.xsx.ncd.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class DeviceType implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2571148727646805744L;

	private Integer id;

	private String name;
	
	private String icon;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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
