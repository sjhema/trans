package com.primovision.lutransport.model.hrreport;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.ReportModel;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.TimeSheet;
/**
 * @author Subodh
 * 
 */
@Entity
@Table(name="hourlypayroll_invoice_details")
public class HourlyPayrollInvoiceDetails extends AbstractBaseModel implements ReportModel{
	
	String driver = "";
	String employeesNo="";
	String category = "";
	String company = "";
	String terminal = "";
	String batchdate = "";
	String batchdateTo = "";
	@Column(name="payroll_invoice_number")
	String payrollinvoiceNo;
	@Column(name="payroll_date")
	String date;
	
	@Column(name="seq_num1")
	private Integer sequenceNum1;
	
	private Date payRollBatchFrom;
	
	private Date payRollBatchTo;
	
	private Date payRollCheckDate;
	
	/*@Column(name="hourly_payroll_invoice_number")
	protected String hourlypayrollinvoiceNumber;*/
	
	@Column(name="regular_hours")
	private Double regularhours;
	
	@Column(name="holiday_hours")
	private Double holidayhours;
	
	@Column(name="ot_hours")
	private Double othours;
	
	@Column(name="dt_hours")
	private Double dthours;
	
	@Column(name="regular_rate")
	private Double regularrate;
	
	@Column(name="ot_rate")
	private Double otrate;
	
	@Column(name="dt_rate")
	private Double dtrate;
	
	@Column(name="regular_amount")
	private Double regularamount;
	
	@Column(name="ot_amount")
	private Double otamount;
	
	@Column(name="dt_amount")
	private Double dtamount;
	
	@Column(name="sum_amount")
	private Double sumamount;

	@Column(name="vacation_amount")
	private Double vacationAmount;
	
	@Column(name="sickParsanol_amount")
	private Double sickParsanolAmount;
	
	@Column(name="bonus_amount")
	private Double bonusAmounts;
	
	@Column(name="sum_tot_vac_sic_bonus")
	private Double sumOfTotVacSicBonus;
	
	@Column(name="holiday_amount")
	private Double holidayAmount;
	
	@ManyToOne
	@JoinColumn(name="timesheet_id")
	TimeSheet timesheet;
	 
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location companyLoc;
	
	@ManyToOne
	@JoinColumn(name="terminal_id")
	private Location terminalLoc;
	
	
	
	@Column
	private Double miscAmount;
	
	@Column
	private Double reimburseAmount;
	
	// Bereavement change
	@Column
	private Double bereavementAmount;
	
	public Double getBereavementAmount() {
		return bereavementAmount;
	}

	public void setBereavementAmount(Double bereavementAmount) {
		this.bereavementAmount = bereavementAmount;
	}

	public Double getVacationAmount() {
		return vacationAmount;
	}

	public void setVacationAmount(Double vacationAmount) {
		this.vacationAmount = vacationAmount;
	}

	public Double getSickParsanolAmount() {
		return sickParsanolAmount;
	}

	public void setSickParsanolAmount(Double sickParsanolAmount) {
		this.sickParsanolAmount = sickParsanolAmount;
	}

