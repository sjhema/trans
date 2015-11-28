package com.primovision.lutransport.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Customer;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.ReportModel;
import com.primovision.lutransport.model.Ticket;

@Entity
@Table(name="invoice_detail")
public class Billing extends AbstractBaseModel implements ReportModel {

	String origin = "";
	String destination = "";
	
	@Column(name="inv_date")
	String date;
	
	String companyname = "";
	
	@Column(name="invoice_number")
	String invoiceNo;
	String loaded;
	String unit;
	String unloaded;
	
	@NotNull
	@Column(name = "invoice_date")
	private String invoice_Date;
	
	
	@Column(name="origin_ticket")
	String originTicket;
	
	@Column(name="destination_ticket")
	String destinationTicket;
	
	@Column(name="minimum_billablewt")
	Double minimumbillablegrossweight=0.0;
	
	@Column(name="origin_gross")
	Double originGrossWt = 0.0;
	
	@Column(name="origin_tare")
	Double originTareWt = 0.0;
	
	@Column(name="origin_net")
	Double originNetWt = 0.0;
	
	@Column(name="origin_tons")
	Double originTonsWt = 0.0;
	
	@Column(name="dest_gross")
	Double destinationGrossWt = 0.0;
	
	@Column(name="dest_tare")
	Double destinationTareWt = 0.0;
	
	@Column(name="dest_net")
	Double destinationNetWt = 0.0;
	
	@Column(name="dest_tons")
	Double destinationTonsWt = 0.0;
	
	@Column(name="bill_gross")
	Double effectiveGrossWt = 0.0;
	
	@Column(name="bill_tare")
	Double effectiveTareWt = 0.0;
	
	@Column(name="bill_net")
	Double effectiveNetWt = 0.0;
	
	@Column(name="bill_tons")
	Double effectiveTonsWt = 0.0;
	
	Double rate = 0.0;
	Double amount=0.0;
	
	@Column(name="fuel_surcharge")
	Double fuelSurcharge = 0.0;
	
	@Column(name="tonnage_premium")
	Double tonnagePremium = 0.0;
	
	@Column(name="demmurage_charge")
	Double demurrageCharge = 0.0;
	
	@Column(name="total_amount")
	Double totAmt = 0.0;
	
	@Column(name="driver")
	private String driver;
	
	@Column(name="terminal")
	private String terminal;
	
	@Transient
	private String enteredBy;
	
	@Transient
	private String billUsing;
	
	@Transient
	private String transferTimeIn;
	
	@Transient
	private String transferTimeOut;
	
	@Transient
	private String landfillTimeIn;
	
	@Transient
	private String landfillTimeOut;
	
	@Transient
	private String processStatus;
	
	@Transient
	private String company;
	
	
	@ManyToOne
	@JoinColumn(name="company_loc")
	private Location companyId;
	
	@ManyToOne
	@JoinColumn(name="driver_companyid")
	private Location driverCompany;
	
	@Column(name="driver_CompanyName")
	private String driverCompanyName;
	
	
	@ManyToOne
	@JoinColumn(name = "custmr_id")
	private Customer customerId;
	
	
	@Transient
	private String subcontractor;
	
	@Transient
	private String trailer;
	
	@Transient
	private String customer;
	
	@Transient
	private String invoiceDate;
	
