package com.xsx.ncd.define;

import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Item;
import com.xsx.ncd.entity.Operator;
import com.xsx.ncd.entity.Repertory;
import com.xsx.ncd.entity.User;

/*
 * NONE(url, 返回数据类型, 返回数据拆包类)
 * 1, 代表list
 * 2，代表bean
 * 3，代表bool字符串
 */
public enum ServiceEnum {
	Error("Error", 0, null), 
	NONE("None", 0, null), 
	ReadAllDepartment("/readAllDepartment", 1, Department.class), 
	Login("/Login", 2, User.class), 
	SaveUser("/SaveUser", 2, User.class), 
	DeleteUser("/DeleteUser", 3, null), 
	CheckUserIsExist("/CheckUserIsExist", 3, null),
	ReadAllOtherUser("/ReadAllUser", 1, User.class), 
	ReadAllOperator("/ReadAllOperator", 1, Operator.class), 
	SaveOperator("/SaveOperator", 2, Operator.class),
	DeleteOperator("/DeleteOperator", 3, null), 
	CheckOperatorIsExist("/CheckOperatorIsExist", 3, null),
	ReadOneOperatorById("/ReadOneOperatorById", 2, Operator.class), 
	ReadAllItems("/ReadAllItems", 1, Item.class),
	SaveRepertoryRecord("/SaveRepertoryRecord", 2, Repertory.class);
	
	private final String name;
	private final Integer index;
	private final Class objectclass;
	
	private ServiceEnum(String name, Integer index, Class objectclass) {
		this.name = name;
		this.index = index;
		this.objectclass = objectclass;
	}
	
	public String getName(){
		return this.name;
	}

	public Integer getIndex() {
		return index;
	}

	public Class getObjectclass() {
		return objectclass;
	}
	
	
}
