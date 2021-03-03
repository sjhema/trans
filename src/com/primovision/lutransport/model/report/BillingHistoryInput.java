package com.primovision.lutransport.model.report;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.ReportModel;

public class BillingHistoryInput implements ReportModel {
	public static String EXCLUDE_NOT_BILLABLE = "Exclude";
	public static String INCLUDE_NOT_BILLABLE = "Include";
	
	private String batchDateFrom;
	private String batchDateTo;
	
	private String fromInvoiceDate;
	private String invoiceDateTo;
	private String company;
	private String driverCompany;
	private String customer;
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
	private String loadedFrom;
	private String loadedTo;
	private String unit;
	private String unloadedFrom;
	private String unloadedTo;
	
	private String billUsing;
	private String transferTimeInFrom;
	private String transferTimeInTo;
	private String transferTimeOutFrom;
	private String transferTimeOutTo;
	private String landfillTimeInFrom;
	private String landfillTimeInTo;
	private String landfillTimeOutFrom;
	private String landfillTimeOutTo;
	
	private String originTicketFrom;
	private String originTicketTo;
	private String destinationTicketFrom;
	private String destinationTicketTo;
	
	private String originGrossWtFrom;
	private String originGrossWtTo;
	
	private String originTareWtFrom;
	private String originTareWtTo;
	
	private String originNetWtFrom;
	private String originNetWtTo;
	
	private String originTonsWtFrom;
	private String originTonsWtTo;
	
	private String landfillGrossWtFrom;
	private String landfillGrossWtTo;
	
	private String landfillTareWtFrom;
	private String landfillTareWtTo;
	
	private String landfillNetWtFrom;
	private String landfillNetWtTo;
	
	private String landfillTonsWtFrom;
	private String landfillTonsWtTo;
	
	private String rateFrom;
	private String amountFrom;
	private String fuelSurchargeFrom;
	private String tonnagePremiumFrom;
	private String demurrageChargeFrom;
	private String totAmtFrom;
	
	private String rateTo;
	private String amountTo;
	private String fuelSurchargeTo;
	private String tonnagePremiumTo;
	private String demurrageChargeTo;
	private String totAmtTo;
	
	private String invoiceNumberFrom;
	private String invoiceNumberTo;
	
	private String totalAmtTo;
	private String totalAmtFrom;
	
	private Integer status;
	private String ticketStatus;
	private String notBillable;
	
	// Peak rate 2nd Feb 2021
	private String isPeakRate = StringUtils.EMPTY;
	
	// Truck driver report
	private String drillDownCompany = StringUtils.EMPTY;
	private String drillDownOrigin = StringUtils.EMPTY;
	private String drillDownDestination = StringUtils.EMPTY;
	
	public String getDrillDownCompany() {
		return drillDownCompany;
	}

	public void setDrillDownCompany(String drillDownCompany) {
		this.drillDownCompany = drillDownCompany;
	}

	public String getDrillDownOrigin() {
		return drillDownOrigin;
	}

	public void setDrillDownOrigin(String drillDownOrigin) {
		this.drillDownOrigin = drillDownOrigin;
	}

	public String getDrillDownDestination() {
		return drillDownDestination;
	}

	public void setDrillDownDestination(String drillDownDestination) {
		this.drillDownDestination = drillDownDestination;
	}

	public String getDriverCompany() {
		return driverCompany;
	}
	
	public void setDriverCompany(String driverCompany) {
		this.driverCompany = driverCompany;
	}
	

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public String getTotalAmtTo() {
		return totalAmtTo;
	}

	public void setTotalAmtTo(String totalAmtTo) {
		this.totalAmtTo = totalAmtTo;
	}

	public String getTotalAmtFrom() {
		return totalAmtFrom;
	}

