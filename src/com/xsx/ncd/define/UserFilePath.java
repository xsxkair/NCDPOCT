package com.xsx.ncd.define;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class UserFilePath {
	
	private String rootDirPath = null;
	
	private String deviceIcoDirPath = null;
	
	@PostConstruct
	public void Init(){
		makeUserDir();
	}
	
	private void makeUserDir(){
		rootDirPath = System.getProperty("user.home")+"\\AppData\\Local\\NCD_Data";
		deviceIcoDirPath = rootDirPath + "\\deviceico";
		
		File rootDir = new File(rootDirPath);
		if(!rootDir.exists())
			rootDir.mkdir();
		
		File icoDir = new File(deviceIcoDirPath);
		if(!icoDir.exists())
			icoDir.mkdir();
	}

	public String getRootDirPath() {
		return rootDirPath;
	}

	public String getDeviceIcoDirPath() {
		return deviceIcoDirPath;
	}

}
