package com.xsx.ncd.entity;

import java.io.Serializable;

public class Item implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7752780793735001924L;

	private Integer id;
	
	private String code;					//项目代号
		
	private String name;					//项目名
	
	private String namecn;					//中文名
	
	private Integer accuracy;				//精度
	
	private String unit;					//单位
	
	private Float minv;				//最低检测线
		
	private Float maxv;				//最高检测线
	
	private Float nlowvalue1;				//参考值1下限
	
	private Float nhighvalue1;			//参考值1上限
	
	private Integer age1;					//参考值1年龄分段
	
	private Float nlowvalue2;				//参考值2下限
	
	private Float nhighvalue2;			//参考值2上限
	
	private Integer age2;					//参考值2年龄分段
	
	private Float nlowvalue3;				//参考值3下限

	private Float nhighvalue3;			//参考值3上限
	
	private Integer age3;					//参考值3年龄分段
	
	private Float nlowvalue4;				//参考值4下限
	
	private Float nhighvalue4;			//参考值4上限

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

	public String getNamecn() {
		return namecn;
	}

	public void setNamecn(String namecn) {
		this.namecn = namecn;
	}

	public Integer getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Float getMinv() {
		return minv;
	}

	public void setMinv(Float minv) {
		this.minv = minv;
	}

	public Float getMaxv() {
		return maxv;
	}

	public void setMaxv(Float maxv) {
		this.maxv = maxv;
	}

	public Float getNlowvalue1() {
		return nlowvalue1;
	}

	public void setNlowvalue1(Float nlowvalue1) {
		this.nlowvalue1 = nlowvalue1;
	}

	public Float getNhighvalue1() {
		return nhighvalue1;
	}

	public void setNhighvalue1(Float nhighvalue1) {
		this.nhighvalue1 = nhighvalue1;
	}

	public Integer getAge1() {
		return age1;
	}

	public void setAge1(Integer age1) {
		this.age1 = age1;
	}

	public Float getNlowvalue2() {
		return nlowvalue2;
	}

	public void setNlowvalue2(Float nlowvalue2) {
		this.nlowvalue2 = nlowvalue2;
	}

	public Float getNhighvalue2() {
		return nhighvalue2;
	}

	public void setNhighvalue2(Float nhighvalue2) {
		this.nhighvalue2 = nhighvalue2;
	}

	public Integer getAge2() {
		return age2;
	}

	public void setAge2(Integer age2) {
		this.age2 = age2;
	}

	public Float getNlowvalue3() {
		return nlowvalue3;
	}

	public void setNlowvalue3(Float nlowvalue3) {
		this.nlowvalue3 = nlowvalue3;
	}

	public Float getNhighvalue3() {
		return nhighvalue3;
	}

	public void setNhighvalue3(Float nhighvalue3) {
		this.nhighvalue3 = nhighvalue3;
	}

	public Integer getAge3() {
		return age3;
	}

	public void setAge3(Integer age3) {
		this.age3 = age3;
	}

	public Float getNlowvalue4() {
		return nlowvalue4;
	}

	public void setNlowvalue4(Float nlowvalue4) {
		this.nlowvalue4 = nlowvalue4;
	}

	public Float getNhighvalue4() {
		return nhighvalue4;
	}

	public void setNhighvalue4(Float nhighvalue4) {
		this.nhighvalue4 = nhighvalue4;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
