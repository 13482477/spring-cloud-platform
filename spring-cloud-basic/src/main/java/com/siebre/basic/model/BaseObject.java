package com.siebre.basic.model;

import java.io.Serializable;
import java.util.Date;

public class BaseObject implements Serializable {

	private static final long serialVersionUID = -5510229019405268937L;
	
	protected Long id;
	
	protected String createUser;
	
	protected Date createDate;
	
	protected String updateUser;
	
	protected Date updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
