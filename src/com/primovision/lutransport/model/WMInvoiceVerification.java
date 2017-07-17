package com.primovision.lutransport.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class WMInvoiceVerification implements ReportModel {
	public static String WM_INVOICE_MISSING_TICKETS_IN_WM_REPORT = "wmInvoiceMissingTicketsInWM";
	public static String WM_INVOICE_MISSING_TICKETS_REPORT = "wmInvoiceMissingTickets";
	public static String WM_INVOICE_MATCHING_REPORT = "wmInvoiceMatching";
	public static String WM_INVOICE_DISCREPANCY_REPORT = "wmInvoiceDiscrepancy";
	
	public static String WM_INVOICE_HOLD_TICKETS_ACTION = "HOLD_TICKETS";
	
	Long originTicket;
	Long destinationTicket;
	
	Long originId;
	Long destinationId;
	
	String origin = StringUtils.EMPTY;
	String destination = StringUtils.EMPTY;
	
	String wmOrigin = StringUtils.EMPTY;
	String wmDestination = StringUtils.EMPTY;
	
	Date loadDate;
	Date unloadDate;
	
	Long ticket;
	Date txnDate;
	
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
	public Long getOriginId() {
		return originId;
	}
	public void setOriginId(Long originId) {
		this.originId = originId;
	}
	public Long getDestinationId() {
		return destinationId;
	}
	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}
	public Long getTicket() {
		return ticket;
	}
	public void setTicket(Long ticket) {
		this.ticket = ticket;
	}
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}
}
