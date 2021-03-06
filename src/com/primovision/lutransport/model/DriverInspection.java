package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="driver_inspection")
public class DriverInspection extends AbstractBaseModel{

	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@Column(name="weekOfDate")
	private Date weekOfDate;
	
	@Column(name="weekOfDate_to")
	private Date weekOfDateTo;
	
	@ManyToOne
	@JoinColumn(name="driver")
	protected Driver driver;	
	
	@Column(name="entered_by")
	protected  String enteredBy;
	
	@Column(name="weekDate")
	private Date weekDate;
	
	@Transient
	private Date weekDateTo;
	
	@Column(name="weekDate_Day")
	private String weekDateDay;
	
	@Column(name="inspection_status")
	private String inspectionStatus;
	
	@Column(name="day_refcolumn")
	private String dayRefColumn;


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


	public Date getWeekOfDate() {
		return weekOfDate;
	}


	public void setWeekOfDate(Date weekOfDate) {
		this.weekOfDate = weekOfDate;
	}


	public Driver getDriver() {
		return driver;
	}


	public void setDriver(Driver driver) {
		this.driver = driver;
	}


	public String getEnteredBy() {
		return enteredBy;
	}


	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}


	public Date getWeekDate() {
		return weekDate;
	}


	public void setWeekDate(Date weekDate) {
		this.weekDate = weekDate;
	}


	public String getInspectionStatus() {
		return inspectionStatus;
	}


	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}
	
	
	public String getWeekDateDay() {
		return weekDateDay;
	}
	
	public void setWeekDateDay(String weekDateDay) {
		this.weekDateDay = weekDateDay;
	}

	
	public String getDayRefColumn() {
		return dayRefColumn;
	}
	
	public void setDayRefColumn(String dayRefColumn) {
		this.dayRefColumn = dayRefColumn;
	}

	
	public Date getWeekOfDateTo() {
		return weekOfDateTo;
	}
	
	public void setWeekOfDateTo(Date weekOfDateTo) {
		this.weekOfDateTo = weekOfDateTo;
	}
	
	public Date getWeekDateTo() {
		return weekDateTo;
	}
	
	
	public void setWeekDateTo(Date weekDateTo) {
		this.weekDateTo = weekDateTo;
	}
	
}
