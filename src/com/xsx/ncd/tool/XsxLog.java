package com.xsx.ncd.tool;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;

@Component
public class XsxLog {
	
	private Logger infoLog = null;
	
	@PostConstruct
	public void createMyLog(){
		infoLog = Logger.getLogger("infoLog");
		infoLog.setLevel(Level.INFO);
		
		Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
	}
	
	public void info(String msg){
		infoLog.info(msg);
	}
}
