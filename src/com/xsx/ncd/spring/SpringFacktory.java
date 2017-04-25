package com.xsx.ncd.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringFacktory {
	
	private static ApplicationContext ctx = null;
	
	public static void SpringFacktoryInit(){
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	public static ApplicationContext getCtx() {
		return ctx;
	}

}