	public TimeSheet getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(TimeSheet timesheet) {
		this.timesheet = timesheet;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	
	

	

	public String getEmployeesNo() {
		return employeesNo;
	}

	public void setEmployeesNo(String employeesNo) {
		this.employeesNo = employeesNo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBatchdate() {
		return batchdate;
	}

	public void setBatchdate(String batchdate) {
		this.batchdate = batchdate;
	}

	public String getPayrollinvoiceNo() {
		return payrollinvoiceNo;
	}

	public void setPayrollinvoiceNo(String payrollinvoiceNo) {
		this.payrollinvoiceNo = payrollinvoiceNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getRegularhours() {
		return regularhours;
	}

	public void setRegularhours(Double regularhours) {
		this.regularhours = regularhours;
	}

	public Double getOthours() {
		return othours;
	}

	public void setOthours(Double othours) {
		this.othours = othours;
	}

	public Double getDthours() {
		return dthours;
	}

	public void setDthours(Double dthours) {
		this.dthours = dthours;
	}

	public Double getRegularrate() {
		return regularrate;
	}

	public void setRegularrate(Double regularrate) {
		this.regularrate = regularrate;
	}

	public Double getOtrate() {
		return otrate;
	}

	public void setOtrate(Double otrate) {
		this.otrate = otrate;
	}

	public Double getDtrate() {
		return dtrate;
	}

	public void setDtrate(Double dtrate) {
		this.dtrate = dtrate;
	}

	public Double getRegularamount() {
		return regularamount;
	}

	public void setRegularamount(Double regularamount) {
		this.regularamount = regularamount;
	}

	public Double getOtamount() {
		return otamount;
	}

	public void setOtamount(Double otamount) {
		this.otamount = otamount;
	}

	public Double getDtamount() {
		return dtamount;
	}

	public void setDtamount(Double dtamount) {
		this.dtamount = dtamount;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public Double getSumamount() {
		return sumamount;
	}

	public void setSumamount(Double sumamount) {
		this.sumamount = sumamount;
	}

	

	public Double getSumOfTotVacSicBonus() {
		return sumOfTotVacSicBonus;
	}

	public void setSumOfTotVacSicBonus(Double sumOfTotVacSicBonus) {
		this.sumOfTotVacSicBonus = sumOfTotVacSicBonus;
	}

	public Double getBonusAmounts() {
		return bonusAmounts;
	}

	public void setBonusAmounts(Double bonusAmounts) {
		this.bonusAmounts = bonusAmounts;
	}

	public Double getHolidayAmount() {
		return holidayAmount;
	}

	public void setHolidayAmount(Double holidayAmount) {
		this.holidayAmount = holidayAmount;
	}

	/*public Date getHourlypayrollinvoiceDate() {
		return hourlypayrollinvoiceDate;
	}

	public void setHourlypayrollinvoiceDate(Date hourlypayrollinvoiceDate) {
		this.hourlypayrollinvoiceDate = hourlypayrollinvoiceDate;
	}*/

	/*public String getHourlypayrollinvoiceNumber() {
		return hourlypayrollinvoiceNumber;
	}

	public void setHourlypayrollinvoiceNumber(String hourlypayrollinvoiceNumber) {
		this.hourlypayrollinvoiceNumber = hourlypayrollinvoiceNumber;
	}*/
	
	
	
	public Double getMiscAmount() {
		return miscAmount;
	}
	
	public void setMiscAmount(Double miscAmount) {
		this.miscAmount = miscAmount;
	}
	
	public Double getReimburseAmount() {
		return reimburseAmount;
	}
	
	
	public void setReimburseAmount(Double reimburseAmount) {
		this.reimburseAmount = reimburseAmount;
	}
	
	public String getBatchdateTo() {
		return batchdateTo;
	}
	
	
	public void setBatchdateTo(String batchdateTo) {
		this.batchdateTo = batchdateTo;
	}
	
	
	public Double getHolidayhours() {
		return holidayhours;
	}
	
	public void setHolidayhours(Double holidayhours) {
		this.holidayhours = holidayhours;
	}
	
	public Location getCompanyLoc() {
		return companyLoc;
	}
	
	public void setCompanyLoc(Location companyLoc) {
		this.companyLoc = companyLoc;
	}
	
	
	public Location getTerminalLoc() {
		return terminalLoc;
	}
	
	public void setTerminalLoc(Location terminalLoc) {
		this.terminalLoc = terminalLoc;
	}	 
	
	
	public Date getPayRollBatchFrom() {
		return payRollBatchFrom;
	}
	
	public Date getPayRollBatchTo() {
		return payRollBatchTo;
	}
	
	
	public void setPayRollBatchFrom(Date payRollBatchFrom) {
		this.payRollBatchFrom = payRollBatchFrom;
	}
	
	public void setPayRollBatchTo(Date payRollBatchTo) {
		this.payRollBatchTo = payRollBatchTo;
	}
	
	
	public Date getPayRollCheckDate() {
		return payRollCheckDate;
	}
	
	public void setPayRollCheckDate(Date payRollCheckDate) {
		this.payRollCheckDate = payRollCheckDate;
	}

	public Integer getSequenceNum1() {
		return sequenceNum1;
	}

	public void setSequenceNum1(Integer sequenceNum1) {
		this.sequenceNum1 = sequenceNum1;
	}
	
}
