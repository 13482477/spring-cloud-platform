package com.siebre.admin.init.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.admin.authority.entity.Authority;
import com.siebre.admin.authority.mapper.AuthorityMapper;
import com.siebre.admin.resource.util.InitResourceTool;
import com.siebre.admin.role.entity.Role;
import com.siebre.admin.role.service.RoleService;
import com.siebre.admin.user.entity.User;
import com.siebre.admin.user.service.UserService;

/**
 * Created by AdamTang on 2017/3/24.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
@RestController
public class InitSystemController {

    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    public void initAdminAuth(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(username);
        user.setActive(Boolean.TRUE);
        user.setMobile("your mobile");
        user.setEmail("your@email.example");
        user.setCreateUser(username);
        user.setCreateDate(new Date());
        userService.createUser(user);

        List<Authority> allAuthorities  = authorityMapper.getAllAuthorities();
        Long[] authorityIds = getAuthorityIds(allAuthorities);
        Role role = new Role();
        role.setRoleName("系统管理员");
        role.setRoleCode("SIEBRE_CLOUD_ADMINISTRATOR");
        role.setDescription("系统管理员");
        role.setCreateUser(username);
        role.setCreateDate(new Date());
        role.setUpdateUser(username);
        role.setUpdateDate(new Date());
        roleService.createRole(role);
        roleService.grant(role.getId(), authorityIds);

        List<Long> roleIds = new ArrayList<Long>();
        roleIds.add(role.getId());
        this.userService.updateUserRoles(user.getId(), roleIds);
    }

    private Long[] getAuthorityIds(List<Authority> allAuthorities) {
        List<Long> outputCollection = new ArrayList<>();
        CollectionUtils.collect(allAuthorities, input -> input.getId(), outputCollection);
        return outputCollection.toArray(new Long[]{});
    }
}
