package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "changed_ticket")
public class ChangedTicket extends AbstractBaseModel {
	@Transient
	public static Integer CHANGED_STATUS_IN_PROCESS = 0;
	@Transient
	public static Integer CHANGED_STATUS_PROCESSED = 1;
	
	@Column(name="base_ticket_id")
	protected Long baseTicketId;
	
	@Column(name="changed_status")
	protected Integer changedStatus;
	
	@NotNull
	@Column(name = "load_date")
	protected Date loadDate;
	
	@NotEmpty
	@Column(name = "tranfer_time_in")
	protected String transferTimeIn;

	@NotEmpty
	@Column(name = "tranfer_time_out")
	protected String transferTimeOut;
	
	@NotEmpty
	@Column(name = "landfill_time_in")
	protected String landfillTimeIn;

	@NotEmpty
	@Column(name = "landfill_time_out")
	protected String landfillTimeOut;

	@ManyToOne
	@JoinColumn(name="vehicle")
	protected Vehicle vehicle;

	@ManyToOne
	@JoinColumn(name="trailer")
	protected Vehicle trailer;

	@NotNull
	@Column(name = "unload_date")
	protected Date unloadDate;

	@ManyToOne
	@JoinColumn(name="origin")
	protected Location origin;
	
	@ManyToOne
	@JoinColumn(name="new_origin")
	protected Location newOrigin;

	@NotNull
	@Column(name = "origin_ticket")
	protected Long originTicket;

	@ManyToOne
	@JoinColumn(name="destination")
	protected Location destination;
	
	@ManyToOne
	@JoinColumn(name="new_destination")
	protected Location newDestination;

	@NotNull
	@Column(name = "destination_ticket")
	protected Long destinationTicket;

	@NotNull
	@Column(name = "transfer_gross")
	protected Double transferGross;
	
	@Column(name = "driver_payrate")
	protected Double driverPayRate;
	
	@Column(name = "new_driver_payrate")
	protected Double newDriverPayRate;

	@NotNull
	@Column(name = "transfer_tare")
	protected Double transferTare;
	
	@NotNull
	@Column(name = "transfer_ton")
	protected Double transferTons;
	
	@NotNull
	@Column(name = "landfill_gross")
	protected Double landfillGross;

	@NotNull
	@Column(name = "landfill_tare")
	protected Double landfillTare;
	
	@NotNull
	@Column(name = "landfill_ton")
	protected Double landfillTons;
	
	
	@Column(name = "gallon")
	protected Double gallons=0.0;

	@ManyToOne
	@JoinColumn(name="driver")
	protected Driver driver;
	
	@ManyToOne
	@JoinColumn(name="new_driver")
	protected Driver newDriver;

	@NotNull
	@Column(name = "bill_batch")
	protected Date billBatch;
	
	@Column(name = "new_bill_batch")
	protected Date newBillBatch;

	@ManyToOne
	@JoinColumn(name = "terminal")
	protected Location terminal;
	
	@NotNull
	@Column(name = "transfer_net")
	protected Double transferNet;
	
	@NotNull
	@Column(name = "landfill_net")
	protected Double landfillNet;
	
    @ManyToOne
    @JoinColumn(name = "customer_id")
    protected Customer customer;
	
   @ManyToOne
	@JoinColumn(name="company_location")
	protected Location companyLocation;
    
   @ManyToOne
	@JoinColumn(name="driver_company")
	protected Location driverCompany;
    
	@ManyToOne
	@JoinColumn(name = "subcontractor_id")
	protected SubContractor subcontractor;
	
	@Column(name="ticketstatus")
	protected Integer ticketStatus;
	
	@Column(name="voucher_status")
	protected Integer voucherStatus=1;
	
	@Column(name="voucher_date")
	protected Date voucherDate;
	
	@Column(name="voucher_number")
	protected String voucherNumber;
	
	@Column(name="invoice_date")
	protected Date invoiceDate;
	
	@Column(name="invoice_number")
	protected String invoiceNumber;
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	@Column(name = "new_payroll_batch")
	protected Date newPayRollBatch;
	
	protected Integer payRollStatus;
	
	@Column(name="paperVerifiedStatus")
	private Integer paperVerifiedStatus;
	
	@Column(name="autoCreated")
	private Integer autoCreated;
	
	
	
	public Integer getAutoCreated() {
		return autoCreated;
	}

	public void setAutoCreated(Integer autoCreated) {
		this.autoCreated = autoCreated;
	}

	@Column(name = "notes")
	private String notes;
	
	@Column(name = "new_notes")
	private String newNotes;
	
	public Integer getPaperVerifiedStatus() {
		return paperVerifiedStatus;
	}

	public void setPaperVerifiedStatus(Integer paperVerifiedStatus) {
		this.paperVerifiedStatus = paperVerifiedStatus;
	}


	//-----------------------------
	@Column(name="entered_by")
	protected  String enteredBy;
	

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	
	//-------------------------------
	public Double getGallons() {
		return gallons;
	}

	public void setGallons(Double gallons) {
		this.gallons = gallons;
	}
	
	
	public Integer getTicketStatus() {
		return ticketStatus;
	}

	public Date getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public Integer getVoucherStatus() {
		return voucherStatus;
	}

