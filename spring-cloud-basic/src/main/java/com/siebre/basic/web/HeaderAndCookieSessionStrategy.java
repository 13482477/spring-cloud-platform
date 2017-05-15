package com.siebre.basic.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

public class HeaderAndCookieSessionStrategy implements HttpSessionStrategy {
	
	private HeaderHttpSessionStrategy headerHttpSessionStrategy = new HeaderHttpSessionStrategy();
	
	private CookieHttpSessionStrategy cookieHttpSessionStrategy = new CookieHttpSessionStrategy();

	@Override
	public String getRequestedSessionId(HttpServletRequest request) {
		HttpSessionStrategy httpSessionStrategy = this.getCurrentHttpSessionStrategy(request);
		return httpSessionStrategy.getRequestedSessionId(request);
	}

	@Override
	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		HttpSessionStrategy httpSessionStrategy = this.getCurrentHttpSessionStrategy(request);
		httpSessionStrategy.onNewSession(session, request, response);
	}

	@Override
	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSessionStrategy httpSessionStrategy = this.getCurrentHttpSessionStrategy(request);
		httpSessionStrategy.onInvalidateSession(request, response);
	}
	
	private HttpSessionStrategy getCurrentHttpSessionStrategy(HttpServletRequest request) {
		return headerHttpSessionStrategy.getRequestedSessionId(request) != null ? this.headerHttpSessionStrategy : this.cookieHttpSessionStrategy;
	}

}
