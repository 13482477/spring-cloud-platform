package com.siebre.admin.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.siebre.admin.init.controller.InitSystemController;

import javax.servlet.*;
import java.io.IOException;

/**
 * 权限拦截过滤器
 * 
 * @author lizhiqiang
 */
public class SiebreAdminSecurityFilter extends AbstractSecurityInterceptor implements Filter {

	private static Logger logger = LoggerFactory.getLogger(InitSystemController.class);

	/** 没有权限错误信息 */
	public static final String ACCESS_DENIED = "Access Denied";

	// 权限资源管理对象
	private FilterInvocationSecurityMetadataSource metadata;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain c) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(req, res, c);
		invoke(fi);
	}

	public void invoke(FilterInvocation fi) throws IOException, ServletException {

		InterceptorStatusToken token = null;

		try {
			token = super.beforeInvocation(fi);
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				logger.error("The public invaction is not allowed, request url:{}", fi.getRequest().getRequestURL());
				throw new AccessDeniedException(ACCESS_DENIED);
			}
			throw new RuntimeException(e);
		} finally {

			super.afterInvocation(token, null);
		}
	}

	@Override
	public void init(FilterConfig f) throws ServletException {
	}

	public void setMetadata(FilterInvocationSecurityMetadataSource metadata) {
		this.metadata = metadata;
	}

	@Override
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.metadata;
	}
}
