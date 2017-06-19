package com.siebre.gateway.security.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.siebre.gateway.security.entity.Authority;
import com.siebre.gateway.security.entity.Resource;

/**
 * @author lizhiqiang
 */
@Configurable
public class CustomerSecurityMetadata implements FilterInvocationSecurityMetadataSource {

    protected static final Logger logger = Logger.getLogger(CustomerSecurityMetadata.class);

    //采用线程安全的
    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new ConcurrentHashMap<RequestMatcher, Collection<ConfigAttribute>>();

    @Autowired
    private ResourceRemoteService resourceRemoteService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Set<ConfigAttribute> configAttribute = new HashSet<ConfigAttribute>();
        if (requestMap.isEmpty()) {
            return configAttribute;
        }

        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for (Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            RequestMatcher requestMatcher = entry.getKey();
            if (requestMatcher.matches(request)) {
                configAttribute.addAll(entry.getValue());
                break;
            }
        }

        return configAttribute;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        if (requestMap.isEmpty()) {
            return null;
        }
        Collection<ConfigAttribute> result = new ArrayList<ConfigAttribute>();

        for (Collection<ConfigAttribute> ca : requestMap.values()) {
            result.addAll(ca);
        }
        return result;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

    public void initSecurityMeta() {
        List<Resource> resources = resourceRemoteService.allResourcesAndPermission().getData();
        this.refreshRequestMap(resources);
    }

    private void refreshRequestMap(List<Resource> resources) {
        this.requestMap.clear();
        for (Resource resource : resources) {
            addResource(resource);
        }
    }

    public void addResource(Resource resource) {
        if (StringUtils.isNotEmpty(resource.getUrl())) {
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resource.getUrl());
            String[] authorityCodes = this.getAuthorityCodes(resource.getAuthorities());
            Collection<ConfigAttribute> configAttributes = SecurityConfig.createList(authorityCodes);
            requestMap.put(requestMatcher, configAttributes);
        }
    }

    public void removeResource(Resource resource) {
        if (StringUtils.isNotEmpty(resource.getUrl())) {

            RequestMatcher requestMatcher = new AntPathRequestMatcher(resource.getUrl());
            //只采用url的方式下requestMatcher可以通过url的hashcode进行匹配
            requestMap.remove(requestMatcher);
        }
    }

    private String[] getAuthorityCodes(Set<Authority> allAuthorities) {
        List<String> outputCollection = new ArrayList<String>();
        CollectionUtils.collect(allAuthorities, new Transformer<Authority, String>() {
            public String transform(Authority input) {
                return input.getAuthorityCode();
            }
        }, outputCollection);
        return outputCollection.toArray(new String[]{});
    }
}
