package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.ProbationType;

@Entity
@Table(name="driver")
public class Driver extends AbstractBaseModel{	
	
	@NotEmpty
	@Column(name="staff_id")
	private String staffId;
	
	@Column(name="oldstaff_id")
	private String OldStaffId;	
	
	@Column(name="ssn")
	private String ssn;
	
	@NotEmpty
	@Column(name="first_name")
	private String firstName;
	
	@NotEmpty
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="full_name")
	private String fullName;
	
	
	@Column(name="date_hired")
	private Date dateHired;
		
	@Column(name="date_rehired")
	private Date dateReHired;
	
	@Column(name="anniversar_date")
	private Date anniversaryDate;
	
	@Column(name="next_anniversar_date")
	private Date nextAnniversaryDate;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;

	@ManyToOne
	@JoinColumn(name="payroll_probation")
	private ProbationType payrollProbation;
	
	@ManyToOne
	@JoinColumn(name="leave_probation")
	private ProbationType leaveProbation;
	
	
	@ManyToOne
	@JoinColumn(name="general_probation")
	private ProbationType generalProbation;
	
	
	@Column(name="leave_probation_start_date")
	private Date dateLeaveProbationStart;
	
	@Column(name="leave_probation_end_date")
	private Date dateLeaveProbationEnd;
	
	
	@Column(name="general_probation_start_date")
	private Date dateGeneralProbationStart;
	
	@Column(name="general_probation_end_date")
	private Date dateGeneralProbationEnd;
	
	
	@Column(name="leave_probation_days")
	private int leaveProbationDays=0;
	
	@Column(name="general_probation_days")
	private int generalProbationDays=0;
	
	
	@Column(name="date_terminated")
	private Date dateTerminated;


	@Column(name="probation_start_date")
	private Date dateProbationStart;
	
	@Column(name="probation_end_date")
	private Date dateProbationEnd;
	
	@ManyToOne
	@JoinColumn(name="catagory")
	private EmployeeCatagory catagory;
	
	@Column(name="probation_days")
	private int probationDays=0;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name = "leave_qualifier")
	private String leaveQualifier;
	
	@Column(name = "pay_term")
	private String payTerm;
	
	
	@Column(name="shift")
	private String shift;
	
	@Transient
	private String companies;
	
	@Transient
	private String terminals;
	
	@Transient
	private String categories;
	
	@Transient
	private String hiredDate;
	
	@Transient
	private String rehiredDate;
	
	@Transient
	private String probationStartDate;
	
	@Transient
	private String probationEndDate;
	
	@Transient
	private String empanniversaryDate;
	
	
	@Transient
	private String datesTerminated;
	
	@Transient
	private String empstatus;
	
	@Transient
	private String empshift;
	
	
	public Date getDateHired() {
		return dateHired;
	}

	public void setDateHired(Date dateHired) {
		this.dateHired = dateHired;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFullName(){
		return this.fullName;
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

	public void setDateTerminated(Date dateTerminated) {
		this.dateTerminated = dateTerminated;
	}

	public Date getDateTerminated() {
		return dateTerminated;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getOldStaffId() {
		return OldStaffId;
	}

	public void setOldStaffId(String oldStaffId) {
		OldStaffId = oldStaffId;
	}

	public Date getDateReHired() {
		return dateReHired;
	}

	public void setDateReHired(Date dateReHired) {
		this.dateReHired = dateReHired;
	}

	public Date getDateProbationStart() {
		return dateProbationStart;
	}

	public void setDateProbationStart(Date dateProbationStart) {
		this.dateProbationStart = dateProbationStart;
	}

	public Date getDateProbationEnd() {
		return dateProbationEnd;
	}

	public void setDateProbationEnd(Date dateProbationEnd) {
		this.dateProbationEnd = dateProbationEnd;
	}

	public EmployeeCatagory getCatagory() {
		return catagory;
	}

	public void setCatagory(EmployeeCatagory catagory) {
		this.catagory = catagory;
	}

	public int getProbationDays() {
		return probationDays;
	}

	public void setProbationDays(int probationDays) {
		this.probationDays = probationDays;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getCompanies() {
		return companies;
	}

	public void setCompanies(String companies) {
		this.companies = companies;
	}

	public String getTerminals() {
		return terminals;
	}

	public void setTerminals(String terminals) {
		this.terminals = terminals;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	

	public String getDatesTerminated() {
		return datesTerminated;
	}

	public void setDatesTerminated(String datesTerminated) {
		this.datesTerminated = datesTerminated;
	}

	public String getEmpstatus() {
		return empstatus;
	}

	public void setEmpstatus(String empstatus) {
		this.empstatus = empstatus;
	}
	
	public Date getAnniversaryDate() {
		return anniversaryDate;
	}
	
	public void setAnniversaryDate(Date anniversaryDate) {
		this.anniversaryDate = anniversaryDate;
	}	
	
	public Date getNextAnniversaryDate() {
		return nextAnniversaryDate;
	}
	
	public void setNextAnniversaryDate(Date nextAnniversaryDate) {
		this.nextAnniversaryDate = nextAnniversaryDate;
	}
	
	public String getLeaveQualifier() {
		return leaveQualifier;
	}
	
	public void setLeaveQualifier(String leaveQualifier) {
		this.leaveQualifier = leaveQualifier;
	}
	
	public String getEmpshift() {
		return empshift;
	}
	
	public void setEmpshift(String empshift) {
		this.empshift = empshift;
	}
	
	
	public void setEmpanniversaryDate(String empanniversaryDate) {
		this.empanniversaryDate = empanniversaryDate;
	}
	
	public void setHiredDate(String hiredDate) {
		this.hiredDate = hiredDate;
	}
	
	public void setProbationEndDate(String probationEndDate) {
		this.probationEndDate = probationEndDate;
	}
	
	public void setProbationStartDate(String probationStartDate) {
		this.probationStartDate = probationStartDate;
	}
	
	public void setRehiredDate(String rehiredDate) {
		this.rehiredDate = rehiredDate;
	}
	
	public String getEmpanniversaryDate() {
		return empanniversaryDate;
	}
	public String getHiredDate() {
		return hiredDate;
	}
	public String getProbationEndDate() {
		return probationEndDate;
	}
	
	public String getProbationStartDate() {
		return probationStartDate;
	}
	
	public String getRehiredDate() {
		return rehiredDate;
	}
	
	 public String getPayTerm() {
		return payTerm;
	}
	 
	 public void setPayTerm(String payTerm) {
		this.payTerm = payTerm;
	}
	 
	 public ProbationType getPayrollProbation() {
		return payrollProbation;
	}
	 
	 public void setPayrollProbation(ProbationType payrollProbation) {
		this.payrollProbation = payrollProbation;
	}

	public ProbationType getLeaveProbation() {
		return leaveProbation;
	}

	public void setLeaveProbation(ProbationType leaveProbation) {
		this.leaveProbation = leaveProbation;
	}

	public ProbationType getGeneralProbation() {
		return generalProbation;
	}

	public void setGeneralProbation(ProbationType generalProbation) {
		this.generalProbation = generalProbation;
	}

	public Date getDateLeaveProbationStart() {
		return dateLeaveProbationStart;
	}

	public void setDateLeaveProbationStart(Date dateLeaveProbationStart) {
		this.dateLeaveProbationStart = dateLeaveProbationStart;
	}

	public Date getDateLeaveProbationEnd() {
		return dateLeaveProbationEnd;
	}

	public void setDateLeaveProbationEnd(Date dateLeaveProbationEnd) {
		this.dateLeaveProbationEnd = dateLeaveProbationEnd;
	}

	public Date getDateGeneralProbationStart() {
		return dateGeneralProbationStart;
	}

	public void setDateGeneralProbationStart(Date dateGeneralProbationStart) {
		this.dateGeneralProbationStart = dateGeneralProbationStart;
	}

	public Date getDateGeneralProbationEnd() {
		return dateGeneralProbationEnd;
	}

	public void setDateGeneralProbationEnd(Date dateGeneralProbationEnd) {
		this.dateGeneralProbationEnd = dateGeneralProbationEnd;
	}

	public int getLeaveProbationDays() {
		return leaveProbationDays;
	}

	public void setLeaveProbationDays(int leaveProbationDays) {
		this.leaveProbationDays = leaveProbationDays;
	}

	public int getGeneralProbationDays() {
		return generalProbationDays;
	}

	public void setGeneralProbationDays(int generalProbationDays) {
		this.generalProbationDays = generalProbationDays;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	 
	 
}
