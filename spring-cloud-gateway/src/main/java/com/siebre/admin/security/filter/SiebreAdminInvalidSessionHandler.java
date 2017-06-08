package com.siebre.admin.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 会话失效处理类
 * @author lizhiqiang
 */
public class SiebreAdminInvalidSessionHandler implements InvalidSessionStrategy {

	protected final Log logger = LogFactory.getLog(getClass());

	/** 响应类型是JSON */
	private static final String RESPONSE_TYPE_JSON = "application/json";

	/** 会话失效的响应代码 */
	private static final int RESPONSE_TYPE_INVALID_SESSION_ERROR = 3;

	/** 回话失效的响应信息 */
	private static final String RES_INVALID_SESSION_MSG = "Invalid Session";

	// 跳转策略
	protected final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	// 跳转地址
	private String redirectUrl;

	// 需要移除的cookie
	private String[] removeCookies;

	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		logger.debug("[SiebreSessionHandler] session invalid");

		// 获得请求头信息
		String header = request.getHeader("accept");

		// 删除cookie
		for (String c : removeCookies) {
			Cookie cookie = new Cookie(c, null);
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

		// 重新创建session
		request.getSession();

		if (StringUtils.contains(header, RESPONSE_TYPE_JSON)) {
			// AJAX请求时，发现session失效，给于失效的响应信息
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("returnType", RESPONSE_TYPE_INVALID_SESSION_ERROR);
			obj.put("returnMessage", RES_INVALID_SESSION_MSG);

			ObjectMapper objectMapper = new ObjectMapper();
			response.getWriter().print(objectMapper.writeValueAsString(obj));
			return;
		}

		redirectStrategy.sendRedirect(request, response, redirectUrl);
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String[] getRemoveCookies() {
		return removeCookies;
	}

	public void setRemoveCookies(String[] removeCookies) {
		this.removeCookies = removeCookies;
	}
}
