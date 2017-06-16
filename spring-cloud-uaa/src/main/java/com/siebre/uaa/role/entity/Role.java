package com.siebre.uaa.role.entity;

import java.util.ArrayList;
import java.util.List;

import com.siebre.basic.model.BaseObject;
import com.siebre.uaa.authority.entity.Authority;

/**
 * 角色模型
 */
public class Role extends BaseObject {

	private static final long serialVersionUID = -6791378540349198047L;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色代码
	 */
	private String roleCode;

	/**
	 * 角色描述
	 */
	private String description;

	/**
	 * 资源
	 */
	private List<Authority> authorities = new ArrayList<Authority>();

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

}
