package com.primovision.lutransport.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="state")
public class State extends AbstractBaseModel{
	private String name;
	
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
