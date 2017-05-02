package com.xsx.ncd.tool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HttpUtils {

	private User tempuser = null;
	private String tempStr = null;
	private JSONObject jsonObject = null;
	private JSONArray jsonArray = null;
	private List<User> tempUserList = null;
	private Boolean isOK = null;
	
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

	public User SaveUser(User user){
		
		tempStr = httpClientTool.myHttpPostJson("/SaveUser", user);
		
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
	
	public Boolean deleteUser(User user){
		tempStr = httpClientTool.myHttpPostJson("/DeleteUser", user);
		
		if(tempStr == null)
			return true;
		else if(tempStr.indexOf("true") >= 0){
			return true;
		}
		else 
			return false;
	}
	
	public List<User> readAllUserBut(User user){
		
		tempStr = httpClientTool.myHttpPostJson("/ReadAllUser", user);
		
		if(tempStr == null)
			return null;
		else{
			try {
				jsonArray = JSONArray.fromObject(tempStr);

				tempUserList = JSONArray.toList(jsonArray, User.class);
			} catch (Exception e) {
				// TODO: handle exception
				tempUserList = null;
			}
			
			return tempUserList;
		}
	}
	
	public Boolean checkUserIsExist(User user){
		
		tempStr = httpClientTool.myHttpPostJson("/CheckUserIsExist", user);

		if(tempStr == null)
			return true;
		else if(tempStr.indexOf("true") >= 0){
			return true;
		}
		else 
			return false;
	}
}
