package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.User;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

@Component
public class UserSession {

	private ObjectProperty<User> user = new SimpleObjectProperty<>();

	public void setUser(User user) {
		this.user.set(user);
	}

	public ObjectProperty<User> getUserProperty() {
		return user;
	}
	
	public User getUser() {
		return user.get();
	}
}
