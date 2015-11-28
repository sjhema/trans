package com.primovision.lutransport.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.ReportModel;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Ticket;

/**
 * @author kishan
 *
 */
@Entity
@Table(name="subcontractor_invoice_detail_new")
public class SubcontractorBillingNew extends AbstractBaseModel implements ReportModel {
	String sub_origin = "";
	String sub_destination = "";
	String date;
	
	String sub_unit;
	
	@Column(name="trailer_num")
	private String trailer;
	
	@Column(name="voucher_number")
	String invoiceNo;
	
	String loaded;	
	
	String unloaded;
	
	@Column(name="unload_date")
	protected Date unloadDate;
	
	@Column(name="origin_ticket")
	String originTicket;
	
	@Column(name="destination_ticket")
	String destinationTicket;
	
	@Column(name="minimum_billablewt")
	Double minimumbillablegrossweight=0.0;
	
	@Column(name="origin_gross")
	Double originGrossWt = 0.0;
	
	/*@Column(name="origin_tare")*/
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
	
	Double subcontrate = 0.0;
	
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
	
	@Column(name="entered_by")
	private String enteredBy;	
	
	@Column(name="billUsing")
	private String billUsing;
	
	
	
	@Column(name="transferTimeIn")
	private String transferTimeIn;
	
	@Column(name="transferTimeOut")
	private String transferTimeOut;
	
	@Column(name="landfillTimeIn")
	private String landfillTimeIn;
	
	@Column(name="landfillTimeOut")
	private String landfillTimeOut;
	
	@Column(name="processStatus")
	private String processStatus;
	
	@Column(name="subcontractorname")
	private String subcontractor;
	
	@Column(name="invoicedate")
	private String invoiceDate;
	
	@Column(name="companyname")
	private String company;
	
	@Column(name="other_charges")
	private Double otherCharges=0.0;
	
	@Column(name="miscelleneouscharges")
	private String miscelleneousCharges;
	
	
	@Column(name="company_location")
	private long companyLocationId;
	
	@Column(name="driver_companylocation")
	private long driverCompanyId;
	
	
	public long getDriverCompanyId() {
		return driverCompanyId;
	}
	
	public void setDriverCompanyId(long driverCompanyId) {
		this.driverCompanyId = driverCompanyId;
	}
	

	public void setCompanyLocationId(long companyLocationId) {
		this.companyLocationId = companyLocationId;
	}
	
	public long getCompanyLocationId() {
		return companyLocationId;
	}
	
	public Double getOtherCharges() {
		return otherCharges;
	}
	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}
	
	@Column(name="tickt_id")
	private long ticket;
	
	@Column(name="subcntractor_id")
	private long subcontractorId;
	
	
	

	
	public long getSubcontractorId() {
		return subcontractorId;
	}
	
	public void setSubcontractorId(long subcontractorId) {
		this.subcontractorId = subcontractorId;
	}
	
	
	
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getLoaded() {
		return loaded;
	}
	public void setLoaded(String loaded) {
		this.loaded = loaded;
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
	public void setTicket(long ticket) {
		this.ticket = ticket;
	}
	
	public long getTicket() {
		return ticket;
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

	public String getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(String subcontractor) {
		this.subcontractor = subcontractor;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMiscelleneousCharges() {
		return miscelleneousCharges;
	}

	public void setMiscelleneousCharges(String miscelleneousCharges) {
		this.miscelleneousCharges = miscelleneousCharges;
	}

	public String getSub_origin() {
		return sub_origin;
	}

	public void setSub_origin(String sub_origin) {
		this.sub_origin = sub_origin;
	}

	public String getSub_destination() {
		return sub_destination;
	}

	public void setSub_destination(String sub_destination) {
		this.sub_destination = sub_destination;
	}

	public String getSub_unit() {
		return sub_unit;
	}

	public void setSub_unit(String sub_unit) {
		this.sub_unit = sub_unit;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Double getSubcontrate() {
		return subcontrate;
	}

	public void setSubcontrate(Double subcontrate) {
		this.subcontrate = subcontrate;
	}

	public String getBillUsing() {
		return billUsing;
	}

	public void setBillUsing(String billUsing) {
		this.billUsing = billUsing;
	}
	
	
	public String getTrailer() {
		return trailer;
	}
	
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	
	public Date getUnloadDate() {
		return unloadDate;
	}
	
	public void setUnloadDate(Date unloadDate) {
		this.unloadDate = unloadDate;
	}
	
}

