package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;
/**
 * @author kishor
 *
 */
@Entity
@Table(name="driver_rate")
public class DriverPayRate extends AbstractBaseModel{
	
	@ManyToOne
	@JoinColumn(name="catagory")
	private EmployeeCatagory catagory;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;

	@ManyToOne
	@JoinColumn(name="transfer_station")
	private Location transferStation;
	
	@ManyToOne
	@JoinColumn(name="landfill")
	private Location landfill;


	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;

	@Column(name="rate_type")
	private Integer rateType;

	@Column(name="rate_using")
	private Integer rateUsing;
	
	@NotNull
	@Column(name="payrate")
	private Double payRate;
	
	
	@Column(name="probationrate")
	private Double probationRate;
	
	@NotNull
	@Column(name="nightPayRate")
	private Double nightPayRate;
	
	
	@NotNull
	@Column(name="Sundayratefactor")
	private Double sundayRateFactor=2.0;
	
	@Column(name="note")
	private String notes;
	
	@Column(name="alert_status")
	protected Integer alertStatus=1;
	
	@Transient
	private Date validFromTemp;
	
	@Transient
	private Date validToTemp;
	
	@Transient
   private String rateStatus;
	
	@Transient
	public String getRateStatus() {
		return rateStatus;
	}

	@Transient
	public void setRateStatus(String rateStatus) {
		this.rateStatus = rateStatus;
	}

	public Integer getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(Integer alertStatus) {
		this.alertStatus = alertStatus;
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
	

	public Date getValidToTemp() {
		return validToTemp;
	}


	public void setValidFromTemp(Date validFromTemp) {
		this.validFromTemp = validFromTemp;
	}
	

	public void setValidToTemp(Date validToTemp) {
		this.validToTemp = validToTemp;
	}

	public Double getPayRate() {
		return payRate;
	}

	public void setPayRate(Double payRate) {
		this.payRate = payRate;
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

	public Location getTransferStation() {
		return transferStation;
	}

	public void setTransferStation(Location transferStation) {
		this.transferStation = transferStation;
	}

	public Location getLandfill() {
		return landfill;
	}

	public void setLandfill(Location landfill) {
		this.landfill = landfill;
	}

	
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Integer getRateType() {
		return rateType;
	}

	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}

	public Integer getRateUsing() {
		return rateUsing;
	}

	public void setRateUsing(Integer rateUsing) {
		this.rateUsing = rateUsing;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	
	public Double getNightPayRate() {
		return nightPayRate;
	}
	
	public void setNightPayRate(Double nightPayRate) {
		this.nightPayRate = nightPayRate;
	}
	
	
	public Double getSundayRateFactor() {
		return sundayRateFactor;
	}
	
	public void setSundayRateFactor(Double sundayRateFactor) {
		this.sundayRateFactor = sundayRateFactor;
	}
	
	
	public Double getProbationRate() {
		return probationRate;
	}
	
	public void setProbationRate(Double probationRate) {
		this.probationRate = probationRate;
	}
	

}
