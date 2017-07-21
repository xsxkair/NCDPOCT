package com.xsx.ncd.define;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
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
	ReadAllDepartment("/readAllDepartment", new TypeReference<List<Department>>(){}),
	
	Login("/Login", new TypeReference<User>(){}), 
	SaveUser("/SaveUser", new TypeReference<User>(){}), 
	DeleteUser("/DeleteUser", new TypeReference<Boolean>(){}), 
	CheckUserIsExist("/CheckUserIsExist", new TypeReference<Boolean>(){}),
	ReadAllOtherUser("/ReadAllOtherUser", new TypeReference<List<User>>(){}),
	ReadAllUser("/ReadAllUser", new TypeReference<List<User>>(){}),
	
	ReadAllOperator("/ReadAllOperator", new TypeReference<List<Operator>>(){}), 
	SaveOperator("/SaveOperator", new TypeReference<Operator>(){}),
	DeleteOperator("/DeleteOperator", new TypeReference<Boolean>(){}), 
	CheckOperatorIsExist("/CheckOperatorIsExist", new TypeReference<Boolean>(){}),
	ReadOneOperatorById("/ReadOneOperatorById",  new TypeReference<Operator>(){}),
	QueryOperatorByDepartment("/QueryOperatorByDepartment",  new TypeReference<List<Operator>>(){}),
	
	ReadAllItems("/ReadAllItems", new TypeReference<List<Item>>(){}),
	
	SaveRepertoryRecord("/SaveRepertoryRecord", new TypeReference<Repertory>(){}),
	QueryRepertoryNumByCard("/QueryRepertoryNumByCard", new TypeReference<Long>(){}),
	
	QueryCardLotNumLikeThis("/QueryCardLotNumLikeThis", new TypeReference<List<String>>(){}),
	QueryCardByLotNum("/QueryCardByLotNum", new TypeReference<Card>(){}),
	
	SaveDeviceTypeAndIco("/SaveDeviceTypeAndIco", new TypeReference<String>(){}),
	QueryAllDeviceType("/QueryAllDeviceType", new TypeReference<List<DeviceType>>(){}),
	QueryAllDeviceTypeJson("/QueryAllDeviceTypeJson", new TypeReference<List<DeviceTypeJson>>(){}),
	
	QueryDeviceByDeviceId("/QueryDeviceByDeviceId", new TypeReference<Device>(){}),
	QueryThisDepartmentAllDeviceList("/QueryThisDepartmentAllDeviceList", new TypeReference<List<Device>>(){}),
	AddNewDevice("/AddNewDevice", new TypeReference<String>(){}),
	UpDateDevice("/UpDateDevice", new TypeReference<String>(){}),
	QueryAllDeviceInSample("/QueryAllDeviceInSample", new TypeReference<List<DeviceJson>>(){}),
	QueryAllDeviceByDepartmentInSample("/QueryAllDeviceByDepartmentInSample", new TypeReference<List<DeviceJson>>(){}),
	
	QueryDeviceErrorRecord("/QueryDeviceErrorRecord", new TypeReference<RecordJson<ErrorRecordItem>>(){}),
	QueryDeviceAdjustRecord("/QueryDeviceAdjustRecord", new TypeReference<RecordJson<AdjustRecordItem>>(){}),
	QueryDeviceMaintenanceRecord("/QueryDeviceMaintenanceRecord", new TypeReference<RecordJson<MaintenanceRecordItem>>(){}),
	QueryDeviceQualityRecord("/QueryDeviceQualityRecord", new TypeReference<RecordJson<QualityRecordItem>>(){}),
	
	QueryThisDeviceNotHandledReportNumAndLastTime("/QueryThisDeviceNotHandledReportNumAndLastTime", new TypeReference<List<Long>>(){}),
	QueryAllNotHandledReportNum("/QueryAllNotHandledReportNum", new TypeReference<Long>(){}),
	QueryDeviceReportNotHandled("/QueryDeviceReportNotHandled", new TypeReference<RecordJson<DeviceReportItem>>(){}),
	QueryReportJsonByFilter("/QueryReportJsonByFilter", new TypeReference<RecordJson<DeviceReportItem>>(){}),
	QueryNcdYGFXYReportById("/QueryNcdYGFXYReportById", new TypeReference<NCD_YGFXY>(){}),
	SaveNcdYGFXYReport("/SaveNcdYGFXYReport",new TypeReference<NCD_YGFXY>(){}),
	
	QueryAllCardRepertory("/QueryAllCardRepertory", new TypeReference<Map<String,Long>>(){}),
	QueryDepartmentAllCardRepertory("/QueryDepartmentAllCardRepertory", new TypeReference<List<DataJson<String, Department, String, String, Long>>>(){}),
	QueryDeviceActivity("/QueryDeviceActivity", new TypeReference<List<DataJson<String, String, String, String, Long>>>(){})
	
	;
	
	private final String url;
	private final TypeReference<?> valueTypeRef;
	
	private ServiceEnum(String url, TypeReference<?> valueTypeRef) {
		this.url = url;
		this.valueTypeRef = valueTypeRef;
	}

	public String getUrl() {
		return url;
	}

	public TypeReference<?> getValueTypeRef() {
		return valueTypeRef;
	}
	
}
