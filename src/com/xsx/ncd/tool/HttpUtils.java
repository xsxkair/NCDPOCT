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
	
	public User login(String account, String password){
		Map<String, String> parm = new HashMap<>();
		parm.put("account", account);
		parm.put("password", password);
		
		tempStr = httpClientTool.myHttpPost("/Login", parm);
		
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
