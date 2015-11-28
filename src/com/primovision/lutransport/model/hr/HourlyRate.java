package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name="hourly_rate")
public class HourlyRate extends AbstractBaseModel{

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
	
	@Column(name ="daily_hours")
	protected Double dailyHours=0.0;
	

	@Column(name ="weekly_hours")
	protected Double weeklyHours=0.0;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name="hourlyregular_rate")
	private Double hourlyRegularRate;
	
	@Column(name="hourlyovertime_rate")
	private Double hourlyOvertimeRate;
	
	@Column(name="hourlydoubletime_rate")
	private Double hourlyDoubleTimeRate;
	
	@Transient
	private Date validFromTemp;
	
	@Transient
	private Date validToTemp;
	
	


	public Date getValidFrom() {
		setValidFromTemp(validFrom);
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		setValidToTemp(validTo);
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
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

	

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Double getHourlyRegularRate() {
		return hourlyRegularRate;
	}

	public void setHourlyRegularRate(Double hourlyRegularRate) {
		this.hourlyRegularRate = hourlyRegularRate;
	}

	public Double getHourlyOvertimeRate() {
		return hourlyOvertimeRate;
	}

	public void setHourlyOvertimeRate(Double hourlyOvertimeRate) {
		this.hourlyOvertimeRate = hourlyOvertimeRate;
	}

	public Double getHourlyDoubleTimeRate() {
		return hourlyDoubleTimeRate;
	}

	public void setHourlyDoubleTimeRate(Double hourlyDoubleTimeRate) {
		this.hourlyDoubleTimeRate = hourlyDoubleTimeRate;
	}
	
	public Double getWeeklyHours() {
		return weeklyHours;
	}
	public Double getDailyHours() {
		return dailyHours;
	}
	
	public void setDailyHours(Double dailyHours) {
		this.dailyHours = dailyHours;
	}
	
	
	public void setWeeklyHours(Double weeklyHours) {
		this.weeklyHours = weeklyHours;
	}
	
	
}
