package com.siebre.uaa.role.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.uaa.role.entity.Role;
import com.siebre.uaa.role.service.RoleService;

@RestController
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value = "/api/v1/roles", method = {RequestMethod.GET})
	public WebResult<List<Role>> list(
								@RequestParam(value = "roleName", required = false) String roleName,
								@RequestParam(value = "roleCode", required = false) String roleCode,
								PageInfo pageInfo
			) {

		List<Role> data = this.roleService.findRoles(roleName, roleCode, pageInfo);
		return WebResult.<List<Role>>builder().returnCode("200").data(data).build();
	}
	
	@RequestMapping(value = "/api/v1/role", method = {RequestMethod.POST})
	public WebResult<Role> create(@RequestBody Role role) {
		this.roleService.createRole(role);
		return WebResult.<Role>builder().returnCode("200").data(role).build();
	}

	@RequestMapping(value = "/api/v1/role", method = {RequestMethod.PUT})
	public WebResult<Role> upate(Role role) {
		this.roleService.updateRole(role);
		return WebResult.<Role>builder().returnCode("200").data(role).build();
	}

	@RequestMapping(value = "/api/v1/role/{id}", method = {RequestMethod.GET})
	public WebResult<Role> get(@PathVariable Long id) {
		Role role = this.roleService.loadRole(id);
		return WebResult.<Role>builder().returnCode("200").data(role).build();
	}
	
	@RequestMapping(value="/api/v1/role/grant/{roleId}", method = RequestMethod.POST)
	public WebResult<Object> grant(@PathVariable Long roleId, @RequestBody Long[] resourceIds) {
		this.roleService.grant(roleId, resourceIds);
		return WebResult.<Object>builder().returnCode("200").build();
	}

}
