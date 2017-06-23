package com.siebre.security.entity;

import java.io.Serializable;

/**
 * 权限模型
 */
public class Authority implements Serializable {
	
	private static final long serialVersionUID = 4382105630022269739L;

	private Long id;

	/**
     * 权限名称
     */
    private String authorityName;

    /**
     * 权限代码
     */
    private String authorityCode;

    /**
     * 权限描述
     */
    private String description;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getAuthorityCode() {
		return authorityCode;
	}

	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
