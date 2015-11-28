package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="vehicle")
public class Vehicle extends AbstractBaseModel {
	
	private static final long serialVersionUID = 748152809471997087L;
	
	
	private Integer unit;
	
	private String unitNum;
	
	@NotEmpty
	@ManyToOne
	@JoinColumn(name="owner")
	private Location owner;
	
	/*@ManyToOne
	@JoinColumn(name="location_id")
	private Location location;*/
	
	@NotEmpty
	@Column(name="year")
	private String year;
	
	@NotEmpty
	@Column(name="make")
	private String make;
	
	@NotEmpty
	@Column(name="model")
	private String model;
	
	@NotEmpty
	@Column(name="vin_number")
	private String vinNumber;
	
	@NotEmpty
	@Column(name="plate")
	private String plate;
	
	@Column(name="type")
	private Integer type;
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;
	
	
	

	public Location getOwner() {
		return owner;
	}

	public void setOwner(Location owner) {
		this.owner = owner;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getVinNumber() {
		return vinNumber;
	}

	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Integer getUnit() {
		return unit;
	}
	
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
	
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	
	public String getUnitNum() {
		return unitNum;
	}


/*	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}*/

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
	
	
	
}
