package com.primovision.lutransport.model.injury;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;

@Entity
@Table(name = "injury")
public class Injury extends AbstractBaseModel {
	@Transient
	public static Integer INJURY_STATUS_OPEN = 1;
	@Transient
	public static Integer INJURY_STATUS_CLOSED = 2;
	@Transient
	public static Integer INJURY_STATUS_NOT_REPORTED = 3;
	
	@Column(name = "incident_date")
	private Date incidentDate;
	
	@Column(name = "incident_time")
	private String incidentTime;
	
	@Column(name = "incident_time_ampm")
	private String incidentTimeAMPM;
	
	@Column(name = "return_to_work_date")
	private Date returnToWorkDate;
	
	@Column(name = "incident_day_of_week")
	private String incidentDayOfWeek;
	
	@ManyToOne
	@JoinColumn(name="location")
	private Location location;
	
	@ManyToOne
	@JoinColumn(name="driver")
	private Driver driver;
	
   @ManyToOne
   @JoinColumn(name = "insurance_company")
   private InsuranceCompany insuranceCompany;
   
   @ManyToOne
   @JoinColumn(name = "claim_rep")
   private InsuranceCompanyRep claimRep;
	
   @ManyToOne
	@JoinColumn(name="driver_company")
	private Location driverCompany;
   
   @ManyToOne
 	@JoinColumn(name="driver_terminal")
 	private Location driverTerminal;
   
   @ManyToOne
	@JoinColumn(name="driver_category")
	private EmployeeCatagory driverCategory;
   
	@Column(name="injury_status")
	private Integer injuryStatus;
	
	@ManyToOne
	@JoinColumn(name="incident_type")
	private InjuryIncidentType incidentType;
	
	@Column(name="claim_no")
	private String claimNumber;
	
	@Column(name="return_to_work")
	private String returnToWork;
	
	@Column(name="notes")
	private String notes;
	
	@ManyToOne
	@JoinColumn(name="injury_to")
	private InjuryToType injuryTo;
	
	@Column(name="no_of_lost_work_days")
	private Integer noOfLostWorkDays;
	
	@Column(name="tarp_related_injury")
	private String tarpRelatedInjury;
	
	@Column(name="first_report_of_injury")
	private String firstReportOfInjury;
	
	@Column(name="osha_recordable")
	private String oshaRecordable;
	
	@Column(name="medical_cost")
	private Double medicalCost;
	
	@Column(name="indemnity_cost")
	private Double indemnityCost;
	
	@Column(name="expense")
	private Double expense;
	
	@Column(name="reserve")
	private Double reserve;
	
	@Column(name="total_paid")
	private Double totalPaid;
	
	@Column(name="total_claimed")
	private Double totalClaimed;
	
	@Column(name="attorney")
	private String attorney;
	
	@Transient
	private String employed;
	
	@Transient
	private String working;

	@Transient
	public String getEmployed() {
		return employed;
	}

	@Transient
	public void setEmployed(String employed) {
		this.employed = employed;
	}

	@Transient
	public String getWorking() {
		return working;
	}

	@Transient
	public void setWorking(String working) {
		this.working = working;
	}

	public Date getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(Date incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getIncidentTime() {
		return incidentTime;
	}

	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public InsuranceCompany getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public InsuranceCompanyRep getClaimRep() {
		return claimRep;
	}

	public void setClaimRep(InsuranceCompanyRep claimRep) {
		this.claimRep = claimRep;
	}

	public Location getDriverCompany() {
		return driverCompany;
	}

	public void setDriverCompany(Location driverCompany) {
		this.driverCompany = driverCompany;
	}

	public Integer getInjuryStatus() {
		return injuryStatus;
	}

	public void setInjuryStatus(Integer injuryStatus) {
		this.injuryStatus = injuryStatus;
	}

	public InjuryIncidentType getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(InjuryIncidentType incidentType) {
		this.incidentType = incidentType;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getReturnToWork() {
		return returnToWork;
	}

	public void setReturnToWork(String returnToWork) {
		this.returnToWork = returnToWork;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public InjuryToType getInjuryTo() {
		return injuryTo;
	}

	public void setInjuryTo(InjuryToType injuryTo) {
		this.injuryTo = injuryTo;
	}

	public Integer getNoOfLostWorkDays() {
		return noOfLostWorkDays;
	}

	public void setNoOfLostWorkDays(Integer noOfLostWorkDays) {
		this.noOfLostWorkDays = noOfLostWorkDays;
	}

	public String getTarpRelatedInjury() {
		return tarpRelatedInjury;
	}

	public void setTarpRelatedInjury(String tarpRelatedInjury) {
		this.tarpRelatedInjury = tarpRelatedInjury;
	}

	public String getFirstReportOfInjury() {
		return firstReportOfInjury;
	}

	public void setFirstReportOfInjury(String firstReportOfInjury) {
		this.firstReportOfInjury = firstReportOfInjury;
	}

	public Location getDriverTerminal() {
		return driverTerminal;
	}

	public void setDriverTerminal(Location driverTerminal) {
		this.driverTerminal = driverTerminal;
	}

	public Date getReturnToWorkDate() {
		return returnToWorkDate;
	}

	public void setReturnToWorkDate(Date returnToWorkDate) {
		this.returnToWorkDate = returnToWorkDate;
	}

	public String getIncidentDayOfWeek() {
		return incidentDayOfWeek;
	}

	public void setIncidentDayOfWeek(String incidentDayOfWeek) {
		this.incidentDayOfWeek = incidentDayOfWeek;
	}

	public String getOshaRecordable() {
		return oshaRecordable;
	}

	public void setOshaRecordable(String oshaRecordable) {
		this.oshaRecordable = oshaRecordable;
	}

	public Double getMedicalCost() {
		return medicalCost;
	}

	public void setMedicalCost(Double medicalCost) {
		this.medicalCost = medicalCost;
	}

	public Double getIndemnityCost() {
		return indemnityCost;
	}

	public void setIndemnityCost(Double indemnityCost) {
		this.indemnityCost = indemnityCost;
	}

	public Double getExpense() {
		return expense;
	}

	public void setExpense(Double expense) {
		this.expense = expense;
	}

	public Double getReserve() {
		return reserve;
	}

	public void setReserve(Double reserve) {
		this.reserve = reserve;
	}

	public Double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(Double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Double getTotalClaimed() {
		return totalClaimed;
	}

	public void setTotalClaimed(Double totalClaimed) {
		this.totalClaimed = totalClaimed;
	}

	public String getAttorney() {
		return attorney;
	}

	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}

	public String getIncidentTimeAMPM() {
		return incidentTimeAMPM;
	}

	public void setIncidentTimeAMPM(String incidentTimeAMPM) {
		this.incidentTimeAMPM = incidentTimeAMPM;
	}

	public EmployeeCatagory getDriverCategory() {
		return driverCategory;
	}

	public void setDriverCategory(EmployeeCatagory driverCategory) {
		this.driverCategory = driverCategory;
	}
}
