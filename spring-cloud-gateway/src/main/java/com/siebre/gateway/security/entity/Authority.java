package com.siebre.gateway.security.entity;

/**
 * 权限模型
 */
public class Authority {
	
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