	/*@Transient
	private Double gallon;*/
	@Column(name="gallon")
	private Double gallon=0.0;
		
	
	public Location getDriverCompany() {
		return driverCompany;
	}
	
	
	public void setDriverCompany(Location driverCompany) {
		this.driverCompany = driverCompany;
	}
	
	
	
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public Double getGallon() {
		return gallon;
	}
	public void setGallon(Double gallon) {
		this.gallon = gallon;
	}
	public Location getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Location companyId) {
		this.companyId = companyId;
	}
	public Customer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Customer customerId) {
		this.customerId = customerId;
	}
	
	
	public String getDriverCompanyName() {
		return driverCompanyName;
	}
	
	
	public void setDriverCompanyName(String driverCompanyName) {
		this.driverCompanyName = driverCompanyName;
	}
	
	
	@ManyToOne
	@JoinColumn(name="ticket_id")
	Ticket ticket;

	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getLoaded() {
		return loaded;
	}
	public void setLoaded(String loaded) {
		this.loaded = loaded;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnloaded() {
		return unloaded;
	}
	public void setUnloaded(String unloaded) {
		this.unloaded = unloaded;
	}
	public String getOriginTicket() {
		return originTicket;
	}
	public void setOriginTicket(String originTicket) {
		this.originTicket = originTicket;
	}
	public String getDestinationTicket() {
		return destinationTicket;
	}
	public void setDestinationTicket(String destinationTicket) {
		this.destinationTicket = destinationTicket;
	}
	public Double getOriginGrossWt() {
		return originGrossWt;
	}
	public void setOriginGrossWt(Double originGrossWt) {
		this.originGrossWt = originGrossWt;
	}
	public Double getMinimumbillablegrossweight() {
		return minimumbillablegrossweight;
	}
	public void setMinimumbillablegrossweight(Double minimumbillablegrossweight) {
		this.minimumbillablegrossweight = minimumbillablegrossweight;
	}
	public Double getOriginTareWt() {
		return originTareWt;
	}
	public void setOriginTareWt(Double originTareWt) {
		this.originTareWt = originTareWt;
	}
	public Double getOriginNetWt() {
		return originNetWt;
	}
	public void setOriginNetWt(Double originNetWt) {
		this.originNetWt = originNetWt;
	}
	public Double getOriginTonsWt() {
		return originTonsWt;
	}
	public void setOriginTonsWt(Double originTonsWt) {
		this.originTonsWt = originTonsWt;
	}
	
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getFuelSurcharge() {
		return fuelSurcharge;
	}
	public void setFuelSurcharge(Double fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}
	public Double getTonnagePremium() {
		return tonnagePremium;
	}
	public void setTonnagePremium(Double tonnagePremium) {
		this.tonnagePremium = tonnagePremium;
	}
	public Double getDemurrageCharge() {
		return demurrageCharge;
	}
	public void setDemurrageCharge(Double demurrageCharge) {
		this.demurrageCharge = demurrageCharge;
	}
	public Double getTotAmt() {
		return totAmt;
	}
	public void setTotAmt(Double totAmt) {
		this.totAmt = totAmt;
	}
	public Ticket getTicket() {
		return ticket;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	public Double getDestinationGrossWt() {
		return destinationGrossWt;
	}
	public void setDestinationGrossWt(Double destinationGrossWt) {
		this.destinationGrossWt = destinationGrossWt;
	}
	public Double getDestinationTareWt() {
		return destinationTareWt;
	}
	public void setDestinationTareWt(Double destinationTareWt) {
		this.destinationTareWt = destinationTareWt;
	}
	public Double getDestinationNetWt() {
		return destinationNetWt;
	}
	public void setDestinationNetWt(Double destinationNetWt) {
		this.destinationNetWt = destinationNetWt;
	}
	public Double getDestinationTonsWt() {
		return destinationTonsWt;
	}
	public void setDestinationTonsWt(Double destinationTonsWt) {
		this.destinationTonsWt = destinationTonsWt;
	}
	public Double getEffectiveGrossWt() {
		return effectiveGrossWt;
	}
	public void setEffectiveGrossWt(Double effectiveGrossWt) {
		this.effectiveGrossWt = effectiveGrossWt;
	}
	public Double getEffectiveTareWt() {
		return effectiveTareWt;
	}
	public void setEffectiveTareWt(Double effectiveTareWt) {
		this.effectiveTareWt = effectiveTareWt;
	}
	public Double getEffectiveNetWt() {
		return effectiveNetWt;
	}
	public void setEffectiveNetWt(Double effectiveNetWt) {
		this.effectiveNetWt = effectiveNetWt;
	}
	public Double getEffectiveTonsWt() {
		return effectiveTonsWt;
	}
	public void setEffectiveTonsWt(Double effectiveTonsWt) {
		this.effectiveTonsWt = effectiveTonsWt;
	}
	public String getBillUsing() {
		return billUsing;
	}
	public void setBillUsing(String billUsing) {
		this.billUsing = billUsing;
	}
	public String getTransferTimeIn() {
		return transferTimeIn;
	}
	public void setTransferTimeIn(String transferTimeIn) {
		this.transferTimeIn = transferTimeIn;
	}
	public String getTransferTimeOut() {
		return transferTimeOut;
	}
	public void setTransferTimeOut(String transferTimeOut) {
		this.transferTimeOut = transferTimeOut;
	}
	public String getLandfillTimeIn() {
		return landfillTimeIn;
	}
	public void setLandfillTimeIn(String landfillTimeIn) {
		this.landfillTimeIn = landfillTimeIn;
	}
	public String getLandfillTimeOut() {
		return landfillTimeOut;
	}
	public void setLandfillTimeOut(String landfillTimeOut) {
		this.landfillTimeOut = landfillTimeOut;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSubcontractor() {
		return subcontractor;
	}
	public void setSubcontractor(String subcontractor) {
		this.subcontractor = subcontractor;
	}
	public String getTrailer() {
		return trailer;
	}
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	public void setInvoice_Date(String invoice_Date) {
		this.invoice_Date = invoice_Date;
	}
	
	public String getInvoice_Date() {
		return invoice_Date;
	}
	
	
}
