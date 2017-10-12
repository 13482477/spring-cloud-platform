package com.siebre.security.authentication;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by jhonelee on 2017/7/19.
 */
public class OpenApiAuthentication extends AbstractAuthenticationToken {

    private String devId;

    private String appId;

    private String appAuthToken;

    private String sign;

    private HttpServletRequest httpServletRequest;


    public OpenApiAuthentication(String devId, String appId, String appAuthToken, String sign, javax.servlet.http.HttpServletRequest httpServletRequest) {
        super(null);
        this.devId = this.devId;
        this.appId = this.appId;
        this.appAuthToken = this.appAuthToken;
        this.sign = this.sign;
        this.httpServletRequest = this.httpServletRequest;
    }

    public OpenApiAuthentication(String devId, String appId, String appAuthToken, String sign, HttpServletRequest httpServletRequest, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.devId = devId;
        this.appId = appId;
        this.appAuthToken = appAuthToken;
        this.sign = sign;
        this.httpServletRequest = httpServletRequest;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public Object getCredentials() {
        return this.appAuthToken;
    }

    @Override
    public Object getPrincipal() {
        return this.devId;
    }
}
