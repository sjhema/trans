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
@Table(name = "ez_toll")
public class EzToll extends AbstractBaseModel
{
	@ManyToOne
	@JoinColumn(name = "tool_company")
    protected TollCompany toolcompany;
	
	@ManyToOne
	@JoinColumn(name = "company")
    protected Location company;
	
	@ManyToOne
	@JoinColumn(name = "terminal")
	protected Location terminal;
	
	@ManyToOne
    @JoinColumn(name = "tolltag_number")
    protected VehicleTollTag tollTagNumber;
	
	@ManyToOne
    @JoinColumn(name = "plate_number")
    protected Vehicle plateNumber;
	
	@NotNull
	@Column(name="transaction_date")
	protected Date transactiondate;
	
	@Column(name = "unitNum")
	protected String unitNum;
	
	@Column(name = "driver_fullname")
	protected String driverFullName;
	
	
	@NotEmpty
	@Column(name = "transaction_time")
	protected String transactiontime;
	
	@NotEmpty
	@Column(name = "Agency")
	protected String agency;
	
	@NotNull
	@Column(name="amount")
	protected Double amount;
	
	@Column(name="invoice_date")
	protected Date invoiceDate;

	@Transient
	private String tollcompanies;
	
	@Transient
	private String companies;
	
	@Transient
	private String units;
	
	@Transient
	private String tollTagNumbers;
	
	@Transient
	private String terminals;
	
	@Transient
	private String plates;
	
	@Transient
	private String transfersDate;
	
	@ManyToOne
	@JoinColumn(name="driver")
	protected Driver driver;
	
	@ManyToOne
	@JoinColumn(name="unit")
	protected Vehicle unit;
	
	@Transient
	private String drivername="";
	
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	public String getPlates() {
		return plates;
	}

	public void setPlates(String plates) {
		this.plates = plates;
	}

	public String getTollTagNumbers() {		
		return tollTagNumbers;
	}

	public void setTollTagNumbers(String tollTagNumbers) {
		this.tollTagNumbers = tollTagNumbers;
	}

	public String getTollcompanies() {
		return tollcompanies;
	}

	public void setTollcompanies(String tollcompanies) {
		this.tollcompanies = tollcompanies;
	}

	public String getCompanies() {
		return companies;
	}

	public void setCompanies(String companies) {
		this.companies = companies;
	}

	public String getTerminals() {
		return terminals;
	}

	public void setTerminals(String terminals) {
		this.terminals = terminals;
	}

	public String getTransfersDate() {
		return transfersDate;
	}

	public void setTransfersDate(String transfersDate) {
		this.transfersDate = transfersDate;
	}

	public TollCompany getToolcompany() {
		return toolcompany;
	}

	public void setToolcompany(TollCompany toolcompany) {
		this.toolcompany = toolcompany;
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

	public VehicleTollTag getTollTagNumber() {		
		return tollTagNumber;
	}

	public void setTollTagNumber(VehicleTollTag tollTagNumber) {
		this.tollTagNumber = tollTagNumber;
	}	
	
	public Vehicle getPlateNumber() {		
		return plateNumber;
	}

	public void setPlateNumber(Vehicle plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Date getTransactiondate() {
		return transactiondate;
	}

	public void setTransactiondate(Date transactiondate) {
		this.transactiondate = transactiondate;
	}

	public String getTransactiontime() {
		return transactiontime;
	}

	public void setTransactiontime(String transactiontime) {
		this.transactiontime = transactiontime;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	public String getUnits() {
		return units;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Vehicle getUnit() {
		return unit;
	}

	public void setUnit(Vehicle unit) {
		this.unit = unit;
	}

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}
	
	public String getDriverFullName() {
		return driverFullName;
	}
	
	public String getUnitNum() {
		return unitNum;
	}
	
	
	public void setDriverFullName(String driverFullName) {
		this.driverFullName = driverFullName;
	}
	
	
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EzToll other = (EzToll) obj;
		
		
		
		if(other.getToolcompany().equals(this.getToolcompany()) && other.getCompany().equals(this.getCompany()) && 
				other.getTerminal().equals(this.getTerminal()) 
				&& other.getTollTagNumber().equals(this.getTollTagNumber()) && other.getPlateNumber().equals(this.getPlateNumber()) && 
				other.getDriver().equals(this.getDriver()) && other.getTransactiondate().equals(this.getTransactiondate()) &&
				other.getTransactiontime().equalsIgnoreCase(this.getTransactiontime())&&
				other.getAgency().equalsIgnoreCase(this.getAgency()) && other.getAmount().equals(this.getAmount())) 
		{
			
			return true;
		}
		
		
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (getId().equals(other.getId())) { 
			return true;
		}
		return false;
	}
	
}
