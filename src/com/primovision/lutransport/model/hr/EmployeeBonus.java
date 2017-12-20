package com.primovision.lutransport.model.hr;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;

/**
 * @author Subodh
 *
 */
@Entity
@Table(name="employeebonus")
public class EmployeeBonus extends AbstractBaseModel{
	@Transient
	public static String REVERT_MODE = "REVERT";
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	private String mode = StringUtils.EMPTY;
	
    @ManyToOne
	@JoinColumn(name="employee_id")
	protected Driver driver;
	
	
	@ManyToOne
	@JoinColumn(name="category")
	protected EmployeeCatagory category;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	protected Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	protected Location terminal;

	@NotNull
	@Column(name="date_hired")
	protected Date dateHired;
	
	@Column(name="experience")
	protected String experience;
	
	@Column(name="emp_number")
	protected String empnumber;
	
	@ManyToOne
	@JoinColumn(name="bonus_type")
	protected BonusType bonustype;

	@Column(name="bonus_amount")
	protected Double bonusamount;
	
	@ManyToOne
	@JoinColumn(name="refferal_id")
	protected Driver refferal;
	
	@Column(name="refferal_hired_date")
	protected Date refferalhireddate;
	
	@NotNull
	@Column(name = "batchfrom")
	protected Date batchFrom;
	
	@Column(name="misc_notes")
	protected String miscNotes;
	
	
	@Column(name="misc_amount")
	protected Double misamount;
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	protected Integer payRollStatus=1;
	
	@OneToMany(mappedBy="bonus",orphanRemoval=true)
	private List<EmpBonusTypesList> bonusTypesLists;
	
	public List<EmpBonusTypesList> getBonusTypesLists() {
		return bonusTypesLists;
	}

	public void setBonusTypesLists(List<EmpBonusTypesList> bonusTypesLists) {
		this.bonusTypesLists = bonusTypesLists;
	}

	@NotNull
	@Column(name = "batchto")
	protected Date batchTo;

	@Transient
	protected String companies;
	@Transient
	protected String terminals;
	@Transient
	protected String employees;
	@Transient
	protected String categories;
	@Transient
	protected String employeesNo;
	@Transient
	protected String bonustypes;
	@Transient
	protected Double amounts;
	@Transient
    protected String batchFroms;
	@Transient
	protected String batchTos;
	
	@Transient
	protected String notes;
	
	
	
	public String getBatchFroms() {
		return batchFroms;
	}

	public void setBatchFroms(String batchFroms) {
		this.batchFroms = batchFroms;
	}

	public String getBatchTos() {
		return batchTos;
	}

	public void setBatchTos(String batchTos) {
		this.batchTos = batchTos;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public EmployeeCatagory getCategory() {
		return category;
	}

	public void setCategory(EmployeeCatagory category) {
		this.category = category;
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

	public Date getDateHired() {
		return dateHired;
	}

	public void setDateHired(Date dateHired) {
		this.dateHired = dateHired;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public BonusType getBonustype() {
		return bonustype;
	}

	public void setBonustype(BonusType bonustype) {
		this.bonustype = bonustype;
	}

	public Double getBonusamount() {
		return bonusamount;
	}

	public void setBonusamount(Double bonusamount) {
		this.bonusamount = bonusamount;
	}

	public Driver getRefferal() {
		return refferal;
	}

	public void setRefferal(Driver refferal) {
		this.refferal = refferal;
	}

	public Date getRefferalhireddate() {
		return refferalhireddate;
	}

	public void setRefferalhireddate(Date refferalhireddate) {
		this.refferalhireddate = refferalhireddate;
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

	public String getEmployees() {
		return employees;
	}

	public void setEmployees(String employees) {
		this.employees = employees;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getEmployeesNo() {
		return employeesNo;
	}

	public void setEmployeesNo(String employeesNo) {
		this.employeesNo = employeesNo;
	}

	public Double getAmounts() {
		return amounts;
	}

	public void setAmounts(Double amounts) {
		this.amounts = amounts;
	}

	public String getEmpnumber() {
		return empnumber;
	}

	public void setEmpnumber(String empnumber) {
		this.empnumber = empnumber;
	}

	public String getBonustypes() {
		return bonustypes;
	}

	public void setBonustypes(String bonustypes) {
		this.bonustypes = bonustypes;
	}

	public Date getBatchFrom() {
		return batchFrom;
	}

	public void setBatchFrom(Date batchFrom) {
		this.batchFrom = batchFrom;
	}

	public Date getBatchTo() {
		return batchTo;
	}

	public void setBatchTo(Date batchTo) {
		this.batchTo = batchTo;
	}

	

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	
	public void setMiscNotes(String miscNotes) {
		this.miscNotes = miscNotes;
	}
	
	public void setMisamount(Double misamount) {
		this.misamount = misamount;
	}
	
	public String getMiscNotes() {
		return miscNotes;
	}
	
	public Double getMisamount() {
		return misamount;
	}

	public Date getPayRollBatch() {
		return payRollBatch;
	}

	public void setPayRollBatch(Date payRollBatch) {
		this.payRollBatch = payRollBatch;
	}

	public Integer getPayRollStatus() {
		return payRollStatus;
	}

	public void setPayRollStatus(Integer payRollStatus) {
		this.payRollStatus = payRollStatus;
	}
	
	
	@Transient
	public String getMode() {
		return mode;
	}

	@Transient
	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
