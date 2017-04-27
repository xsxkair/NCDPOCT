package com.xsx.ncd.tool;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class HttpClientTool {
	
	private final String ServerUrlHead = "http://116.62.108.201:8080/NCDPOCT_Server";
	private StringBuffer urlStringBuffer = new StringBuffer();
	
	private final MediaType mediaJsonType = MediaType.parse("application/json; charset=utf-8");
	private final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
	private final OkHttpClient client = new OkHttpClient();
	
	public String myHttpPostJson(String url, Object parm){
		JSONObject jsonObject = JSONObject.fromObject(parm);
		
		urlStringBuffer.setLength(0);
		urlStringBuffer.append(ServerUrlHead);
		urlStringBuffer.append(url);
		
		RequestBody body = RequestBody.create(mediaJsonType, jsonObject.toString());
		Request request = new Request.Builder()
		      .url(urlStringBuffer.toString())
		      .post(body)
		      .build();
		Response response;
		try {
			response = client.newCall(request).execute();
			if(response.isSuccessful()) {
				return response.body().string();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public String myHttpPost(String url, Map<String, String> parm){
		Set<String> paramKeySet = parm.keySet();
		Response response;
		
		urlStringBuffer.setLength(0);
		urlStringBuffer.append(ServerUrlHead);
		urlStringBuffer.append(url);
		
		FormBody.Builder requestFormBodyBuilder = new FormBody.Builder();

		for (String string : paramKeySet) {
			requestFormBodyBuilder.add(string, parm.get(string));
		}
		
		RequestBody requestBody = requestFormBodyBuilder.build();
		
		Request request = new Request.Builder()
		      .url(urlStringBuffer.toString())
		      .post(requestBody)
		      .build();
		
		try {
			response = client.newCall(request).execute();
			if(response.isSuccessful()) {
				return response.body().string();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
