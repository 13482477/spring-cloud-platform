package com.siebre.uaa.authority.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siebre.uaa.authority.entity.Authority;
import com.siebre.uaa.authority.mapper.AuthorityMapper;

/**
 * Created by AdamTang on 2017/3/6.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@Service
public class AuthorityService {

    private AuthorityMapper authorityMapper;

    @Autowired
    public void setAuthorityMapper(AuthorityMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }

    public List<Authority> getAuthoritiesByResourceId(Long resourceId) {
        List<Authority> authorities = authorityMapper.selectByResourceID(resourceId);
        return authorities;
    }
}
