package com.xsx.ncd.tool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsx.ncd.define.DeviceIcoInfo;
import com.xsx.ncd.define.Message;
import com.xsx.ncd.define.ServiceEnum;
import com.xsx.ncd.entity.Device;
import com.xsx.ncd.entity.DeviceType;
import com.xsx.ncd.handler.HttpTemplet;
import com.xsx.ncd.spring.ActivitySession;

import javafx.scene.image.Image;
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
	
	//private final String ServerUrlHead = "http://192.168.0.56:8080/NCDPOCT_Server";
	private final String ServerUrlHead = "http://116.62.108.201:8080/NCDPOCT_Server";
	
	private final MediaType mediaJsonType = MediaType.parse("application/json; charset=utf-8");
	private final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
	private final OkHttpClient client = new OkHttpClient();
	
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
	 * 同步方式post json数据
	 */
	public String myHttpSynchronousPostJson(String url, Object parm){
		
		String value = null;
		
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

		try {
			Response response = client.newCall(request).execute();
			
			if(response.isSuccessful()) {
				value = response.body().string();
				response.body().close();;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return value;	
	}
	
	/*
	 * 异步方式post json数据
	 */
	public Boolean myHttpAsynchronousPostJson(HttpTemplet httpTemplet, ServiceEnum serviceEnum, Object parm){

		Message message = new Message(serviceEnum, null);
		StringBuffer urlBuffer = new StringBuffer(ServerUrlHead);
		urlBuffer.append(serviceEnum.getName());

		try {
			jsonString = mapper.writeValueAsString(parm);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

			httpTemplet.PostMessageToThisActivity(message);
			return false;
		}
		
		RequestBody body = RequestBody.create(mediaJsonType, jsonString);
		Request request = new Request.Builder()
		      .url(urlBuffer.toString())
		      .post(body)
		      .build();
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1){
				// TODO Auto-generated method stub
				try {
					if(serviceEnum.getIndex() == 1){
						JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, serviceEnum.getObjectclass()); 
						message.setObj(mapper.readValue(arg1.body().string(), javaType));
					}
					else if(serviceEnum.getIndex() == 2){
						if(serviceEnum.getObjectclass().equals(String.class))
							message.setObj(arg1.body().string());
						else{
							message.setObj(mapper.readValue(arg1.body().string(), serviceEnum.getObjectclass()));
						}
					}
					else if(serviceEnum.getIndex() == 4)
					{
						String name = arg1.header("Content-disposition");
						name = (name.split("="))[1];
						
						message.setObj(new DeviceIcoInfo(name, new Image(arg1.body().byteStream())));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				arg1.body().close();
				
				httpTemplet.PostMessageToThisActivity(message);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				httpTemplet.PostMessageToThisActivity(message);
			}
		});
		
		return true;
	}
	
	public void myHttpPostDeviceType(HttpTemplet httpTemplet, ServiceEnum serviceEnum, 
			DeviceType deviceType, File onFile){
		
		Message message = new Message(serviceEnum, "Error");
		StringBuffer urlBuffer = new StringBuffer(ServerUrlHead);
		urlBuffer.append(serviceEnum.getName());
		
		try {
			jsonString = mapper.writeValueAsString(deviceType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

			httpTemplet.PostMessageToThisActivity(message);
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
				
				httpTemplet.PostMessageToThisActivity(message);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				httpTemplet.PostMessageToThisActivity(message);
			}
		});
	}
	
/*	public void myHttpPostDeviceType(ServiceEnum serviceEnum, DeviceType deviceType, File onFile,
			File offFile, File errorFile){
		
		Message message = new Message(serviceEnum, null);
		
		urlStringBuffer.setLength(0);
		urlStringBuffer.append(ServerUrlHead);
		urlStringBuffer.append(serviceEnum.getName());
		
		try {
			jsonString = mapper.writeValueAsString(deviceType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

			activitySession.getActivityPane().get().PostMessageToThisActivity(message);
			return;
		}
		
		RequestBody onFileBody = RequestBody.create(MediaType.parse("application/octet-stream"), onFile);
		RequestBody offFileBody = RequestBody.create(MediaType.parse("application/octet-stream"), offFile);
		RequestBody errorFileBody = RequestBody.create(MediaType.parse("application/octet-stream"), errorFile);

		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("deviceType", jsonString)
				.addFormDataPart("onico", onFile.getName(), onFileBody)
				.addFormDataPart("offico", offFile.getName(), offFileBody)
				.addFormDataPart("errorico", errorFile.getName(), errorFileBody)
				.build();
		
		Request request = new Request.Builder()
		      .url(urlStringBuffer.toString())
		      .post(requestBody)
		      .build();
		
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				try {
					jsonString = arg1.body().string();

					if(jsonString != null){
						if(jsonString != null){
							if(jsonString.indexOf("true") >= 0)
								message.setObj(true);
							else 
								message.setObj(false);
						}
					}
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				activitySession.getActivityPane().get().PostMessageToThisActivity(message);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				activitySession.getActivityPane().get().PostMessageToThisActivity(message);
			}
		});
	}*/
}
