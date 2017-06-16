package com.siebre.uaa.resource.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.siebre.basic.model.BaseObject;
import com.siebre.uaa.authority.entity.Authority;
import com.siebre.uaa.enums.ResourceType;

/**
 * Created by AdamTang on 2017/3/2.
 * Project:siebre-cloud-platform
 * Version:1.0
 * 资源模型
 */
public class Resource extends BaseObject {

	private static final long serialVersionUID = 2315408666679506035L;

	/**
	 * 资源类型
	 */
	private ResourceType resourceType;
	
	/**
	 * 资源名称
	 */
	private String resourceName;
	
	/**
	 * 资源代码
	 */
	private String resourceCode;

	/**
     * 资源url
     */
    private String url;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 父级资源
     */
    private Resource parent;

    /**
     * 父级资源id
     */
    private Long parentId;

    /**
     * 资源权限
     */
    private Set<Authority> authorities = new HashSet<Authority>();

    /**
     * 是否展开
     */
    private Boolean open;

    /**
     * 是否父级节点
     */
    private Boolean isParent;
    
    /**
     * 是否选中
     */
    private Boolean checked;
    
    /**
     * 子节点
     */
    private List<Resource> children = new ArrayList<Resource>();

	/**
	 * 排序序列
	 */
	private Integer sequence;

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
