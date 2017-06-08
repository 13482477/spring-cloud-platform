package com.siebre.admin.security.logout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class SiebreAdminLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		request.getSession().invalidate();
		super.onLogoutSuccess(request, response, authentication);
	}

}
