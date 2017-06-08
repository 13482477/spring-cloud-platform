package com.siebre.admin.resource.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siebre.admin.authority.entity.Authority;
import com.siebre.admin.authority.mapper.AuthorityMapper;
import com.siebre.admin.enums.ResourceType;
import com.siebre.admin.resource.entity.Resource;
import com.siebre.admin.resource.mapper.ResourceMapper;
import com.siebre.admin.role.entity.Role;
import com.siebre.admin.role.mapper.RoleMapper;

/**
 * Created by AdamTang on 2017/3/6. Project:siebre-cloud-platform Version:1.0
 */
@Service
public class ResourceService {

	@Autowired
	private ResourceMapper resourceMapper;

	@Autowired
	private AuthorityMapper authorityMapper;

	@Autowired
	private RoleMapper roleMapper;

	public List<Resource> loadAllResourceWithAuthority() {
		return this.resourceMapper.getAllResourceWithAuthority();
	}

	@Transactional
	public void createResource(Resource resource) {
		resource.setId(null);
		if (resource.getSequence() == null) {
			resource.setSequence(initSequence(resource.getParentId()));
		}
		resourceMapper.insert(resource);

		initAuthority(resource);
	}

	private void initAuthority(Resource resource) {
		// TODO 对一个resource添加不同的权限
		Authority authority = new Authority();
		authority.setAuthorityCode("ROLE_" + resource.getResourceCode());
		authority.setAuthorityName(resource.getResourceName());
		Set<Authority> authorities = new HashSet<>();

		authorities.add(authority);
		resource.setAuthorities(authorities);

		this.authorityMapper.insert(authority);

		this.resourceMapper.insertResourceAuthority(resource.getId(), authority.getId());
	}

	private int initSequence(Long parentId) {
		int count = resourceMapper.getChildrenCount(parentId).intValue();
		return (count + 1) * 100;
	}

	public void updateResource(Resource resource) {
		Resource dbResource = resourceMapper.selectByPrimaryKey(resource.getId());
		dbResource.setUrl(resource.getUrl());
		dbResource.setDescription(resource.getDescription());
		dbResource.setResourceType(resource.getResourceType());
		dbResource.setResourceName(resource.getResourceName());
		dbResource.setResourceCode(resource.getResourceCode());
		resourceMapper.updateByPrimaryKeySelective(dbResource);
	}

	public Resource getResourceByID(Long id) {
		return resourceMapper.selectByPrimaryKey(id);
	}

	public List<Resource> getResourceByParentId(Long parentId) {
		List<Resource> resources = this.resourceMapper.getChildren(parentId);

		for (Resource resource : resources) {
			int count = resourceMapper.getChildrenCount(resource.getId()).intValue();

			if (count != 0) {
				resource.setOpen(true);
				resource.setIsParent(true);
			}
		}
		return resources;
	}

	public List<Resource> loadResourceTreeByRole(Long roleId) {
		Role selectedRole = this.roleMapper.getRole(roleId);

		Set<String> authorityCodes = this.generateAuthorityCodeSet(selectedRole);

		List<Resource> roots = this.resourceMapper.getSortedResourceByParent(null, null);
		for (Resource root : roots) {
			root.setOpen(true);
			this.checkedNode(root, authorityCodes);
			this.loadResourceByParent(root, root.getChildren(), authorityCodes, null);
		}
		return roots;
	}

	public List<Resource> loadAuthorityMenu(Set<String> authorityCodes) {
		Resource topLevelResource = this.resourceMapper.getResourceByResourceCode("SIEBRE_CLOUD_ADMIN");

		List<Resource> roots = this.resourceMapper.getSortedResourceByParent(topLevelResource.getId(), ResourceType.MENU);

		for (Resource root : roots) {
			root.setOpen(true);
			this.checkedNode(root, authorityCodes);
			this.loadResourceByParent(root, root.getChildren(), authorityCodes, ResourceType.MENU);
		}
		return roots;
	}

	public void deleteResource(Long id) {
		resourceMapper.clearAuthority(id);
		resourceMapper.deleteByPrimaryKey(id);
	}

	private void loadResourceByParent(Resource parent, List<Resource> currentList, Set<String> authorityCodes, ResourceType type) {
		List<Resource> nodes = resourceMapper.getSortedResourceByParent(parent.getId(), type);
		currentList.addAll(nodes);
		for (Resource r : nodes) {
			r.setOpen(true);
			this.checkedNode(r, authorityCodes);
			this.loadResourceByParent(r, r.getChildren(), authorityCodes, type);
		}
	}

	private Set<String> generateAuthorityCodeSet(Role role) {
		Set<String> result = new HashSet<>();
		for (Authority authority : role.getAuthorities()) {
			result.add(authority.getAuthorityCode());
		}
		return result;
	}

	private boolean checkedNode(Resource resource, Set<String> authorityCodes) {
		for (Authority resourceAuthority : resource.getAuthorities()) {
			if (authorityCodes.contains(resourceAuthority.getAuthorityCode())) {
				resource.setChecked(Boolean.TRUE);
				return true;
			}
		}
		return false;
	}

}
