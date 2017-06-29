package com.siebre.uaa.initializer.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.siebre.basic.utils.JsonUtil;
import com.siebre.uaa.authority.entity.Authority;
import com.siebre.uaa.authority.mapper.AuthorityMapper;
import com.siebre.uaa.enums.ResourceType;
import com.siebre.uaa.initializer.dto.ResourceDTO;
import com.siebre.uaa.resource.entity.Resource;
import com.siebre.uaa.resource.service.ResourceService;
import com.siebre.uaa.role.entity.Role;
import com.siebre.uaa.role.service.RoleService;
import com.siebre.uaa.user.entity.User;
import com.siebre.uaa.user.service.UserService;

/**
 * 初始化Resource用 Created by AdamTang on 2017/3/22. Project:siebre-cloud-platform
 * Version:1.0
 */
@Service
public class InitializerService {
	private static Logger logger = LoggerFactory.getLogger(InitializerService.class);

	@Autowired
	private ResourceService service;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorityMapper authorityMapper;


	private PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

	private SaltSource saltSource = new ReflectionSaltSource();
	
	public void initResource() {

		URL url = this.getClass().getClassLoader().getResource("resource2.xml");

		String filePath = url.getPath();

		File xml = new File(filePath);

		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(ResourceDTO.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ResourceDTO resource = (ResourceDTO) unmarshaller.unmarshal(xml);
			logger.debug(JsonUtil.toJson(resource, true));
			transform(new Resource(), resource, 0);
			
		} catch (JAXBException e) {
			logger.error("xml解析失败", e);
		}
	}

	private void transform(Resource root, ResourceDTO dto, int index) {
		Resource resource = new Resource();
		resource.setResourceName(dto.getName());
		resource.setResourceType(ResourceType.valueOf(dto.getType()));
		resource.setUrl(dto.getUrl());
		resource.setResourceCode(dto.getCode());
		resource.setDescription(dto.getDescription());
		resource.setSequence((index + 1) * 100);
		resource.setParentId(root.getId());
		resource.setCreateUser("admin");
		resource.setCreateDate(new Date());

		service.createResource(resource);

		List<ResourceDTO> child = dto.getResourceList();
		for (int i = 0; i < child.size(); i++) {
			transform(resource, child.get(i), i);
		}
	}

	public void initAdminAuth() {
		User user = new User();
		user.setUsername("admin");
		user.setPassword(this.passwordEncoder.encodePassword("password", user.getUsername()));
		user.setNickname("admin");
		user.setActive(Boolean.TRUE);
		user.setMobile("your mobile");
		user.setEmail("your@email.example");
		user.setCreateUser("admin");
		user.setCreateDate(new Date());
		userService.createUser(user);

		List<Authority> allAuthorities = authorityMapper.getAllAuthorities();
		Long[] authorityIds = getAuthorityIds(allAuthorities);
		Role role = new Role();
		role.setRoleName("系统管理员");
		role.setRoleCode("SIEBRE_CLOUD_ADMINISTRATOR");
		role.setDescription("系统管理员");
		role.setCreateUser("admin");
		role.setCreateDate(new Date());
		role.setUpdateUser("admin");
		role.setUpdateDate(new Date());
		roleService.createRole(role);
		roleService.grant(role.getId(), authorityIds);

		List<Long> roleIds = new ArrayList<Long>();
		roleIds.add(role.getId());
		// this.userService.addUserRoles(user.getId(), roleIds);
	}

	private Long[] getAuthorityIds(List<Authority> allAuthorities) {
		List<Long> outputCollection = new ArrayList<>();
		CollectionUtils.collect(allAuthorities, input -> input.getId(), outputCollection);
		return outputCollection.toArray(new Long[] {});
	}

}
