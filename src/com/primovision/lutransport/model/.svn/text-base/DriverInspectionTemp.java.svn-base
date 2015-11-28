package com.primovision.lutransport.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;


@Entity
public class DriverInspectionTemp extends AbstractBaseModel{

	@Transient
	private Location company;	
	
	@Transient
	private Location terminal;	
	
	@Transient
	private Date weekOfDate;	
	
	@Transient
	protected Driver driver;
	
	@Transient
	protected  String enteredBy;

	@Transient
	private List daysList;

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

	public List getDaysList() {
		return daysList;
	}

	public void setDaysList(List daysList) {
		this.daysList = daysList;
	}
	
	
	
	
	
}
