package com.xsx.ncd.spring;

import org.springframework.stereotype.Component;

import com.xsx.ncd.entity.User;

@Component
public class UserSession {

	private User user = null;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
