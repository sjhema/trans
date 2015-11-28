package com.primovision.lutransport.model.driver;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="driver_tripsheet")
public class TripSheet extends AbstractBaseModel{

	@NotNull
	@Column(name = "load_date")
	protected Date loadDate;
	
	@NotNull
	@Column(name = "unload_date")
	protected Date unloadDate;
	
	@Column(name="entered_by")
	protected String enteredBy;
	
	
	@ManyToOne
	@JoinColumn(name="origin")
	protected Location origin;

	
	@Column(name = "origin_ticket")
	protected Long originTicket;
	
	
	@Column(name = "pay_rate")
	protected Double payRate;
	
	@ManyToOne
	@JoinColumn(name="destination")
	protected Location destination;

	
	@Column(name = "destination_ticket")
	protected Long destinationTicket;
	
	
	@ManyToOne
	@JoinColumn(name="truck")
	protected Vehicle truck;

	@ManyToOne
	@JoinColumn(name="trailer")
	protected Vehicle trailer;
	
	
	protected String tempTrailer;
	
	
	protected String tempTruck;
	
	@Transient
	protected String tempBatchDate;
	
	@Transient
	protected String temploadDate;
	
	@Transient
	protected String tempunloadDate;
	
	@Transient
	protected String tempDriverName;
	
	@Transient
	protected String tempOriginName;
	
	@Transient
	protected String tempDestName;
	
	
	
	@ManyToOne
	@JoinColumn(name="driver")
	protected Driver driver;
	
	@NotNull
	@Column(name = "batch_date")
	protected Date batchDate;
	
	@Column(name="incomplete")
	protected String incomplete="No";
	
	@Column(name="verification_status")
	protected String verificationStatus;
	
	
	@ManyToOne
	@JoinColumn(name="driver_company")
	protected Location driverCompany;
	
	
	@ManyToOne
	@JoinColumn(name="terminal")
	protected Location terminal;
	
	String companyName;
	
	String terminalName;
	

	public Date getLoadDate() {
		return loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}

	public Date getUnloadDate() {
		return unloadDate;
	}

	public void setUnloadDate(Date unloadDate) {
		this.unloadDate = unloadDate;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Long getOriginTicket() {
		return originTicket;
	}

	public void setOriginTicket(Long originTicket) {
		this.originTicket = originTicket;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public Long getDestinationTicket() {
		return destinationTicket;
	}

	public void setDestinationTicket(Long destinationTicket) {
		this.destinationTicket = destinationTicket;
	}

	public Vehicle getTruck() {
		return truck;
	}

	public void setTruck(Vehicle truck) {
		this.truck = truck;
	}

	public Vehicle getTrailer() {
		return trailer;
	}

	public void setTrailer(Vehicle trailer) {
		this.trailer = trailer;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Date getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}
	
	
	public String getIncomplete() {
		return incomplete;
	}
	
	public void setIncomplete(String incomplete) {
		this.incomplete = incomplete;
	}
	
	
	public void setTempTrailer(String tempTrailer) {
		this.tempTrailer = tempTrailer;
	}
	
	public String getTempTrailer() {
		return tempTrailer;
	}
	
	
	public String getTempTruck() {
		return tempTruck;
	}
	
	public void setTempTruck(String tempTruck) {
		this.tempTruck = tempTruck;
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
	
	public String getVerificationStatus() {
		return verificationStatus;
	}
	
	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	
	public String getTerminalName() {
		return terminalName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTempBatchDate() {
		return tempBatchDate;
	}

	public void setTempBatchDate(String tempBatchDate) {
		this.tempBatchDate = tempBatchDate;
	}

	public String getTemploadDate() {
		return temploadDate;
	}

	public void setTemploadDate(String temploadDate) {
		this.temploadDate = temploadDate;
	}

	public String getTempunloadDate() {
		return tempunloadDate;
	}

	public void setTempunloadDate(String tempunloadDate) {
		this.tempunloadDate = tempunloadDate;
	}

	public String getTempDriverName() {
		return tempDriverName;
	}

	public void setTempDriverName(String tempDriverName) {
		this.tempDriverName = tempDriverName;
	}

	public String getTempOriginName() {
		return tempOriginName;
	}

	public void setTempOriginName(String tempOriginName) {
		this.tempOriginName = tempOriginName;
	}

	public String getTempDestName() {
		return tempDestName;
	}

	public void setTempDestName(String tempDestName) {
		this.tempDestName = tempDestName;
	}
	
	public Double getPayRate() {
		return payRate;
	}
	
	public void setPayRate(Double payRate) {
		this.payRate = payRate;
	}
	
	public String getEnteredBy() {
		return enteredBy;
	}
	
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
}
