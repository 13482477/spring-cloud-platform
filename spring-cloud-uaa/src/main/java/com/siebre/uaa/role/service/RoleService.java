package com.siebre.uaa.role.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.basic.query.PageInfo;
import com.siebre.uaa.authority.entity.Authority;
import com.siebre.uaa.resource.entity.Resource;
import com.siebre.uaa.resource.mapper.ResourceMapper;
import com.siebre.uaa.role.entity.Role;
import com.siebre.uaa.role.mapper.RoleMapper;
import com.siebre.uaa.user.entity.User;

@Service
public class RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private ResourceMapper resourceMapper;

	public List<Role> getAllRoles() {
		return null;
	}

	public List<Role> getUserRoles(User user) {
		return null;
	}

	public List<Role> getUserRoles(Long userId) {
		return roleMapper.selectByUserID(userId);
	}

	public List<Role> findRoles(String roleName, String roleCode, PageInfo pageInfo) {
		return this.roleMapper.findRole(roleName, roleCode, pageInfo);
	}

	public void createRole(Role role) {
		this.roleMapper.insert(role);
	}

	public void updateRole(Role role) {
		this.roleMapper.updateByPrimaryKeySelective(role);
	}

	public Role loadRole(Long id) {
		return this.roleMapper.selectByPrimaryKey(id);
	}

	@Transactional
	public void grant(Long roleId, Long[] resourceIds) {
		List<Resource> resources = new ArrayList<Resource>();
		if (ArrayUtils.isNotEmpty(resourceIds)) {
			resources = this.resourceMapper.getResourcesByIds(resourceIds);
		}
		this.roleMapper.removeRoleAuthority(roleId);
		
		Set<Long> authorityIds = new HashSet<Long>();
		for (Resource r : resources) {
			for (Authority authority : r.getAuthorities()) {
				authorityIds.add(authority.getId());
			}
		}
		
		if (!authorityIds.isEmpty()) {
			this.roleMapper.grantAuthority(roleId, authorityIds);
		}

	}

}
