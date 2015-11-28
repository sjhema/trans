package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;
/**
 * @author Subodh
 * 
 */
@Entity
@Table(name="hourlypayroll_invoices")
public class HourlyPayrollInvoice extends AbstractBaseModel {

	@NotNull
	@Column(name = "payroll_invoice_date")
	private Date payrollinvoicedate;
	
	
	@Column(name = "bill_batchfrom")
	protected Date billBatchFrom;
	
	@Column(name = "bill_batchto")
	protected Date billBatchTo;
	
		
	@Column(name="sum_regular_amount")
	private Double sumregularamount=0.0;
	
	@Column(name="sum_ot_amount")
	private Double sumotamount=0.0;
	
	@Column(name="sum_dt_amount")
	private Double sumdtamount=0.0;
	
	@Column(name="sum_total_amount")
	private Double sumtotalamount=0.0;

	@Column(name="company")
	String company = "";
	
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location companyLoc;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	

	
public void setCompany(String company) {
	this.company = company;
}

public String getCompany() {
	return company;
}
	

	public Date getPayrollinvoicedate() {
		return payrollinvoicedate;
	}

	public void setPayrollinvoicedate(Date payrollinvoicedate) {
		this.payrollinvoicedate = payrollinvoicedate;
	}

	

	public Double getSumregularamount() {
		return sumregularamount;
	}

	public void setSumregularamount(Double sumregularamount) {
		this.sumregularamount = sumregularamount;
	}

	public Double getSumotamount() {
		return sumotamount;
	}

	public void setSumotamount(Double sumotamount) {
		this.sumotamount = sumotamount;
	}

	public Double getSumdtamount() {
		return sumdtamount;
	}

	public void setSumdtamount(Double sumdtamount) {
		this.sumdtamount = sumdtamount;
	}

	public Double getSumtotalamount() {
		return sumtotalamount;
	}

	public void setSumtotalamount(Double sumtotalamount) {
		this.sumtotalamount = sumtotalamount;
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
	
	
	public void setCompanyLoc(Location companyLoc) {
		this.companyLoc = companyLoc;
	}
	
	
	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}
	
	
	public Location getCompanyLoc() {
		return companyLoc;
	}
	
	
	public Location getTerminal() {
		return terminal;
	}
	
}
