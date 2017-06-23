package com.siebre.basic.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtils {
	
	
	public static HttpServletRequest getCurrentRequest() {
		return RequestContextHolder.currentRequestAttributes() == null ? null : ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public static HttpSession getCurrentSession() {
		return getCurrentRequest() == null ? null : getCurrentRequest().getSession(false);
	}

}
