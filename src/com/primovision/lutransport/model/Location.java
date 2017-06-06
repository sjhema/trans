package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="location")
public class Location extends AbstractBaseModel {
	@Transient
	public static String PHILADELPHIA = "Philadelphia Transfer Station";
	@Transient
	public static String FORGE = "Philadelphia Transfer - Forge";
	@Transient
	public static String BQE = "BQE Transfer Station";
	@Transient
	public static String VARICK_I = "Varick I Transfer Station";
	
	@NotEmpty
	private String name;
	
	private String code;
	
	private Integer type;
	
	@Column(name = "long_name")
	private String longName;

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
