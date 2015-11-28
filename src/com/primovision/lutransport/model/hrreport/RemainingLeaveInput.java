package com.primovision.lutransport.model.hrreport;


/**
 * @author kishor
 *
 */
public class RemainingLeaveInput {
	
	private String employeesId="";
	private String employees="";
	private String company="";
	private String terminal="";
	private String category="";
	private String leaveType="";
	private Double daysAccrude=0.0;
	private Double daysEarned=0.0;
	private Double daysAvailable=0.0;
	private Double daysused=0.0;
	private Double daysremain=0.0;
	private Double hoursAccrued=0.0;
	private Double hoursEarned=0.0;
	private Double hoursAvailable=0.0;
    private Double hoursused=0.0;
	private Double hourremain=0.0;
	private Double amount=0.0;
	
	private String note="";
	
	public String getEmployeesId() {
		return employeesId;
	}
	public void setEmployeesId(String employeesId) {
		this.employeesId = employeesId;
	}
	public String getEmployees() {
		return employees;
	}
	public void setEmployees(String employees) {
		this.employees = employees;
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
	public Double getDaysEarned() {
		return daysEarned;
	}
	public void setDaysEarned(Double daysEarned) {
		this.daysEarned = daysEarned;
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
	public Double getHoursEarned() {
		return hoursEarned;
	}
	public void setHoursEarned(Double hoursEarned) {
		this.hoursEarned = hoursEarned;
	}
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	public Double getDaysAccrude() {
		return daysAccrude;
	}
	
	
	public Double getDaysAvailable() {
		return daysAvailable;
	}
	
	public Double getHoursAccrued() {
		return hoursAccrued;
	}
	
	public Double getHoursAvailable() {
		return hoursAvailable;
	}
	
	public void setDaysAccrude(Double daysAccrude) {
		this.daysAccrude = daysAccrude;
	}
	
	
	public void setDaysAvailable(Double daysAvailable) {
		this.daysAvailable = daysAvailable;
	}
	
	public void setHoursAccrued(Double hoursAccrued) {
		this.hoursAccrued = hoursAccrued;
	}
	
	public void setHoursAvailable(Double hoursAvailable) {
		this.hoursAvailable = hoursAvailable;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
	public Double getAmount() {
		return amount;
	}
	
	

}
