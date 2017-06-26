package com.siebre.uaa.initializer.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.web.WebResult;
import com.siebre.uaa.authority.entity.Authority;
import com.siebre.uaa.authority.mapper.AuthorityMapper;
import com.siebre.uaa.initializer.service.InitializerService;
import com.siebre.uaa.role.entity.Role;
import com.siebre.uaa.role.service.RoleService;
import com.siebre.uaa.user.entity.User;
import com.siebre.uaa.user.service.UserService;

/**
 * Created by AdamTang on 2017/3/24. Project:siebre-cloud-platform Version:1.0
 */

@RestController
public class InitializerController {

	private static Logger logger = LoggerFactory.getLogger(InitializerController.class);
	@Autowired
	private InitializerService tool;

	@Autowired
	private AuthorityMapper authorityMapper;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/v1/initializer", method = RequestMethod.POST)
	public WebResult<String> createUser(String username, String password, ModelMap map) {
		URL resource = this.getClass().getClassLoader().getResource("resource.xml");

		if (resource == null) {
			WebResult.<String>builder().returnCode("500").returnMessage("无法找到初始化文件!").build();
		}
		File xml = new File(resource.getFile());
		
		tool.initResourceFromFile(xml);
		
		initAdminAuth(username, password);

		return null;
	}

	public void initAdminAuth(String username, String password) {
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

		List<Authority> allAuthorities = authorityMapper.getAllAuthorities();
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
//		this.userService.addUserRoles(user.getId(), roleIds);
	}

	private Long[] getAuthorityIds(List<Authority> allAuthorities) {
		List<Long> outputCollection = new ArrayList<>();
		CollectionUtils.collect(allAuthorities, input -> input.getId(), outputCollection);
		return outputCollection.toArray(new Long[] {});
	}
}
