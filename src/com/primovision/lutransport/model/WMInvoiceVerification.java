package com.primovision.lutransport.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class WMInvoiceVerification implements ReportModel {
	Long originTicket;
	Long destinationTicket;
	
	String origin = StringUtils.EMPTY;
	String destination = StringUtils.EMPTY;
	
	String wmOrigin = StringUtils.EMPTY;
	String wmDestination = StringUtils.EMPTY;
	
	Date loadDate;
	Date unloadDate;
	
	public Long getOriginTicket() {
		return originTicket;
	}
	public void setOriginTicket(Long originTicket) {
		this.originTicket = originTicket;
	}
	public Long getDestinationTicket() {
		return destinationTicket;
	}
	public void setDestinationTicket(Long destinationTicket) {
		this.destinationTicket = destinationTicket;
	}
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
}
