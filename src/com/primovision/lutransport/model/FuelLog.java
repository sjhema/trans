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

/**
 * @author Ravi
 * 
 */

@Entity
@Table(name = "fuel_log")
public class FuelLog extends AbstractBaseModel implements ReportModel{

	private static final long serialVersionUID = -7960731852472127475L;
	
	@ManyToOne
	@JoinColumn(name="fuel_vendor")
	protected FuelVendor fuelvendor;
	
	@ManyToOne
	@JoinColumn(name="driver_id")
	protected Driver driversid;
	
	@Column(name = "unitNum")
	protected String unitNum;
	
	@Column(name = "driver_fullname")
	protected String driverFullName;
	
	@ManyToOne
	@JoinColumn(name="company")
	protected Location company;
	
	@ManyToOne
	@JoinColumn(name = "terminal")
	protected Location terminal;
	
	@ManyToOne
	@JoinColumn(name="driver_firstName")
	protected Driver driverFname;
	
	@ManyToOne
	@JoinColumn(name="driver_lastName")
	protected Driver driverLname;
	
	@ManyToOne
	@JoinColumn(name="state")
	protected State state;
	
	@ManyToOne
	@JoinColumn(name="unit")
	protected Vehicle unit;
	
	@NotNull
	@Column(name="invoice_date")
	protected Date invoiceDate;
	
	@NotEmpty
	@Column(name="invoice_number")
	protected String invoiceNo;
	
	@NotNull
	@Column(name="transaction_date")
	protected Date transactiondate;
	
	//@NotEmpty
	@Column(name = "transaction_time")
	protected String transactiontime;

	/*@NotEmpty
	@Column(name="fuel_card_number")
	protected String fuelCardNumber;*/
	
	@ManyToOne
	@JoinColumn(name="fuelcard")
	protected FuelCard fuelcard; 
	
	@NotEmpty
	@Column(name = "fuel_type")
	protected String fueltype;

	//@NotEmpty
	@Column(name = "city")
	protected String city;
	
	@Column(name = "fuel_violation")
	protected String fuelViolation;
	
	@NotNull
	@Column(name="gallons")
	protected Double gallons;
	
	@NotNull
	@Column(name="unit_price")
	protected Double unitprice;
	
	@NotNull
	@Column(name="fees")
	protected Double fees;
	
	@NotNull
	@Column(name="discounts")
	protected Double discounts;
	
	//@NotNull
	@Column(name="gross_cost")
	protected Double grosscost;
	
	@NotNull
	@Column(name="amount")
	protected Double amount;
   
	@Transient
	private String companies;
	
	@Transient
	private String states;
	
	@Transient
	private String fuelVenders;
	
	@Transient
	private String invoicedDate;
	
	@Transient
	private String units;
	
	@Transient
	private String terminals;
	
	
	public void setFuelViolation(String fuelViolation) {
		this.fuelViolation = fuelViolation;
	}
	
	public String getFuelViolation() {
		return fuelViolation;
	}
	
	
	
	public FuelCard getFuelcard() {
		return fuelcard;
	}

	public void setFuelcard(FuelCard fuelcard) {
		this.fuelcard = fuelcard;
	}

	public String getFuelVenders() {
		return fuelVenders;
	}

	public void setFuelVenders(String fuelVenders) {
		this.fuelVenders = fuelVenders;
	}

	@Transient
	private String drivers;
	
	@Transient
	private String fuelCardNumbers;
	
	@Transient
	private String transactionsDate;
	
	
	
	
	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public String getTransactionsDate() {
		return transactionsDate;
	}

	public void setTransactionsDate(String transactionsDate) {
		this.transactionsDate = transactionsDate;
	}

	public String getFuelCardNumbers() {
		return fuelCardNumbers;
	}

	public void setFuelCardNumbers(String fuelCardNumbers) {
		this.fuelCardNumbers = fuelCardNumbers;
	}

	public String getDrivers() {
		return drivers;
	}

	public void setDrivers(String drivers) {
		this.drivers = drivers;
	}

	public String getTerminals() {
		return terminals;
	}

