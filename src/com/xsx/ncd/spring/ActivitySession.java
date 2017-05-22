package com.xsx.ncd.spring;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Observable;
import java.util.Stack;

import org.springframework.stereotype.Component;

import com.xsx.ncd.handler.Activity;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

@Component
public class ActivitySession extends Observable{
		
		private Deque<Activity> activityStack = new ArrayDeque<>();

		public Deque<Activity> getActivityStack() {
			return activityStack;
		}

		public void clearActivityStach(){
			while(!activityStack.isEmpty()){
				Activity activityTemplet = activityStack.pop();
				activityTemplet.onDestroy();
			}
		}
		
		public void clearAndSetOriginActivityAs(Activity activity, Object object){
			
			clearActivityStach();
			
			startActivity(activity, object);
		}
		
		public void pushActivity(Activity activity, Object object){
			
			activityStack.peek().onPause();
			
			activityStack.push(activity);
			
			setChanged();
			
			notifyObservers(object);
		}
		
		public void backToThisActivity(Activity activity){
			Activity tempActivity = null;

			while((tempActivity = activityStack.peek()) != null){
				if(tempActivity.equals(activity))
					break;
				else{
					finishActivity();
				}
			}
		}
		
	public void startActivity(Activity activity, Object object){
		Activity tempActivity = activityStack.peek();
		if(tempActivity != null)
			tempActivity.onPause();
		
		activityStack.push(activity);
		activityStack.peek().onStart(object);
		
		setChanged();
		
		notifyObservers();
	}
	
	public void finishActivity(){
		activityStack.peek().onDestroy();
		activityStack.pop();
		activityStack.peek().onResume();
		
		setChanged();
		
		notifyObservers();
	}
}
