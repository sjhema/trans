package com.primovision.lutransport.model.report;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.AbstractBaseModel;

public class WMInvoiceReport extends AbstractBaseModel {
	private Long ticket;
	private Long reissuedTicket;
	
	private Date txnDate;
	
	private String timeIn = StringUtils.EMPTY;
	private String timeOut = StringUtils.EMPTY;
	
	private String wmVehicle = StringUtils.EMPTY;
	private String wmTrailer = StringUtils.EMPTY;
	
	private String wmOrigin = StringUtils.EMPTY;
	private String wmDestination = StringUtils.EMPTY;
	
	private String originName = StringUtils.EMPTY;
	private String destinationName = StringUtils.EMPTY;
	
	private Double gross;
	private Double tare;
	private Double net;
	private Double fsc;
	private Double amount;
	private Double totalAmount;
	
	private String wmStatus = StringUtils.EMPTY;

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

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
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

	public Double getFsc() {
		return fsc;
	}

	public void setFsc(Double fsc) {
		this.fsc = fsc;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getWmStatus() {
		return wmStatus;
	}

	public void setWmStatus(String wmStatus) {
		this.wmStatus = wmStatus;
	}
}
