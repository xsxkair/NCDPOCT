package com.xsx.ncd.application;

import com.xsx.ncd.handler.LoginHandler;
import com.xsx.ncd.spring.SpringFacktory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class AppStart extends Application{
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	class LoadResourceTask extends Task<Boolean>{

		@Override
		protected Boolean call() {
			// TODO Auto-generated method stub
			try {
				long start =  System.currentTimeMillis();
				//初始化spring，创建bean
    			SpringFacktory.SpringFacktoryInit();
    			long end = System.currentTimeMillis();
    			System.out.println(end-start);
    			return true;
    			
			} catch (Exception e) {
				// TODO: handle exception
	    		return false;
			}

		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		LoadResourceTask loadResourceTask = new LoadResourceTask();
		new Thread(loadResourceTask).start();
		
		loadResourceTask.valueProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				
				if(newValue){
					SpringFacktory.getCtx().getBean(LoginHandler.class).startLoginActivity();
				}
			}
		});
	}
}
