package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Ravi
 * 
 */

@Entity
@Table(name = "Vehicle_toll_tag")
public class VehicleTollTag extends AbstractBaseModel{
	
	@ManyToOne
	@JoinColumn(name = "vehicle")
    protected Vehicle vehicle;
	
	/*@Column(name = "toll_tag")
    protected Integer tollTag;*/
	
	@NotEmpty
    @Column(name = "toll_tag_number")
    protected String tollTagNumber;
	
	/*@Column(name = "toll_tag_status")
    protected Integer tollTagStatus;*/
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;
	
	@Column(name="unit")
	private String unit;
	
	@ManyToOne
	@JoinColumn(name = "toll_company")
    protected TollCompany tollCompany;
	
	
	@Transient
	private Date validFromTemp;
	
	@Transient
	private Date validToTemp;
	
	
	
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	/*public Integer getTollTag() {
		return tollTag;
	}

	public void setTollTag(Integer tollTag) {
		this.tollTag = tollTag;
	}*/

	public String getTollTagNumber() {		
		return tollTagNumber;
	}

	public void setTollTagNumber(String tollTagNumber) {
		this.tollTagNumber = tollTagNumber;
	}

/*	public Integer getTollTagStatus() {		
		return tollTagStatus;
	}

	public void setTollTagStatus(Integer tollTagStatus) {
		this.tollTagStatus = tollTagStatus;
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
		}*/
		
		public void setUnit(String unit) {
			this.unit = unit;
		}
		
		public String getUnit() {
			return unit;
		}

public TollCompany getTollCompany() {
	return tollCompany;
}

public void setTollCompany(TollCompany tollCompany) {
	this.tollCompany = tollCompany;
}
	


public void setValidFrom(Date validFrom) {
	this.validFrom = validFrom;
}

public void setValidTo(Date validTo) {
	this.validTo = validTo;
}


	
public Date getValidFrom() {
	setValidFromTemp(validFrom);
	return validFrom;
}

public Date getValidTo() {
	setValidToTemp(validTo);
	return validTo;
}


public Date getValidFromTemp() {		
	return validFromTemp;
}

public void setValidFromTemp(Date validFromTemp) {
	this.validFromTemp = validFromTemp;
}

public Date getValidToTemp() {		
	return validToTemp;
}

public void setValidToTemp(Date validToTemp) {
	this.validToTemp = validToTemp;
}	
	
}

