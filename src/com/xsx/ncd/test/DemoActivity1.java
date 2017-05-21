package com.xsx.ncd.test;

import com.xsx.ncd.handler.Activity;

import javafx.scene.layout.AnchorPane;

public class DemoActivity1 extends Activity{

	public void test() {
		this.setRootPane(new AnchorPane());
		System.out.println(this.getRootPane());
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return null;
	}

}
