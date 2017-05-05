package com.xsx.ncd.tool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Department;
import com.xsx.ncd.entity.Operator;
import com.xsx.ncd.entity.User;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

@Component
public class HttpUtils extends Service<Object>{
	
	private ServiceEnum serviceEnum = ServiceEnum.NONE;
	private Object parmData = null;
	
	private  ObjectMapper mapper = new ObjectMapper();
	
	private User tempuser = null;
	private String tempStr = null;

	private List<User> tempUserList = null;
	
	@Autowired private HttpClientTool httpClientTool;

	public ServiceEnum getServiceEnum() {
		return serviceEnum;
	}

	public void setServiceEnum(ServiceEnum serviceEnum) {
		this.serviceEnum = serviceEnum;
	}

	public Object getParmData() {
		return parmData;
	}

	public void setParmData(Object parmData) {
		this.parmData = parmData;
	}

	@Override
	protected Task<Object> createTask() {
		// TODO Auto-generated method stub
		return new MyTask();
	}
	
	public void startHttpService(ServiceEnum serviceEnum, Object object){
		this.serviceEnum = serviceEnum;
		this.parmData = object;
		this.restart();
	}
	
	class MyTask extends Task<Object>{

		@Override
		protected Object call() {
			// TODO Auto-generated method stub
			if(ServiceEnum.NONE.equals(serviceEnum))
				return null;
			
			tempStr = httpClientTool.myHttpPostJson(serviceEnum.getName(), parmData);
			
			if(tempStr == null)
				return null;
			else{
				try {
					
					if(serviceEnum.getIndex() == 1){
						JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, serviceEnum.getObjectclass()); 
						
						return mapper.readValue(tempStr, javaType);
					}
					else if(serviceEnum.getIndex() == 2){
						return mapper.readValue(tempStr, serviceEnum.getObjectclass());
					}
					else if(serviceEnum.getIndex() == 3){
						if(tempStr == null)
							return true;
						else if(tempStr.indexOf("true") >= 0)
							return true;
						else 
							return false;
					}
					else
						return null;
				} catch (Exception e) {
					// TODO: handle exception
					return null;
				}
			}
		}
	}
}
