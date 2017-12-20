package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;

@Entity
@Table(name ="miscellaneous_amount")
public class MiscellaneousAmount extends AbstractBaseModel {	
	@Transient
	public static String REVERT_MODE = "REVERT";
	
	@NotNull
	@Column(name = "batchfrom")
	protected Date batchFrom;
	
	@NotNull
	@Column(name = "batchto")
	protected Date batchTo;
	
	@ManyToOne
	@JoinColumn(name="employee_id")
	protected Driver driver;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	protected Location terminal;
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	protected Integer payRollStatus=1;
	
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@Column(name="misc_amount")
	protected Double misamount;
	
	@Column(name="misc_detailed_notes")
	protected String miscDetailedNotes;
	
	@Column(name="misc_type")
	protected String miscType;
	
	
	@Column(name="sequence_number")
	protected String sequenceNumber;
	
	@Column(name="misc_notes")
	protected String miscNotes;

	public Date getBatchFrom() {
		return batchFrom;
	}

	public void setBatchFrom(Date batchFrom) {
		this.batchFrom = batchFrom;
	}

	public Date getBatchTo() {
		return batchTo;
	}

	public void setBatchTo(Date batchTo) {
		this.batchTo = batchTo;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public Double getMisamount() {
		return misamount;
	}

	public void setMisamount(Double misamount) {
		this.misamount = misamount;
	}

	public String getMiscNotes() {
		return miscNotes;
	}

	public void setMiscNotes(String miscNotes) {
		this.miscNotes = miscNotes;
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
	
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	 
	
	public String getMiscDetailedNotes() {
		return miscDetailedNotes;
	}
	
	public void setMiscDetailedNotes(String miscDetailedNotes) {
		this.miscDetailedNotes = miscDetailedNotes;
	}
	
	
	public String getMiscType() {
		return miscType;
	}
	
	
	public void setMiscType(String miscType) {
		this.miscType = miscType;
	}
	
	
}
