package com.xsx.ncd.define;

import java.util.List;
import java.util.Map;

public class RecordJson<T> {
	
	private int totalPageNum ;
	
	private Long parm1;
	
	private List<T> Records;


	public RecordJson() {

	}
	
	public RecordJson(int totalPageNum, List<T> records) {
		super();
		this.totalPageNum = totalPageNum;
		Records = records;
	}

	public RecordJson(int totalPageNum, Long parm1, List<T> records) {
		super();
		this.totalPageNum = totalPageNum;
		this.parm1 = parm1;
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

	public Long getParm1() {
		return parm1;
	}

	public void setParm1(Long parm1) {
		this.parm1 = parm1;
	}

}
