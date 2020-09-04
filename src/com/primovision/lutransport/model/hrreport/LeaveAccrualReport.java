package com.primovision.lutransport.model.hrreport;

import org.apache.commons.lang.StringUtils;

public class LeaveAccrualReport {
	private String employee = StringUtils.EMPTY;
	private String company = StringUtils.EMPTY;
	private String terminal = StringUtils.EMPTY;
	private String category = StringUtils.EMPTY;
	private String leaveType = StringUtils.EMPTY;
	private String ssn = StringUtils.EMPTY;
	private Integer accrualYear = 0;
	
	private String employeeStaffId = StringUtils.EMPTY;
	private Double daysAvailable = 0.0;
	private Double daysUsed = 0.0;
	private Double daysRemaining = 0.0;
	private Double hoursAvailable = 0.0;
   private Double hoursUsed = 0.0;
	private Double hoursRemaining = 0.0;
	private Double dailyAmount = 0.0;
	private Double hourlyAmount = 0.0;
	private Double dailyRate = 0.0;
	private Double hourlyRate = 0.0;
	
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public Integer getAccrualYear() {
		return accrualYear;
	}
	public void setAccrualYear(Integer accrualYear) {
		this.accrualYear = accrualYear;
	}
	public String getEmployeeStaffId() {
		return employeeStaffId;
	}
	public void setEmployeeStaffId(String employeeStaffId) {
		this.employeeStaffId = employeeStaffId;
	}
	public Double getDaysAvailable() {
		return daysAvailable;
	}
	public void setDaysAvailable(Double daysAvailable) {
		this.daysAvailable = daysAvailable;
	}
	public Double getDaysUsed() {
		return daysUsed;
	}
	public void setDaysUsed(Double daysUsed) {
		this.daysUsed = daysUsed;
	}
	public Double getDaysRemaining() {
		return daysRemaining;
	}
	public void setDaysRemaining(Double daysRemaining) {
		this.daysRemaining = daysRemaining;
	}
	public Double getHoursAvailable() {
		return hoursAvailable;
	}
	public void setHoursAvailable(Double hoursAvailable) {
		this.hoursAvailable = hoursAvailable;
	}
	public Double getHoursUsed() {
		return hoursUsed;
	}
	public void setHoursUsed(Double hoursUsed) {
		this.hoursUsed = hoursUsed;
	}
	public Double getHoursRemaining() {
		return hoursRemaining;
	}
	public void setHoursRemaining(Double hoursRemaining) {
		this.hoursRemaining = hoursRemaining;
	}
	public Double getDailyAmount() {
		return dailyAmount;
	}
	public void setDailyAmount(Double dailyAmount) {
		this.dailyAmount = dailyAmount;
	}
	public Double getHourlyAmount() {
		return hourlyAmount;
	}
	public void setHourlyAmount(Double hourlyAmount) {
		this.hourlyAmount = hourlyAmount;
	}
	public Double getDailyRate() {
		return dailyRate;
	}
	public void setDailyRate(Double dailyRate) {
		this.dailyRate = dailyRate;
	}
	public Double getHourlyRate() {
		return hourlyRate;
	}
	public void setHourlyRate(Double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
}
