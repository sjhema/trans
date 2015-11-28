package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.ReportModel;
import com.primovision.lutransport.model.Ticket;

/**
 * @author subodh
 * 
 */

@Entity
@Table(name = "ptod")
public class Ptod extends AbstractBaseModel /*implements ReportModel*/{

	/*private static final long serialVersionUID = -7960731852472127475L;*/
	
	/*@NotEmpty
	@Column(name="name", unique=true)
	private String name;*/
	/*@ManyToOne
	@JoinColumn(name="employee")
	protected Employee employee;*/
	
	
    @ManyToOne
	@JoinColumn(name="company")
	protected Location company;
	
	@ManyToOne
	@JoinColumn(name = "terminal")
	protected Location terminal;
	
	@ManyToOne
	@JoinColumn(name = "category")
	protected EmployeeCatagory category;
	
	@ManyToOne
	@JoinColumn(name = "leave_type")
	protected LeaveType leavetype;
	
	@Column(name = "leave_qualifier")
	private String leaveQualifier;
	
	//@NotNull
	@Column(name = "experience_day")
	protected Integer experienceindays;
	
	//@NotNull
	@Column(name = "experience_year")
	protected Integer experienceinyears;
	
	@Column(name = "experience_year_to")
	protected Integer experienceinyearsTo;
	
	@NotNull
	@Column(name = "annual_accrual")
	protected Integer annualoraccrual;

	//@NotNull
	@Column(name = "day_earned")
	protected Double dayearned;
	
	//@NotNull
	@Column(name = "hours_earned")
	protected Double hoursearned;
	
	//@NotNull
	@Column(name = "ptod_rate")
	protected Double rate;
	
	//@NotNull
	@Column(name = "hourly_rate")
	protected Double hourlyrate;
	
	@NotNull
	@Column(name="effective_date")
	protected Date effectiveDate;
	
	@NotNull
	@Column(name="end_date")
	protected Date endDate;
	
	
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name="calculate")
	private Integer calculateFlag=0;

	@Transient
    private int noOfRecord;
	
	
	
	public int getNoOfRecord() {
		return noOfRecord;
	}

	public void setNoOfRecord(int noOfRecord) {
		this.noOfRecord = noOfRecord;
	}

	public Integer getExperienceinyearsTo() {
		return experienceinyearsTo;
	}

	public void setExperienceinyearsTo(Integer experienceinyearsTo) {
		this.experienceinyearsTo = experienceinyearsTo;
	}

	public Integer getAnnualoraccrual() {
		return annualoraccrual;
	}

	public void setAnnualoraccrual(Integer annualoraccrual) {
		this.annualoraccrual = annualoraccrual;
	}

	public Integer getExperienceindays() {
		return experienceindays;
	}

	public LeaveType getLeavetype() {
		return leavetype;
	}

	public void setLeavetype(LeaveType leavetype) {
		this.leavetype = leavetype;
	}

	public void setExperienceindays(Integer experienceindays) {
		this.experienceindays = experienceindays;
	}

	public Integer getExperienceinyears() {
		return experienceinyears;
	}

	public void setExperienceinyears(Integer experienceinyears) {
		this.experienceinyears = experienceinyears;
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

	


	public EmployeeCatagory getCategory() {
		return category;
	}

	public void setCategory(EmployeeCatagory category) {
		this.category = category;
	}

	/*public String getLeavetype() {
		return leavetype;
	}

	public void setLeavetype(String leavetype) {
		this.leavetype = leavetype;
	}*/

	

	public Double getDayearned() {
		return dayearned;
	}

	public void setDayearned(Double dayearned) {
		this.dayearned = dayearned;
	}

	public Double getHoursearned() {
		return hoursearned;
	}

	public void setHoursearned(Double hoursearned) {
		this.hoursearned = hoursearned;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getHourlyrate() {
		return hourlyrate;
	}

	public void setHourlyrate(Double hourlyrate) {
		this.hourlyrate = hourlyrate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getCalculateFlag() {
		return calculateFlag;
	}

	public void setCalculateFlag(Integer calculateFlag) {
		this.calculateFlag = calculateFlag;
	}
	
	public String getLeaveQualifier() {
		return leaveQualifier;
	}
	
	public void setLeaveQualifier(String leaveQualifier) {
		this.leaveQualifier = leaveQualifier;
	}
	
	/*@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ptod other = (Ptod) obj;
		if (other.getOrigin()!=null && this.getOrigin()!=null 
		    && other.getDestination()!=null && this.getDestination()!=null 
		    && (other.getOrigin()==this.getOrigin()) 
		    && (other.getDestination()==this.getDestination()) 
		    && ((other.getOriginTicket().equals(this.getOriginTicket()) 
		    		|| (other.getDestinationTicket().equals(this.getDestinationTicket()))))) {
			return true;
		}
		if (other.getCompany()!=null && this.getCompany()!=null 
			    && other.getTerminal()!=null && this.getTerminal()!=null 
			    && other.getCategory()!=null && this.getCategory()!=null
			    && other.getLeavetype()!=null && this.getLeavetype()!=null
			    
			    && (other.getCompany()==this.getCompany()) 
			    && (other.getTerminal()==this.getTerminal())
			    && (other.getCategory()==this.getCategory())
			    && ((other.getLeavetype().equals(this.getLeavetype())))) {
				return true;
			}
		
		
		
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (getId().equals(other.getId())) { 
			return true;
		}
		return false;
	}
	*/
	
	
	

}
