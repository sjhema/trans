package com.primovision.lutransport.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Customer;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.ReportModel;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="net_report_main")
public class NetReportMain extends AbstractBaseModel implements ReportModel {

	
	
	@Column(name="driver_company")
	protected Long driverCompany;
	
	
	@Column(name="company_location")
	protected Long company;
	
	
	@Column(name="vehicle")
	protected  Long vehicle;

	
	@Column(name="trailer")
	protected Long trailer;
	

	@Column(name = "terminal")
	protected Long terminal;
	
	
	@Column(name = "subcontractor_id")
	protected Long subcontractor;
	
	@Column(name = "orgin_id")
	protected Long origin;
	
	@Column(name = "destination_id")
	protected Long destination;
	
	@Column(name = "load_date")
	protected Date loadDate;
	
	@Column(name = "unload_date")
	protected Date unloadDate;
	
	@Column(name="driver")
	protected Long driver;

	
	@Column(name = "bill_batch")
	protected Date billBatch;
	
	
	@Column(name = "subcontractor_voucherDate")
	protected Date subContractorVoucherDate;
	
	
	@Column(name="ticket_id")
	private Long ticketID;
	
	
	@Column(name="ticket_total_amount")
	private Double ticketTotAmt = 0.0;
	
	
	
	@Column(name="fuel_amount")
	protected Double fuelamount=0.0;
	
	
	@Column(name="toll_amount")
	protected Double tollamount=0.0;
	
	
	@Column(name="transportation_amount")
	protected Double transportationAmount=0.0;
	
	
	@Column(name="subcontractor_total_amount")
	private Double subcontractorTotAmt = 0.0;
	
	
	@Column(name="submisc_amount")
	private String subMiscAmt ;

	
	@Column(name="")
	private String fuelLogIDs ;
	
	
	@Column(name="")
	private String tollTagIDs ;
	
	
	@Column(name="subcontractorvoucher_number")
	private String subcontractorVoucherNumber;
	
	

	public Long getTicketID() {
		return ticketID;
	}


	public void setTicketID(Long ticketID) {
		this.ticketID = ticketID;
	}


	public Double getTicketTotAmt() {
		return ticketTotAmt;
	}


	public void setTicketTotAmt(Double ticketTotAmt) {
		this.ticketTotAmt = ticketTotAmt;
	}


	public Double getFuelamount() {
		return fuelamount;
	}


	public void setFuelamount(Double fuelamount) {
		this.fuelamount = fuelamount;
	}


	public Double getTollamount() {
		return tollamount;
	}


	public void setTollamount(Double tollamount) {
		this.tollamount = tollamount;
	}


	public Double getSubcontractorTotAmt() {
		return subcontractorTotAmt;
	}


	public void setSubcontractorTotAmt(Double subcontractorTotAmt) {
		this.subcontractorTotAmt = subcontractorTotAmt;
	}


	public String getSubMiscAmt() {
		return subMiscAmt;
	}
	
	public void setSubMiscAmt(String subMiscAmt) {
		this.subMiscAmt = subMiscAmt;
	}


	public Long getDriverCompany() {
		return driverCompany;
	}


	public void setDriverCompany(Long driverCompany) {
		this.driverCompany = driverCompany;
	}


	public Long getCompany() {
		return company;
	}


	public void setCompany(Long company) {
		this.company = company;
	}


	public Long getVehicle() {
		return vehicle;
	}


	public void setVehicle(Long vehicle) {
		this.vehicle = vehicle;
	}


	public Long getTrailer() {
		return trailer;
	}


	public void setTrailer(Long trailer) {
		this.trailer = trailer;
	}


	public Long getTerminal() {
		return terminal;
	}


	public void setTerminal(Long terminal) {
		this.terminal = terminal;
	}


	public Long getSubcontractor() {
		return subcontractor;
	}


	public void setSubcontractor(Long subcontractor) {
		this.subcontractor = subcontractor;
	}


	public Long getDriver() {
		return driver;
	}


	public void setDriver(Long driver) {
		this.driver = driver;
	}


	public Date getBillBatch() {
		return billBatch;
	}


	public void setBillBatch(Date billBatch) {
		this.billBatch = billBatch;
	}


	public Date getSubContractorVoucherDate() {
		return subContractorVoucherDate;
	}
	
	public void setSubContractorVoucherDate(Date subContractorVoucherDate) {
		this.subContractorVoucherDate = subContractorVoucherDate;
	}
	
	
	public String getSubcontractorVoucherNumber() {
		return subcontractorVoucherNumber;
	}
	
	public void setSubcontractorVoucherNumber(String subcontractorVoucherNumber) {
		this.subcontractorVoucherNumber = subcontractorVoucherNumber;
	}
	
	
	public String getFuelLogIDs() {
		return fuelLogIDs;
	}	
	
	public String getTollTagIDs() {
		return tollTagIDs;
	}
	
	public void setFuelLogIDs(String fuelLogIDs) {
		this.fuelLogIDs = fuelLogIDs;
	}
	
	public void setTollTagIDs(String tollTagIDs) {
		this.tollTagIDs = tollTagIDs;
	}


	public Long getOrigin() {
		return origin;
	}


	public void setOrigin(Long origin) {
		this.origin = origin;
	}


	public Long getDestination() {
		return destination;
	}


	public void setDestination(Long destination) {
		this.destination = destination;
	}


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
	
	public Double getTransportationAmount() {
		return transportationAmount;
	}

	public void setTransportationAmount(Double transportationAmount) {
		this.transportationAmount = transportationAmount;
	}
	
	
	
}
