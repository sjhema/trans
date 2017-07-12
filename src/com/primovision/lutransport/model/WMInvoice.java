package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "wm_invoice")
public class WMInvoice extends AbstractBaseModel {
	@Column(name = "ticket")
	private Long ticket;
	
	@Column(name = "reissued_ticket")
	private Long reissuedTicket;
	
	@Column(name = "txn_date")
	private Date txnDate;
	
	@Column(name = "time_in")
	private String timeIn;
	
	@Column(name = "time_out")
	private String timeOut;
	
	@Column(name="wm_vehicle")
	private String wmVehicle;
		
	@Column(name="wm_trailer")
	private String wmTrailer;
		
	@Column(name="wm_origin")
	private String wmOrigin;
			 
	@Column(name="wm_destination")
	private String wmDestination;
	
	@Column(name = "gross")
	private Double gross;
	
	@Column(name = "tare")
	private Double tare;

	@Column(name = "net")
	private Double net;
	
	@Column(name = "rate")
	private Double rate;
   
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "fsc")
	private Double fsc;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@ManyToOne
	@JoinColumn(name="origin")
	private Location origin;
	
	@ManyToOne
	@JoinColumn(name="destination")
	private Location destination;
	
	@Column(name="wm_status")
	private String wmStatus;
	
	@Column(name="wm_status_code")
	private String wmStatusCode;
	
	@Column(name="wm_ticket")
	private String wmTicket;

	public Long getTicket() {
		return ticket;
	}

	public void setTicket(Long ticket) {
		this.ticket = ticket;
	}

	public Long getReissuedTicket() {
		return reissuedTicket;
	}

	public void setReissuedTicket(Long reissuedTicket) {
		this.reissuedTicket = reissuedTicket;
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

	public String getWmOrigin() {
		return wmOrigin;
	}

	public void setWmOrigin(String wmOrigin) {
		this.wmOrigin = wmOrigin;
	}

	public String getWmDestination() {
		return wmDestination;
	}

	public void setWmDestination(String wmDestination) {
		this.wmDestination = wmDestination;
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

	public Double getFsc() {
		return fsc;
	}

	public void setFsc(Double fsc) {
		this.fsc = fsc;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public String getWmStatus() {
		return wmStatus;
	}

	public void setWmStatus(String wmStatus) {
		this.wmStatus = wmStatus;
	}

	public String getWmStatusCode() {
		return wmStatusCode;
	}

	public void setWmStatusCode(String wmStatusCode) {
		this.wmStatusCode = wmStatusCode;
	}

	public String getWmTicket() {
		return wmTicket;
	}

	public void setWmTicket(String wmTicket) {
		this.wmTicket = wmTicket;
	}
}
