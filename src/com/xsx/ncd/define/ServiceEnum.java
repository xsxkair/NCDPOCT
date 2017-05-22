package com.xsx.ncd.define;

import java.util.List;
import java.util.Map;

import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.DeviceType;
import com.xsx.ncd.entity.Item;
import com.xsx.ncd.entity.NCD_YGFXY;
import com.xsx.ncd.entity.Operator;
import com.xsx.ncd.entity.Repertory;
import com.xsx.ncd.entity.User;

import javafx.scene.image.Image;

/*
 * NONE(url, 返回数据类型, 返回数据拆包类)
 * 1, 代表list
 * 2，代表bean
 * 3，代表bool字符串
 * 4, 代表返回数据为image的二进制数据流
 */
public enum ServiceEnum {
	Error("Error", null, null), 
	NONE("None", null, null), 
	ReadAllDepartment("/readAllDepartment", List.class, Department.class),
	
	Login("/Login", User.class, null), 
	SaveUser("/SaveUser", User.class, null), 
	DeleteUser("/DeleteUser", Boolean.class, null), 
	CheckUserIsExist("/CheckUserIsExist", Boolean.class, null),
	ReadAllOtherUser("/ReadAllOtherUser", List.class, User.class),
	ReadAllUser("/ReadAllUser", List.class, User.class),
	
	ReadAllOperator("/ReadAllOperator", List.class, Operator.class), 
	SaveOperator("/SaveOperator", Operator.class, null),
	DeleteOperator("/DeleteOperator", Boolean.class, null), 
	CheckOperatorIsExist("/CheckOperatorIsExist", Boolean.class, null),
	ReadOneOperatorById("/ReadOneOperatorById", Operator.class, null),
	QueryOperatorByDepartment("/QueryOperatorByDepartment", List.class, Operator.class),
	
	ReadAllItems("/ReadAllItems", List.class, Item.class),
	
	SaveRepertoryRecord("/SaveRepertoryRecord", Repertory.class, null),
	QueryRepertoryNumByCard("/QueryRepertoryNumByCard", Long.class, null),
	
	QueryCardLotNumLikeThis("/QueryCardLotNumLikeThis", List.class, String.class),
	QueryCardByLotNum("/QueryCardByLotNum", Card.class, null),
	
	SaveDeviceTypeAndIco("/SaveDeviceTypeAndIco", String.class, null),
	QueryAllDeviceType("/QueryAllDeviceType", List.class, DeviceType.class),
	QueryAllDeviceIcoPath("/QueryAllDeviceIcoPath", List.class, String.class),
	
	QueryThisDepartmentAllDeviceList("/QueryThisDepartmentAllDeviceList", List.class, Device.class),
	AddNewDevice("/AddNewDevice", String.class, null),
	QueryAllDeviceInRecordJson("/QueryAllDeviceInRecordJson", List.class, DeviceItem.class),
	
	QueryDeviceErrorRecord("/QueryDeviceErrorRecord", Map.class, null),
	
	QueryAllNotHandledReportNum("/QueryAllNotHandledReportNum", Long.class, null),
	QueryNCDYGFXYReportById("/QueryNCDYGFXYReportById", NCD_YGFXY.class, null);
	
	private final String url;
	private final Class<?> class0;
	private final Class<?> class1;
	
	private ServiceEnum(String url, Class<?> class0, Class<?> class1) {
		this.url = url;
		this.class0 = class0;
		this.class1 = class1;
	}

	public String getUrl() {
		return url;
	}

	public Class<?> getClass0() {
		return class0;
	}

	public Class<?> getClass1() {
		return class1;
	}
	
}
