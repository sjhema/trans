package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "wm_ticket")
public class WMTicket extends AbstractBaseModel {
	@Transient
	public static String ORIGIN_TICKET_TYPE = "ORIGIN";
	@Transient
	public static String DESTINATION_TICKET_TYPE = "DESTINANTION";
	
	@Transient
	public static Integer PROCESSING_STATUS_TICKET_ALREADY_EXISTS = 1;
	@Transient
	public static Integer PROCESSING_STATUS_NO_TRIPSHEET = 2;
	@Transient
	public static Integer PROCESSING_STATUS_PROCESSING = 3;
	@Transient
	public static Integer PROCESSING_STATUS_DONE = 4;
	
	@Column(name = "ticket")
	private Long ticket;
	
	@Column(name = "hauling_ticket")
	private Long haulingTicket;
	
	@Column(name="ticket_type")
	private String ticketType;
	
	@Column(name="processing_status")
	private Integer processingStatus;
	
	@Column(name = "txn_date")
	private Date txnDate;
	
	@Column(name = "time_in")
	private String timeIn;
	
	@Column(name = "time_out")
	private String timeOut;
	
	@Column(name="wm_company")
	private String wmCompany;
	
	@Column(name="wm_vehicle")
	private String wmVehicle;
	
	@Column(name="wm_trailer")
	private String wmTrailer;
	
	@Column(name = "gross")
	private Double gross;
	
	@Column(name = "tare")
	private Double tare;

	@Column(name = "net")
	private Double net;
   
	@Column(name = "tons")
	private Double tons;
	
	@Column(name = "rate")
	private Double rate;
   
	@Column(name = "amount")
	private Double amount;
	
	@ManyToOne
	@JoinColumn(name="driver")
	private Driver driver;
	
	@ManyToOne
	@JoinColumn(name="driver_company")
	private Location driverCompany;

	@ManyToOne
	@JoinColumn(name = "terminal")
	private Location terminal;
	
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;

	@ManyToOne
	@JoinColumn(name="trailer")
	private Vehicle trailer;

	@Column(name = "bill_batch")
	private Date billBatch;
	
	@Column(name = "load_date")
	private Date loadDate;
	
	@ManyToOne
	@JoinColumn(name="origin")
	private Location origin;
	
	@Column(name = "origin_ticket")
	private Long originTicket;
	
	@Column(name = "tranfer_time_in")
	private String transferTimeIn;

	@Column(name = "tranfer_time_out")
	private String transferTimeOut;
	
	@Column(name = "transfer_gross")
	private Double transferGross;
	
	@Column(name = "transfer_tare")
	private Double transferTare;
	
	@Column(name = "transfer_net")
	private Double transferNet;
	
	@Column(name = "transfer_ton")
	private Double transferTons;

	@Column(name = "unload_date")
	private Date unloadDate;
	
	@ManyToOne
	@JoinColumn(name="destination")
	private Location destination;

	@Column(name = "destination_ticket")
	private Long destinationTicket;
	
	@Column(name = "landfill_time_in")
	private String landfillTimeIn;

	@Column(name = "landfill_time_out")
	private String landfillTimeOut;
	
	@Column(name = "landfill_gross")
	private Double landfillGross;

	@Column(name = "landfill_tare")
	private Double landfillTare;
	
	@Column(name = "landfill_net")
	private Double landfillNet;
	
	@Column(name = "landfill_ton")
	private Double landfillTons;

	public Long getTicket() {
		return ticket;
	}

	public void setTicket(Long ticket) {
		this.ticket = ticket;
	}

	public Long getHaulingTicket() {
		return haulingTicket;
	}

	public void setHaulingTicket(Long haulingTicket) {
		this.haulingTicket = haulingTicket;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public Integer getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(Integer processingStatus) {
		this.processingStatus = processingStatus;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public String getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getWmCompany() {
		return wmCompany;
	}

	public void setWmCompany(String wmCompany) {
		this.wmCompany = wmCompany;
	}

	public String getWmVehicle() {
		return wmVehicle;
	}

	public void setWmVehicle(String wmVehicle) {
		this.wmVehicle = wmVehicle;
	}

	public String getWmTrailer() {
		return wmTrailer;
	}

	public void setWmTrailer(String wmTrailer) {
		this.wmTrailer = wmTrailer;
	}

	public Double getGross() {
		return gross;
	}

	public void setGross(Double gross) {
		this.gross = gross;
	}

	public Double getTare() {
		return tare;
	}

	public void setTare(Double tare) {
		this.tare = tare;
	}

	public Double getNet() {
		return net;
	}

	public void setNet(Double net) {
		this.net = net;
	}

	public Double getTons() {
		return tons;
	}

	public void setTons(Double tons) {
		this.tons = tons;
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

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
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

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Vehicle getTrailer() {
		return trailer;
	}

	public void setTrailer(Vehicle trailer) {
		this.trailer = trailer;
	}

	public Date getBillBatch() {
		return billBatch;
	}

	public void setBillBatch(Date billBatch) {
		this.billBatch = billBatch;
	}

	public Date getLoadDate() {
		return loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
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

	public Double getTransferGross() {
		return transferGross;
	}

	public void setTransferGross(Double transferGross) {
		this.transferGross = transferGross;
	}

	public Double getTransferTare() {
		return transferTare;
	}

	public void setTransferTare(Double transferTare) {
		this.transferTare = transferTare;
	}

	public Double getTransferNet() {
		return transferNet;
	}

	public void setTransferNet(Double transferNet) {
		this.transferNet = transferNet;
	}

	public Double getTransferTons() {
		return transferTons;
	}

	public void setTransferTons(Double transferTons) {
		this.transferTons = transferTons;
	}

	public Date getUnloadDate() {
		return unloadDate;
	}

	public void setUnloadDate(Date unloadDate) {
		this.unloadDate = unloadDate;
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

	public Double getLandfillGross() {
		return landfillGross;
	}

	public void setLandfillGross(Double landfillGross) {
		this.landfillGross = landfillGross;
	}

	public Double getLandfillTare() {
		return landfillTare;
	}

	public void setLandfillTare(Double landfillTare) {
		this.landfillTare = landfillTare;
	}

	public Double getLandfillNet() {
		return landfillNet;
	}

	public void setLandfillNet(Double landfillNet) {
		this.landfillNet = landfillNet;
	}

	public Double getLandfillTons() {
		return landfillTons;
	}

	public void setLandfillTons(Double landfillTons) {
		this.landfillTons = landfillTons;
	}
}
