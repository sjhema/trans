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
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;


@Entity
@Table(name="driver_odometer")
public class Odometer extends AbstractBaseModel{
	
	@ManyToOne
	@JoinColumn(name="truck")
	protected Vehicle truck;
	
	@NotNull
	@Column(name = "record_date")
	protected Date recordDate;	
	
	@Column(name="entered_by")
	protected String enteredBy;
	
	
	@Column(name = "batch_date")
	protected Date batchDate;
	
	@Column(name="start_reading")
	protected Integer startReading;	
	
	@Column(name="end_reading")
	protected Integer endReading;	
	
	@Column(name="miles")
	protected Integer miles;
	
	
	@ManyToOne
	@JoinColumn(name="driver")
	protected Driver driver;
	
	
	protected String tempTruck;
	
	protected String tempDriverName;
	
	protected String tempRecordDate;
	
	protected String inComplete="No";
	
	
	@ManyToOne
	@JoinColumn(name="driver_company")
	protected Location driverCompany;
	
	
	@ManyToOne
	@JoinColumn(name="terminal")
	protected Location terminal;
	
	String companyName;
	
	String terminalName;


	public Vehicle getTruck() {
		return truck;
	}


	public void setTruck(Vehicle truck) {
		this.truck = truck;
	}


	public Date getRecordDate() {
		return recordDate;
	}


	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}


	


	public Integer getStartReading() {
		return startReading;
	}


	public void setStartReading(Integer startReading) {
		this.startReading = startReading;
	}


	public Integer getEndReading() {
		return endReading;
	}


	public void setEndReading(Integer endReading) {
		this.endReading = endReading;
	}


	public Integer getMiles() {
		return miles;
	}


	public void setMiles(Integer miles) {
		this.miles = miles;
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
	
	
	public void setInComplete(String inComplete) {
		this.inComplete = inComplete;
	}
	
	public String getInComplete() {
		return inComplete;
	}
	
	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}
	
	public Date getBatchDate() {
		return batchDate;
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
	
	public String getTempDriverName() {
		return tempDriverName;
	}
	
	public void setTempDriverName(String tempDriverName) {
		this.tempDriverName = tempDriverName;
	}
	
	public String getTempRecordDate() {
		return tempRecordDate;
	}
	
	public void setTempRecordDate(String tempRecordDate) {
		this.tempRecordDate = tempRecordDate;
	}
	
	public String getEnteredBy() {
		return enteredBy;
	}
	
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
}
