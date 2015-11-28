package com.primovision.lutransport.model.hrreport;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;
/**
 * @author kishor
 *
 */
@Entity
@Table(name="weeklypay")
public class WeeklyPay extends AbstractBaseModel{
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal_id")
	private Location terminal;
	
	@Column(name = "payroll_batch")
	private Date payRollBatch;
	
	@Column(name="totalrow_count")
	private Integer totalRowCount=0;
	
	@Column(name="sum_basic")
	private Double sumTotal=0.0;
	
	@Column(name="sum_total")
	double sumAmount=0.0;
	
	@Column(name="payroll_number")
	private Date checkDate;

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

	public Date getPayRollBatch() {
		return payRollBatch;
	}

	public void setPayRollBatch(Date payRollBatch) {
		this.payRollBatch = payRollBatch;
	}

	public Integer getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(Integer totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public Double getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(Double sumTotal) {
		this.sumTotal = sumTotal;
	}

	public double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public Date getCheckDate() {
		return checkDate;
	}
	
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	

}
