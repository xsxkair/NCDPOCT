package com.xsx.ncd.define;

import java.util.Map;

import com.xsx.ncd.entity.Card;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.DeviceType;
import com.xsx.ncd.entity.Item;
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
	Error("Error", 0, null), 
	NONE("None", 0, null), 
	ReadAllDepartment("/readAllDepartment", 1, Department.class),
	
	Login("/Login", 2, User.class), 
	SaveUser("/SaveUser", 2, User.class), 
	DeleteUser("/DeleteUser", 2, Boolean.class), 
	CheckUserIsExist("/CheckUserIsExist", 2, Boolean.class),
	ReadAllOtherUser("/ReadAllOtherUser", 1, User.class),
	ReadAllUser("/ReadAllUser", 1, User.class),
	
	ReadAllOperator("/ReadAllOperator", 1, Operator.class), 
	SaveOperator("/SaveOperator", 2, Operator.class),
	DeleteOperator("/DeleteOperator", 2, Boolean.class), 
	CheckOperatorIsExist("/CheckOperatorIsExist", 2, Boolean.class),
	ReadOneOperatorById("/ReadOneOperatorById", 2, Operator.class),
	QueryOperatorByDepartment("/QueryOperatorByDepartment", 1, Operator.class),
	
	ReadAllItems("/ReadAllItems", 1, Item.class),
	
	SaveRepertoryRecord("/SaveRepertoryRecord", 2, Repertory.class),
	QueryRepertoryNumByCard("/QueryRepertoryNumByCard", 2, Long.class),
	
	QueryCardLotNumLikeThis("/QueryCardLotNumLikeThis", 1, String.class),
	QueryCardByLotNum("/QueryCardByLotNum", 2, Card.class),
	
	SaveDeviceTypeAndIco("/SaveDeviceTypeAndIco", 2, String.class),
	QueryAllDeviceType("/QueryAllDeviceType", 1, DeviceType.class),
	QueryAllDeviceIcoPath("/QueryAllDeviceIcoPath", 1, String.class),
	DownloadDeviceIco("/DownloadDeviceIco", 4, DeviceIcoInfo.class),
	
	QueryThisDepartmentAllDeviceList("/QueryThisDepartmentAllDeviceList", 1, Device.class),
	AddNewDevice("/AddNewDevice", 2, String.class),
	QueryAllDeviceInRecordJson("/QueryAllDeviceInRecordJson", 1, DeviceItem.class),
	
	QueryDeviceErrorRecord("/QueryDeviceErrorRecord", 2, Map.class),
	
	QueryAllNotHandledReportNum("/QueryAllNotHandledReportNum", 2, Long.class);
	
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
