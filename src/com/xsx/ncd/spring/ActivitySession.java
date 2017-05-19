package com.xsx.ncd.spring;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Observable;
import java.util.Stack;

import org.springframework.stereotype.Component;

import com.xsx.ncd.handler.ActivityTemplet;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

@Component
public class ActivitySession extends Observable{
		
		//主界面的显示界面
		private ObjectProperty<ActivityTemplet> ActivityPane = new SimpleObjectProperty<>();
		
		private Deque<ActivityTemplet> activityStack = new ArrayDeque<>();

		public ObjectProperty<ActivityTemplet> getActivityPane() {
			return ActivityPane;
		}

		public void setActivityPane(ActivityTemplet activityPane) {
			ActivityPane.set(activityPane);
		}

		public Deque<ActivityTemplet> getActivityStack() {
			return activityStack;
		}

		public void clearActivityStach(){
			while(!activityStack.isEmpty()){
				ActivityTemplet activityTemplet = activityStack.pop();
				activityTemplet.onDestroy();
			}
		}
		
		public void clearAndSetOriginActivityAs(ActivityTemplet activity, Object object){
			
			clearActivityStach();
			
			activityStack.push(activity);
			
			setChanged();
			
			notifyObservers(object);
		}
		
		public void pushActivity(ActivityTemplet activity, Object object){
			
			activityStack.peek().onPause();
			
			activityStack.push(activity);
			
			setChanged();
			
			notifyObservers(object);
		}
		
		public void backToThisActivity(ActivityTemplet activity){
			ActivityTemplet tempActivity = null;
			
			//检查栈顶元素是不是要返回的
			tempActivity = activityStack.peek();
			if(tempActivity.equals(activity))
				return;11
			
			while((tempActivity = activityStack.peek()) != null){
				if(tempActivity.equals(activity))
					break;
				else{
					tempActivity.onDestroy();
					activityStack.pop();
				}
			}
		}
}