	public void setVoucherStatus(Integer voucherStatus) {
		this.voucherStatus = voucherStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Location getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(Location companyLocation) {
		this.companyLocation = companyLocation;
	}

	public void setTicketStatus(Integer ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

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

	public Double getTransferTons() {
		return transferTons;
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

	public Double getLandfillTons() {
		return landfillTons;
	}

	public Date getBillBatch() {
		return billBatch;
	}

	public void setBillBatch(Date billBatch) {
		this.billBatch = billBatch;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}

	public Date getLoadDate() {
		return loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
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

	public Date getUnloadDate() {
		return unloadDate;
	}

	public void setUnloadDate(Date unloadDate) {
		this.unloadDate = unloadDate;
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

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

//	public Customer getCustomer() {
//		return customer;
//	}
//
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}

	public void setTransferTons(Double transferTons) {
		this.transferTons = transferTons;
	}

	public void setLandfillTons(Double landfillTons) {
		this.landfillTons = landfillTons;
	}
	
	public SubContractor getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(SubContractor subcontractor) {
		this.subcontractor = subcontractor;
	}

	public void setTransferNet(Double transferNet) {
		this.transferNet = transferNet;
	}

	public void setLandfillNet(Double landfillNet) {
		this.landfillNet = landfillNet;
	}

	
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public Double getNewDriverPayRate() {
		return newDriverPayRate;
	}

	public void setNewDriverPayRate(Double newDriverPayRate) {
		this.newDriverPayRate = newDriverPayRate;
	}

	public Date getPayRollBatch() {
		return payRollBatch;
	}

	public void setPayRollBatch(Date payRollBatch) {
		this.payRollBatch = payRollBatch;
	}

	public Integer getPayRollStatus() {
		return payRollStatus;
	}

	public void setPayRollStatus(Integer payRollStatus) {
		this.payRollStatus = payRollStatus;
	}
	
	public String getNotes() {
		return notes;
	}
	
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	public Location getDriverCompany() {
		return driverCompany;
	}
	
	
	public void setDriverCompany(Location driverCompany) {
		this.driverCompany = driverCompany;
	}
	
	public Double getDriverPayRate() {
		return driverPayRate;
	}
	
	public void setDriverPayRate(Double driverPayRate) {
		this.driverPayRate = driverPayRate;
	}
	
	public Long getBaseTicketId() {
		return baseTicketId;
	}

	public void setBaseTicketId(Long baseTicketId) {
		this.baseTicketId = baseTicketId;
	}

	public Integer getChangedStatus() {
		return changedStatus;
	}

	public void setChangedStatus(Integer changedStatus) {
		this.changedStatus = changedStatus;
	}
	
	

	public String getNewNotes() {
		return newNotes;
	}

	public void setNewNotes(String newNotes) {
		this.newNotes = newNotes;
	}

	public Location getNewOrigin() {
		return newOrigin;
	}

	public void setNewOrigin(Location newOrigin) {
		this.newOrigin = newOrigin;
	}

	public Location getNewDestination() {
		return newDestination;
	}

	public void setNewDestination(Location newDestination) {
		this.newDestination = newDestination;
	}

	public Driver getNewDriver() {
		return newDriver;
	}

	public void setNewDriver(Driver newDriver) {
		this.newDriver = newDriver;
	}
	

	public Date getNewBillBatch() {
		return newBillBatch;
	}

	public void setNewBillBatch(Date newBillBatch) {
		this.newBillBatch = newBillBatch;
	}

	public Date getNewPayRollBatch() {
		return newPayRollBatch;
	}

	public void setNewPayRollBatch(Date newPayRollBatch) {
		this.newPayRollBatch = newPayRollBatch;
	}

	@Transient
	public String getSubcontractorName() {
		if (subcontractor == null) {
			return StringUtils.EMPTY;
		} else {
			return subcontractor.getName();
		}
	}
	
	@Transient
	public String getTrailerNum() {
		if (trailer == null) {
			return StringUtils.EMPTY;
		} else {
			return trailer.getUnitNum();
		}
	}
	
	@Transient
	public String getOldOriginName() {
		return (getOrigin() != null ? getOrigin().getName() : StringUtils.EMPTY);
	}
	@Transient
	public String getNewOriginName() {
		return (getNewOrigin() != null ? getNewOrigin().getName() : StringUtils.EMPTY);
	}
	@Transient
	public String getOldDestinationName() {
		return (getDestination() != null ? getDestination().getName() : StringUtils.EMPTY);
	}
	@Transient
	public String getNewDestinationName() {
		return (getNewDestination() != null ? getNewDestination().getName() : StringUtils.EMPTY);
	}
	@Transient
	public String getCompanyName() {
		return (getDriverCompany() != null ? getDriverCompany().getName() : StringUtils.EMPTY);
	}
	@Transient
	public String getTerminalName() {
		return (getTerminal() != null ? getTerminal().getName() : StringUtils.EMPTY);
	}
	@Transient
	public String getOldDriverName() {
		return (getDriver() != null ? getDriver().getFullName() : StringUtils.EMPTY);
	}
	@Transient
	public String getNewDriverName() {
		return (getNewDriver() != null ? getNewDriver().getFullName() : StringUtils.EMPTY);
	}
	@Transient
	public Double getOldDriverPayRateValue() {
		return (getDriverPayRate() != null ? getDriverPayRate() : 0.0);
	}
	@Transient
	public Double getNewDriverPayRateValue() {
		return (getNewDriverPayRate() != null ? getNewDriverPayRate() : 0.0);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChangedTicket other = (ChangedTicket) obj;
		if (other.getOrigin()!=null && this.getOrigin()!=null && other.getDestination()!=null && this.getDestination()!=null && (other.getOrigin()==this.getOrigin()) && (other.getDestination()==this.getDestination()) && ((other.getOriginTicket().equals(this.getOriginTicket()) || (other.getDestinationTicket().equals(this.getDestinationTicket()))))) {
			return true;
		}
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (getId().equals(other.getId())) { 
			return true;
		}
		return false;
	}
}