package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
//import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;

/**
 * @author subodh
 * 
 */

@Entity
@Table(name = "leave_current_balance")
public class LeaveCurrentBalance extends AbstractBaseModel{
	@ManyToOne
	@JoinColumn(name="emp_name")
	protected Driver empname;
	
	@ManyToOne
	@JoinColumn(name="leave_type")
	protected LeaveType leavetype;
	
	@ManyToOne
	@JoinColumn(name="emp_category")
	protected EmployeeCatagory empcategory;
	
	@Column(name = "leave_qualifier")
	private String leaveQualifier;
	
	/*@NotNull*/
	
	@Column(name="hours_accrude")
	protected  Double hoursaccrude = 0.0;
	
	@Column(name="hours_available")
	protected  Double hoursavailable;
	
	@Column(name="hours_balance")
	protected  Double hoursbalance;

	@Column(name="hours_used")
	protected  Double hoursused;
	
	@Column(name="hours_remain")
	protected  Double hourremain;
	
	
	@Column(name="days_accrude")
	protected  Double daysaccrude = 0.0;
	
	@Column(name="days_available")
	protected  Double daysavailable;
	
	
	@Column(name="days_balance")
	protected  Double dayssbalance;
	
	@Column(name="days_used")
	protected  Double daysused;
	
	@Column(name="days_remain")
	protected  Double daysremain;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@Column(name="note")
	private String note;
	
	@Column(name="effective_from")
	private Date dateEffectiveFrom;
	
	@Column(name="effective_to")
	private Date dateEffectiveTo;
	
	@Column(name="next_allotment_date")
	private Date nextAllotmentDate;
    
	
	public Double getHoursused() {
		return hoursused;
	}

	public void setHoursused(Double hoursused) {
		this.hoursused = hoursused;
	}

	public Double getHourremain() {
		return hourremain;
	}

	public void setHourremain(Double hourremain) {
		this.hourremain = hourremain;
	}

	public Double getDaysused() {
		return daysused;
	}

	public void setDaysused(Double daysused) {
		this.daysused = daysused;
	}

	public Double getDaysremain() {
		return daysremain;
	}

	public void setDaysremain(Double daysremain) {
		this.daysremain = daysremain;
	}

	public Driver getEmpname() {
		return empname;
	}

	public void setEmpname(Driver empname) {
		this.empname = empname;
	}

	public LeaveType getLeavetype() {
		return leavetype;
	}

	public void setLeavetype(LeaveType leavetype) {
		this.leavetype = leavetype;
	}

	
	
	
	
	public EmployeeCatagory getEmpcategory() {
		return empcategory;
	}

	public void setEmpcategory(EmployeeCatagory empcategory) {
		this.empcategory = empcategory;
	}

	public Double getHoursbalance() {
		return hoursbalance;
	}

	public void setHoursbalance(Double hoursbalance) {
		this.hoursbalance = hoursbalance;
	}

	public Double getDayssbalance() {
		return dayssbalance;
	}

	public void setDayssbalance(Double dayssbalance) {
		this.dayssbalance = dayssbalance;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDateEffectiveFrom() {
		return dateEffectiveFrom;
	}

	public void setDateEffectiveFrom(Date dateEffectiveFrom) {
		this.dateEffectiveFrom = dateEffectiveFrom;
	}

	public Date getDateEffectiveTo() {
		return dateEffectiveTo;
	}

	public void setDateEffectiveTo(Date dateEffectiveTo) {
		this.dateEffectiveTo = dateEffectiveTo;
	}
	
	
	public void setDaysaccrude(Double daysaccrude) {
		this.daysaccrude = daysaccrude;
	}
	
	public void setDaysavailable(Double daysavailable) {
		this.daysavailable = daysavailable;
	}
	
	public void setHoursaccrude(Double hoursaccrude) {
		this.hoursaccrude = hoursaccrude;
	}
	
	public void setHoursavailable(Double hoursavailable) {
		this.hoursavailable = hoursavailable;
	}
	
	
	public Double getDaysaccrude() {
		return daysaccrude;
	}
	
	public Double getDaysavailable() {
		return daysavailable;
	}
	
	public Double getHoursaccrude() {
		return hoursaccrude;
	}
	
	public Double getHoursavailable() {
		return hoursavailable;
	}
	
	public void setLeaveQualifier(String leaveQualifier) {
		this.leaveQualifier = leaveQualifier;
	}
	
	public String getLeaveQualifier() {
		return leaveQualifier;
	}
	
	public Date getNextAllotmentDate() {
		return nextAllotmentDate;
	}
	
	public void setNextAllotmentDate(Date nextAllotmentDate) {
		this.nextAllotmentDate = nextAllotmentDate;
	}
	
}
