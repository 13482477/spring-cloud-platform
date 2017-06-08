package com.siebre.admin.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 李志强
 */
public class SiebreAdminAccessDeniedHandler implements AccessDeniedHandler {

	private static final String RESPONSE_TYPE_JSON = "application/json";

	private static final int RESPONSE_TYPE_ERROR = 2;

	// 错误页面地址
	private String errorPage;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String header = request.getHeader("accept");
		response.setStatus(HttpStatus.SC_FORBIDDEN);

		if (StringUtils.contains(header, RESPONSE_TYPE_JSON)) {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("returnType", RESPONSE_TYPE_ERROR);
			returnMap.put("returnMessage", SiebreAdminSecurityFilter.ACCESS_DENIED);

			ObjectMapper objectMapper = new ObjectMapper();

			response.getWriter().print(objectMapper.writeValueAsString(objectMapper));
			return;
		}

		response.sendRedirect(request.getContextPath() + errorPage);
		return;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
}
