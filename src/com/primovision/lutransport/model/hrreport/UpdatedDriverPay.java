package com.primovision.lutransport.model.hrreport;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;

@Entity
@Table(name="updated_driverpay")
public class UpdatedDriverPay extends AbstractBaseModel {
	@Transient
	public static Integer UPDATED_STATUS_IN_PROCESS = 0;
	@Transient
	public static Integer UPDATED_STATUS_PROCESSED = 1;
	
	@Column(name="updated_status")
	protected Integer updatedStatus;
	
	@ManyToOne
	@JoinColumn(name="company")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@Column(name="driver")
	String driverName;

	@Column(name="no_of_load")
	int noOfLoad=0;
	
	@Column(name="amount")
	Double amount=0.0;
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	@Column(name = "billbatch_fromdate")
	protected Date billBatchDateFrom;
	
	@Column(name = "billbatch_todate")
	protected Date billBatchDateTo;
	
	@Column(name="notes")
	String notes;

	public Integer getUpdatedStatus() {
		return updatedStatus;
	}

	public void setUpdatedStatus(Integer updatedStatus) {
		this.updatedStatus = updatedStatus;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public int getNoOfLoad() {
		return noOfLoad;
	}

	public void setNoOfLoad(int noOfLoad) {
		this.noOfLoad = noOfLoad;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getPayRollBatch() {
		return payRollBatch;
	}

	public void setPayRollBatch(Date payRollBatch) {
		this.payRollBatch = payRollBatch;
	}

	public Date getBillBatchDateFrom() {
		return billBatchDateFrom;
	}

	public void setBillBatchDateFrom(Date billBatchDateFrom) {
		this.billBatchDateFrom = billBatchDateFrom;
	}

	public Date getBillBatchDateTo() {
		return billBatchDateTo;
	}

	public void setBillBatchDateTo(Date billBatchDateTo) {
		this.billBatchDateTo = billBatchDateTo;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
