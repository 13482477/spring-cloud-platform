package com.siebre.security.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色模型
 */
public class Role implements Serializable {
	
	private static final long serialVersionUID = 6092451018013645818L;

	private Long id;

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
	 * 权限
	 */
	private List<Authority> authorities = new ArrayList<Authority>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
