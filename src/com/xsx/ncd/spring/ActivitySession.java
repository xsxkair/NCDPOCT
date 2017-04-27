package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

import com.xsx.ncd.handler.ActivityTemplet;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

@Component
public class ActivitySession {
		
		//主界面的显示界面
		private ObjectProperty<Pane> ActivityPane = new SimpleObjectProperty<>();
		
		private ObjectProperty<ActivityTemplet> rootActivity = new SimpleObjectProperty<>();
		private ObjectProperty<ActivityTemplet> fatherActivity = new SimpleObjectProperty<>();
		private ObjectProperty<ActivityTemplet> childActivity = new SimpleObjectProperty<>();

		public ObjectProperty<Pane> getActivityPane() {
			return ActivityPane;
		}

		public void setActivityPane(Pane activityPane) {
			ActivityPane.set(activityPane);
		}

		public ObjectProperty<ActivityTemplet> getRootActivity() {
			return rootActivity;
		}

		public void setRootActivity(ActivityTemplet rootActivity) {
			this.rootActivity.set(rootActivity);
		}

		public ObjectProperty<ActivityTemplet> getFatherActivity() {
			return fatherActivity;
		}

		public void setFatherActivity(ActivityTemplet fatherActivity) {
			this.fatherActivity.set(fatherActivity);
		}

		public ObjectProperty<ActivityTemplet> getChildActivity() {
			return childActivity;
		}

		public void setChildActivity(ActivityTemplet childActivity) {
			this.childActivity.set(childActivity);
		}

		
}