	public void setTerminals(String terminals) {
		this.terminals = terminals;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getInvoicedDate() {
		return invoicedDate;
	}

	public void setInvoicedDate(String invoicedDate) {
		this.invoicedDate = invoicedDate;
	}

	public String getCompanies() {
		return companies;
	}

	public void setCompanies(String companies) {
		this.companies = companies;
	}
   
	
	
	

	public FuelVendor getFuelvendor() {
		return fuelvendor;
	}

	public void setFuelvendor(FuelVendor fuelvendor) {
		this.fuelvendor = fuelvendor;
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

	public Driver getDriverFname() {
		return driverFname;
	}

	public void setDriverFname(Driver driverFname) {
		this.driverFname = driverFname;
	}

	public Driver getDriverLname() {
		return driverLname;
	}

	public void setDriverLname(Driver driverLname) {
		this.driverLname = driverLname;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Vehicle getUnit() {
		return unit;
	}

	public void setUnit(Vehicle unit) {
		this.unit = unit;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

/*	public String getFuelCardNumber() {
		return fuelCardNumber;
	}

	public void setFuelCardNumber(String fuelCardNumber) {
		this.fuelCardNumber = fuelCardNumber;
	}*/

	public String getFueltype() {
		return fueltype;
	}

	public void setFueltype(String fueltype) {
		this.fueltype = fueltype;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Double getGallons() {
		return gallons;
	}

	public void setGallons(Double gallons) {
		this.gallons = gallons;
	}

	public Double getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Double unitprice) {
		this.unitprice = unitprice;
	}

	public Double getFees() {
		return fees;
	}

	public void setFees(Double fees) {
		this.fees = fees;
	}

	public Double getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Double discounts) {
		this.discounts = discounts;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Driver getDriversid() {
		return driversid;
	}

	public void setDriversid(Driver driversid) {
		this.driversid = driversid;
	}
	
	
	

	public Double getGrosscost() {
		return grosscost;
	}

	public void setGrosscost(Double grosscost) {
		this.grosscost = grosscost;
	}

	
public void setUnitNum(String unitNum) {
	this.unitNum = unitNum;
}


public void setDriverFullName(String driverFullName) {
	this.driverFullName = driverFullName;
}

public String getUnitNum() {
	return unitNum;
}


public String getDriverFullName() {
	return driverFullName;
}

	
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuelLog other = (FuelLog) obj;
			
		if(other.getFuelcard().equals(this.getFuelcard()) && other.getAmount().equals(this.getAmount()) && 
				other.getCity().equalsIgnoreCase(this.getCity()) &&	other.getCompany().equals(this.getCompany()) 
				&& other.getDiscounts().equals(this.getDiscounts()) && other.getDriversid().equals(this.getDriversid()) && 
				other.getFees().equals(this.getFees()) && other.getTransactiondate().equals(this.getTransactiondate()) &&
				other.getFueltype().equalsIgnoreCase(this.getFueltype())&&
				other.getGallons().equals(this.getGallons()) && other.getFuelvendor().equals(this.getFuelvendor()) &&
				other.getInvoiceDate().equals(this.getInvoiceDate()) && other.getInvoiceNo().equalsIgnoreCase(this.getInvoiceNo()) &&
				other.getState().equals(this.getState()) && other.getTerminal().equals(this.getTerminal()) &&
				other.getUnit().equals(this.getUnit()) && other.getUnitprice().equals(this.getUnitprice()) && 
				other.getTransactiontime().equals(this.getTransactiontime())) 
		{
			
			return true;
		}
		/*if (other.getOrigin()!=null && this.getOrigin()!=null 
				&& other.getDestination()!=null && this.getDestination()!=null 
				&& (other.getOrigin()==this.getOrigin()) 
				&& (other.getDestination()==this.getDestination()) 
				&& ((other.getOriginTicket().equals(this.getOriginTicket()) 
						|| (other.getDestinationTicket().equals(this.getDestinationTicket()))))) {
			return true;
		}*/
		
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (getId().equals(other.getId())) { 
			return true;
		}
		return false;
	}

}
