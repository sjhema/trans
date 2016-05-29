package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;

@Entity
@Table(name ="salary_override")
public class SalaryOverride extends AbstractBaseModel {	
	@NotNull
	@Column(name = "payroll_batch")
	private Date payrollBatch;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Driver driver;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@NotNull
	@Column(name="amount")
	private Double amount;
	
	@Column(name="notes")
	private String notes;

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

	public Date getPayrollBatch() {
		return payrollBatch;
	}

	public void setPayrollBatch(Date payrollBatch) {
		this.payrollBatch = payrollBatch;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
