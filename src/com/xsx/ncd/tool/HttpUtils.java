package com.xsx.ncd.tool;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.User;

import net.sf.json.JSONObject;

@Component
public class HttpUtils {

	private User tempuser = null;
	private String tempStr = null;
	private JSONObject jsonObject = null;
	
	@Autowired private HttpClientTool httpClientTool;
	
	public User login(User user){
		
		tempStr = httpClientTool.myHttpPostJson("/Login", user);
		
		if(tempStr == null)
			return null;
		else{
			try {
				jsonObject = JSONObject.fromObject(tempStr);

				tempuser = (User)JSONObject.toBean(jsonObject,User.class);
			} catch (Exception e) {
				// TODO: handle exception
				tempuser = null;
			}
			
			return tempuser;
		}
	}

	public User modifyUserInfo(User user){
		
		tempStr = httpClientTool.myHttpPostJson("/ModifyUserInfo", user);
		
		if(tempStr == null)
			return null;
		else{
			try {
				jsonObject = JSONObject.fromObject(tempStr);

				tempuser = (User)JSONObject.toBean(jsonObject,User.class);
			} catch (Exception e) {
				// TODO: handle exception
				tempuser = null;
			}
			
			return tempuser;
		}
	}
}
