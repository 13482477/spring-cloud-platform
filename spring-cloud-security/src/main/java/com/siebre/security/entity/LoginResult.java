package com.siebre.security.entity;

import java.io.Serializable;

public class LoginResult implements Serializable {

	private static final long serialVersionUID = -1499509941887551998L;
	
	private User user;
	
	private String token;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
