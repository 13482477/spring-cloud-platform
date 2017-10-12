package com.siebre.security.authenticationprovider;

import com.siebre.security.authentication.OpenApiAuthentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

/**
 * Created by jhonelee on 2017/7/19.
 */
public class OpenApiAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(OpenApiAuthentication.class, authentication, "Only OpenApiAuthentication is supported");

        OpenApiAuthentication openApiAuthentication = (OpenApiAuthentication) authentication;

		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(OpenApiAuthentication.class);
	}
}
