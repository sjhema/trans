package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;

/**
 * @author kishor
 *
 */
@Entity
@Table(name="ptod_rate")
public class PtodRate extends AbstractBaseModel{
	
	@NotEmpty
	@Column(name="staff_id")
	private String staffId;
	
	@ManyToOne
	@JoinColumn(name="employee")
	private Driver driver;
	
	@ManyToOne
	@JoinColumn(name="catagory")
	private EmployeeCatagory catagory;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name="ptod_rate")
	private Double ptodRate;

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public EmployeeCatagory getCatagory() {
		return catagory;
	}

	public void setCatagory(EmployeeCatagory catagory) {
		this.catagory = catagory;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Double getPtodRate() {
		return ptodRate;
	}

	public void setPtodRate(Double ptodRate) {
		this.ptodRate = ptodRate;
	}
	
	

}
