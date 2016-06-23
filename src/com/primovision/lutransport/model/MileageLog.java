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

@Entity
@Table(name = "mileage_log")
public class MileageLog extends AbstractBaseModel implements ReportModel {
	@NotNull
	@Column(name="period")
	private Date period;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="state")
	private State state;
	
	@NotNull
	@Column(name="miles")
	private Double miles;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="unit")
	private Vehicle unit;
	
	@NotNull
	@NotEmpty
	@Column(name="unit_num")
	private String unitNum;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="company")
	private Location company;
	
	@NotNull
	@NotEmpty
	@Column(name="vin")
	private String vin;
	
	@Transient
	private String periodStr;

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Double getMiles() {
		return miles;
	}

	public void setMiles(Double miles) {
		this.miles = miles;
	}

	public Vehicle getUnit() {
		return unit;
	}

	public void setUnit(Vehicle unit) {
		this.unit = unit;
	}

	public String getUnitNum() {
		return unitNum;
	}

	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	@Transient
	public String getPeriodStr() {
		return periodStr;
	}

	@Transient
	public void setPeriodStr(String periodStr) {
		this.periodStr = periodStr;
	}
}