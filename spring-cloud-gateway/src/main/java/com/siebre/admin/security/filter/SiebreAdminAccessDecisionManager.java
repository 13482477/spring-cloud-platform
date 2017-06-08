package com.siebre.admin.security.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义的权限验证
 * @author lizhiqiang
 */
public class SiebreAdminAccessDecisionManager implements AccessDecisionManager {

	/** logger */
	protected static final Logger logger = Logger.getLogger(SiebreAdminAccessDecisionManager.class);

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		if (configAttributes == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Siebre Admin AccessDecisionManager.decide(Authentication, Object, Collection<ConfigAttribute>) - configAttributes is null");
			}
			return;
		}

		for (ConfigAttribute configAttribute : configAttributes) {
			String needRole = ((SecurityConfig) configAttribute).getAttribute();
			for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
				if (StringUtils.equals(needRole, grantedAuthority.getAuthority())) {
					return;
				}
			}
		}

		// 权限验证失败
		throw new AccessDeniedException(SiebreAdminSecurityFilter.ACCESS_DENIED);
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
