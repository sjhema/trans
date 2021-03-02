package com.primovision.lutransport.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Customer;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.ReportModel;
import com.primovision.lutransport.model.Ticket;

@Entity
@Table(name="invoice_detail_new")
public class Billing_New extends AbstractBaseModel implements ReportModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String t_origin = "";
	String t_destination = "";
	String bill_date;
	
	
	@Column(name="bill_invoice_number")
	String invoiceNo;
	String loaded;
	String t_unit;
	String unloaded;
	
	
	@Column(name="unload_date")
	protected Date unloadDate;
	
	@Column(name="driver_pay_rate")
	Double driverPayRate = 0.0;
	
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
	
	// Peak rate 2nd Feb 2021
	@Column(name="is_peak_rate")
	String isPeakRate = "N";
	
	@Column(name="fuel_surcharge")
	Double fuelSurcharge = 0.0;
	
	@Column(name="tonnage_premium")
	Double tonnagePremium = 0.0;
	
	@Column(name="demmurage_charge")
	Double demurrageCharge = 0.0;
	
	@Column(name="total_amount")
	Double totAmt = 0.0;
	
	@Column(name="t_driver")
	private String driver;
	
	@Column(name="t_terminal")
	private String terminal;
	
	@Column(name="t_enteredby")
	private String enteredBy;
	
	@Column(name="billing_using")
	private String billUsing;
	
	@Column(name="transfer_time_in")
	private String transferTimeIn;
	
	@Column(name="transfer_time_out")
	private String transferTimeOut;
	
	@Column(name="landfill_time_in")
	private String landfillTimeIn;
	
	@Column(name="landfill_time_out")
	private String landfillTimeOut;
	
	@Column(name="process_status")
	private String processStatus;
	
	@Column(name="tcompany")
	private String company;	
	
	
	
	
	
	@Column(name="bill_subcontractor")
	private String subcontractor;
	
	@Column(name="trailer_num")
	private String trailer;
	
	@Column(name="bill_customer_name")
	private String customer;
	
	@Column(name="invoiced_date")
	private String invoiceDate;
	
	
	
	
	
	@Column(name="driver_companylocid")
	private long driverCompanyId;
	
	
	@Column(name="driver_companylocname")
	private String driverCompanyName;
	
	
	public void setDriverCompanyId(long driverCompanyId) {
		this.driverCompanyId = driverCompanyId;
	}
	
	public long getDriverCompanyId() {
		return driverCompanyId;
	}
	
	
	
	public String getDriverCompanyName() {
		return driverCompanyName;
	}
	
	public void setDriverCompanyName(String driverCompanyName) {
		this.driverCompanyName = driverCompanyName;
	}
	
	@Transient
	private String notBillable;
	
	@Transient
	public String getNotBillable() {
		return notBillable;
	}

	@Transient
	public void setNotBillable(String notBillable) {
		this.notBillable = notBillable;
	}

	/*@Transient
	private Double gallon;*/
	@Column(name="gallon_num")
	private Double gallon=0.0;		
	
	
	@Column(name="tickt_id")
	long ticket;


	public String getT_origin() {
		return t_origin;
	}


	public void setT_origin(String t_origin) {
		this.t_origin = t_origin;
	}


	public String getT_destination() {
		return t_destination;
	}


	public void setT_destination(String t_destination) {
		this.t_destination = t_destination;
	}


	public String getBill_date() {
		return bill_date;
	}


	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
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


	public String getT_unit() {
		return t_unit;
	}


	public void setT_unit(String t_unit) {
		this.t_unit = t_unit;
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


	public Double getMinimumbillablegrossweight() {
		return minimumbillablegrossweight;
	}


	public void setMinimumbillablegrossweight(Double minimumbillablegrossweight) {
		this.minimumbillablegrossweight = minimumbillablegrossweight;
	}


	public Double getOriginGrossWt() {
		return originGrossWt;
	}


	public void setOriginGrossWt(Double originGrossWt) {
		this.originGrossWt = originGrossWt;
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


	public Double getGallon() {
		return gallon;
	}


	public void setGallon(Double gallon) {
		this.gallon = gallon;
	}


	public long getTicket() {
		return ticket;
	}


	public void setTicket(long ticket) {
		this.ticket = ticket;
	}


	
	
	
	public Double getDriverPayRate() {
		return driverPayRate;
	}
	
	public void setDriverPayRate(Double driverPayRate) {
		this.driverPayRate = driverPayRate;
	}
	
	
	public Date getUnloadDate() {
		return unloadDate;
	}
	
	public void setUnloadDate(Date unloadDate) {
		this.unloadDate = unloadDate;
	}

	public String getIsPeakRate() {
		return isPeakRate;
	}

	public void setIsPeakRate(String isPeakRate) {
		this.isPeakRate = isPeakRate;
	}
}