package com.primovision.lutransport.model.report;

import com.primovision.lutransport.model.ReportModel;

public class BillingHistory implements ReportModel {

	private String batchDate;
	private String terminal;
	private String createdBy;
	private String origin = "";
	private String destination = "";
	private String driver;
	private String subcontractor;
	private String truck;
	private String trailer;
	private String date;
	private String invoiceNo;
	private String loaded;
	private String unit;
	private String unloaded;
	private String billUsing;
	private String transferTimeIn;
	private String transferTimeOut;
	private String landfillTimeIn;
	private String landfillTimeOut;
	private String originTicket;
	private String destinationTicket;
	private Double originGrossWt;
	private Double originTareWt;
	private Double originNetWt;
	private Double originTonsWt;
	private Double landfillGrossWt;
	private Double landfillTareWt;
	private Double landfillNetWt;
	private Double landfillTonsWt;
	private Double rate=0.0;
	private Double amount=0.0;
	private Double fuelSurcharge = 0.0;
	private Double tonnagePremium = 0.0;
	private Double demurrageCharge = 0.0;
	private Double totAmt = 0.0;

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

	public Double getFuelSurcharge() {
		return fuelSurcharge;
	}

	public void setFuelSurcharge(Double fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}

	public Double getTotAmt() {
		return totAmt;
	}

	public void setTotAmt(Double totAmt) {
		this.totAmt = totAmt;
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(String subcontractor) {
		this.subcontractor = subcontractor;
	}

	public String getTruck() {
		return truck;
	}

	public void setTruck(String truck) {
		this.truck = truck;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public Double getLandfillGrossWt() {
		return landfillGrossWt;
	}

	public void setLandfillGrossWt(Double landfillGrossWt) {
		this.landfillGrossWt = landfillGrossWt;
	}

	public Double getLandfillTareWt() {
		return landfillTareWt;
	}

	public void setLandfillTareWt(Double landfillTareWt) {
		this.landfillTareWt = landfillTareWt;
	}

	public Double getLandfillNetWt() {
		return landfillNetWt;
	}

	public void setLandfillNetWt(Double landfillNetWt) {
		this.landfillNetWt = landfillNetWt;
	}

	public Double getLandfillTonsWt() {
		return landfillTonsWt;
	}

	public void setLandfillTonsWt(Double landfillTonsWt) {
		this.landfillTonsWt = landfillTonsWt;
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

	public String getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(String batchDate) {
		this.batchDate = batchDate;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
