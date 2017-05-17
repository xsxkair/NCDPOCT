package com.xsx.ncd.define;

import java.util.List;

public class RecordJson<T> {
	
	private int totalPageNum ;
	
	private List<T> Records;

	
	
	public RecordJson() {

	}

	public RecordJson(int totalPageNum, List<T> records) {
		super();
		this.totalPageNum = totalPageNum;
		this.Records = records;
	}

	public int getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(int totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public List<T> getRecords() {
		return Records;
	}

	public void setRecords(List<T> records) {
		Records = records;
	}

}
