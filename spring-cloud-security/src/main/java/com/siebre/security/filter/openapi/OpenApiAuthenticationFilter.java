package com.siebre.security.filter.openapi;

import com.siebre.security.authentication.OpenApiAuthentication;
import com.siebre.security.exception.MissingRequiredParamatersException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jhonelee on 2017/7/20.
 */
public class OpenApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String PARAM_DEV_ID = "devId";

    private static final String PARAM_APP_ID = "appId";

    private static final String PARAM_APP_AUTH_TOKEN = "appAuthToken";

    private static final String PARAM_SIGN = "sign";

    public OpenApiAuthenticationFilter() {
        super(new AntPathRequestMatcher("/*/openApi/**"));
    }

    protected OpenApiAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        OpenApiAuthentication openApiAuthenticationRequest = this.obtainAuthenticationFromRequest(httpServletRequest);

        openApiAuthenticationRequest.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));

        return this.getAuthenticationManager().authenticate(openApiAuthenticationRequest);
    }

    private OpenApiAuthentication obtainAuthenticationFromRequest(HttpServletRequest httpServletRequest) {
        String devId = httpServletRequest.getParameter(PARAM_DEV_ID);
        String appId = httpServletRequest.getParameter(PARAM_APP_ID);
        String appAuthToken = httpServletRequest.getParameter(PARAM_APP_AUTH_TOKEN);
        String sign = httpServletRequest.getParameter(PARAM_SIGN);

        if (StringUtils.isBlank(devId) || StringUtils.isBlank(appId) || StringUtils.isBlank(appAuthToken) || StringUtils.isBlank(sign)) {
            throw new MissingRequiredParamatersException("devId=" + devId + ";appId=" + appId + ";appAuthToken=" + appAuthToken + ";sign=" + sign);
        }

        return new OpenApiAuthentication(devId, appId, appAuthToken, sign, httpServletRequest);
    }
}