	public void setTotalAmtFrom(String totalAmtFrom) {
		this.totalAmtFrom = totalAmtFrom;
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

	public String getBatchDateFrom() {
		return batchDateFrom;
	}

	public void setBatchDateFrom(String batchDateFrom) {
		this.batchDateFrom = batchDateFrom;
	}

	public String getBatchDateTo() {
		return batchDateTo;
	}

	public void setBatchDateTo(String batchDateTo) {
		this.batchDateTo = batchDateTo;
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

	public String getLoadedFrom() {
		return loadedFrom;
	}

	public void setLoadedFrom(String loadedFrom) {
		this.loadedFrom = loadedFrom;
	}

	public String getLoadedTo() {
		return loadedTo;
	}

	public void setLoadedTo(String loadedTo) {
		this.loadedTo = loadedTo;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnloadedFrom() {
		return unloadedFrom;
	}

	public void setUnloadedFrom(String unloadedFrom) {
		this.unloadedFrom = unloadedFrom;
	}

	public String getUnloadedTo() {
		return unloadedTo;
	}

	public void setUnloadedTo(String unloadedTo) {
		this.unloadedTo = unloadedTo;
	}

	public String getBillUsing() {
		return billUsing;
	}

	public void setBillUsing(String billUsing) {
		this.billUsing = billUsing;
	}

	public String getTransferTimeInFrom() {
		return transferTimeInFrom;
	}

	public void setTransferTimeInFrom(String transferTimeInFrom) {
		this.transferTimeInFrom = transferTimeInFrom;
	}

	public String getTransferTimeInTo() {
		return transferTimeInTo;
	}

	public void setTransferTimeInTo(String transferTimeInTo) {
		this.transferTimeInTo = transferTimeInTo;
	}

	public String getTransferTimeOutFrom() {
		return transferTimeOutFrom;
	}

	public void setTransferTimeOutFrom(String transferTimeOutFrom) {
		this.transferTimeOutFrom = transferTimeOutFrom;
	}

	public String getTransferTimeOutTo() {
		return transferTimeOutTo;
	}

	public void setTransferTimeOutTo(String transferTimeOutTo) {
		this.transferTimeOutTo = transferTimeOutTo;
	}

	public String getLandfillTimeInFrom() {
		return landfillTimeInFrom;
	}

	public void setLandfillTimeInFrom(String landfillTimeInFrom) {
		this.landfillTimeInFrom = landfillTimeInFrom;
	}

	public String getLandfillTimeInTo() {
		return landfillTimeInTo;
	}

	public void setLandfillTimeInTo(String landfillTimeInTo) {
		this.landfillTimeInTo = landfillTimeInTo;
	}

	public String getLandfillTimeOutFrom() {
		return landfillTimeOutFrom;
	}

	public void setLandfillTimeOutFrom(String landfillTimeOutFrom) {
		this.landfillTimeOutFrom = landfillTimeOutFrom;
	}

	public String getLandfillTimeOutTo() {
		return landfillTimeOutTo;
	}

	public void setLandfillTimeOutTo(String landfillTimeOutTo) {
		this.landfillTimeOutTo = landfillTimeOutTo;
	}

	public String getOriginTicketFrom() {
		return originTicketFrom;
	}

	public void setOriginTicketFrom(String originTicketFrom) {
		this.originTicketFrom = originTicketFrom;
	}

	public String getOriginTicketTo() {
		return originTicketTo;
	}

	public void setOriginTicketTo(String originTicketTo) {
		this.originTicketTo = originTicketTo;
	}

	public String getDestinationTicketFrom() {
		return destinationTicketFrom;
	}

	public void setDestinationTicketFrom(String destinationTicketFrom) {
		this.destinationTicketFrom = destinationTicketFrom;
	}

	public String getDestinationTicketTo() {
		return destinationTicketTo;
	}

	public void setDestinationTicketTo(String destinationTicketTo) {
		this.destinationTicketTo = destinationTicketTo;
	}

	public String getOriginGrossWtFrom() {
		return originGrossWtFrom;
	}

	public void setOriginGrossWtFrom(String originGrossWtFrom) {
		this.originGrossWtFrom = originGrossWtFrom;
	}

	public String getOriginGrossWtTo() {
		return originGrossWtTo;
	}

	public void setOriginGrossWtTo(String originGrossWtTo) {
		this.originGrossWtTo = originGrossWtTo;
	}

	public String getOriginTareWtFrom() {
		return originTareWtFrom;
	}

	public void setOriginTareWtFrom(String originTareWtFrom) {
		this.originTareWtFrom = originTareWtFrom;
	}

	public String getOriginTareWtTo() {
		return originTareWtTo;
	}

	public void setOriginTareWtTo(String originTareWtTo) {
		this.originTareWtTo = originTareWtTo;
	}

	public String getOriginNetWtFrom() {
		return originNetWtFrom;
	}

	public void setOriginNetWtFrom(String originNetWtFrom) {
		this.originNetWtFrom = originNetWtFrom;
	}

	public String getOriginNetWtTo() {
		return originNetWtTo;
	}

	public void setOriginNetWtTo(String originNetWtTo) {
		this.originNetWtTo = originNetWtTo;
	}

	public String getOriginTonsWtFrom() {
		return originTonsWtFrom;
	}

	public void setOriginTonsWtFrom(String originTonsWtFrom) {
		this.originTonsWtFrom = originTonsWtFrom;
	}

	public String getOriginTonsWtTo() {
		return originTonsWtTo;
	}

	public void setOriginTonsWtTo(String originTonsWtTo) {
		this.originTonsWtTo = originTonsWtTo;
	}

	public String getLandfillGrossWtFrom() {
		return landfillGrossWtFrom;
	}

	public void setLandfillGrossWtFrom(String landfillGrossWtFrom) {
		this.landfillGrossWtFrom = landfillGrossWtFrom;
	}

	public String getLandfillGrossWtTo() {
		return landfillGrossWtTo;
	}

	public void setLandfillGrossWtTo(String landfillGrossWtTo) {
		this.landfillGrossWtTo = landfillGrossWtTo;
	}

	public String getLandfillTareWtFrom() {
		return landfillTareWtFrom;
	}

	public void setLandfillTareWtFrom(String landfillTareWtFrom) {
		this.landfillTareWtFrom = landfillTareWtFrom;
	}

	public String getLandfillTareWtTo() {
		return landfillTareWtTo;
	}

	public void setLandfillTareWtTo(String landfillTareWtTo) {
		this.landfillTareWtTo = landfillTareWtTo;
	}

	public String getLandfillNetWtFrom() {
		return landfillNetWtFrom;
	}

	public void setLandfillNetWtFrom(String landfillNetWtFrom) {
		this.landfillNetWtFrom = landfillNetWtFrom;
	}

	public String getLandfillNetWtTo() {
		return landfillNetWtTo;
	}

	public void setLandfillNetWtTo(String landfillNetWtTo) {
		this.landfillNetWtTo = landfillNetWtTo;
	}

	public String getLandfillTonsWtFrom() {
		return landfillTonsWtFrom;
	}

	public void setLandfillTonsWtFrom(String landfillTonsWtFrom) {
		this.landfillTonsWtFrom = landfillTonsWtFrom;
	}

	public String getLandfillTonsWtTo() {
		return landfillTonsWtTo;
	}

	public void setLandfillTonsWtTo(String landfillTonsWtTo) {
		this.landfillTonsWtTo = landfillTonsWtTo;
	}

	public String getRateFrom() {
		return rateFrom;
	}

	public void setRateFrom(String rateFrom) {
		this.rateFrom = rateFrom;
	}

	public String getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(String amountFrom) {
		this.amountFrom = amountFrom;
	}

	public String getFuelSurchargeFrom() {
		return fuelSurchargeFrom;
	}

	public void setFuelSurchargeFrom(String fuelSurchargeFrom) {
		this.fuelSurchargeFrom = fuelSurchargeFrom;
	}

	public String getTonnagePremiumFrom() {
		return tonnagePremiumFrom;
	}

	public void setTonnagePremiumFrom(String tonnagePremiumFrom) {
		this.tonnagePremiumFrom = tonnagePremiumFrom;
	}

	public String getDemurrageChargeFrom() {
		return demurrageChargeFrom;
	}

	public void setDemurrageChargeFrom(String demurrageChargeFrom) {
		this.demurrageChargeFrom = demurrageChargeFrom;
	}

	public String getTotAmtFrom() {
		return totAmtFrom;
	}

	public void setTotAmtFrom(String totAmtFrom) {
		this.totAmtFrom = totAmtFrom;
	}

	public String getRateTo() {
		return rateTo;
	}

	public void setRateTo(String rateTo) {
		this.rateTo = rateTo;
	}

	public String getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(String amountTo) {
		this.amountTo = amountTo;
	}

	public String getFuelSurchargeTo() {
		return fuelSurchargeTo;
	}

	public void setFuelSurchargeTo(String fuelSurchargeTo) {
		this.fuelSurchargeTo = fuelSurchargeTo;
	}

	public String getTonnagePremiumTo() {
		return tonnagePremiumTo;
	}

	public void setTonnagePremiumTo(String tonnagePremiumTo) {
		this.tonnagePremiumTo = tonnagePremiumTo;
	}

	public String getDemurrageChargeTo() {
		return demurrageChargeTo;
	}

	public void setDemurrageChargeTo(String demurrageChargeTo) {
		this.demurrageChargeTo = demurrageChargeTo;
	}

	public String getTotAmtTo() {
		return totAmtTo;
	}

	public void setTotAmtTo(String totAmtTo) {
		this.totAmtTo = totAmtTo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getNotBillable() {
		return notBillable;
	}

	public void setNotBillable(String notBillable) {
		this.notBillable = notBillable;
	}

	public String getFromInvoiceDate() {
		return fromInvoiceDate;
	}

	public void setFromInvoiceDate(String fromInvoiceDate) {
		this.fromInvoiceDate = fromInvoiceDate;
	}

	public String getInvoiceDateTo() {
		return invoiceDateTo;
	}

	public void setInvoiceDateTo(String invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
	}

	public String getInvoiceNumberFrom() {
		return invoiceNumberFrom;
	}

	public void setInvoiceNumberFrom(String invoiceNumberFrom) {
		this.invoiceNumberFrom = invoiceNumberFrom;
	}

	public String getInvoiceNumberTo() {
		return invoiceNumberTo;
	}

	public void setInvoiceNumberTo(String invoiceNumberTo) {
		this.invoiceNumberTo = invoiceNumberTo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getIsPeakRate() {
		return isPeakRate;
	}

	public void setIsPeakRate(String isPeakRate) {
		this.isPeakRate = isPeakRate;
	}
}
