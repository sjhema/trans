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

/**
 * @author kishor
 *
 */
@Entity
@Table(name="weeklypay_detail")
public class WeeklyPayDetail extends AbstractBaseModel{
	
	@Column(name="employee")
	private String driver;
	
	@Column(name="category")
	private String category;
	
	@Column(name="company_name")
	private String companyname;
	
	@Column(name="terminal_name")
	private String terminalName;
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	
	@Column(name="amount")
	private Double amount=0.0;
	
	@Column(name="sick_personal")
	private Double sickPersonalAmount=0.0;
	
	@Column(name="vacation_amount")
	private Double vacationAmount=0.0;
	
	@Column(name="bonus_amount")
	private Double bonusAmount=0.0;
	
	@Column(name="holiday_amount")
	private Double holidayAmount=0.0;
	
	@Column(name="totalamount")
	private Double totalAmount=0.0;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal_id")
	private Location terminal;
	
	@Column(name="payroll_number")
	private Date checkDate;
	
	
	@Column
	private Double miscAmount;
	
	@Column
	private Double reimburseAmount;
	
	
	public Double getMiscAmount() {
		return miscAmount;
	}
	
	
	public Double getReimburseAmount() {
		return reimburseAmount;
	}
	
	
	public void setMiscAmount(Double miscAmount) {
		this.miscAmount = miscAmount;
	}
	
	
	public void setReimburseAmount(Double reimburseAmount) {
		this.reimburseAmount = reimburseAmount;
	}
	

	public Date getCheckDate() {
		return checkDate;
	}
	
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public Date getPayRollBatch() {
		return payRollBatch;
	}

	public void setPayRollBatch(Date payRollBatch) {
		this.payRollBatch = payRollBatch;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getSickPersonalAmount() {
		return sickPersonalAmount;
	}

	public void setSickPersonalAmount(Double sickPersonalAmount) {
		this.sickPersonalAmount = sickPersonalAmount;
	}

	public Double getVacationAmount() {
		return vacationAmount;
	}

	public void setVacationAmount(Double vacationAmount) {
		this.vacationAmount = vacationAmount;
	}

	public Double getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(Double bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public Double getHolidayAmount() {
		return holidayAmount;
	}

	public void setHolidayAmount(Double holidayAmount) {
		this.holidayAmount = holidayAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
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
	
	
	

}
