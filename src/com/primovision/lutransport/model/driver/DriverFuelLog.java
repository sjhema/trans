package com.primovision.lutransport.model.driver;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="driver_fuellog")
public class DriverFuelLog extends AbstractBaseModel{

	@ManyToOne
	@JoinColumn(name="truck")
	protected Vehicle truck;
	
	@NotNull
	@Column(name = "transaction_date")
	protected Date transactionDate;	
	
	@NotNull
	@Column(name="gallons")
	protected Double gallons;
	
	@ManyToOne
	@JoinColumn(name="driver_fuelcard")
	protected DriverFuelCard driverFuelCard;
	
	@ManyToOne
	@JoinColumn(name="fuelcard")
	protected FuelCard fuelCard;
	
	
	@Column(name = "batch_date")
	protected Date batchDate;
	
	@Column(name="entered_by")
	protected String enteredBy;
	
	
	@ManyToOne
	@JoinColumn(name="driver")
	protected Driver driver;
	
	
	protected String tempTruck;
	
	protected String tempTransactionDate;
	
	protected String tempFuelCardNum;
	
	protected String driverName;
	
	
	@ManyToOne
	@JoinColumn(name="driver_company")
	protected Location driverCompany;
	
	String companyName;
	
	String terminalName;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	protected Location terminal;

	public Vehicle getTruck() {
		return truck;
	}

	public void setTruck(Vehicle truck) {
		this.truck = truck;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getGallons() {
		return gallons;
	}

	public void setGallons(Double gallons) {
		this.gallons = gallons;
	}

	public DriverFuelCard getDriverFuelCard() {
		return driverFuelCard;
	}

	public void setDriverFuelCard(DriverFuelCard driverFuelCard) {
		this.driverFuelCard = driverFuelCard;
	}

	public FuelCard getFuelCard() {
		return fuelCard;
	}

	public void setFuelCard(FuelCard fuelCard) {
		this.fuelCard = fuelCard;
	}

	public Date getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getTempTruck() {
		return tempTruck;
	}

	public void setTempTruck(String tempTruck) {
		this.tempTruck = tempTruck;
	}

	public String getTempTransactionDate() {
		return tempTransactionDate;
	}

	public void setTempTransactionDate(String tempTransactionDate) {
		this.tempTransactionDate = tempTransactionDate;
	}

	public String getTempFuelCardNum() {
		return tempFuelCardNum;
	}

	public void setTempFuelCardNum(String tempFuelCardNum) {
		this.tempFuelCardNum = tempFuelCardNum;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public Location getDriverCompany() {
		return driverCompany;
	}

	public void setDriverCompany(Location driverCompany) {
		this.driverCompany = driverCompany;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}
	
	public String getEnteredBy() {
		return enteredBy;
	}
	
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	

	}
