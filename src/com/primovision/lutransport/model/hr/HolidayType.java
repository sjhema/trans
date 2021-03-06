package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;
/**
 * @author kishor
 *
 */
@Entity
@Table(name="holiday_type")
public class HolidayType extends AbstractBaseModel{

	@NotEmpty
	@Column(name="name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="leave_type")
	private LeaveType leaveType;
	
	@ManyToOne
	@JoinColumn(name="catagory")
	private EmployeeCatagory catagory;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@NotNull
	@Column(name="date_from")
	private Date dateFrom;
	
	@NotNull
	@Column(name="date_to")
	private Date dateTo;
	
	@Column(name="paid")
	private Integer paid=1;
	
	@NotNull
	@Column(name="batch_date")
	private Date batchdate;
	
	
	@Column(name="rate")
	private Double rate;
	
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	protected Integer payRollStatus=1;
	
	@Column(name="amount")
	private Double amount;
	
	@NotNull
	@Column(name="no_ofdays")
	private Integer noOfDays;
	
	

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EmployeeCatagory getCatagory() {
		return catagory;
	}

	public void setCatagory(EmployeeCatagory catagory) {
		this.catagory = catagory;
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

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
	}

	public Date getBatchdate() {
		return batchdate;
	}

	public void setBatchdate(Date batchdate) {
		this.batchdate = batchdate;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
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
	
	
}
