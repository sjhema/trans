package com.primovision.lutransport.model.report;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.AbstractBaseModel;

public class WMTicketReport extends AbstractBaseModel {
	private Long ticket;
	private Long haulingTicket;
	
	private String ticketType = StringUtils.EMPTY;
	private String processingStatus = StringUtils.EMPTY;;
	
	private Date txnDate;
	private Date loadUnloadDate;
	
	private String timeIn = StringUtils.EMPTY;;
	private String timeOut = StringUtils.EMPTY;;
	
	private String wmVehicle = StringUtils.EMPTY;;
	private String wmTrailer = StringUtils.EMPTY;;
	
	private String wmDestination = StringUtils.EMPTY;;
	
	private Integer vehicle;
	private Integer trailer;
	
	private Double gross;
	private Double tare;
	private Double net;
	private Double tons;
	
	private Date loadDate;
	private String originName = StringUtils.EMPTY;;
	
	private Date unloadDate;
	private String destinationName = StringUtils.EMPTY;;
	
	public Integer getVehicle() {
		return vehicle;
	}
	public void setVehicle(Integer vehicle) {
		this.vehicle = vehicle;
	}
	public Integer getTrailer() {
		return trailer;
	}
	public void setTrailer(Integer trailer) {
		this.trailer = trailer;
	}
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
	
	public String getProcessingStatus() {
		return processingStatus;
	}
	public void setProcessingStatus(String processingStatus) {
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
	public Double getTons() {
		return tons;
	}
	public void setTons(Double tons) {
		this.tons = tons;
	}
	public Date getLoadDate() {
		return loadDate;
	}
	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}
	public String getOriginName() {
		return originName;
	}
	public void setOriginName(String originName) {
		this.originName = originName;
	}
	public Date getUnloadDate() {
		return unloadDate;
	}
	public void setUnloadDate(Date unloadDate) {
		this.unloadDate = unloadDate;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public Date getLoadUnloadDate() {
		return loadUnloadDate;
	}
	public void setLoadUnloadDate(Date loadUnloadDate) {
		this.loadUnloadDate = loadUnloadDate;
	}
}
