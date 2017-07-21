package com.xsx.ncd.define;

import java.util.List;
import java.util.Map;

public class DataJson<T1,T2,T3,T4,T5> {
	
	private T1 parm1 = null;
	
	private T2 parm2 = null;
	
	private List<T3> parmList = null;
	
	private Map<T4, T5> parmMap = null;

	public DataJson() {

	}

	public DataJson(T1 parm1, T2 parm2, List<T3> parmList, Map<T4, T5> parmMap) {
		super();
		this.parm1 = parm1;
		this.parm2 = parm2;
		this.parmList = parmList;
		this.parmMap = parmMap;
	}

	public T1 getParm1() {
		return parm1;
	}

	public void setParm1(T1 parm1) {
		this.parm1 = parm1;
	}

	public T2 getParm2() {
		return parm2;
	}

	public void setParm2(T2 parm2) {
		this.parm2 = parm2;
	}

	public List<T3> getParmList() {
		return parmList;
	}

	public void setParmList(List<T3> parmList) {
		this.parmList = parmList;
	}

	public Map<T4, T5> getParmMap() {
		return parmMap;
	}

	public void setParmMap(Map<T4, T5> parmMap) {
		this.parmMap = parmMap;
	}

}
