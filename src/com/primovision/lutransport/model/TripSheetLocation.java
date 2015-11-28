package com.primovision.lutransport.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="tripsheet_location")
public class TripSheetLocation extends AbstractBaseModel {
	
	@NotEmpty
	private String name;	
	
	private Integer type;
	
	@ManyToOne
	@JoinColumn(name = "terminal")
	protected Location terminal;
	
	
	@ManyToOne
	@JoinColumn(name="driver_company")
	protected Location driverCompany;

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

	public Location getDriverCompany() {
		return driverCompany;
	}
	
	public void setDriverCompany(Location driverCompany) {
		this.driverCompany = driverCompany;
	}
	
	public Location getTerminal() {
		return terminal;
	}
	
	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}
}
