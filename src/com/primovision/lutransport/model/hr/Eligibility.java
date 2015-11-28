package com.primovision.lutransport.model.hr;

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
@Table(name="eligibility")
public class Eligibility extends AbstractBaseModel{
	@ManyToOne
	@JoinColumn(name="catagory")
	private EmployeeCatagory catagory;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	
	@ManyToOne
	@JoinColumn(name="leave_type")
	private LeaveType leaveType;
	
	@Column(name="probation_days")
	private Integer probationDays;
	
	@Column(name="probation_year")
	private Integer probationYear;
	
	@Column(name="before_days")
	private Integer beforeDays;
	
	@Column(name="after_days")
	private Integer afterDays;
	
	@Column(name="workforweek")
	private Integer workForWeek;
	
	@Column(name="prior_noticedays")
	private Integer priorNoticeDays;
	
	@Column(name="prior_noticeweeks")
	private Integer priorNoticeWeeks;
	
	@Column(name="novaction_from")
	private Date noVactionDateFrom;
	
	@Column(name="novaction_to")
	private Date noVactionDateTo;
	
	

	public Integer getPriorNoticeDays() {
		return priorNoticeDays;
	}

	public void setPriorNoticeDays(Integer priorNoticeDays) {
		this.priorNoticeDays = priorNoticeDays;
	}

	public Integer getPriorNoticeWeeks() {
		return priorNoticeWeeks;
	}

	public void setPriorNoticeWeeks(Integer priorNoticeWeeks) {
		this.priorNoticeWeeks = priorNoticeWeeks;
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

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public Integer getProbationDays() {
		return probationDays;
	}

	public void setProbationDays(Integer probationDays) {
		this.probationDays = probationDays;
	}

	public Integer getProbationYear() {
		return probationYear;
	}

	public void setProbationYear(Integer probationYear) {
		this.probationYear = probationYear;
	}

	public Integer getBeforeDays() {
		return beforeDays;
	}

	public void setBeforeDays(Integer beforeDays) {
		this.beforeDays = beforeDays;
	}

	public Integer getAfterDays() {
		return afterDays;
	}

	public void setAfterDays(Integer afterDays) {
		this.afterDays = afterDays;
	}

	public Integer getWorkForWeek() {
		return workForWeek;
	}

	public void setWorkForWeek(Integer workForWeek) {
		this.workForWeek = workForWeek;
	}

	public Date getNoVactionDateFrom() {
		return noVactionDateFrom;
	}

	public void setNoVactionDateFrom(Date noVactionDateFrom) {
		this.noVactionDateFrom = noVactionDateFrom;
	}

	public Date getNoVactionDateTo() {
		return noVactionDateTo;
	}

	public void setNoVactionDateTo(Date noVactionDateTo) {
		this.noVactionDateTo = noVactionDateTo;
	}
	
	
}
