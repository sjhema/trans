package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;

@Entity
@Table(name="probation_type")
public class ProbationType extends AbstractBaseModel{
	


	@NotEmpty
	@Column(name="probation_name")
	private String probationName;
	
	
	@JoinColumn(name="probation_category")
	private String probationCategory;
	
	@ManyToOne
	@JoinColumn(name="emp_category")
	private EmployeeCatagory empCategory;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@NotNull
	@Column(name="date_from")
	private Date dateFrom;
	
	@NotNull
	@Column(name="date_to")
	private Date dateTo;
	
	
	
	@NotNull
	@Column(name="no_ofdays")
	private Integer noOfDays;
	
	

	

	public EmployeeCatagory getEmpCategory() {
		return empCategory;
	}
	
	public void setEmpCategory(EmployeeCatagory empCategory) {
		this.empCategory = empCategory;
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

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	

	public Integer getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getProbationName() {
		return probationName;
	}

	public void setProbationName(String probationName) {
		this.probationName = probationName;
	}

	public String getProbationCategory() {
		return probationCategory;
	}
	
	public void setProbationCategory(String probationCategory) {
		this.probationCategory = probationCategory;
	}
	
	
	

	
	

}
