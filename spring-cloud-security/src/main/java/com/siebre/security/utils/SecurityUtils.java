package com.siebre.security.utils;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContext;

import com.siebre.basic.web.WebUtils;
import com.siebre.security.entity.User;

public class SecurityUtils {

	private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

	public static User getCurrentUser() {
		HttpSession session = WebUtils.getCurrentSession();
		if (session == null) {
			return null;
		}

		SecurityContext securityContext = (SecurityContext) WebUtils.getCurrentSession().getAttribute(SPRING_SECURITY_CONTEXT);
		if (securityContext == null) {
			return null;
		}

		return (User) securityContext.getAuthentication().getPrincipal();
	}

}
