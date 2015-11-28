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
@Table(name="driverpay")
public class DriverPayroll extends AbstractBaseModel{
	
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@Column(name = "bill_batchfrom")
	protected Date billBatchFrom;
	
	@Column(name = "bill_batchto")
	protected Date billBatchTo;
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	@Column(name="totalrow_count")
	int totalRowCount=0;
	
	@Column(name="sum_total")
	double sumTotal=0.0;
    
	@Column(name="sum_amount")
	double sumAmount=0.0;
	
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

	public Date getBillBatchFrom() {
		return billBatchFrom;
	}

	public void setBillBatchFrom(Date billBatchFrom) {
		this.billBatchFrom = billBatchFrom;
	}

	public Date getBillBatchTo() {
		return billBatchTo;
	}

	public void setBillBatchTo(Date billBatchTo) {
		this.billBatchTo = billBatchTo;
	}

	public Date getPayRollBatch() {
		return payRollBatch;
	}

	public void setPayRollBatch(Date payRollBatch) {
		this.payRollBatch = payRollBatch;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public double getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(double sumTotal) {
		this.sumTotal = sumTotal;
	}

	public double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(double sumAmount) {
		this.sumAmount = sumAmount;
	}
	
	
	

}
