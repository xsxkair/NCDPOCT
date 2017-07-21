package com.xsx.ncd.tool;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsx.ncd.define.HttpPostType;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.DeviceType;
import com.xsx.ncd.handler.Activity;
import com.xsx.ncd.spring.ActivitySession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class HttpClientTool {
	
	private ObjectMapper mapper = new ObjectMapper();
	private String jsonString = null;
	
	private final String ServerUrlHead = "http://192.168.0.15:8080/NCDPOCT_Server";
	
	private final MediaType mediaJsonType = MediaType.parse("application/json; charset=utf-8");
	private final OkHttpClient client = new OkHttpClient();
	
	public static final String ServiceDeviceIcoUrl = "http://116.62.108.201:8080/ico/";
	
	@Autowired ActivitySession activitySession;

	
	@PostConstruct
	private void init(){
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");  
        mapper.setDateFormat(outputFormat);
	}
	
	public OkHttpClient getClient() {
		return client;
	}

	/*
	 * 以json形式创建请求
	 */
	private Request makeMyJsonRequest(String url, Object parm){
		
		StringBuffer urlBuffer = new StringBuffer(ServerUrlHead);
		urlBuffer.append(url);
		
		try {
			jsonString = mapper.writeValueAsString(parm);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		RequestBody body = RequestBody.create(mediaJsonType, jsonString);
		Request request = new Request.Builder()
		      .url(urlBuffer.toString())
		      .post(body)
		      .build();
		
		return request;
	}
	
	/*
	 * 以表单形式创建请求
	 */
	private Request makeMyFormRequest(String url, Map<String, String> parm){
		
		StringBuffer urlBuffer = new StringBuffer(ServerUrlHead);
		urlBuffer.append(url);
		
		FormBody.Builder requestFormBodyBuilder = new FormBody.Builder();
		
		Set<String> keySet = parm.keySet();
		for (String string : keySet) {
			requestFormBodyBuilder.add(string, parm.get(string));
		}
		
		RequestBody requestBody = requestFormBodyBuilder.build();
		
		Request request = new Request.Builder()
		      .url(urlBuffer.toString())
		      .post(requestBody)
		      .build();
		
		return request;
	}
	
	public <T> T myHttpPost(Activity activity, ServiceEnum serviceEnum, HttpPostType httpPostType, Object jsonParm, 
			Map<String, String> formParm){
		T value = null;
		Request request = null;
		
		if(httpPostType.getIsJson())
			request = makeMyJsonRequest(serviceEnum.getUrl(), jsonParm);
		else
			request = makeMyFormRequest(serviceEnum.getUrl(), formParm);

		if(request == null)
			return value;

		//异步
		if(httpPostType.getIsAsynchronous()){
			Message message = new Message(serviceEnum, null);
			Call call = client.newCall(request);
			call.enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, Response arg1){
					// TODO Auto-generated method stub
					if(arg1.isSuccessful()) {
						try {
							message.setObj(mapper.readValue(arg1.body().string(), serviceEnum.getValueTypeRef()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						activity.PostMessageToThisActivity(message);
					}
					
					arg1.body().close();
				}
				
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					activity.PostMessageToThisActivity(message);
				}
			});
		}
		//同步
		else{
			
			try {
				Response response = client.newCall(request).execute();
				
				if(response.isSuccessful()) {
					value = mapper.readValue(response.body().string(), serviceEnum.getValueTypeRef());
				}
				
				response.body().close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return value;
		}
		
		return null;
	}
	
	public void myHttpPostDeviceType(Activity activity, ServiceEnum serviceEnum, 
			DeviceType deviceType, File onFile){
		
		Message message = new Message(serviceEnum, "Error");
		StringBuffer urlBuffer = new StringBuffer(ServerUrlHead);
		urlBuffer.append(serviceEnum.getUrl());
		
		try {
			jsonString = mapper.writeValueAsString(deviceType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

			activity.PostMessageToThisActivity(message);
			return;
		}
		
		RequestBody onFileBody = RequestBody.create(MediaType.parse("application/octet-stream"), onFile);

		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("deviceType", jsonString)
				.addFormDataPart("ico", onFile.getName(), onFileBody)
				.build();
		
		Request request = new Request.Builder()
		      .url(urlBuffer.toString())
		      .post(requestBody)
		      .build();
		
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) {
				// TODO Auto-generated method stub
				try {
					message.setObj(arg1.body().string());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				arg1.body().close();
				
				activity.PostMessageToThisActivity(message);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				activity.PostMessageToThisActivity(message);
			}
		});
	}
	

}
