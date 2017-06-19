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
	public static String PHILADELPHIA_TRANSFER_STATION = "Philadelphia Transfer Station";
	@Transient
	public static String FORGE_TRANSFER_STATION = "Philadelphia Transfer - Forge";
	@Transient
	public static String BQE_TRANSFER_STATION = "BQE Transfer Station";
	@Transient
	public static String VARICK_I_TRANSFER_STATION = "Varick I Transfer Station";
	@Transient
	public static String YONKERS_TRANSFER_STATION = "Yonkers Transfer";
	
	@NotEmpty
	private String name;
	
	private String code;
	
	private Integer type;
	
	@Column(name = "long_name")
	private String longName;
	
	@Column(name = "hauling_name")
	private String haulingName;
	
	

	public String getHaulingName() {
		return haulingName;
	}

	public void setHaulingName(String haulingName) {
		this.haulingName = haulingName;
	}

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
