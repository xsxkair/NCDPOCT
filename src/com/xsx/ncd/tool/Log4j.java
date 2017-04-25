package com.xsx.ncd.tool;

import java.io.File;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.stereotype.Component;


public class Log4j {
	

	public void initLog4j() {
		Properties prop = new Properties();
		File directory = new File("");
		
		prop.setProperty("log4j.rootLogger", "DEBUG, xsx");
		prop.setProperty("log4j.appender.xsx", "org.apache.log4j.DailyRollingFileAppender");
		prop.setProperty("log4j.appender.xsx.File", directory.getAbsolutePath()+"\\xsx.log");
		prop.setProperty("log4j.appender.xsx.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.xsx.layout.ConversionPattern", "[%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} method:%l%n%m%n");

		PropertyConfigurator.configure(prop);
	}
}
